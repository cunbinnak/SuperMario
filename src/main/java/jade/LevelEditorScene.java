package jade;


import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import render.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{



    private int vertexId, fragmentId, shaderProgram;

    private float[] vertexArray = {
            //position         //color
            100.5f, 0.5f,0.0f,   1.0f,0.0f, 0.0f,1.0f, //Botton right  0
           0.5f,  100.5f,0.0f,   0.0f,1.0f, 0.0f,1.0f, //top left      1
            100.5f,  100.5f,0.0f,   0.0f,0.0f, 1.0f,1.0f, //top right     2
           0.5f, 0.5f,0.0f,   1.0f,1.0f, 0.0f,1.0f, //Botton left   3
    };

    //important: must be in conter - clockwise order

    private int[] elementArray ={
        /*
              x     x

              x     x
         */
            2,1,0, //top triangle
            0,1,3 //botton left triangle

    };
    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    public LevelEditorScene(){



    }

    @Override
    public  void init(){

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        //======================================================
        //generate VAO,VBO and EBO buffer object and sent to GPU
        //======================================================

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create a float buffer of verties
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        //create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        //add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize =4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize)* floatSizeBytes;
        glVertexAttribPointer(0,positionsSize,GL_FLOAT,false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize,GL_FLOAT,false,vertexSizeBytes,positionsSize* floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        camera = new  Camera(new Vector2f());
        dt = dt*100;
        camera.position.x -= dt * 50.0f;
        //bind shader program
       defaultShader.use();
       defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
       defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        //bind the VAO we're using
        glBindVertexArray(vaoID);

        //Enable the vertex attribute pointer
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,elementArray.length,GL_UNSIGNED_INT,0);

        //Unbind evething
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detach();

    }
}

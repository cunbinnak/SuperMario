package jade;


import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    public String vertexShadersSrc = "#version 330 core\n" +
            "\n" +
            "layout (location =0) in vec3 aPos;\n" +
            "layout (location =1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";
    public String fragmentShadersSrc ="#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexId, fragmentId, shaderProgram;

    private float[] vertexArray = {
            //position         //color
            0.5f, -0.5f,0.0f,   1.0f,0.0f,0.0f,1.0f//Botton right  0
            -0.5f, 0.5f,0.0f,   0.0f,1.0f, 0.0f,1.0f, //top left   1
            0.5f,  0.5f,0.0f,    0.0f,0.0f,1.0f ,1.0f, //top right 2
            -0.5f, -0.5f,0.0f,  1.0f,1.0f,0.0f,1.0f, //Botton left 3
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

    public LevelEditorScene(){


    }

    @Override
    public  void init(){
        //====================================================
        //Compile and link shaders
        //====================================================

        //First load and compile the vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        //pass the shader source to the GPU
        glShaderSource(vertexId,vertexShadersSrc);
        glCompileShader(vertexId);

        //check for errors in compilation
        int success = glGetShaderi(vertexId,GL_COMPILE_STATUS);
        if (success==GL_FALSE){
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: default.glsl vertex compile");
            System.out.println(glGetShaderInfoLog(vertexId,len));
            assert false:"";
        }

        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        //pass the shader source to the GPU
        glShaderSource(fragmentId,fragmentShadersSrc);
        glCompileShader(fragmentId);

        //check for errors in compilation
         success = glGetShaderi(fragmentId,GL_COMPILE_STATUS);
        if (success==GL_FALSE){
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: default.glsl fragment compile");
            System.out.println(glGetShaderInfoLog(fragmentId,len));
            assert false:"";
        }

        //link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vertexId);
        glAttachShader(shaderProgram,fragmentId);
        glLinkProgram(shaderProgram);

        //check for linking errors
        success = glGetProgrami(shaderProgram,GL_LINK_STATUS);
        if (success==GL_FALSE){
            int len = glGetProgrami(shaderProgram,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: default.glsl linking shader compile");
            System.out.println(glGetProgramInfoLog(shaderProgram,len));
            assert false:"";
        }

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
        glBindBuffer(GL_ARRAY_BUFFER,vaoID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        //create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,vaoID);
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
        //bind shader program
        glUseProgram(shaderProgram);
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
        glUseProgram(0);

    }
}

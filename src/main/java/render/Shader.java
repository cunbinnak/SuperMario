package render;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filePath;


    public Shader(String filepath){
        this.filePath= filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");


            //find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") +6;
            int eol = source.indexOf("\r\n",index);
            String firstPattern = source.substring(index, eol).trim();

            //find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol)+6;
            eol = source.indexOf("\r\n", index);
            String sercondPattern = source.substring(index,eol).trim();

            if (firstPattern.equals("vertex")){
                vertexSource = splitString[1];
            }else if(firstPattern.equals("fragment")){
                fragmentSource = splitString[1];
            }else {
                throw new IOException("Unexpect token " + firstPattern);
            }

            if (sercondPattern.equals("vertex")){
                vertexSource = splitString[2];
            }else if(sercondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            }else {
                throw new IOException("Unexpect token " + sercondPattern);
            }

        }catch (IOException e){
            e.printStackTrace();
            assert false:"Error: Could not open file for shader: " + filePath+ " !";
        }
    }

    public void compile(){

        //====================================================
        //Compile and link shaders
        //====================================================

        int vertexId, fragmentId;
        //First load and compile the vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        //pass the shader source to the GPU
        glShaderSource(vertexId,vertexSource);
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
        glShaderSource(fragmentId,fragmentSource);
        glCompileShader(fragmentId);

        //check for errors in compilation
        success = glGetShaderi(fragmentId,GL_COMPILE_STATUS);
        if (success==GL_FALSE){
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: "+filePath + " fragment compile");
            System.out.println(glGetShaderInfoLog(fragmentId,len));
            assert false:"";
        }


        //link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID,vertexId);
        glAttachShader(shaderProgramID,fragmentId);
        glLinkProgram(shaderProgramID);

        //check for linking errors
        success = glGetProgrami(shaderProgramID,GL_LINK_STATUS);
        if (success==GL_FALSE){
            int len = glGetProgrami(shaderProgramID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: "+filePath+" linking shader compile");
            System.out.println(glGetProgramInfoLog(shaderProgramID,len));
            assert false:"";
        }
    }

    public void use(){
        glUseProgram(shaderProgramID);
    }

    public void detach(){
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID,varName);

        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation,false,matBuffer);
    }
}

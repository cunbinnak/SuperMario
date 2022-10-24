package render;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {

    private String filePath;
    private int textID;

    public Texture(String filePath){
        this.filePath = filePath;

        //Generate texture on GPU
        textID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D,textID);

        //set texture parameter
        //Repeat image in both direction
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T, GL_REPEAT);

        // when stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //when shriking an image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filePath, width, height,channels,0);

        if(image !=null){
            if (channels.get(0)==3){
                glTexImage2D(GL_TEXTURE_2D, 0,GL_RGB,width.get(),height.get(),0,GL_RGB,GL_UNSIGNED_BYTE,image );
            }else if(channels.get(0)==4){
                glTexImage2D(GL_TEXTURE_2D, 0,GL_RGBA,width.get(),height.get(),0,GL_RGBA,GL_UNSIGNED_BYTE,image );
            }else {
                assert false:"Error: Texture - Unknow number of channels " + channels.get(0) + " !";
            }

        }else {
            assert false:"Error: Texture- Could not load image "+ filePath + " !";
        }

        stbi_image_free(image);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, textID);
    }

    public void unBind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}

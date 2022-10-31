package jade;


import components.Sprite;
import components.SpriteRender;
import components.Spritesheet;
import org.joml.Vector2f;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene{


    public LevelEditorScene(){



    }

    @Override
    public  void init(){
        loadResources();
        this.camera = new Camera(new Vector2f(-250,0));

        Spritesheet sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        GameOject obj1 = new GameOject("Obj1", new Transform(new Vector2f(100,100), new Vector2f(100,100)));
        obj1.addComponent(new SpriteRender(sprites.getSprite(0)));
        this.addGameOjectToScene(obj1);

        GameOject obj2 = new GameOject("Obj2", new Transform(new Vector2f(300,100), new Vector2f(100,100)));
        obj2.addComponent(new SpriteRender(sprites.getSprite(15)));
        this.addGameOjectToScene(obj2);



    }

    private void loadResources(){

        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",new Spritesheet(
                AssetPool.getTexture("assets/images/spritesheet.png"),16,16,26,0));
    }
    @Override
    public void update(float dt) {

        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)){
            camera.position.x -=100f *dt;
        }else if(KeyListener.isKeyPressed(GLFW_KEY_LEFT)){
            camera.position.x +=100f *dt;
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_UP)){
            camera.position.y -= 100f *dt;
        }else if(KeyListener.isKeyPressed(GLFW_KEY_DOWN)){
            camera.position.y += 100f *dt;
        }

        for (GameOject go: this.gameOjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}

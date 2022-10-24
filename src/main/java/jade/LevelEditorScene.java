package jade;


import components.SpriteRender;
import org.joml.Vector2f;
import org.joml.Vector4f;
import render.Renderer;

public class LevelEditorScene extends Scene{


    public LevelEditorScene(){



    }

    @Override
    public  void init(){
//        this.renderer = new Renderer();
        this.camera = new Camera(new Vector2f());


        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600- xOffset * 2);
        float totalHeight = (float) (300 - yOffset *2);

        float xSize = totalWidth /100.0f;
        float ySize = totalHeight/100.0f;

        for (int x=0; x<100; x++){
            for (int y=0; y<100; y++){
                float xPos = xOffset + (x * xSize);
                float yPos = yOffset + (y * ySize);

                GameOject game = new GameOject("Obj" + x +" " + y,new Transform(new Vector2f(xPos,yPos),new Vector2f(xSize,ySize)));
                game.addComponent(new SpriteRender(new Vector4f(xPos/ totalWidth,yPos/totalHeight,1,1)));
                this.addGameOjectToScene(game);
            }
        }

    }

    @Override
    public void update(float dt) {

        for (GameOject go: this.gameOjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}

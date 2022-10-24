package jade;

import render.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {


    public Renderer renderer = new Renderer();
    public Camera camera;
    private boolean isRunning = false;
    public List<GameOject> gameOjects = new ArrayList<>();

    public Scene (){

    }

    public void init(){

    }

    public void start(){
        for (GameOject go:gameOjects){
            go.start();
            this.renderer.add(go);
        }
        isRunning= true;
    }

    public void addGameOjectToScene(GameOject go){
        if (!isRunning){
            gameOjects.add(go);
        }else {
            gameOjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }
    public abstract void update(float dt);

    public Camera camera(){
        return this.camera;
    }
}

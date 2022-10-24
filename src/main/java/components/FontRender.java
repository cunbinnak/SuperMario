package components;

import jade.Component;

public class FontRender extends Component {



    @Override
    public void start(){
        if (gameOject.getComponent(SpriteRender.class)!=null){
            System.out.println("Found font render");
        }
    }

    @Override
    public void update(float dt) {

    }
}

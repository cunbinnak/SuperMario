package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener instance;
    private double scrollX,scrollY;
    private double xPos, yPos, lastY, lastX; //cac toa do cua dien di chuyen chuot

    private boolean mouseButtonPressed [] = new boolean[3]; //luu vi tri click
    private boolean idDragging; //keo tha chuot

    private MouseListener(){
        this.scrollX =0.0;
        this.scrollY =0.0;
        this.xPos =0.0;
        this.yPos =0.0;
        this.lastX =0.0;
        this.lastY =0.0;

    }

    public static MouseListener get(){
        if (MouseListener.instance ==null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos){

        get().lastY = get().yPos;
        get().lastX = get().xPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().idDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mousetButtonCallback(long window, int button, int action, int mods){

        //check action xem bam click hay chua
        if(action == GLFW_PRESS){
            if (button < get().mouseButtonPressed.length){
                get().mouseButtonPressed [button]= true;
            }
        }else if(action== GLFW_RELEASE){
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().idDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window , double xOffset, double yOffset){
        get().scrollY = yOffset;
        get().scrollX = xOffset;
    }

    public static void endFrame(){
        get().scrollX=0.0;
        get().scrollY=0.0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX(){
        return (float) get().xPos;
    }

    public static float getY(){
        return (float) get().yPos;
    }

    public static float getDx(){
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy(){
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX(){
        return (float) get().scrollX;
    }

    public static float getScrollY(){
        return (float) get().scrollY;
    }

    public static boolean idDragging(){
        return get().idDragging;
    }

    public static boolean mouseButtonDown(int button){
        if (button< get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }else {
            return false;
        }
    }
}


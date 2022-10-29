package jade;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;
    public float r,g,b,a;
    private boolean fadeToBlack = false;



    private static Window window = null;
    private static Scene currentScene;

    public Window(){
        this.width = 1366;
        this.height = 768;
        this.title = "Mr-CONG_SUPER-MARIO";
        r =1;
        b =1;
        g =1;
        a =1;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();

                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false: "Unknow scene:"+ newScene;
                break;
        }
    }

    public static Window get(){

        if (Window.window ==null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }
    public void run(){
        System.out.println("Hello, let play with me!!! and enjoy this moment");

        Init();
        Loop();

        //set free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //termirate GLFW and the free error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void Init(){
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        //Configure GLFW
        glfwDefaultWindowHints();// optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL,NULL);

        if (glfwWindow== NULL)
            //loi tao cua so man hinh game
            throw new IllegalStateException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(glfwWindow, (window, key, scancode, action, mods) -> {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(glfwWindow, true); // We will detect this in the rendering loop
            });

        //set pointcursor
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        //setmousebutton
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mousetButtonCallback);
        //set thanh cuon
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        //set key
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallback);

        //Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);
        //Make the window visible (hien thi cua so)
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();
        Window.changeScene(0);
    }

    public void Loop(){

        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(glfwWindow)){
            // invoked during this call.
            glfwPollEvents();


            //set mau sac nen
            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if (dt>0){
                currentScene.update(dt);
            }


            glfwSwapBuffers(glfwWindow); // swap the color buffers

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;



        }
    }
}

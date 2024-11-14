//import java.awt.*;
//import javax.swing.*;
//import com.jogamp.opengl.*;
//import com.jogamp.opengl.awt.GLCanvas;
//import com.jogamp.opengl.awt.GLJPanel;
//import com.jogamp.opengl.util.FPSAnimator;
//import static com.jogamp.opengl.GL.*;
//import static com.jogamp.opengl.GL2.*;
//
//class Renderer {
//
//    // Array of 144 float values representing 72 2D points
//    float[] array = {
//            0.29761907f, 0.45421583f,
//            0.20238096f, 0.29368397f,
//            0.74999997f, 0.29368397f,
//            0.65476197f, 0.45421583f,
//            -0.02701211f, 0.7499916f,
//            -0.02701211f, -0.75000379f,
//            0.10714287f, -0.74977316f,
//            0.10714287f, 0.74852419f,
//            0.34523807f, 0.29368397f,
//            0.34523807f, -0.1647749f,
//            0.47714287f, -0.1647749f,
//            0.47714287f, 0.29368397f,
//            0.47714287f, 0.29368397f,
//            0.47714287f, -0.1647749f,
//            0.60714287f, -0.1647749f,
//            0.60714287f, 0.29368397f,
//            0.65476197f, 0.29368397f,
//            0.65476197f, -0.10764567f,
//            0.74999997f, -0.10764567f,
//            0.74999997f, 0.29368397f,
//            0.20238096f, 0.29368397f,
//            0.20238096f, -0.10764567f,
//            0.29761907f, -0.10764567f,
//            0.29761907f, 0.29368397f,
//            0.41666667f, 0.74852424f,
//            0.36904767f, 0.641503f,
//            0.58333337f, 0.641503f,
//            0.53571427f, 0.74852424f,
//            0.41666667f, 0.53448177f,
//            0.36904767f, 0.64150297f,
//            0.58333337f, 0.64150297f,
//            0.53571427f, 0.53448177f,
//            -0.60714285f, 0.45421583f,
//            -0.70238095f, 0.29368397f,
//            -0.1547619f, 0.29368397f,
//            -0.24999999f, 0.45421583f,
//            -0.5595238f, -0.29493284f,
//            -0.5595238f, -0.7497731f,
//            -0.46428571f, -0.7497731f,
//            -0.46428571f, -0.29493284f,
//            -0.39285714f, -0.29493284f,
//            -0.39285714f, -0.7497731f,
//            -0.29761904f, -0.7497731f,
//            -0.29761904f, -0.29493284f,
//            -0.24999999f, 0.29368397f,
//            -0.20238094f, -0.05413505f,
//            -0.10714285f, -0.02737975f,
//            -0.1547619f, 0.29368397f,
//            -0.48809523f, 0.74852424f,
//            -0.53571428f, 0.641503f,
//            -0.32142856f, 0.641503f,
//            -0.36904761f, 0.74852424f,
//            -0.48809523f, 0.53448177f,
//            -0.53571428f, 0.64150297f,
//            -0.32142856f, 0.64150297f,
//            -0.36904761f, 0.53448177f,
//            -0.5595238f, 0.29368397f,
//            -0.67857142f, -0.29493284f,
//            -0.17857142f, -0.29493284f,
//            -0.32142856f, 0.29368397f,
//            -0.60714285f, 0.29368397f,
//            -0.6547619f, -0.05413505f,
//            -0.75f, -0.02737975f,
//            -0.70238095f, 0.29368397f,
//            0.51190477f, -0.1647749f,
//            0.51190477f, -0.74977308f,
//            0.60714287f, -0.74977308f,
//            0.60714287f, -0.1647749f,
//            0.34523807f, -0.1647749f,
//            0.34523807f, -0.74977308f,
//            0.44047617f, -0.74977308f,
//            0.44047617f, -0.1647749f
//    };
//
//    public void init(GLAutoDrawable d) {}
//
//    public void resize(GLAutoDrawable d, int width, int height) {
//        GL2 gl = d.getGL().getGL2(); // get the OpenGL 2 graphics context
//        gl.glViewport(0, 0, width, height);
//    }
//
//    public void display(GLAutoDrawable d) {
//        GL2 gl = d.getGL().getGL2();  // get the OpenGL 2 graphics context
//        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
//        gl.glLoadIdentity();
//        gl.glOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
//        gl.glColor3f(1.0f, 1.0f, 1.0f);
//
//        // Draw points
//        gl.glBegin(GL_POINTS);
//        for (int i = 0; i < array.length; i += 2) {
//            gl.glVertex2f(array[i], array[i + 1]); // use only x, y coordinates
//        }
//        gl.glEnd();
//    }
//
//    public void dispose(GLAutoDrawable d) {}
//}
//
//class MyGui extends JFrame implements GLEventListener {
//
//    private Renderer renderer;
//
//    public void createGUI() {
//        setTitle("Jogl Points Visualization");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        GLProfile glp = GLProfile.get(GLProfile.GL2);
//        GLCapabilities caps = new GLCapabilities(glp);
//        GLJPanel canvas = new GLJPanel(caps); // Use GLJPanel instead of GLCanvas
//        setSize(800, 800);
//        getContentPane().add(canvas);
//        final FPSAnimator ani = new FPSAnimator(canvas, 60, true);
//        canvas.addGLEventListener(this);
//        setVisible(true);
//        renderer = new Renderer();
//        ani.start();
//    }
//
//    @Override
//    public void init(GLAutoDrawable d) {
//        renderer.init(d);
//    }
//
//    @Override
//    public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
//        renderer.resize(d, width, height);
//    }
//
//    @Override
//    public void display(GLAutoDrawable d) {
//        renderer.display(d);
//    }
//
//    @Override
//    public void dispose(GLAutoDrawable d) {
//        renderer.dispose(d);
//    }
//}
//
//public class FirstTriangle {
//    public static void main(String[] args) {
//        System.setProperty("sun.java2d.uiScale", "1.0");
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                MyGui myGUI = new MyGui();
//                myGUI.createGUI();
//            }
//        });
//    }
//}

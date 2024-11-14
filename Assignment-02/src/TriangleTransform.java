import java.awt.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import static com.jogamp.opengl.GL2.*;

class Renderer {

    float[] barData = { 0.02f, 0.2f, -0.02f, 0.2f, -0.02f, -0.2f, 0.02f, -0.2f };

    float[] ballData = { 0.02f, 0.02f, -0.02f, 0.02f, -0.02f, -0.02f, 0.02f, -0.02f };

    float[] score0Data = { 0.06f, 0.1f, 0.04f, 0.1f, 0.04f, -0.1f, 0.06f,
            -0.1f, -0.04f, 0.1f, -0.06f, 0.1f, -0.06f, -0.1f, -0.04f, -0.1f,
            0.05f, 0.1f, 0.05f, 0.08f, -0.05f, 0.08f, -0.05f, 0.1f, 0.05f,
            -0.08f, 0.05f, -0.1f, -0.05f, -0.1f, -0.05f, -0.08f };

    float[] score1Data = { 0.01f, 0.1f, -0.01f, 0.1f, -0.01f, -0.1f, 0.01f,
            -0.1f };

    float[] score2Data = { 0.06f, 0.1f, 0.04f, 0.1f, 0.04f, 0.0f,
            0.06f, 0.0f, -0.04f, 0.0f, -0.06f, 0.0f,
            -0.06f, -0.1f, -0.04f, -0.1f, 0.05f, 0.1f, 0.05f,
            0.08f, -0.05f, 0.08f, -0.05f, 0.1f, 0.05f, -0.08f, 0.05f,
            -0.1f, -0.05f, -0.1f, -0.05f, -0.08f, 0.05f,
            0.01f, 0.05f, -0.01f, -0.05f, -0.01f, -0.05f,
            0.01f };

    float[] score3Data = { 0.06f, 0.1f, 0.04f, 0.1f, 0.04f, -0.1f, 0.06f,
            -0.1f, 0.05f, 0.1f, 0.05f, 0.08f, -0.05f, 0.08f, -0.05f, 0.1f,
            0.05f, -0.08f, 0.05f, -0.1f, -0.05f, -0.1f, -0.05f, -0.08f, 0.05f,
            0.01f, 0.05f, -0.01f, -0.05f, -0.01f, -0.05f, 0.01f};

    // Helper method to draw a quad based on vertex data
    private void drawQuad(GL2 gl, float[] vertices) {
        gl.glBegin(GL_QUADS);
        for (int i = 0; i < vertices.length; i += 2) {
            gl.glVertex2f(vertices[i], vertices[i + 1]);
        }
        gl.glEnd();
    }

    public void init(GLAutoDrawable d) {}

    public void resize(GLAutoDrawable d, int width, int height) {
        GL2 gl = d.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    public void display(GLAutoDrawable d) {
        GL2 gl = d.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);

        gl.glColor3f(1.0f, 1.0f, 1.0f);

        // Draw left paddle
        gl.glPushMatrix();
        gl.glTranslatef(-0.9f, 0.0f, 0.0f); // Position paddle on the left
        drawQuad(gl, barData);
        gl.glPopMatrix();

        // Draw right paddle
        gl.glPushMatrix();
        gl.glTranslatef(0.9f, 0.0f, 0.0f); // Position paddle on the right
        drawQuad(gl, barData);
        gl.glPopMatrix();

        // Draw ball in the center
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, 0.0f);
        drawQuad(gl, ballData);
        gl.glPopMatrix();

        // Draw score for Player 1 on the left
        gl.glPushMatrix();
        gl.glTranslatef(-0.2f, 0.8f, 0.0f); // Position score on the top left
        drawQuad(gl, score1Data);
        gl.glPopMatrix();

        // Draw score for Player 2 on the right
        gl.glPushMatrix();
        gl.glTranslatef(0.2f, 0.8f, 0.0f); // Position score on the top right
        drawQuad(gl, score3Data); // Use score data for player 2
        gl.glPopMatrix();
    }

    public void dispose(GLAutoDrawable d) {}
}

class MyGui extends JFrame implements GLEventListener {

    private Renderer renderer;

    public void createGUI() {
        setTitle("Pong Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLJPanel canvas = new GLJPanel(caps);
        setSize(400, 400);
        getContentPane().add(canvas);
        final FPSAnimator ani = new FPSAnimator(canvas, 60, true);
        canvas.addGLEventListener(this);
        setVisible(true);
        renderer = new Renderer();
        ani.start();
    }

    @Override
    public void init(GLAutoDrawable d) {
        renderer.init(d);
    }

    @Override
    public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
        renderer.resize(d, width, height);
    }

    @Override
    public void display(GLAutoDrawable d) {
        renderer.display(d);
    }

    @Override
    public void dispose(GLAutoDrawable d) {
        renderer.dispose(d);
    }
}

public class TriangleTransform {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        SwingUtilities.invokeLater(() -> {
            MyGui myGUI = new MyGui();
            myGUI.createGUI();
        });
    }
}

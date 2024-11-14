import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import static com.jogamp.opengl.GL2.*;

class Renderer implements KeyListener {

    private static final float[] barData = { 0.02f, 0.2f, -0.02f, 0.2f, -0.02f, -0.2f, 0.02f, -0.2f };
    private static final float[] ballData = { 0.02f, 0.02f, -0.02f, 0.02f, -0.02f, -0.02f, 0.02f, -0.02f };
    private static final float[] score1Data = { 0.01f, 0.1f, -0.01f, 0.1f, -0.01f, -0.1f, 0.01f, -0.1f };

    private float leftPaddleY = 0.0f;
    private float rightPaddleY = 0.0f;
    private float ballX = 0.0f, ballY = 0.0f;
    private float ballDirX = 0.01f, ballDirY = 0.01f;
    private boolean gameStarted = false;
    private boolean moveLeftPaddleUp = false, moveLeftPaddleDown = false;
    private boolean moveRightPaddleUp = false, moveRightPaddleDown = false;
    private int leftScore = 0, rightScore = 0;

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

        // Update paddle positions based on movement state
        if (moveLeftPaddleUp && leftPaddleY < 0.8f) leftPaddleY += 0.02f;
        if (moveLeftPaddleDown && leftPaddleY > -0.8f) leftPaddleY -= 0.02f;
        if (moveRightPaddleUp && rightPaddleY < 0.8f) rightPaddleY += 0.02f;
        if (moveRightPaddleDown && rightPaddleY > -0.8f) rightPaddleY -= 0.02f;

        // Draw left paddle
        gl.glPushMatrix();
        gl.glTranslatef(-0.9f, leftPaddleY, 0.0f);
        drawQuad(gl, barData);
        gl.glPopMatrix();

        // Draw right paddle
        gl.glPushMatrix();
        gl.glTranslatef(0.9f, rightPaddleY, 0.0f);
        drawQuad(gl, barData);
        gl.glPopMatrix();

        // Ball movement and collision
        if (gameStarted) {
            ballX += ballDirX;
            ballY += ballDirY;

            // Reflect off top/bottom
            if (ballY > 0.98f || ballY < -0.98f) ballDirY = -ballDirY;

            // Reflect off paddles
            if (ballX < -0.88f && ballY > leftPaddleY - 0.2f && ballY < leftPaddleY + 0.2f) {
                ballDirX = -ballDirX;
            } else if (ballX > 0.88f && ballY > rightPaddleY - 0.2f && ballY < rightPaddleY + 0.2f) {
                ballDirX = -ballDirX;
            }

            // Scoring
            if (ballX < -1.0f) {
                rightScore++;
                resetBall();
            } else if (ballX > 1.0f) {
                leftScore++;
                resetBall();
            }

            // Check for winning condition
            if (leftScore == 3 || rightScore == 3) {
                gameStarted = false;
                leftScore = 0;
                rightScore = 0;
            }
        }

        // Draw ball
        gl.glPushMatrix();
        gl.glTranslatef(ballX, ballY, 0.0f);
        drawQuad(gl, ballData);
        gl.glPopMatrix();

        // Draw score "1" as an example
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.8f, 0.0f);
        drawQuad(gl, score1Data);
        gl.glPopMatrix();
    }

    private void resetBall() {
        ballX = 0.0f;
        ballY = 0.0f;
        ballDirX = (Math.random() > 0.5 ? 0.01f : -0.01f);
        ballDirY = (Math.random() > 0.5 ? 0.01f : -0.01f);
    }

    public void dispose(GLAutoDrawable d) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                if (!gameStarted) {
                    gameStarted = true;
                    resetBall();
                }
                break;
            case KeyEvent.VK_W: moveLeftPaddleUp = true; break;
            case KeyEvent.VK_S: moveLeftPaddleDown = true; break;
            case KeyEvent.VK_P: moveRightPaddleUp = true; break;
            case KeyEvent.VK_L: moveRightPaddleDown = true; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: moveLeftPaddleUp = false; break;
            case KeyEvent.VK_S: moveLeftPaddleDown = false; break;
            case KeyEvent.VK_P: moveRightPaddleUp = false; break;
            case KeyEvent.VK_L: moveRightPaddleDown = false; break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}

class MyGui extends JFrame implements GLEventListener {

    private Renderer renderer;

    public void createGUI() {
        setTitle("Pong Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLJPanel canvas = new GLJPanel( caps);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(renderer = new Renderer());
        setSize(400, 400);
        getContentPane().add(canvas);
        canvas.setFocusable(true);
        canvas.requestFocus();
        setVisible(true);
        new FPSAnimator(canvas, 60, true).start();
    }

    @Override
    public void init(GLAutoDrawable d) { renderer.init(d); }
    @Override
    public void reshape(GLAutoDrawable d, int x, int y, int width, int height) { renderer.resize(d, width, height); }
    @Override
    public void display(GLAutoDrawable d) { renderer.display(d); }
    @Override
    public void dispose(GLAutoDrawable d) { renderer.dispose(d); }
}

public class TriangleTransform {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyGui().createGUI());
    }
}

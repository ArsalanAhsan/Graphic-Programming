import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

class Renderer {

    private float ballX = 0.0f, ballY = 0.0f;
    private float ballDX = 0.01f, ballDY = 0.01f;
    private float leftBatY = 0.0f, rightBatY = 0.0f;
    private float batSpeed = 0.03f;
    private boolean leftBatUp, leftBatDown, rightBatUp, rightBatDown;
    private int leftScore = 0, rightScore = 0;
    private boolean gameStarted = false;
    private boolean gameWon = false;

    // Method to draw a quad using 2D coordinates
    private void drawQuad(GL2 gl, float[] data) {
        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0; i < data.length; i += 2) {
            gl.glVertex2f(data[i], data[i + 1]);
        }
        gl.glEnd();
    }

    private void drawBat(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(-0.9f, leftBatY, 0.0f);
        drawQuad(gl, PongArrays.barData);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0.9f, rightBatY, 0.0f);
        drawQuad(gl, PongArrays.barData);
        gl.glPopMatrix();
    }

    private void drawBall(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(ballX, ballY, 0.0f);
        drawQuad(gl, PongArrays.ballData);
        gl.glPopMatrix();
    }

    private void moveBats() {
        if (leftBatUp && leftBatY < 0.8f) leftBatY += batSpeed;
        if (leftBatDown && leftBatY > -0.8f) leftBatY -= batSpeed;
        if (rightBatUp && rightBatY < 0.8f) rightBatY += batSpeed;
        if (rightBatDown && rightBatY > -0.8f) rightBatY -= batSpeed;
    }

    private void moveBall() {
        ballX += ballDX;
        ballY += ballDY;


        if (ballY >= 1.0f || ballY <= -1.0f) {
            ballDY = -ballDY;
        }

        // Reflect ball on bats
        if (ballX <= -0.88f && Math.abs(ballY - leftBatY) < 0.2f) { // Left bat
            ballDX = -ballDX;
        }
        if (ballX >= 0.88f && Math.abs(ballY - rightBatY) < 0.2f) { // Right bat
            ballDX = -ballDX;
        }


        if (ballX < -1.0f) {
            rightScore++;
            resetBall();
        } else if (ballX > 1.0f) {
            leftScore++;
            resetBall();
        }


        if (leftScore == 3 || rightScore == 3) {
            gameWon = true;
            gameStarted = false; // Stop the game
        }
    }

    private void resetBall() {
        ballX = 0.0f;
        ballY = 0.0f;
        ballDX = (ballDX > 0 ? -0.01f : 0.01f);
    }

    public void init(GLAutoDrawable d) {}

    public void resize(GLAutoDrawable d, int width, int height) {
        GL2 gl = d.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }

    public void display(GLAutoDrawable d) {
        GL2 gl = d.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        drawBat(gl);
        drawBall(gl);

        if (gameStarted) {
            moveBats();
            moveBall();
        }


        drawScore(gl);
    }

    private void drawScore(GL2 gl) {
        // Left score
        gl.glPushMatrix();
        gl.glTranslatef(-0.5f, 0.9f, 0.0f);
        drawQuad(gl, PongArrays.getScoreData(leftScore));
        gl.glPopMatrix();

        // Right score
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0.9f, 0.0f);
        drawQuad(gl, PongArrays.getScoreData(rightScore));
        gl.glPopMatrix();
    }

    public void dispose(GLAutoDrawable d) {}

    public void setGameStarted(boolean started) {
        gameStarted = started;
    }

    public void setBatMovement(int bat, boolean up, boolean moving) {
        if (bat == 0) { // Left bat
            if (up) leftBatUp = moving;
            else leftBatDown = moving;
        } else { // Right bat
            if (up) rightBatUp = moving;
            else rightBatDown = moving;
        }
    }

    public void resetGame() {
        leftScore = 0;
        rightScore = 0;
        gameWon = false;
        resetBall();
    }

    public boolean isGameWon() {
        return gameWon;
    }
}

class MyGui extends JFrame implements GLEventListener {

    private Renderer renderer;

    public void createGUI() {
        setTitle("Pong Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLJPanel canvas = new GLJPanel(caps);
        setSize(320, 320);
        getContentPane().add(canvas);
        final FPSAnimator ani = new FPSAnimator(canvas, 60, true);
        canvas.addGLEventListener(this);

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        if (renderer.isGameWon()) {
                            renderer.resetGame();
                        }
                        renderer.setGameStarted(true);
                        break;
                    case KeyEvent.VK_W:
                        renderer.setBatMovement(0, true, true);
                        break;
                    case KeyEvent.VK_S:
                        renderer.setBatMovement(0, false, true);
                        break;
                    case KeyEvent.VK_P:
                        renderer.setBatMovement(1, true, true);
                        break;
                    case KeyEvent.VK_L:
                        renderer.setBatMovement(1, false, true);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        renderer.setBatMovement(0, true, false);
                        break;
                    case KeyEvent.VK_S:
                        renderer.setBatMovement(0, false, false);
                        break;
                    case KeyEvent.VK_P:
                        renderer.setBatMovement(1, true, false);
                        break;
                    case KeyEvent.VK_L:
                        renderer.setBatMovement(1, false, false);
                        break;
                }
            }
        });

        setVisible(true);
        renderer = new Renderer();
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
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
        javax.swing.SwingUtilities.invokeLater(() -> {
            MyGui myGUI = new MyGui();
            myGUI.createGUI();
        });
    }
}

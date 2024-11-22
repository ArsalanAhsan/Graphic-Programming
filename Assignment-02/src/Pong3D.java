import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.glu.GLU;

class Renderer implements GLEventListener {

    private float ballX = 0.0f, ballY = 0.0f, ballZ = -2.5f;
    private float ballDX = 0.02f, ballDY = 0.02f;
    private float leftBatY = 0.0f, rightBatY = 0.0f;
    private float batSpeed = 0.05f;
    private boolean leftBatUp, leftBatDown, rightBatUp, rightBatDown;

    // Setter methods for bat movement
    public void setLeftBatUp(boolean value) {
        this.leftBatUp = value;
    }

    public void setLeftBatDown(boolean value) {
        this.leftBatDown = value;
    }

    public void setRightBatUp(boolean value) {
        this.rightBatUp = value;
    }

    public void setRightBatDown(boolean value) {
        this.rightBatDown = value;
    }

    private void drawCube(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);

        // Front face (red)
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);

        // Back face (green)
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        // Left face (blue)
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);

        // Right face (yellow)
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);

        // Top face (magenta)
        gl.glColor3f(1.0f, 0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);

        // Bottom face (cyan)
        gl.glColor3f(0.0f, 1.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        gl.glEnd();
    }

    private void drawBat(GL2 gl, float x, float y) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, -2.5f);
        gl.glScalef(0.2f, 0.8f, 0.1f);
        drawCube(gl);
        gl.glPopMatrix();
    }

    private void drawBall(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(ballX, ballY, ballZ);
        gl.glScalef(0.2f, 0.2f, 0.2f);
        drawCube(gl);
        gl.glPopMatrix();
    }

    private void drawField(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex3f(-2.0f, -1.5f, -2.5f);
        gl.glVertex3f(2.0f, -1.5f, -2.5f);
        gl.glVertex3f(2.0f, 1.5f, -2.5f);
        gl.glVertex3f(-2.0f, 1.5f, -2.5f);
        gl.glEnd();
        gl.glPopMatrix();
    }

    private void moveBall() {
        ballX += ballDX;
        ballY += ballDY;

        // Reflect ball when it hits the top or bottom
        if (ballY >= 1.5f || ballY <= -1.5f) {
            ballDY = -ballDY;
        }

        // Reflect ball when it hits the paddles
        if (ballX <= -1.8f && Math.abs(ballY - leftBatY) < 0.4f) {
            ballDX = -ballDX;
        }
        if (ballX >= 1.8f && Math.abs(ballY - rightBatY) < 0.4f) {
            ballDX = -ballDX;
        }
    }

    private void moveBats() {
        if (leftBatUp && leftBatY < 1.2f) leftBatY += batSpeed;
        if (leftBatDown && leftBatY > -1.2f) leftBatY -= batSpeed;
        if (rightBatUp && rightBatY < 1.2f) rightBatY += batSpeed;
        if (rightBatDown && rightBatY > -1.2f) rightBatY -= batSpeed;
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Set perspective projection
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60.0, 16.0 / 9.0, 1.5, 5.0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        moveBats();
        moveBall();

        drawField(gl);
        drawBat(gl, -1.9f, leftBatY);
        drawBat(gl, 1.9f, rightBatY);
        drawBall(gl);
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);
    }

    public void dispose(GLAutoDrawable drawable) {}

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }
}

class MyGui extends JFrame implements GLEventListener {

    private Renderer renderer;

    public void createGUI() {
        setTitle("3D Pong Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLJPanel canvas = new GLJPanel(caps);

        renderer = new Renderer();
        canvas.addGLEventListener(renderer);

        getContentPane().add(canvas);
        final FPSAnimator animator = new FPSAnimator(canvas, 60, true);

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        renderer.setLeftBatUp(true);
                        break;
                    case KeyEvent.VK_S:
                        renderer.setLeftBatDown(true);
                        break;
                    case KeyEvent.VK_UP:
                        renderer.setRightBatUp(true);
                        break;
                    case KeyEvent.VK_DOWN:
                        renderer.setRightBatDown(true);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        renderer.setLeftBatUp(false);
                        break;
                    case KeyEvent.VK_S:
                        renderer.setLeftBatDown(false);
                        break;
                    case KeyEvent.VK_UP:
                        renderer.setRightBatUp(false);
                        break;
                    case KeyEvent.VK_DOWN:
                        renderer.setRightBatDown(false);
                        break;
                }
            }
        });

        canvas.setFocusable(true);
        canvas.requestFocusInWindow();

        animator.start();
        setVisible(true);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        renderer.init(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        renderer.dispose(drawable);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        renderer.display(drawable);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        renderer.reshape(drawable, x, y, width, height);
    }
}

public class Pong3D {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyGui gui = new MyGui();
            gui.createGUI();
        });
    }
}

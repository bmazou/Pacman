// package pacman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class App extends JPanel implements ActionListener {
    private final Font smallFont = new Font("Arial", Font.BOLD, BLOCK_SIZE);
    public boolean inGame = false;
    public boolean dying = false;

    public static final int TIMER_DELAY = 15;
    private final short[][] levelData = Map.getMap();
    public static final int BLOCK_SIZE = 36;
    public static final int X_BLOCK_COUNT = Map.getMapWidth();
    public static final int Y_BLOCK_COUNT = Map.getMapHeight();

    public static final int SCREEN_WIDTH = X_BLOCK_COUNT * BLOCK_SIZE;
    public static final int SCREEN_HEIGHT = Y_BLOCK_COUNT * BLOCK_SIZE;
    // public static final int N_BLOCKS = Map.getMap().length;
    public int lives, score;
    
    private Image heart, ghost;
    private Image up, down, left, right;

    public static int[] viableSpeeds = getDivisors(BLOCK_SIZE);
    public short[][] screenData;
    private Timer timer;
    private Pacman pacman;
    private Ghost[] ghosts;
    private int GHOST_COUNT;
    private final int GHOST_SPEED = 3;
    private final int PACMAN_SPEED = 4;

    public App() {
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();

        System.out.println("Ghost count: " + GHOST_COUNT);
        
        // TODO: Add win condition
    }

    private void setGhostCount() {
        int numOfBlocks = X_BLOCK_COUNT * Y_BLOCK_COUNT;

        // Set GHOST_COUNT to the number of blocks ** 3
        GHOST_COUNT = (int) Math.pow(numOfBlocks, 0.30) - 1;
    
    }

    private void loadImages() {
        down = new ImageIcon(getClass().getResource("./images/down.gif")).getImage();
        up = new ImageIcon(getClass().getResource("./images/up.gif")).getImage();
        left = new ImageIcon(getClass().getResource("./images/left.gif")).getImage();
        right = new ImageIcon(getClass().getResource("./images/right.gif")).getImage();
        ghost = new ImageIcon(getClass().getResource("./images/ghost.gif")).getImage();
        heart = new ImageIcon(getClass().getResource("./images/heart.png")).getImage();

        // Resize the images to fit the block size
        down = down.getScaledInstance(BLOCK_SIZE - 5, BLOCK_SIZE - 5,
        Image.SCALE_DEFAULT);
        up = up.getScaledInstance(BLOCK_SIZE - 5, BLOCK_SIZE - 5,
        Image.SCALE_DEFAULT);
        left = left.getScaledInstance(BLOCK_SIZE - 5, BLOCK_SIZE - 5,
        Image.SCALE_DEFAULT);
        right = right.getScaledInstance(BLOCK_SIZE - 5, BLOCK_SIZE - 5,
        Image.SCALE_DEFAULT);
        ghost = ghost.getScaledInstance(BLOCK_SIZE - 5, BLOCK_SIZE - 5,
        Image.SCALE_DEFAULT);
    }

    private static int[] getDivisors(int n ) {
        ArrayList<Integer> divisors = new ArrayList<Integer>();
        for (int i = 1; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                divisors.add(i);
                if (n / i != i) {           // If the divisor is not the square root of n
                    divisors.add(n / i);    // Add the other divisor
                }
            }
        }

        // Sort the divisors in ascending order and return them as an array
        divisors.sort(null);
        return divisors.stream().mapToInt(i -> i).toArray();
    }


    private void initVariables() {
        setGhostCount();

        pacman = new Pacman(PACMAN_SPEED, this);

        screenData = new short[Y_BLOCK_COUNT][X_BLOCK_COUNT];

        ghosts = new Ghost[GHOST_COUNT];
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i] = new Ghost(GHOST_SPEED, this, pacman);
        }

        timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }

    private void playGame(Graphics2D g2d) {
        if (dying) {
            death();

        } else {
            pacman.move();
            drawPacman(g2d);
            moveGhosts(g2d);
        }
    }

    private void showIntroScreen(Graphics2D g2d) {
        String start = "Press SPACE to start";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, X_BLOCK_COUNT*BLOCK_SIZE/4, Y_BLOCK_COUNT*BLOCK_SIZE/2);
    }

    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_WIDTH / X_BLOCK_COUNT + 96, SCREEN_WIDTH + X_BLOCK_COUNT);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_HEIGHT, this);
        }
    }

    private void death() {
        lives--;

        if (lives == 0) {
            inGame = false;
        }

        respawn();    // Reset starting position
    }

    private void moveGhosts(Graphics2D g2d) {
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].move();
            drawGhost(g2d, ghosts[i].x, ghosts[i].y);
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) {
        g2d.drawImage(ghost, x, y, this);
    }

    private void drawPacman(Graphics2D g2d) {
        Direction dir = pacman.direction;
        int pac_x = pacman.x;
        int pac_y = pacman.y;

        if (dir == Direction.LEFT) {
            g2d.drawImage(left, pac_x + 1, pac_y + 1, this);
        } else if (dir == Direction.RIGHT) {
            g2d.drawImage(right, pac_x + 1, pac_y + 1, this);
        } else if (dir == Direction.UP) {
            g2d.drawImage(up, pac_x + 1, pac_y + 1, this);
        } else {
            g2d.drawImage(down, pac_x + 1, pac_y + 1, this);
        }
    }


    private void drawMaze(Graphics2D g2d) {
        for (int y = 0; y < Y_BLOCK_COUNT; y++) {
            for (int x = 0; x < X_BLOCK_COUNT; x++) {
                g2d.setColor(new Color(0, 72, 251));
                g2d.setStroke(new BasicStroke(5));

                boolean isWall = levelData[y][x] == 0;
                if ((isWall)) {  
                    boolean neigboursTopWall = y > 0 && levelData[y - 1][x] == 0;
                    if (!neigboursTopWall) {
                        g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE, x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE);
                    }

                    boolean neigboursBottomWall = y < Y_BLOCK_COUNT - 1 && levelData[y + 1][x] == 0;
                    if (!neigboursBottomWall) {
                        g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE + BLOCK_SIZE - 1, x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE + BLOCK_SIZE - 1);
                    }

                    boolean neigboursLeftWall = x > 0 && levelData[y][x - 1] == 0;
                    if (!neigboursLeftWall) {
                        g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE, x * BLOCK_SIZE, y * BLOCK_SIZE + BLOCK_SIZE - 1);
                    }

                    boolean neigboursRightWall = x < X_BLOCK_COUNT - 1 && levelData[y][x + 1] == 0;
                    if (!neigboursRightWall) {
                        g2d.drawLine(x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE, x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE + BLOCK_SIZE - 1);
                    }
                }

                boolean hasWallOnLeft = (screenData[y][x] & 1) != 0;; 
                if (hasWallOnLeft) {  // Draw a left border
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE, x * BLOCK_SIZE, y * BLOCK_SIZE + BLOCK_SIZE - 1);
                }

                boolean hasWallOnTop = (screenData[y][x] & 2) != 0;
                if (hasWallOnTop) {  // Draw a top border
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE, x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE);
                }

                boolean hasWallOnRight = (screenData[y][x] & 4) != 0;
                if (hasWallOnRight) {  // Draw a right border
                    g2d.drawLine(x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE, x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE + BLOCK_SIZE - 1);
                }

                boolean hasWallOnBottom = (screenData[y][x] & 8) != 0;
                if (hasWallOnBottom) {  // Draw a bottom border
                    g2d.drawLine(x*BLOCK_SIZE, y*BLOCK_SIZE + BLOCK_SIZE - 1, x*BLOCK_SIZE + BLOCK_SIZE - 1, y*BLOCK_SIZE + BLOCK_SIZE - 1);
                }

                boolean hasFood = (screenData[y][x] & 16) != 0;
                if (hasFood) {
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.fillOval(x*BLOCK_SIZE + (int)(BLOCK_SIZE*0.375) , y * BLOCK_SIZE + (int)(BLOCK_SIZE*0.375), BLOCK_SIZE/4, BLOCK_SIZE/4);
                }
            }
        }
    }


    private void resetMap() {
        for (int y = 0; y < Y_BLOCK_COUNT; y++) {
            for (int x = 0; x < X_BLOCK_COUNT; x++) {
                screenData[y][x] = levelData[y][x];
            }
        }
    }

    private void initGame() {
        lives = 3;
        score = 0;
        resetMap();
        respawn();
    }

    
    // Get's called during beginning of game and when pacman loses a life: resets pacman and ghosts
    private void respawn() {
        pacman.newGame();
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].newGame();
        }
        dying = false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        drawMaze(g2d);
        drawScore(g2d);

        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    // controls
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    pacman.requestedDir = Direction.LEFT;
                } else if (key == KeyEvent.VK_RIGHT) {
                    pacman.requestedDir = Direction.RIGHT;
                } else if (key == KeyEvent.VK_UP) {
                    pacman.requestedDir = Direction.UP;
                } else if (key == KeyEvent.VK_DOWN) {
                    pacman.requestedDir = Direction.DOWN;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    initGame();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}

// package pacman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class App extends JPanel implements ActionListener {
    private Dimension d;
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    public boolean inGame = false;
    public boolean dying = false;

    public static final int BLOCK_SIZE = 24;
    // public static final int X_BLOCK_COUNT = Map.getMap()[0].length;
    public static final int N_BLOCKS = Map.getMap().length;
    public static final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    public int lives, score;
    private final short[][] levelData = Map.getMap();
    
    private Image heart, ghost;
    private Image up, down, left, right;


    public short[][] screenData;
    private Timer timer;
    private Pacman pacman;
    private Ghost[] ghosts;
    private final int GHOST_COUNT = 4;
    private final int GHOST_SPEED = 3;
    private final int PACMAN_SPEED = 4;

    public App() {
        Map.printMapDimensions();
        System.out.println(N_BLOCKS);
        Map.printMap(levelData);

        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();

        // TODO: Add win condition
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

    private void initVariables() {
        pacman = new Pacman(PACMAN_SPEED, 0 * BLOCK_SIZE, 0 * BLOCK_SIZE, this);

        d = new Dimension(BLOCK_SIZE * N_BLOCKS, BLOCK_SIZE * N_BLOCKS);
        screenData = new short[N_BLOCKS][N_BLOCKS];

        ghosts = new Ghost[GHOST_COUNT];
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i] = new Ghost(GHOST_SPEED, 4 * BLOCK_SIZE, 4 * BLOCK_SIZE, this, pacman);
        }

        timer = new Timer(25, this);
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
        g2d.drawString(start, SCREEN_SIZE / 4, 150);
    }

    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    private void death() {
        lives--;

        if (lives == 0) {
            inGame = false;
        }

        continueLevel();    // Reset starting position
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


    private void drawMaze2(Graphics2D g2d) {
        for (int y = 0; y < N_BLOCKS; y++) {
            for (int x = 0; x < N_BLOCKS; x++) {
                g2d.setColor(new Color(0, 72, 251));
                g2d.setStroke(new BasicStroke(5));

                if ((levelData[y][x] == 0)) {  // Draw a wall
                    g2d.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
                if ((screenData[y][x] & 1 ) != 0) {  // Draw a left border
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE, x * BLOCK_SIZE, y * BLOCK_SIZE + BLOCK_SIZE - 1);
                }
                if ((screenData[y][x] & 2) != 0) {  // Draw a top border
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE, x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE);
                }
                if ((screenData[y][x] & 4) != 0) {  // Draw a right border
                    g2d.drawLine(x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE, x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE + BLOCK_SIZE - 1);
                }
                if ((screenData[y][x] & 8) != 0) {  // Draw a bottom border
                    g2d.drawLine(x * BLOCK_SIZE, y * BLOCK_SIZE + BLOCK_SIZE - 1, x * BLOCK_SIZE + BLOCK_SIZE - 1, y * BLOCK_SIZE + BLOCK_SIZE - 1);
                }
                if ((screenData[y][x] & 16) != 0) {
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.fillOval(x * BLOCK_SIZE + 10, y * BLOCK_SIZE + 10, BLOCK_SIZE / 4, BLOCK_SIZE / 4);
                }
            }
        }
    }


    //TODO Tohle sjednotit s initLevel?
    private void initGame() {
        lives = 3;
        score = 0;
        initLevel();
    }

    private void initLevel() {
        for (int x = 0; x < N_BLOCKS; x++) {
            for (int y = 0; y < N_BLOCKS; y++) {
                screenData[x][y] = levelData[x][y];
            }
        }

        continueLevel();
    }

    // Get's called during beginning of game and when pacman loses a life - resets
    // pacman and ghosts
    private void continueLevel() {
        for (int i = 0; i < ghosts.length; i++) {
            // TODO Tady se zase orientovat podle mapy
            ghosts[i].newGame(4 * BLOCK_SIZE, 4 * BLOCK_SIZE);
        }
        pacman.newGame(7 * BLOCK_SIZE, 11 * BLOCK_SIZE);
        dying = false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze2(g2d);
        // drawMaze(g2d);
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

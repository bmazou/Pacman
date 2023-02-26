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

public class Game extends JPanel implements ActionListener {
    public static boolean inGame = false;
    public static boolean dying = false;
    public static boolean gameWon = false;

    public static final int TIMER_DELAY = 15;
    private short[][] levelData;
    public static final int BLOCK_SIZE = 36;
    public static int X_BLOCK_COUNT;
    public static int Y_BLOCK_COUNT;
    private Font smallFont = new Font("Helvetica", Font.BOLD, 20);
    private Font introScreenFont;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static int lives, score;
    public int foodCount;
    
    private Image heart, ghost;
    private Image up, down, left, right;

    public static int[] viableSpeeds = getViableSpeeds(BLOCK_SIZE);
    public static short[][] screenData;
    private Timer timer;
    private Pacman pacman;
    private Ghost[] ghosts;
    private int GHOST_COUNT;
    private final int GHOST_SPEED = 3;
    private final int PACMAN_SPEED = 4;

    private App app;
    private boolean mapPregenerated = true;

    public Game(App app) {
        /**
         * Constructor of the game
         * <p>
         * Initialize the game and add the key listener
         * 
         * @params app The main class of the game
         */
        this.app = app;
        loadImages();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();     
    }
    
    private void initGame() {
        /**
         * Initialize a new game
         */
        initVariables();

        resetMap();
        foodCount = Map.getFoodCount();
        respawn();
    }

    private void initVariables() {
        /**
         * Initialize the variables of the game
         * <p>
         * Generates a new map, sets the number of ghosts, resets the game state, creates pacman, the ghosts and the timer
         */
        generateNewMap();
        setGhostCount();
        resetGameState();

        pacman = new Pacman(PACMAN_SPEED);

        screenData = new short[Y_BLOCK_COUNT][X_BLOCK_COUNT];

        ghosts = new Ghost[GHOST_COUNT];
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i] = new Ghost(GHOST_SPEED, pacman, i);
        }

        timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }

    private void resetGameState() {
        /**
         * Reset the game values to start a new game
         */
        dying = false;
        gameWon = false;
        lives = 3;
        score = 0;
    }

    private void generateNewMap() {
        /**
         * Generate a new map
         * <p>
         * Gets a new map from the Map class and sets the variables accordingly
         * 
         */
        Map.generateMap();
        levelData = Map.getMap();
        foodCount = Map.getFoodCount();

        X_BLOCK_COUNT = Map.getMapWidth();
        Y_BLOCK_COUNT = Map.getMapHeight();
        SCREEN_HEIGHT = Y_BLOCK_COUNT * BLOCK_SIZE;
        SCREEN_WIDTH = X_BLOCK_COUNT * BLOCK_SIZE;

        app.setSize(Game.SCREEN_WIDTH + 20, Game.SCREEN_HEIGHT + 100);

        introScreenFont = new Font("Arial", Font.BOLD, BLOCK_SIZE*X_BLOCK_COUNT/10);
    }

    private void setGhostCount() {
        /**
         * Set the number of ghosts based on the current map
         * <p>
         * The number of ghosts is based on the number of blocks in the map, approximately a cube root of the number of blocks
         */
        int numOfBlocks = X_BLOCK_COUNT * Y_BLOCK_COUNT;
        GHOST_COUNT = (int) Math.pow(numOfBlocks, 0.30) - 1;
    }

    private static int[] getViableSpeeds(int n ) {
        /**
         * Gets viable speeds for the ghosts and pacman
         * <p>
         * The speeds have to be divisors of the block size, so that GameObjects.atIntersection() works properly
         * 
         * @param n The size of blocks
         * @return An array of viable speeds
         */
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


    private void respawn() {
        /**
         * Reset the position of pacman and the ghosts
         * <p>
         * Get's called during beginning of game or when pacman loses a life: resets pacman and ghosts positions
         */
        pacman.newGame();
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].newGame();
        }
        dying = false;
    }

    private void playGame(Graphics2D g2d) {
        /**
         * Main game loop
         * <p>
         * Checks if the game is won, if pacman died
         * Otherwise, moves pacman and the ghosts and draws them
         * 
         * @param g2d The graphics object
         */
        if (score == foodCount) {
            gameWon = true;
            inGame = false;
            return;
        }

        if (dying) {
            death();
        }
        else {
            pacman.move();
            drawPacman(g2d);
            moveGhosts(g2d);
        }
    }

    private void death() {
        /**
         * Handle the death of pacman
         * <p>
         * Decrease the number of lives and respawn pacman and the ghosts
         * If the number of lives is 0, the game is over
         */
        lives--;

        if (lives == 0) {
            inGame = false;
        }

        respawn();    // Reset starting position
    }

    private void moveGhosts(Graphics2D g2d) {
        /**
         * Move the ghosts
         * <p>
         * Loops through the ghosts and moves them
         * 
         * @param g2d The graphics object
         */
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i].move();
            drawGhost(g2d, ghosts[i].x, ghosts[i].y);
        }
    }


    private void resetMap() {
        /**
         * Reset the map to the original values
         * <p>
         * i.e. reset the map to the original map - add the food eaten back
         */
        for (int y = 0; y < Y_BLOCK_COUNT; y++) {
            for (int x = 0; x < X_BLOCK_COUNT; x++) {
                screenData[y][x] = levelData[y][x];
            }
        }
    }


    private void loadImages() {
        /**
         * Load the images for the game
         * <p>
         * Loads the images for pacman, ghosts, food, and the lives and resizes them to fit the block size
         */
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


    //* ------------------ Drawing methods ------------------ *//
    public void paintComponent(Graphics g) {
        /**
         * Paint the game
         * <p>
         * Using the Graphics object, draw the game, based on the current state of the game
         * If the game is in progress, continue game loop
         * Otherwise show the intro screen or the win screen
         * 
         * @param g The graphics object
         */
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        drawMaze(g2d);
        drawScore(g2d);


        if (inGame) {
            playGame(g2d);
        } else {
            if (gameWon)
                showWinScreen(g2d);
            else
                showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void showWinScreen(Graphics2D g2d) {
        /**
         * Display a win screen
         * 
         * @param g2d The graphics object
         */
        String win = "You win!";
        g2d.setColor(Color.yellow);
        g2d.drawString(win, X_BLOCK_COUNT*BLOCK_SIZE/4, Y_BLOCK_COUNT*BLOCK_SIZE/2 - smallFont.getSize());
        showIntroScreen(g2d);
    }

    private void showIntroScreen(Graphics2D g2d) {
        /**
         * Show an intro screen
         * 
         * @param g2d The graphics object
         */
        g2d.setFont(introScreenFont);
        String start = "Press SPACE to start";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, 0, Y_BLOCK_COUNT*BLOCK_SIZE/2);
    }

    private void drawScore(Graphics2D g) {
        /**
         * Display the current score, lives and map size 
         * 
         * @param g The graphics object
         */
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, 8, SCREEN_HEIGHT + BLOCK_SIZE/2);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_HEIGHT + BLOCK_SIZE/2 + smallFont.getSize(), this);
        }

        drawMapSize(g);
    }

    private void drawMapSize(Graphics2D g) {
        /**
         * Display the map size
         * 
         * @param g The graphics object
         */
        g.setFont(smallFont);
        g.setColor(Color.black);
        String s = "Map size: " + X_BLOCK_COUNT + "x" + Y_BLOCK_COUNT;
        g.drawString(s, SCREEN_WIDTH/2 - g.getFontMetrics().stringWidth(s) /2, SCREEN_HEIGHT + BLOCK_SIZE);
    }  


    private void drawGhost(Graphics2D g2d, int x, int y) {
        /**
         * Draw a ghost
         * 
         * @param g2d The graphics object
         * @param x The x coordinate of the ghost
         * @param y The y coordinate of the ghost
         */
        g2d.drawImage(ghost, x, y, this);
    }

    private void drawPacman(Graphics2D g2d) {
        /**
         * Draw pacman
         * <p>
         * Draw pacman based on the direction he is moving
         * 
         * @param g2d The graphics object
         */
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
        /**
         * Draw the maze
         * <p>
         * Draw the maze based on the map logic described in the pdf documentation
         * 
         * @param g2d The graphics object
         */
        for (int y = 0; y < Y_BLOCK_COUNT; y++) {
            for (int x = 0; x < X_BLOCK_COUNT; x++) {
                g2d.setColor(new Color(0, 72, 251));
                g2d.setStroke(new BasicStroke(5));

                boolean isAWall = levelData[y][x] == 0;
                if ((isAWall)) {  
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
    


    //* -------------------------- KeyAdapter -------------------------- *//
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            /**
             * Handle key presses
             * <p>
             * Handle key presses
             * - Update the direction of pacman if game is in progress
             * - Start a new game if game is not in progress
             * 
             * @param e The key event
             */
            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_LEFT) {
                    pacman.requestDirection(Direction.LEFT);
                } else if (key == KeyEvent.VK_RIGHT) {
                    pacman.requestDirection(Direction.RIGHT);
                } else if (key == KeyEvent.VK_UP) {
                    pacman.requestDirection(Direction.UP);
                } else if (key == KeyEvent.VK_DOWN) {
                    pacman.requestDirection(Direction.DOWN);
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    score = 0;
                    if (!mapPregenerated) initGame();

                    mapPregenerated = false;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /**
         * Handle timer events
         * <p>
         * Handle timer events
         * - Update the game state
         * - Repaint the game
         * 
         * @param e The action event
         */
        repaint();
    }
}

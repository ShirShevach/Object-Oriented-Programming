package src;

import danogl.components.CoordinateSpace;
import danogl.gui.rendering.ImageRenderable;
import src.brick_strategies.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * the main of the game. init the classes and run the game.
 */
public class BrickerGameManager extends GameManager {

    private static final float BALL_SPEED = 250;
    public static final int BORDER_WIDTH = 10;
    private static final int NUM_BRICKS_ROW = 8;
    private static final int NUM_BRICKS_COL = 7;
    private static final int NUM_LIFE = 3;
    private static final float SIZE_NUM_WIDTH = 20;
    private static final float SIZE_NUM_HEIGHT = 20;
    private static final float START_OF_NUM = 20;
    private static final String BACKGROUND_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String HEART_PATH = "assets/heart.png";
    private static final String BRICK_PATH = "assets/brick.png";
    private static final String PADDLE_PATH = "assets/paddle.png";
    private static final String BALL_PATH = "assets/ball.png";
    public static final String SOUND_BALL_PATH = "assets/blop_cut_silenced.wav";
    private static final String MASSAGE_WIN = "You win!";
    private static final String MASSAGE_LOSE = "You lose!";
    private static final String MASSAGE_Q = " Play again?";
    private static final String NAME_GAME = "Bricker";
    private static final float SIZE_WIDTH = 700;
    private static final float SIZE_HEIGHT = 500;
    private static final char W = 'W';
    public static final float SPACE_HEART = START_OF_NUM+ SIZE_NUM_WIDTH + BORDER_WIDTH +10;
    private Ball ball;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private Counter lifeCounter;
    private Counter bricksCounter;
    private ImageRenderable brickImage;
    private ImageRenderable graphicLifeImage;
    private Random randomStrategy;

    /**
     * BrickerGameManager's Constructor
     * @param windowTitle Game's name
     * @param windowDimensions of the game
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        lifeCounter = new Counter(NUM_LIFE);
        bricksCounter = new Counter(NUM_BRICKS_ROW*NUM_BRICKS_COL);
        
        createBackground();
        createGraphicLife();
        createNumericLife();
        createBall();
        createPaddle();
        createBricks();
        createBorders();
    }

    /*
    create the gameObject background.
     */
    private void createBackground() {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_PATH, false);
        GameObject background = new GameObject
                (Vector2.ZERO, new Vector2(windowDimensions.x(), windowDimensions.y()), backgroundImage);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /*
    create the gameObject NumericLife.
     */
    private void createNumericLife() {
        GameObject numericLife = new NumericLifeCounter(lifeCounter,
                Vector2.ZERO, new Vector2(SIZE_NUM_WIDTH, SIZE_NUM_HEIGHT), gameObjects());
        numericLife.setCenter(new Vector2(BORDER_WIDTH +START_OF_NUM, (int)windowDimensions.y()-30));
        gameObjects().addGameObject(numericLife, Layer.UI);
    }

    /*
    create the gameObject GraphicLife.
     */
    private void createGraphicLife() {
        graphicLifeImage = imageReader.readImage(HEART_PATH, true);

        for (int i = 0; i < 3; i++) {
            GameObject heart = new GraphicLifeCounter(Vector2.ZERO, new Vector2(20, 20), lifeCounter,
                    graphicLifeImage, gameObjects(), i);
            heart.setCenter(new Vector2(SPACE_HEART +20*i, (int)windowDimensions.y()-30));
            gameObjects().addGameObject(heart, Layer.UI);
        }

    }

    /*
    create the gameObject Borders
     */
    private void createBorders() {
        // left
        GameObject left = new GameObject
                (Vector2.ZERO, new Vector2(BORDER_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(left, Layer.STATIC_OBJECTS);
        // right
        GameObject right = new GameObject
                (new Vector2(windowDimensions.x(), 0), new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        null);
        gameObjects().addGameObject(right, Layer.STATIC_OBJECTS);
        // top
        GameObject top = new GameObject
                (new Vector2(0, 0), new Vector2(windowDimensions.x(), BORDER_WIDTH),
                        null);
        gameObjects().addGameObject(top, Layer.STATIC_OBJECTS);
    }

    /*
    create the gameObject Bricks
     */
    private void createBricks() {
        brickImage = imageReader.readImage(BRICK_PATH, false);
        StrategyFactory strategyFactory = new StrategyFactory(imageReader,
                soundReader,
                gameObjects(),
                windowDimensions,
                inputListener,
                ball,
                windowController,
                this,
                graphicLifeImage,
                lifeCounter);

        float x = (this.windowDimensions.x() - 2* BORDER_WIDTH) / 8;
        float y = ((this.windowDimensions.x() - BORDER_WIDTH) / 3)/7;
        Brick brick;
        for (int i = 0; i < NUM_BRICKS_ROW; i++) {
            for (int j = 0; j < NUM_BRICKS_COL; j++) {
                randomStrategy = new Random();
                Strategy strategy = strategyFactory.chooseStrategy(new CollisionStrategy(gameObjects()),
                        randomStrategy.nextInt(1, 6), 0);
                brick = new Brick(Vector2.ZERO, new Vector2(x, y), brickImage,
                        strategy, bricksCounter);

                brick.setCenter(new Vector2(BORDER_WIDTH + x*i+45, BORDER_WIDTH +y*j+20));
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    /*
    create the GameObject Paddle.
     */
    private void createPaddle() {
        Renderable paddleImage = imageReader.readImage(PADDLE_PATH, false);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(200, 20), paddleImage, inputListener,
                windowDimensions, BORDER_WIDTH);
        paddle.setCenter(new Vector2(windowDimensions.x()/2, (int)windowDimensions.y()-30));
        gameObjects().addGameObject(paddle);
    }

    /*
    create the GameObject Ball.
     */
    private void createBall() {
        Renderable ballImage = imageReader.readImage(BALL_PATH, true);
        Sound collisionSound = soundReader.readSound(SOUND_BALL_PATH);
        this.ball =
                new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound,
                        this);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        ball.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2+50));
        float ballVelX = BALL_SPEED;
        float ballVellY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVellY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVellY));
        gameObjects().addGameObject(ball);
    }

    /*
    the function override the GameManger's update because its
    check if the game is over (win or lose)
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    /*
    the function check if the game is over.
     */
    private void checkForGameEnd() {
        float ballHeights = ball.getCenter().y();
        String prompt = "";
        boolean enter = false;
        if (bricksCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.getExtendedKeyCodeForChar(W))) {
            enter = true;
            prompt = MASSAGE_WIN;
        }
        if (ballHeights > windowDimensions.y()) {
            lifeCounter.decrement();
            if (lifeCounter.value() == 0) {
                enter = true;
                prompt = MASSAGE_LOSE;
            }
            else {
                ball.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2+30));
            }
        }
        if (enter) {
            prompt += MASSAGE_Q;
            if (windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    /**
     * The function init the class BrickerGameManager and call the function 'run'.
     * @param args that we receive from the command line.
     */
    public static void main(String[] args) {
        new BrickerGameManager(NAME_GAME, new Vector2(SIZE_WIDTH, SIZE_HEIGHT)).run();

    }
}

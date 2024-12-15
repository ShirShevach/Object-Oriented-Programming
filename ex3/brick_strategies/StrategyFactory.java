package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;

import java.util.Random;

/**
 * StrategyFactory class
 */
public class StrategyFactory {
    private final Random random;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private GameObjectCollection gameObjects;
    private Vector2 windowDimensions;
    private UserInputListener inputListener;
    private Ball ball;
    private WindowController windowController;
    private BrickerGameManager gameManager;
    private Renderable graphicLifeImage;
    private Counter lifeCounter;
    private Counter numPaddle;

    /**
     * StrategyFactory constructor
     * @param imageReader for puck and more paddle
     * @param soundReader for puck
     * @param gameObjects for all the strategies
     * @param windowDimensions for more paddle
     * @param inputListener for more paddle
     * @param ball for change camera
     * @param windowController for change camera
     * @param gameManager for change camera
     * @param graphicLifeImage for more life
     * @param lifeCounter for more life
     */
    public StrategyFactory(ImageReader imageReader,
                    SoundReader soundReader,
                    GameObjectCollection gameObjects,
                    Vector2 windowDimensions,
                    UserInputListener inputListener,
                    Ball ball,
                    WindowController windowController,
                    BrickerGameManager gameManager,
                    Renderable graphicLifeImage,
                    Counter lifeCounter) {
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
        this.ball = ball;
        this.windowController = windowController;
        this.gameManager = gameManager;
        this.graphicLifeImage = graphicLifeImage;
        this.lifeCounter = lifeCounter;
        this.random = new Random();
        this.numPaddle = new Counter(1);
    }

    /**
     * The function selects a strategy from 6 options
     * @param strategy the strategy that activates before the
     *                 selected strategy
     * @param num which strategy
     * @param depth of the recursion
     * @return the selected strategy
     */
    public Strategy chooseStrategy(Strategy strategy, int num, int depth) {
        if (num == 1) { // puck
            Renderable ballImage = imageReader.readImage("assets/mockBall.png", true);
            Sound collisionSound = soundReader.readSound(BrickerGameManager.SOUND_BALL_PATH);
            return new PuckStrategy(gameObjects, strategy, ballImage, collisionSound);
        }
        if (num == 2) { // another paddle
            Renderable MorePaddleImage = imageReader.readImage("assets/botGood.png", false);
            return new MorePaddleStrategy(gameObjects, strategy, MorePaddleImage,
                    windowDimensions, inputListener, BrickerGameManager.BORDER_WIDTH, numPaddle);
        }
        if (num == 3) { // change camera
            return new ChangeCameraStrategy(gameObjects, strategy, ball, windowController,gameManager);
        }
        if (num == 4) { // hearts
            return new HeartStrategy(gameObjects, strategy, graphicLifeImage, windowDimensions,
                    BrickerGameManager.SPACE_HEART, lifeCounter);
        }
        if (num == 5) { // basis
            return strategy;
        }
        if (num == 6) { // double
            if (depth >= 2) {
                return chooseStrategy(strategy, random.nextInt(1, 5), depth+1);
            }
            Strategy one = chooseStrategy(strategy, random.nextInt(1, 5), depth+1);
            return chooseStrategy(one, random.nextInt(1, 6), depth+1);
        }
        return null;
    }

}

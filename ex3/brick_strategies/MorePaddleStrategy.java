package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Brick;
import src.gameobjects.MorePaddle;

/**
 * The function manage the add more paddle to the game.
 */
public class MorePaddleStrategy extends StrategyDecorator {
    private GameObjectCollection gameObjects;
    private Renderable paddleImage;
    private Vector2 windowDimensions;
    private UserInputListener inputListener;
    private int borderWidth;
    private Counter numPaddle;

    /**
     * MorePaddleStrategy's constructor
     * @param gameObjects the parameter for the class
     * @param strategy that the class save to operate if it's not null
     * @param paddleImage image if paddle
     * @param windowDimensions the dimension of the game's window.
     * @param inputListener to follow after the player request
     * @param borderWidth of the borders
     * @param numPaddle counter that check if there are no more than 2
     *                  paddle in the game.
     */
    public MorePaddleStrategy(GameObjectCollection gameObjects, Strategy strategy,
                              Renderable paddleImage,
                              Vector2 windowDimensions, UserInputListener inputListener,
                              int borderWidth, Counter numPaddle) {
        super(gameObjects, strategy);
        this.gameObjects = gameObjects;
        this.paddleImage = paddleImage;
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
        this.borderWidth = borderWidth;
        this.numPaddle = numPaddle;
    }

    /**
     * the function handle the situation that we hit a
     * brick that suppose to add more paddle to the game.
     * @param brick the brick the happened the hit
     * @param other the ball the hited the brick
     * @param counter number of brick counter
     */
    @Override
    public void OnCollision(Brick brick, GameObject other, Counter counter) {
        super.OnCollision(brick, other, counter);
        if (numPaddle.value() == 1) {
            GameObject paddle = new MorePaddle(Vector2.ZERO, new Vector2(200, 20), paddleImage, inputListener,
                    windowDimensions, borderWidth, gameObjects, numPaddle);
            paddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2));
            gameObjects.addGameObject(paddle);
            numPaddle.increment();
        }
    }
}

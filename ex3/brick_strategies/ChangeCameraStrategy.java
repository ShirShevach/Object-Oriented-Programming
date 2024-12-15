package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.Brick;

/**
 * The function manage the change camera strategy.
 */
public class ChangeCameraStrategy extends StrategyDecorator{
    private Camera camera;
    private GameObjectCollection gameObjects;
    private BrickerGameManager gameManager;
    private Ball ball;
    private WindowController windowController;
    private int startHit = 0;

    /**
     * change camera's constructor
     * @param gameObjects the parameter for the class
     * @param strategy that the class save to operate if it's not null
     * @param ball the object the camera follow
     * @param windowController for the Camera class
     * @param gameManager to set the camera.
     */
    public ChangeCameraStrategy(GameObjectCollection gameObjects,
                                Strategy strategy,
                                Ball ball,
                                WindowController windowController,
                                BrickerGameManager gameManager) {
        super(gameObjects, strategy);
        this.gameObjects = gameObjects;
        this.gameManager = gameManager;
        this.ball = ball;
        this.windowController = windowController;
    }

    /**
     * the function handle the situation that we hit a
     * brick that suppose to change the camera.
     * @param brick the brick the happened the hit
     * @param other the ball the hited the brick
     * @param counter number of brick counter
     */
    @Override
    public void OnCollision(Brick brick, GameObject other, Counter counter) {
        camera = new Camera(ball, Vector2.ZERO,
                windowController.getWindowDimensions().mult(1.2f),
                windowController.getWindowDimensions());
        super.OnCollision(brick, other, counter);
        if (gameManager.getCamera() == null) {
            gameManager.setCamera(camera);
        }
    }
}

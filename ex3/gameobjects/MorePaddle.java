package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * MorePaddle class
 */
public class MorePaddle extends Paddle {

    Counter count_hit;
    private GameObjectCollection gameObjects;
    private Counter numPaddle;

    /**
     * MorePaddle constructor
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener to know where to move
     * @param windowsDimensions dimension of the game screen
     * @param borderWidth space from the start of the screen
     * @param gameObjects to remove the paddle
     * @param numPaddle count if it is not more than 2
     */
    public MorePaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      UserInputListener inputListener, Vector2 windowsDimensions, int borderWidth,
                      GameObjectCollection gameObjects, Counter numPaddle) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowsDimensions, borderWidth);
        this.gameObjects = gameObjects;
        this.numPaddle = numPaddle;
        count_hit = new Counter(0);
    }

    /**
     * The function handles the case where the ball hits the paddle.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        count_hit.increment();
        if (count_hit.value() == 3) {
            gameObjects.removeGameObject(this);
            numPaddle.decrement();
        }
    }
}

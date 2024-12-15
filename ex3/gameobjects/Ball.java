package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;

/**
 * the ball class
 */
public class Ball extends BasisBall {
    private final Counter hitWhenCameraOn;
    private Sound collisionSound;
    private BrickerGameManager gameManager;

    /**
     * the ball constructor
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param renderable The renderable representing the object. Can be null, in which case
     *                   the GameObject will not be rendered.
     * @param collisionSound the sound the play when the ball hit an object
     * @param gameManager for the change camera
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound,
                BrickerGameManager gameManager) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        this.collisionSound = collisionSound;
        this.gameManager = gameManager;
        hitWhenCameraOn = new Counter(0);
    }

    /**
     * The function handles the case where the ball hits a game object.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (gameManager.getCamera() != null) {
            if (hitWhenCameraOn.value() < 4) {
                hitWhenCameraOn.increment();
            }
            if (hitWhenCameraOn.value() == 4) {
                gameManager.setCamera(null);
                hitWhenCameraOn.reset();
            }
        }
    }
}

package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * the basis ball class
 */
public class BasisBall extends GameObject {
    private final Sound collisionSound;

    /**
     * the basis ball constructor
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions Width and height in window coordinates.
     * @param renderable The renderable representing the object. Can be null, in which case
     *                   the GameObject will not be rendered.
     * @param collisionSound the sound the play when the ball hit an object
     */
    public BasisBall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                     Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
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
        Vector2 newVal = getVelocity().flipped(collision.getNormal());
        setVelocity(newVal);
        this.collisionSound.play();
    }
}

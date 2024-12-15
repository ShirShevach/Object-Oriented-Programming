package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Paddle Class
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 400;
    private UserInputListener inputListener;
    private Vector2 windowsDimensions;
    private int borderWidth;

    /**
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener to know where to move.
     * @param windowsDimensions to know where to stop.
     * @param borderWidth space from the start of the screen
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowsDimensions, int borderWidth) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowsDimensions = windowsDimensions;
        this.borderWidth = borderWidth;
    }

    /**
     * The function handles the case where the ball hits the paddle.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && this.getTopLeftCorner().x() > borderWidth) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                this.getTopLeftCorner().x() + this.getDimensions().x() < windowsDimensions.x() - borderWidth) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}

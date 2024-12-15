package Bricker.gameobjects;

import Bricker.movementStragedy.MovementStragedy;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;


public class Paddle extends GameObject {
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    private static final float MOVEMENT_SPEED = 400;
    private MovementStragedy movementStragdy;

    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  MovementStragedy movementStragdy) {
        super(topLeftCorner, dimensions, renderable);
        this.movementStragdy = movementStragdy;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = movementStragdy.calcMovementDir(this);
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }

}

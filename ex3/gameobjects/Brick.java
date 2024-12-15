package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.brick_strategies.Strategy;

public class Brick extends GameObject {
    private Strategy strategy;
    private Counter counter;

    /**
     * brick constructor
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param strategy to activates when ball hits the brick
     * @param counter A few bricks remained in the game
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 Strategy strategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.strategy = strategy;
        this.counter = counter;
    }

    /**
     * The function handles the case where the ball hits the brick.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.strategy.OnCollision(this, other, counter);
    }
}

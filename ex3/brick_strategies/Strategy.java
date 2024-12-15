package src.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;
import src.gameobjects.Brick;

/**
 * interface Strategy
 */
public interface Strategy {
    /**
     * A function that represents what happens when
     * there is a brick collision
     * @param brick the current brick
     * @param other the ball that hit the brick
     * @param counter number of bricks that left in the game
     */
    void OnCollision(Brick brick, GameObject other, Counter counter);
}

package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import src.gameobjects.Brick;

/**
 * class StrategyDecorator, represents the classes:
 * Puck, MorePaddle, ChangeCamera, MoreLife
 */
public abstract class StrategyDecorator implements Strategy {
    private GameObjectCollection gameObject;
    private final Strategy object;

    /**
     * StrategyDecorator's constructor
     * @param gameObject for the strategy
     * @param object more strategy to activate
     */
    StrategyDecorator(GameObjectCollection gameObject, Strategy object) {
        this.gameObject = gameObject;
        this.object = object;
    }

    /**
     * not abstract class, activates the more strategy.
     * @param brick the current brick
     * @param other the ball that hit the brick
     * @param counter number of bricks that left in the game
     */
    @Override
    public void OnCollision(Brick brick, GameObject other, Counter counter) {
        this.object.OnCollision(brick, other, counter);
    }
}

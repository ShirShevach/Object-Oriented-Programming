package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;
import src.gameobjects.Brick;

/**
 * the basis class the handle collision in brick.
 */
public class CollisionStrategy implements Strategy{
    private final GameObjectCollection gameObjects;

    /**
     * CollisionStrategy's constructor
     * @param gameObjects class game object
     */
    public CollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * the function remove brick that ball hit it.
     * @param brick the brick that the ball hitted it.
     * @param other the ball
     * @param counter number of bricks counter
     */
    public void OnCollision(Brick brick, GameObject other, Counter counter) {
        boolean ans = gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS);
        if (ans)
            counter.decrement();
    }
}

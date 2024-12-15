package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * GraphicLifeCounter class
 */
public class GraphicLifeCounter extends GameObject {
    private Counter counter;
    private GameObjectCollection gameObjects;
    private int i;

    /**
     *  GraphicLifeCounter constructor
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param counter       How many lives are left in the game.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param gameObjects   for remove a heart
     * @param i             which heart to remove.
     */
    public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions, Counter counter,
                              Renderable renderable, GameObjectCollection gameObjects, int i) {
        super(topLeftCorner, dimensions, renderable);
        this.counter = counter;
        this.gameObjects = gameObjects;
        this.i = i;
    }

    /**
     * remove heart when the ball fall from the screen
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
        if (i == counter.value()) {
            gameObjects.removeGameObject(this, Layer.UI);
        }
    }
}

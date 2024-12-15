package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * NumericLifeCounter Class
 */
public class NumericLifeCounter extends GameObject {
    private int curr_num;
    private final TextRenderable textRenderable;
    private Counter counter;
    private GameObjectCollection gameObjects;

    /**
     * NumericLifeCounter class
     * @param Counter how faw life left in the game
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param gameObjects .
     */
    public NumericLifeCounter(Counter Counter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, null);
        textRenderable = new TextRenderable("3");
        textRenderable.setColor(Color.GREEN);
        this.renderer().setRenderable(textRenderable);
        counter = Counter;
        curr_num = counter.value();
        this.gameObjects = gameObjects;
    }

    /**
     * the function update the number on the screen in accordance to the life counter
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
        if (curr_num != counter.value()) {
            curr_num = counter.value();
            if (counter.value() == 4) {
                textRenderable.setString("4");
                textRenderable.setColor(Color.GREEN);
            } if (counter.value() == 3) {
                textRenderable.setString("3");
                textRenderable.setColor(Color.GREEN);
            } if (counter.value() == 2) {
                textRenderable.setString("2");
                textRenderable.setColor(Color.YELLOW);
            } else if (counter.value() == 1) {
                textRenderable.setString("1");
                textRenderable.setColor(Color.RED);
            }
            this.renderer().setRenderable(textRenderable);

        }
    }
}

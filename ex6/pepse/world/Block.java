package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Block extends GameObject {
    public static final int SIZE = 30;

    /**
     * Constructs an instance of the class
     * @param topLeftCorner the top left corner of the block
     * @param renderable the color and shape of the block
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
    }
}

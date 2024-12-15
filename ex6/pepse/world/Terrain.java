package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.PerlinNoise;

import java.awt.*;

public class Terrain {
    private static final float ONE_PERCENTAGE = .1f;
    private static final float TWO_HUNDRED = 200;
    private static final float FOUR_HUNDRED = 400;
    private static final float PART_OF_THE_SCREEN = (float) 2 / 3;
    private final float groundHeightAtX0;
    private final PerlinNoise perlinNoise;
    private final Vector2 windowDimensions;
    private int seed;
    private GameObjectCollection gameObjects;
    private int groundLayer;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);


    /**
     * Constructs an instance of the class
     * @param gameObjects GameObjectCollection instance, keeps all the GameObjects of the program
     * @param groundLayer the layer in witch we will create the terrain of the world
     * @param windowDimensions the dimensions of the window of the program
     * @param seed the seed of the random object that we will use for the terrain height
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = windowDimensions.y() * PART_OF_THE_SCREEN;
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        this.perlinNoise = new PerlinNoise(seed);
    }


    /**
     * The method calculates the height of the ground, following the perlin noise algorithm
     * @param x the position on the horizontal axis
     * @return the height of the ground
     */
    public float groundHeightAt(float x) {
        int newX = closestXDivisibleByBlockSize(x);
        float perlinY = (float) perlinNoise.noise(newX*ONE_PERCENTAGE);
        float y = groundHeightAtX0 + perlinY* TWO_HUNDRED -FOUR_HUNDRED;
        return (float) Math.floor(y / Block.SIZE) * Block.SIZE;
    }


    /*
    This function finds the x index that is divisible by the size of a block
     */
    private int closestXDivisibleByBlockSize(double x) {
        x = Math.floor(x);
        while (x % Block.SIZE != 0) {
            x -= 1;
        }
        return (int) x;
    }


    /**
     * This method creates the ground of the world in a specific range
     * @param minX the lower bound of the range
     * @param maxX the upper bound of the range
     */
    public void createInRange(int minX, int maxX) {
        double minx = Math.floor(minX / Block.SIZE) * Block.SIZE;
        for (double x = minx; x<maxX; x += Block.SIZE) {
            double groundHeight = (double) groundHeightAt((float) x);
            for (double y = windowDimensions.y(); y >= groundHeight; y -= Block.SIZE) {
                GameObject block = new Block(new Vector2((float) x, (float) y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                block.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
                gameObjects.addGameObject(block, groundLayer);
            }

        }

    }

}

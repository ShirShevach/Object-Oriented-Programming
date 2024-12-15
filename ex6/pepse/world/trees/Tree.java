package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.gui.ImageReader;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Tree {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final String FLOWER_PATH = "pepse/flower.png";
    private static final int SIZE_OF_TREE_TOP = 3;
    private static final double PROBABILITY_OF_LEAF = 0.9;
    private static final double PROBABILITY_OF_TREE = 0.1;
    private static final int RANGE_BLOCK_IN_TREE = 6;
    private static final int MIN_BLOCK_IN_TREE = 6;


    private GameObjectCollection gameObjects;
    private int layer;
    private Vector2 windowDimensions;
    private int seed;
    private ImageReader imageReader=null;
    private Terrain terrain;

    /**
     * Creates an instance of the class Tree
     * @param terrain the terrain of the world
     * @param gameObjects GameObjectCollection instance, keeps all the GameObjects of the program
     * @param layer int, the layer in witch we will put the night object
     * @param windowDimensions the dimensions of the window of the program
     * @param seed the seed of the random object that we will use
     */
    public Tree(Terrain terrain, GameObjectCollection gameObjects,
         int layer, Vector2 windowDimensions,
         int seed) {
        this.terrain = terrain;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.windowDimensions = windowDimensions;
        this.seed = seed;

    }

    /**
     * Constructs an instance of the class.
     * @param terrain the terrain of the world
     * @param gameObjects GameObjectCollection instance, keeps all the GameObjects of the program
     * @param layer int, the layer in witch we will put the night object
     * @param windowDimensions the dimensions of the window of the program
     * @param seed the seed of the random object that we will use
     * @param imageReader object that allows us to read an image file
     */
    public Tree(Terrain terrain, GameObjectCollection gameObjects,
                int layer, Vector2 windowDimensions,
                int seed, ImageReader imageReader) {
        this.terrain = terrain;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        this.imageReader = imageReader;
    }

    /**
     * The method creates the trees with a probability of 10% and put them
     * on the earth that is given within a specific range
     * @param minX lower bound of the range
     * @param maxX upper bound of the range
     */
    public void createInRange(int minX, int maxX) {
        double minx = Math.floor(minX / Block.SIZE) * Block.SIZE;
        for (double x = minx; x < maxX; x += Block.SIZE) {
            if (probabilityFromThreshold(PROBABILITY_OF_TREE, (float) x)) {
                makeTree((float) x);
            }
        }
    }

    /*
    Initializes a tree with its trunk and treetop
     */
    private void makeTree(float x) {
        double groundHeight = terrain.groundHeightAt(x);
        double numBlocksInTrunk = makeTrunk(x, groundHeight);
        makeTreeTop(x, numBlocksInTrunk, groundHeight);
    }

    /*
    Makes the treetop and initializes all the leaves
     */
    private void makeTreeTop(float x, double numBlocksInTrunk, double groundHeight) {
        int[] sizeArr = {4, 6, 8};
        Random random2 = new Random(Objects.hash(x, seed));
        int index = random2.nextInt(SIZE_OF_TREE_TOP);
        int size = sizeArr[index];
        float yLeft = (float) (groundHeight - numBlocksInTrunk*Block.SIZE - (size/2)*Block.SIZE);
        float xLeft = x - (size/2)*Block.SIZE;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size+1; j++) {
                if (probabilityFromThreshold(PROBABILITY_OF_LEAF, (xLeft + j*Block.SIZE)*(yLeft + i*Block.SIZE) )) {
                    createLeaf(yLeft, xLeft, i, j);
                }
                else if (imageReader != null){
                    createFlower(yLeft, xLeft, i, j);
                }
            }
        }
    }

    /*
    Creates flower in a specific position on the treetop
     */
    private void createFlower(float yLeft, float xLeft, int i, int j) {
        Vector2 leafLocation = new Vector2(xLeft + j *Block.SIZE, yLeft + i *Block.SIZE );
        GameObject flower = new GameObject(leafLocation, new Vector2(Block.SIZE*2, Block.SIZE*2),
                imageReader.readImage(FLOWER_PATH, true));
        flower.setCenter(flower.getCenter().add(new Vector2(-Block.SIZE/2, -Block.SIZE/2)));
        gameObjects.addGameObject(flower, layer+1);
    }

    /*
    Create a leaf in a specific position on the treetop
    */
    private void createLeaf(float yLeft, float xLeft, int i, int j) {
        Vector2 leafLocation = new Vector2(xLeft + j *Block.SIZE, yLeft + i *Block.SIZE );
        GameObject leaf = new Leaf(leafLocation,
                new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)), gameObjects, layer+1);
        gameObjects.addGameObject(leaf, layer+1);
        gameObjects.layers().shouldLayersCollide(layer+1, Layer.STATIC_OBJECTS, true);
    }

    /*
    Calculates the probability from the position. Return true if the probabilty
    is higher than the given threshold
    */
    private boolean probabilityFromThreshold(double threshold, float x) {
        Random random1 = new Random(Objects.hash(x, seed));
        return random1.nextFloat() <= threshold;
    }

    /*
    Initializes a trunk of a tree
    */
    private float makeTrunk(float x, double groundHeight) {
        Random random3 = new Random(Objects.hash(x, seed));
        float numBlocksInTrunk = random3.nextInt(RANGE_BLOCK_IN_TREE)+MIN_BLOCK_IN_TREE;
        for (int i = 1; i <= numBlocksInTrunk; i++) {
            GameObject block = new Block(new Vector2(x, (float) (groundHeight - i*Block.SIZE)),
                    new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR)));
            gameObjects.addGameObject(block, layer);
            block.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        }
        return numBlocksInTrunk;
    }
}

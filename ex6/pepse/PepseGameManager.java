package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Moon;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;

public class PepseGameManager extends GameManager {
    private static final float CYCLE_LENGTH = 30;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int TREE_LAYER = Layer.STATIC_OBJECTS + 1;
    private static final int RIGHT = 1;
    private static final int LEFT = 0;
    private static final int LEAF_LAYER = Layer.STATIC_OBJECTS + 2;
    private static final int SEED = 227;
    private static final float PART_OF_THE_SCREEN = (float) 2 / 3;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final Color MOON_HALO_COLOR = new Color(5, 246, 233, 61);
    private static final float SET_CAMERA_IN_Y = 100;
    private static final int HALO_LAYER = Layer.BACKGROUND + 10;

    private Terrain terrain;
    private Avatar avatar;
    private Vector2 windowDimensions;
    private Tree tree;
    private float distanceToCreate;
    private float oldX;
    private int leftBorder;
    private int rightBorder;


    /**
     * The main function of the program
     * @param args the arguments of the program
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * This function initializes all the objects of the game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        int seed = SEED;
        this.windowDimensions = windowController.getWindowDimensions();
        this.distanceToCreate = windowDimensions.x()* PART_OF_THE_SCREEN;
        this.leftBorder = (int) - distanceToCreate;
        this.rightBorder = (int) (windowDimensions.x() + distanceToCreate);
        letThereBeLight(imageReader);
        letThereBeEarth(imageReader, seed);
        letUsMakeHumankind(imageReader, inputListener);
    }


    /*
    This method initializes the sky, the night and the sun and moon
     */
    private void letThereBeLight(ImageReader imageReader) {
        Sky.create(this.gameObjects(), windowDimensions, Layer.BACKGROUND);
        Night.create(this.gameObjects(), Layer.FOREGROUND, windowDimensions, CYCLE_LENGTH);
        GameObject sun = Sun.create(this.gameObjects(), Layer.BACKGROUND+1, windowDimensions, CYCLE_LENGTH);
        SunHalo.create(this.gameObjects(), HALO_LAYER, sun, SUN_HALO_COLOR);
        GameObject moon = Moon.create(this.gameObjects(), Layer.BACKGROUND+1, windowDimensions, CYCLE_LENGTH,
                imageReader);
        SunHalo.create(this.gameObjects(), HALO_LAYER, moon, MOON_HALO_COLOR);
    }

    /*
    This function initializes the terrain and the tree with their trunk and treetop
     */
    private void letThereBeEarth(ImageReader imageReader, int seed) {
        terrain = new Terrain(this.gameObjects(), TERRAIN_LAYER, windowDimensions, seed);
        terrain.createInRange(leftBorder, rightBorder);
        tree = new Tree(terrain, this.gameObjects(), TREE_LAYER, windowDimensions, seed, imageReader);
        tree.createInRange(leftBorder, rightBorder);
    }


    /*
    This function initializes the avatar.
     */
    private void letUsMakeHumankind(ImageReader imageReader, UserInputListener inputListener) {
        avatar = Avatar.create(this.gameObjects(), Layer.DEFAULT, windowDimensions,
                inputListener, imageReader);
        avatar.setTerrain(terrain);
        this.gameObjects().layers().shouldLayersCollide(TERRAIN_LAYER, AVATAR_LAYER, true);
        this.gameObjects().layers().shouldLayersCollide(TREE_LAYER, AVATAR_LAYER, true);
        this.oldX = avatar.getCenter().x();
        setCamera(new Camera(avatar, Vector2.ZERO.add(new Vector2(0,-SET_CAMERA_IN_Y)), windowDimensions,
                windowDimensions));
    }


    /**
     * Override of the update function from the super class GameManager
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int side = inWhichSideCreateNewRange();
        if (side != -1) {
            if (side == LEFT) {
                tree.createInRange((int) (leftBorder - distanceToCreate), leftBorder);
                terrain.createInRange((int) (leftBorder - distanceToCreate), leftBorder);
                deleteInRange((int) (rightBorder - distanceToCreate), rightBorder);
                leftBorder -= distanceToCreate;
                rightBorder -= distanceToCreate;
            } else if (side == RIGHT) {
                tree.createInRange(rightBorder, (int) (rightBorder + distanceToCreate));
                terrain.createInRange(rightBorder, (int) (rightBorder + distanceToCreate));
                deleteInRange(leftBorder, (int) (leftBorder + distanceToCreate));
                leftBorder += distanceToCreate;
                rightBorder += distanceToCreate;
            }
            oldX = avatar.getCenter().x();
        }
        this.gameObjects().layers().shouldLayersCollide(TERRAIN_LAYER, AVATAR_LAYER, true);
        this.gameObjects().layers().shouldLayersCollide(TREE_LAYER, AVATAR_LAYER, true);
    }


    /*
    This method deletes all the objects that are in the range that we want to erase
     */
    private void deleteInRange(int minX, int maxX) {
        for (int layer = TERRAIN_LAYER; layer < LEAF_LAYER + 1; layer++) {
            for (GameObject block : this.gameObjects().objectsInLayer(layer)) {
                if (minX < block.getCenter().x() && block.getCenter().x() < maxX) {
                    gameObjects().removeGameObject(block, layer);
                }
            }
        }
    }


    /*
    This function returns the sid in which we want to create a new range
     */
    private int inWhichSideCreateNewRange() {
        float currentX = avatar.getCenter().x();
        float distance = currentX - oldX;
        if (Math.abs(distance) >= distanceToCreate) {
            if (distance > 0) {
                return RIGHT;
            }
            return LEFT;
        }
        return -1;
    }
}

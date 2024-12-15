package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final float JUMP_SPEED = 300;
    private static final float FLIGHT_SPEED = 200;
    private static final float ENERGY_NUMBER = 100;
    private static final float AVATAR_SIZE = 70;
    private static final double TIME_CLIPS_IDLE = 0.5;
    private static final double TIME_CLIPS_NOT_IDLE = 0.1;
    private static final String PICTURE_FILE = "pepse/";
    private static final String IDLE_RIGHT_0 = "Margery_Idle right_0.png";
    private static final String IDLE_RIGHT_1 = "Margery_Idle right_1.png";
    private static final String IDLE_LEFT_0 = "Margery_Idle Left_0.png";
    private static final String AVATAR_NAME = "Daisy";
    private static final int NUM_OF_RUN = 6;
    private static final String RUN_LEFT = "Margery_run left_%d.png";
    private static final String RUN_RIGHT = "Margery_run right_%d.png";
    private static final String JUMP_LEFT = "Margery_jump left_0.png";
    private static final String JUMP_RIGHT = "Margery_jump right_0.png";
    private static final String IDLE_LEFT_1 = "Margery_Idle Left_1.png";
    private static final String FLY_RIGHT_UP = "FlyRightUp.png";
    private static final String FLY_RIGHT_DOWN = "FlyRightDown.png";
    private static final String FLY_LEFT_UP = "FlyLeftUp.png";
    private static final String FLY_LEFT_DOWN = "FlyLeftDown.png";
    private static final double ENERGY_VARIATION = 0.5;
    private static final float ENERGY_TEXT_SIZE = 20;
    private static final float HALF = 2;
    private static final float ACCELERATION_Y = 500;
    private static GameObjectCollection gameObjects;
    private static Vector2 windowDimensions;
    private static UserInputListener inputListener;
    private static ImageReader imageReader;
    private Terrain terrain;
    private static AnimationRenderable leftIdleAnimation;
    private static float energy;
    private static TextRenderable textRenderable;
    private static GameObject energyObject;
    private AnimationRenderable leftRunAnimation;
    private AnimationRenderable rightRunAnimation;
    private AnimationRenderable rightIdleAnimation;
    private int side;
    private ImageRenderable rightJumpPicture;
    private ImageRenderable leftJumpPicture;
    private AnimationRenderable rightFlyAnimation;
    private AnimationRenderable leftFlyAnimation;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, AnimationRenderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.transform().setAccelerationY(ACCELERATION_Y);
    }

    /*
    the function set the field terrain
     */
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
        Vector2 initLocation = new Vector2(windowDimensions.x()/HALF,
                (float) terrain.groundHeightAt(windowDimensions.x()/HALF)).add(new Vector2(0, -AVATAR_SIZE/2));
        this.setCenter(initLocation);
    }

    /*
    The method creates all the animations and images of the avatar
     */
    private void createAnimation() {
        rightIdleAnimation = createAnimationWith2Paths(IDLE_RIGHT_0, IDLE_RIGHT_1, TIME_CLIPS_IDLE, imageReader);
        leftRunAnimation = createRunAnimation(RUN_LEFT);
        rightRunAnimation = createRunAnimation(RUN_RIGHT);
        leftJumpPicture = createJumpAnimation(JUMP_LEFT);
        rightJumpPicture = createJumpAnimation(JUMP_RIGHT);
        rightFlyAnimation = createAnimationWith2Paths(FLY_RIGHT_UP, FLY_RIGHT_DOWN, TIME_CLIPS_NOT_IDLE, imageReader);
        leftFlyAnimation = createAnimationWith2Paths(FLY_LEFT_UP, FLY_LEFT_DOWN, TIME_CLIPS_NOT_IDLE, imageReader);
    }


    /*
    This method creates the image of the avatar when it jumps
     */
    private ImageRenderable createJumpAnimation(String path) {
        return imageReader.readImage(PICTURE_FILE + path, true);
    }


    /*
    This method creates the animation of the avatar when it runs
     */
    private AnimationRenderable createRunAnimation(String path) {
        Renderable[] runArr = new Renderable[NUM_OF_RUN];
        for (int i = 0; i < NUM_OF_RUN; i++) {
            runArr[i] = imageReader.readImage(
                    String.format(PICTURE_FILE + path, i), true);
        }
        return new AnimationRenderable(runArr, TIME_CLIPS_NOT_IDLE);
    }

    /*
    This method creates the animation when the avatar blinks.
     */
    private static AnimationRenderable createAnimationWith2Paths(String path1, String path2, double waitTime,
                                                          ImageReader imageReader) {
        String idle1 = PICTURE_FILE + path1;
        String idle2 = PICTURE_FILE + path2;
        Renderable[] idleArr = new Renderable[]{imageReader.readImage(idle1, true),
                imageReader.readImage(idle2, true)};
        return new AnimationRenderable(idleArr, waitTime);
    }

    /**
     * This methods creates an instance of avatar
     * @param gameObjects GameObjectCollection instance, keeps all the GameObjects of the program
     * @param layer int, the layer in witch we will put the night object
     * @param windowDimensions the dimensions of the window of the program
     * @param inputListener allows us to knows the keys pressed by the user
     * @param imageReader allows us to read an image file
     * @return nothing
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 windowDimensions,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        Avatar.gameObjects = gameObjects;
        Avatar.windowDimensions = windowDimensions;
        Avatar.inputListener = inputListener;
        Avatar.imageReader = imageReader;
        Avatar.energy = ENERGY_NUMBER;
        Avatar.leftIdleAnimation = createAnimationWith2Paths(IDLE_LEFT_0, IDLE_LEFT_1, TIME_CLIPS_IDLE, imageReader);
        Vector2 topLeftCorner = Vector2.ZERO;
        Avatar avatar = new Avatar(topLeftCorner, new Vector2(AVATAR_SIZE,AVATAR_SIZE),
                leftIdleAnimation);
        avatar.createAnimation();
        avatar.setTag(AVATAR_NAME);
        gameObjects.addGameObject(avatar, layer);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        createNumericEnergy();
        return avatar;
    }

    /*
    This method renders the energy of the avatar in the top left corner of the window
     */
    private static void createNumericEnergy() {
        textRenderable = new TextRenderable(Float.toString(ENERGY_NUMBER));
        energyObject = new GameObject(Vector2.ZERO, new Vector2(ENERGY_TEXT_SIZE, ENERGY_TEXT_SIZE), textRenderable);
        Avatar.gameObjects.addGameObject(energyObject, Layer.UI);
        energyObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Override of the method onCollisionEnter from the super class GameObject
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.transform().setVelocityX(0);
    }

    /**
     * Override of the method update from the super class GameObject
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
        updateHorizontalMovement();
        updateVerticalMovement();
        updateEnergy();
        fixUpHeight();
    }

    /*
    This method update the energy of the avatar
     */
    private void updateEnergy() {
        if (avatarOnTheGround() && energy < ENERGY_NUMBER) {
            energy += ENERGY_VARIATION;
            textRenderable.setString(Float.toString(energy));
            energyObject.renderer().setRenderable(textRenderable);
        }
    }

    /*
    This function handles the jump and the flight of the avatar
     */
    private void updateVerticalMovement() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            if (avatarOnTheGround() && !inputListener.isKeyPressed(KeyEvent.VK_SHIFT)) {
                handleJump();
            }
            else if (avatarFlies() && energy > 0) {
                handleFly();
            }
        }
    }

    /*
   This function handles the flight of the avatar
    */
    private void handleFly() {
        float movementDirY;
        energy -= ENERGY_VARIATION;
        textRenderable.setString(Float.toString(energy));
        energyObject.renderer().setRenderable(textRenderable);
        movementDirY = -FLIGHT_SPEED;
        transform().setVelocityY(movementDirY);
        if (side == LEFT) {
            renderer().setRenderable(leftFlyAnimation);
        }
        else {
            renderer().setRenderable(rightFlyAnimation);
        }
    }


    /*
    This function handles the jump of the avatar
    */
    private void handleJump() {
        float movementDirY;
        movementDirY = -JUMP_SPEED;
        transform().setVelocityY(movementDirY);
        if (side == LEFT) {
            renderer().setRenderable(leftJumpPicture);
        }
        else {
            renderer().setRenderable(rightJumpPicture);
        }
    }


    /*
    This function handles the right and left movements of the avatar
     */
    private void updateHorizontalMovement() {
        float movementDirX = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
                movementDirX -= MOVEMENT_SPEED;
                renderer().setRenderable(leftRunAnimation);
                side = LEFT;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
                movementDirX += MOVEMENT_SPEED;
                renderer().setRenderable(rightRunAnimation);
                side = RIGHT;
        }
        if (!inputListener.isKeyPressed(KeyEvent.VK_LEFT) && !inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (side == LEFT) {
                renderer().setRenderable(leftIdleAnimation);
            }
            else {
                renderer().setRenderable(rightIdleAnimation);
            }
        }
        transform().setVelocityX(movementDirX);
    }


    /*
    This method avoid the situation in witch the avatar sinks in the terrain
     */
    private void fixUpHeight() {
        if (this.getCenter().y() + this.getDimensions().y()/HALF > terrain.groundHeightAt(this.getCenter().x())+20) {
            this.setCenter(new Vector2(this.getCenter().x(),
                    (float) terrain.groundHeightAt(this.getCenter().x()) - this.getDimensions().y()/2));
            transform().setVelocityY(0);
        }
    }

    /*
    This function checks if the avatar is on the ground or not
     */
    private boolean avatarOnTheGround() {
        return this.getVelocity().y() == 0;
    }


    /*
    This function checks if the avatar is flying or not
     */
    private boolean avatarFlies() {
        return inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT);
    }

}

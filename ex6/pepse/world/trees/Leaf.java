package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

public class Leaf extends Block {
    private static final float FADEOUT_TIME = 15;
    private static final int LIFE_TIME_RANGE = 90;
    private static final int MIN_LIFE_TIME = 10;
    private static final int DEATH_TIME_RANGE = 2;
    private static final int MIN_DEATH_TIME = 4;
    private static final int HALF = 2;
    private static final float FALL_VELOCITY = 30;
    private static final Float HORIZONTAL_MOVEMENT = 20f;
    private static final Float LEAF_ANGLE_IN_WIND = 10f;
    private static final int START_MOVE_RANGE = 5;
    private static final int SIZE_BLOCK_VARIATION = 3;
    private Vector2 topLeftCorner;
    private GameObjectCollection gameObjects;
    private int layer;
    private Random random;
    private Transition<Float> t;

    /**
     * Constructs an instance of the class.
     * @param topLeftCorner The position of the object on the screen.
     * @param renderable A printable object corresponding to the class.
     * @param gameObjects GameObjectCollection instance, keeps all the GameObjects of the program
     * @param layer the layer of leaf in the game
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable,
                GameObjectCollection gameObjects, int layer) {
        super(topLeftCorner, renderable);
        this.topLeftCorner = topLeftCorner;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.random = new Random();
        addFunctionalityToLeaf();
    }

    /*
    the function add movement in the wind to the leaf and init its cycle life.
     */
    private void addFunctionalityToLeaf() {
        makeTheWindBlowsOnTheLeaves();
        initLifeLeaf();
    }

    /*
    the function init life cycle to the leaf
     */
    private void initLifeLeaf () {
        float lifeTime = random.nextInt(LIFE_TIME_RANGE) + MIN_LIFE_TIME;
        float deathTime = random.nextInt(DEATH_TIME_RANGE) + MIN_DEATH_TIME;
        leafCycle(lifeTime, deathTime);
    }

    /*
    the function do one life cycle to the leaf.
     */
    private void leafCycle(float life, float death) {
        Runnable returnLeaf = () -> {
            this.setCenter(topLeftCorner.add(new Vector2(Block.SIZE/HALF, Block.SIZE/HALF)));
            this.renderer().setOpaqueness(1f);
            this.transform().setVelocityX(0);
            initLifeLeaf ();
        };

        Runnable leafFallsAndFadesOut = () -> {
            this.renderer().fadeOut(FADEOUT_TIME,
                    ()->new ScheduledTask(this, death, false, returnLeaf));
            this.transform().setVelocityY(FALL_VELOCITY);
            this.leafTwirlsInTheFall();
        };

        new ScheduledTask(this, life, false, leafFallsAndFadesOut);
    }

    /*
    the function twirls the leaf and make its fall.
     */
    private void leafTwirlsInTheFall() {
        t = new Transition<Float>(
                this, // the game object being changed
                (v) -> this.transform().setVelocityX(v), // the method to call
                -HORIZONTAL_MOVEMENT, // initial transition value
                HORIZONTAL_MOVEMENT, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                FADEOUT_TIME / 8, // transtion fully over a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null);
    }

    /*
    the function override the super function, and handle the case when
    the leaf hit the land.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.transform().setVelocityY(0);
        this.transform().setVelocityX(0);
        this.removeComponent(t);
        super.update(0);
    }

    /*
    the function make the leaf move and change its size in the wind.
     */
    private void makeTheWindBlowsOnTheLeaves() {
        new ScheduledTask(this, random.nextFloat() + random.nextInt(START_MOVE_RANGE),
                false, ()->new Transition<Float>(
                this,
                this.renderer()::setRenderableAngle, -LEAF_ANGLE_IN_WIND, LEAF_ANGLE_IN_WIND,
                Transition.LINEAR_INTERPOLATOR_FLOAT, 1,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null));

        new ScheduledTask(this, random.nextFloat() + random.nextInt(START_MOVE_RANGE),
                false, ()->new Transition<Vector2>(
                this, this::setDimensions, new Vector2(Block.SIZE, Block.SIZE),
                new Vector2(Block.SIZE - SIZE_BLOCK_VARIATION, Block.SIZE + SIZE_BLOCK_VARIATION),
                Transition.LINEAR_INTERPOLATOR_VECTOR, 1,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null));
    }


}

package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * SpecialHeart Class
 */
public class SpecialHeart extends GameObject {
    private static int HAS_HEART = 0;
    private final GameObjectCollection gameObjects;
    private final Vector2 windowDimensions;
    private final Counter lifeCounter;
    private final Renderable graphicLifeImage;
    private float space;


    /**
     * SpecialHeart constructor
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param gameObjects .
     * @param windowDimensions to know where to stop.
     * @param lifeCounter to know if to add more life
     * @param graphicLifeImage the picture of the heart
     * @param space from the start of the screen
     */
    public SpecialHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        GameObjectCollection gameObjects, Vector2 windowDimensions,
                        Counter lifeCounter, Renderable graphicLifeImage, float space) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects = gameObjects;
        this.windowDimensions = windowDimensions;
        this.lifeCounter = lifeCounter;
        this.graphicLifeImage = graphicLifeImage;
        this.space = space;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        if (super.shouldCollideWith(other) &&
                (other instanceof Paddle) &&
                !(other instanceof MorePaddle)) {
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getTopLeftCorner().y() > windowDimensions.y()) {
            gameObjects.removeGameObject(this);
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjects.removeGameObject(this);
        addLife();
    }

    private void addLife() {
        if (lifeCounter.value() < 4) {
            GameObject heart = new GraphicLifeCounter(Vector2.ZERO, new Vector2(20, 20), lifeCounter,
                    graphicLifeImage, gameObjects, lifeCounter.value());
            heart.setCenter(new Vector2(space + 20 * lifeCounter.value(), (int) windowDimensions.y() - 30));
            gameObjects.addGameObject(heart, Layer.UI);
            lifeCounter.increment();
        }
    }
}

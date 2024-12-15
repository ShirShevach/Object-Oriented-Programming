package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Brick;
import src.gameobjects.GraphicLifeCounter;
import src.gameobjects.SpecialHeart;

/**
 *  * The function manage the Heart Strategy.
 */
public class HeartStrategy extends StrategyDecorator{
    private GameObjectCollection gameObjects;
    private Renderable graphicLifeImage;
    private Vector2 windowDimensions;
    private float space;
    private Counter lifeCounter;

    /**
     * HeartStrategy's constructor
     * @param gameObjects the class game objects
     * @param strategy that the class save to operate if it's not null
     * @param graphicLifeImage picture of heart
     * @param windowDimensions the dimension of the game's window.
     * @param space from 0 to the start of the first heart (axis x)
     * @param lifeCounter count the life the player left.
     */
    public HeartStrategy(GameObjectCollection gameObjects,
                         Strategy strategy,
                         Renderable graphicLifeImage,
                         Vector2 windowDimensions,
                         float space,
                         Counter lifeCounter) {
        super(gameObjects, strategy);
        this.gameObjects = gameObjects;
        this.graphicLifeImage = graphicLifeImage;
        this.windowDimensions = windowDimensions;
        this.space = space;
        this.lifeCounter = lifeCounter;
    }

    /**
     * the function handle the situation that we hit a
     * brick that suppose to operate the heart.
     * @param brick the brick the happened the hit
     * @param other the ball the hited the brick
     * @param counter number of brick counter
     */
    @Override
    public void OnCollision(Brick brick, GameObject other, Counter counter) {
        super.OnCollision(brick, other, counter);

        // create heart from the middle of the brick
        GameObject heart = new SpecialHeart(new Vector2(brick.getCenter()),
                new Vector2(20,20), graphicLifeImage,gameObjects, windowDimensions,
                lifeCounter, graphicLifeImage, space);
        heart.setVelocity(Vector2.DOWN.mult(100));
        gameObjects.addGameObject(heart);
    }
}

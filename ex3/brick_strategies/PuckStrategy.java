package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Brick;
import src.gameobjects.BasisBall;
import java.util.Random;

/**
 * The function manage the add 3 balls to the game from the brick.
 */
public class PuckStrategy extends StrategyDecorator {
    private static final float BALL_SPEED = 250;
    private GameObjectCollection gameObjects;
    private Renderable ballImage;
    private Sound collisionSound;

    /**
     * MorePaddleStrategy's constructor
     * @param gameObjects the parameter for the class
     * @param strategy that the class save to operate if it's not null
     * @param ballImage image of the ball
     * @param collisionSound sound that play when the balls hit the bricks
     */
    public PuckStrategy(GameObjectCollection gameObjects, Strategy strategy, Renderable ballImage,
                        Sound collisionSound) {
        super(gameObjects, strategy);
        this.gameObjects = gameObjects;
        this.ballImage = ballImage;
        this.collisionSound = collisionSound;
    }

    /**
     * the function handle the situation that we hit a
     * brick that suppose to add 3 balls from the removing brick.
     * @param brick the current brick
     * @param other the ball that hit the brick
     * @param counter number of bricks that left in the game
     */
    @Override
    public void OnCollision(Brick brick, GameObject other, Counter counter) {
        super.OnCollision(brick, other, counter);
        // create 3 puck balls
        for (int i = 0; i < 3; i++) {
            createBall(brick.getCenter());
        }
    }

    /*
    A helper function that creates a ball
     and adds it to the game.
     */
    private void createBall(Vector2 center) {
        BasisBall ball =
                new BasisBall(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        ball.setCenter(center);
        float ballVelX = BALL_SPEED;
        float ballVellY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVellY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVellY));
        gameObjects.addGameObject(ball);
    }
}

package Bricker.main;
import Bricker.gameobjects.Ball;
import Bricker.gameobjects.Paddle;
import Bricker.movementStragedy.AIMovementstragedy;
import Bricker.movementStragedy.UserMovementStragedy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final float BALL_SPEED = 250;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;

    BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // create ball
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        this.ball =
                new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_SPEED));
        windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2));
        float ballVelX = BALL_SPEED;
        float ballVellY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVellY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVellY));
        gameObjects().addGameObject(ball);

        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);
        // create user paddle
        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(200, 20), paddleImage, new UserMovementStragedy(inputListener));
        userPaddle.setCenter(new Vector2(windowDimensions.x()/2, (int)windowDimensions.y()-30));
        gameObjects().addGameObject(userPaddle);

        // create ai paddle
        GameObject aiPaddle = new Paddle(Vector2.ZERO, new Vector2(200, 20), paddleImage, new AIMovementstragedy(ball));
        aiPaddle.setCenter(new Vector2(windowDimensions.x()/2, 30));
        gameObjects().addGameObject(aiPaddle);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        chackForGameEnd();
    }

    private void chackForGameEnd() {
        float ballHeights = ball.getCenter().y();
        String prompt = "";
        if (ballHeights < 0) {
            // we lost
            prompt = "You win!";
        }
        if (ballHeights > windowDimensions.y()) {
            // we win
            prompt = "You lose!";
        }
        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    public static void main(String[] args) {
        new BrickerGameManager("Bricker", new Vector2(700, 500)).run();

    }

}



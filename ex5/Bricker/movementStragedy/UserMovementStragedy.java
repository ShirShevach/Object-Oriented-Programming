package Bricker.movementStragedy;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class UserMovementStragedy implements MovementStragedy {
    private UserInputListener inputListener;

    public UserMovementStragedy(UserInputListener inputListener) {

        this.inputListener = inputListener;
    }

    @Override
    public Vector2 calcMovementDir(GameObject owner) {
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        return movementDir;
    }
}

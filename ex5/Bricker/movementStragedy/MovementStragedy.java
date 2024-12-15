package Bricker.movementStragedy;

import Bricker.gameobjects.Paddle;
import danogl.GameObject;
import danogl.util.Vector2;

public interface MovementStragedy {
    Vector2 calcMovementDir(GameObject owner);
}

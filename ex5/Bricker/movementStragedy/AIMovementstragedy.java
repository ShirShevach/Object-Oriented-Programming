package Bricker.movementStragedy;

import danogl.GameObject;
import danogl.util.Vector2;

public class AIMovementstragedy implements MovementStragedy{
    private GameObject objectToFollow;
    public AIMovementstragedy(GameObject objectToFollow) {
        this.objectToFollow = objectToFollow;
    }

    @Override
    public Vector2 calcMovementDir(GameObject owner) {
        Vector2 movementDir = Vector2.ZERO;
        if (objectToFollow.getCenter().x() < owner.getCenter().x())
            movementDir = Vector2.LEFT;
        if (objectToFollow.getCenter().x() > owner.getCenter().x())
            movementDir = Vector2.RIGHT;
        return movementDir;
    }
}

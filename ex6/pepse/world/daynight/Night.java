package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {
    private static final float MIDNIGHT_OPACITY = 0.7f;


    /**
     * The method creates a GameObject and put a veil on the screen with a changing opacity.
     * @param gameObjects GameObjectCollection instance, keeps all the GameObjects of the program
     * @param layer int, the layer in witch we will put the night object
     * @param windowDimensions the dimensions of the window of the program
     * @param cycleLength the length of a full day (in seconds)
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(new Color(0f, 0f, 0f, MIDNIGHT_OPACITY)));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag("night");

        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength/2, // transtion fully over half a day
        Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
        null); // nothing further to execute upon reaching final value

        return night;
    }
}

package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    private static final float SUN_SIZE = 80;
    private static final float HEIGHT_SUN_POSITION = (float) 1/6;
    private static final float PART_IN_X = 2;
    private static final String SUN_NAME = "sun";
    private static final Float INITIAL_DEGREE = 270f;
    private static final Float CYCLE_IN_DEGREE = 360f;


    /*
    The function calculates the current position of the sun from the parametric equation of its ellipse.
     */
    private static Vector2 calcSunPosition(Vector2 windowDimensions,
                                           float angleInSky) {
        float a = windowDimensions.x() / PART_IN_X;
        float b = windowDimensions.y() * (1-HEIGHT_SUN_POSITION);
        float centerX = windowDimensions.x() / 2;
        float centerY = windowDimensions.y();
        double x = centerX + a*Math.cos(Math.toRadians(angleInSky));
        double y = centerY + b*Math.sin(Math.toRadians(angleInSky));
        return new Vector2((float) x, (float) y);
    }

    /**
     * The method creates a GameObject sun and initializes its ellipse.
     * @param gameObjects GameObjectCollection instance, keeps all the GameObjects of the program
     * @param layer int, the layer in witch we will put the night object
     * @param windowDimensions the dimensions of the window of the program
     * @param cycleLength the length of a full day (in seconds)
     * @return nothing
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {
        Vector2 initialSunPosition = new Vector2(windowDimensions.x()/2,
                windowDimensions.y()* HEIGHT_SUN_POSITION);
        GameObject sun = new GameObject(Vector2.ZERO, new Vector2(SUN_SIZE, SUN_SIZE),
                new OvalRenderable(Color.YELLOW));
        sun.setCenter(initialSunPosition);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(SUN_NAME);

        new Transition<Float>(
                sun, // the game object being changed
                (alpha) -> sun.setCenter(calcSunPosition(windowDimensions, alpha)), // the method to call
                INITIAL_DEGREE, // initial transition value
                INITIAL_DEGREE+CYCLE_IN_DEGREE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength, // transtion fully over a day
                Transition.TransitionType.TRANSITION_LOOP, // Choose appropriate ENUM value
                null); // nothing further to execute upon reaching final value

        return sun;
    }
}

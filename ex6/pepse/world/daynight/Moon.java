package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Moon{
    private static final float SUN_SIZE = 100;

    private static final String MOON_PATH = "pepse/moon.png";
    private static final Float INITIAL_DEGREE = 90f;
    private static final Float CYCLE_IN_DEGREE = 360f;
    private static final String MOON_NAME = "moon";
    private static final float PART_IN_X = 2;
    private static final float PART_IN_Y = (float) 5 / 6;
    private static final float HALF = 2;

    /*
    The function calculates the current position of the moon from the parametric equation of its ellipse.
    */
    private static Vector2 calcMoonPosition(Vector2 windowDimensions,
                                            float angleInSky) {
        float a = windowDimensions.x() / PART_IN_X;
        float b = windowDimensions.y() * PART_IN_Y;
        float centerX = windowDimensions.x() / HALF;
        float centerY = windowDimensions.y();
        double x = centerX + a*Math.cos(Math.toRadians(angleInSky));
        double y = centerY + b*Math.sin(Math.toRadians(angleInSky));
        return new Vector2((float) x, (float) y);
    }

    /**
     * The method creates a GameObject moon and initializes its ellipse.
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
            float cycleLength, ImageReader imageReader) {
        Vector2 initialMoonPosition = new Vector2(windowDimensions.x()/HALF, -windowDimensions.y());
        Renderable moonImage  = imageReader.readImage(MOON_PATH,
                true);
        GameObject moon = new GameObject(Vector2.ZERO, new Vector2(SUN_SIZE, SUN_SIZE), moonImage);
        moon.setCenter(initialMoonPosition);
        moon.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(moon, layer);
        moon.setTag(MOON_NAME);

        new Transition<Float>(
                moon, // the game object being changed
                (alpha) -> moon.setCenter(calcMoonPosition(windowDimensions, alpha)), // the method to call
                INITIAL_DEGREE, // initial transition value
                INITIAL_DEGREE+CYCLE_IN_DEGREE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength, // transtion fully over a day
                Transition.TransitionType.TRANSITION_LOOP, // Choose appropriate ENUM value
                null); // nothing further to execute upon reaching final value

        return moon;
    }

}

package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    /**
     * This method creates the halo of the sun
     * @param gameObjects GameObjectCollection instance, keeps all the GameObjects of the program
     * @param layer int, the layer in witch we will put the night object
     * @param sun the sun object that will get the halo
     * @param color the color of the sun halo
     * @return nothing
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {
        GameObject sunHalo = new GameObject(Vector2.ZERO, sun.getDimensions().mult(2),
                new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.setTag("sunHalo");
        sunHalo.addComponent((deltaTime) -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }

}

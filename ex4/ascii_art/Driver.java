package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_art.img_to_char.CharRenderer;
import image.Image;

import java.util.logging.Logger;

/**
 * Driver class, get the args, and start the Shell
 */
public class Driver {
    private static final String ERROR_LENGTH_ARGS = "USAGE: java asciiArt ";
    private static final String ERROR_OPEN_FILE = "Failed to open image file ";

    /**
     * the main function. get the args, open the picture,
     * convert it to Image type, and start the function run
     * in class Shell
     * @param args the args that got in the edit configuration.
     * @throws Exception if the file doesn't exist.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(ERROR_LENGTH_ARGS);
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe(ERROR_OPEN_FILE + args[0]);
            return;
        }
        new Shell(img).run();
    }
}
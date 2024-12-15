package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.FileImage;
import image.Image;

import java.util.HashSet;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Shell class. Simulates running a command line on a computer.
 */
public class Shell {
    private static final String ARROWS = ">>> ";
    private static final String EXIT = "exit";
    private static final String CHAR = "chars";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String RES = "res";
    private static final String CONSOLE = "console";
    private static final String RENDER = "render";
    private static final String INCORRECT_COMMAND_IN_RUN = "Did not executed due to incorrect command";

    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final String FONT = "Ariel";
    private static final String ALL_WORD = "all";
    private static final String SPACE_WORD = "space";
    private static final Character SPACE = ' ';
    private static final char GAL = '~';
    private static final char HYPHEN = '-';
    private static final String ERROR_ADD_OR_REMOVE_REQUEST = "Did not %s due to incorrect format";
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final char ZERO = '0';
    private static final int INIT_NUMBER_OF_CHARS = 10;
    private static final String OUTPUT_FILE_NAME = "out.html";
    private static final String FONT_NAME = "Courier New";
    private static final String ERROR_RES_LIMIT = "Did not change due to exceeding boundaries";
    private static final int DOUBLE = 2;
    private static final String WIDTH_SET_MASSAGE = "Width set to %s";
    private static final int LENGTH_REQUEST = 3;
    private static final int INDEX_SPACE = 1;
    private static final int INDEX_CHAR_1 = 0;
    private static final int INDEX_CHAR_2 = 2;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private final ConsoleAsciiOutput consoleAsciiOutput;
    private final HtmlAsciiOutput htmlAsciiOutput;
    private int charsInRow;
    private final BrightnessImgCharMatcher charMatcher;
    private final HashSet<Character> charsSet;
    private Consumer<Character> addFunction;
    private Consumer<Character> removeFunction;
    private boolean console;

    /**
     * Shell's constructor. init the fields
     * @param img the img that the class operate with.
     */
    Shell(Image img) {
        minCharsInRow = Math.max(1, img.getWidth()/img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow),
                minCharsInRow);
        console = false;
        consoleAsciiOutput = new ConsoleAsciiOutput();
        htmlAsciiOutput = new HtmlAsciiOutput(OUTPUT_FILE_NAME, FONT_NAME);
        charMatcher = new BrightnessImgCharMatcher((FileImage) img, FONT);
        charsSet = new HashSet<>();
        addFunction = charsSet::add;
        removeFunction = charsSet::remove;
        for (int i = 0; i < INIT_NUMBER_OF_CHARS; i++) {
            char a = (char) (ZERO + i);
            charsSet.add(a);
        }
    }

    /**
     * The function requests input from the user, and handles it.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(ARROWS);
        String ans = scanner.nextLine();
        while (!ans.equals(EXIT)) {
            if (ans.equals(CHAR)) {
                printChars();
            } else if (ans.startsWith(ADD) && ans.charAt(ADD.length()) == SPACE) {
                eatRequest(ans.substring(ADD.length()+1), addFunction, ADD);
            } else if (ans.startsWith(REMOVE) && ans.charAt(REMOVE.length()) == SPACE) {
                eatRequest(ans.substring(REMOVE.length()+1), removeFunction, REMOVE);
            } else if (ans.startsWith(RES) && ans.charAt(RES.length()) == SPACE) {
                resNumChars(ans.substring(RES.length()+1));
            } else if (ans.equals(CONSOLE)) {
                console = true;
            } else if (ans.equals(RENDER)) {
                convertImgToAscii();
            } else {
                System.out.println(INCORRECT_COMMAND_IN_RUN);
            }
            System.out.print(ARROWS);
            ans = scanner.nextLine();
        }
    }

    /*
    the function convert the big picture to ascii picture.
     */
    private void convertImgToAscii() {
        Character[] charList = new Character[charsSet.size()];
        int i = 0;
        for (char c: charsSet) {
            charList[i++] = c;
        }
        var chars = charMatcher.chooseChars(charsInRow, charList);
        if (console) {
            consoleAsciiOutput.output(chars);
        }
        else {
            htmlAsciiOutput.output(chars);
        }
    }

    /*
    The function increases and decreases the resolution of the image.
     */
    private void resNumChars(String substring) {
        if (substring.equals(UP)) {
            if (charsInRow*DOUBLE > maxCharsInRow) {
                System.out.println(ERROR_RES_LIMIT);
                return;
            }
            charsInRow *= DOUBLE;

        }
        else if (substring.equals(DOWN)) {
            if (charsInRow/DOUBLE < minCharsInRow) {
                System.out.println(ERROR_RES_LIMIT);
                return;
            }
            charsInRow /= DOUBLE;
        }
        else {
            System.out.println(INCORRECT_COMMAND_IN_RUN);
            return;
        }
        System.out.println(String.format(WIDTH_SET_MASSAGE, charsInRow));
    }

    /*
    the function prints the chars with space between one to another.
     */
    private void printChars() {
        for (char c: charsSet) {
            System.out.print(c + SPACE);
        }
        System.out.println();
    }

    /*
    the function handle the add and remove request.
     */
    private void eatRequest(String substring, Consumer<Character> function, String name) {
        BiConsumer<Character, Character> funcChars = (c1, c2) -> {
        for (int c = Math.min(c1, c2); c <= Math.max(c1, c2); c++) {
            function.accept((char) c);
        }};
        if (substring.length() == 1) {
            function.accept(substring.charAt(INDEX_CHAR_1));
        } else if (substring.equals(ALL_WORD)) {
            funcChars.accept(SPACE, GAL);
        } else if (substring.equals(SPACE_WORD)) {
            function.accept(SPACE);
        } else if (substring.length() == LENGTH_REQUEST && substring.charAt(INDEX_SPACE) == HYPHEN) {
            funcChars.accept(substring.charAt(INDEX_CHAR_1), substring.charAt(INDEX_CHAR_2));
        } else {
            System.out.println(String.format(ERROR_ADD_OR_REMOVE_REQUEST, name));
        }
    }
}

package ascii_art.img_to_char;


import image.FileImage;
import image.Image;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class BrightnessImgCharMatcher {
    private static final double MAX_REG = 255;
    private static final int PIXELS = 16;
    private static final Double RED_NUM = 0.2126;
    private static final Double GREEN_NUM = 0.7152;
    private static final Double BLUE_NUM = 0.0722;
    private static final char SPACE = ' ';
    private Double minBrightness;
    private Double maxBrightness;
    HashMap<Character, Double> brightnessChar;
    private int pixels;
    private FileImage img;
    private String fontName;
    private int numCharsInRow;
    private boolean doLinearStretch = false;

    /**
     * BrightnessImgCharMatcher's constructor
     * @param img the img in type FileImage
     * @param fontName the font of the execute picture.
     */
    public BrightnessImgCharMatcher(Image img, String fontName) {
        this.img = (FileImage) img;
        this.fontName = fontName;
        this.pixels = PIXELS;
    }

    /**
     * the function get numCharsInRow and charSet, and do ascii picture
     * @param numCharsInRow num of chars that will be in one row on the ascii picture.
     * @param charSet set of chars the function will use to draw the picture.
     * @return ascii picture (char[][])
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        this.numCharsInRow = numCharsInRow;
        createMapChars(charSet);
        int numRows = img.getHeight()/(img.getWidth()/numCharsInRow);
        char[][] charPicture = new char[numRows][numCharsInRow];
        img.setNumCols(numCharsInRow);
        Iterator<Color[][]> subImgArray = img.iterator();
        for (int i = 0; i<numRows; i++) {
            for (int j = 0; j < numCharsInRow; j++) {
                Double averageColor = averageSubImgBrightness(subImgArray.next());
                char closetsChar = findClosestChar(averageColor);
                charPicture[i][j] = closetsChar;
            }
        }
        return charPicture;
    }

    /*
    the function find the closets char to some color. and return the char.
     */
    private char findClosestChar(Double averageColor) {
        Double minDistance = Double.POSITIVE_INFINITY;
        char minC = SPACE;
        for (char c : brightnessChar.keySet()) {
            if (Math.abs(brightnessChar.get(c)-averageColor) < minDistance) {
                minDistance = Math.abs(brightnessChar.get(c)-averageColor);
                minC = c;
            }
        }
        return minC;
    }

    /*
    the function create hashMap of the chars, that will be like this:
    {char : brightness, ...}
     */
    private void createMapChars(Character[] charSet) {
        this.brightnessChar = new HashMap<>();
        this.minBrightness = Double.POSITIVE_INFINITY;
        this.maxBrightness = Double.NEGATIVE_INFINITY;
        for (char c : charSet)
            if (brightnessChar.get(c) == null)
                addChar(c);
        if (doLinearStretch) {linearStretching();}
    }

    /*
    the function find the average color in sun img, and return it in
    grey color.
     */
    private Double averageSubImgBrightness(Color[][] subImg) {
        Double sumGreyColor = 0.0;
        for (Color[] row: subImg) {
            for (Color color: row) {
                Double ans = color.getRed() * RED_NUM +
                        color.getGreen() * GREEN_NUM +
                        color.getBlue() * BLUE_NUM;
                sumGreyColor += ans;
            }
        }
        return sumGreyColor / ((subImg.length * subImg.length)*MAX_REG);
    }

    /*
    the function do linear stretching to all the brightness
    values of the chars.
     */
    private void linearStretching() {
        minBrightness = Collections.min(brightnessChar.values());
        maxBrightness = Collections.max(brightnessChar.values());
        for (char key : brightnessChar.keySet()) {
            Double ans = (brightnessChar.get(key) - minBrightness) / (maxBrightness - minBrightness);
            brightnessChar.put(key, 1-ans);
        }
        doLinearStretch = false;
        System.out.println(brightnessChar);
    }

    /*
    the function add char to th the hashMap with the fit brightness.
     */
    private void addChar(char c) {
        doLinearStretch = true;
        boolean[][] trueMatrix = CharRenderer.getImg(c, pixels, fontName);
        int num = 0;
        for (boolean[] matrix : trueMatrix) {
            for (boolean value: matrix) {
                if (!value) {
                    num += 1;
                }
            }
        }
        Double average = (double) num / (pixels*pixels);
        this.brightnessChar.put(c, average);
    }
}

package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
public class FileImage implements Image, Iterable<Color[][]>{
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final int NUM_OF_BITS = 16;
    private static final int DOUBLE = 2;

    private Color[][] pixelArray;
    private final int newWidth;
    private final int newHeight;
    private final int spaceWidth;
    private final int spaceHeight;
    private final java.awt.image.BufferedImage im;
    private int sizeSub;
    private int numCols;

    /**
     * the constructor of the class FileImage
     * @param filename path of the picture
     * @throws IOException
     */
    public FileImage(String filename) throws IOException {
        im = ImageIO.read(new File(filename));
        int origWidth = im.getWidth(), origHeight = im.getHeight();
        newWidth = closestPowerOf2(origWidth);
        newHeight = closestPowerOf2(origHeight);
        spaceWidth = (newWidth - origWidth) / DOUBLE;
        spaceHeight = (newHeight - origHeight) / DOUBLE;
        copyImgToMatrix();
    }

    /*
    the function copy the Image to Color[][] pixelArray.
     */
    private void copyImgToMatrix() {
        pixelArray = new Color[newHeight][newWidth];
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (i >= spaceHeight && i < (newHeight - spaceHeight) &&
                        j >= spaceWidth && j < (newWidth - spaceWidth)) {
                    pixelArray[i][j] = new Color(im.getRGB(j - spaceWidth, i - spaceHeight));
                }
                else {
                    pixelArray[i][j] = DEFAULT_COLOR;
                }
            }
        }
    }

    /*
    the function get the closet power 2 of number.
    */
    private int closestPowerOf2(int n) {
        n -= 1;
        for (int i = 1; i <= NUM_OF_BITS; i*=DOUBLE) {
            n |= n >> i;
        }
        n += 1;
        return n;
    }

    /**
     * the function return the width of the Image
     * @return the width
     */
    @Override
    public int getWidth() {
        return newWidth;
    }

    /**
     * the function return the height of the Image
     * @return the height
     */
    @Override
    public int getHeight() {
        return newHeight;
    }

    /**
     * the function return the pixel of given x, y
     * @param x the row
     * @param y the col
     * @return the pixel in row x and y col in Image
     */
    @Override
    public Color getPixel(int x, int y) {
        if (0 < x && x < newWidth && 0 < y && y < newHeight)
            return pixelArray[x][y];
        return null;
    }

    /**
     * set the num of cols in the final chars matrix
     * @param numCols number of chars in row
     */
    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    /**
     * the function create iterator to the Image, that
     * the items are sub-Image of the picture.
     * @return Iterator<Color[][]> of the image/
     */
    @Override
    public Iterator<Color[][]> iterator() {
        this.sizeSub = this.newWidth / numCols;
        int numRows = (newHeight/sizeSub);
        class Iter implements Iterator<Color[][]> {
            int i = 0; int j = 0;
            public boolean hasNext() {
                return (i < numRows) && (j < numCols);
            }
            public Color[][] next() {
                Color[][] subImg = new Color[sizeSub][sizeSub];
                for (int row = 0; row < sizeSub; row++) {
                    for (int col = 0; col < sizeSub; col ++) {
                        subImg[row][col] =
                                pixelArray[sizeSub * i + row][sizeSub * j + col];
                    }
                }
                j++;
                if (j == numCols) { i++; j = 0;}
                return subImg;
            }
        }
        return new Iter();
    }
}

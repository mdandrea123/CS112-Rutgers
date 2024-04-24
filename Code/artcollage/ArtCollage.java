
/*************************************************************************
 *  Compilation:  javac ArtCollage.java
 *  Execution:    java ArtCollage Flo2.jpeg
 *
 *  @author: Michael Dandrea, mjd463, mjd463@scarletmail.rutgers.edu
 *
 *************************************************************************/

import java.awt.Color;

public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;

    /*
     * One-argument Constructor 1. set default values of collageDimension to 4 and
     * tileDimension to 100 2. initializes original with the filename image 3.
     * initializes collage as a Picture of tileDimension*collageDimension x
     * tileDimension*collageDimension, where each pixel is black (see all
     * constructors for the Picture class). 4. update collage to be a scaled version
     * of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage(String filename) {
        collageDimension = 4;
        tileDimension = 100;
        original = new Picture(filename);
        collage = new Picture((collageDimension * tileDimension), (collageDimension * tileDimension));

        for (int col = 0; col < 400; col++) {
            for (int row = 0; row < 400; row++) {
                int col1 = col * original.width() / 400;
                int row1 = row * original.height() / 400;
                Color color = original.get(col1, row1);
                collage.set(col, row, color);
            }
        }
    }

    /*
     * Three-arguments Constructor 1. set default values of collageDimension to cd
     * and tileDimension to td 2. initializes original with the filename image 3.
     * initializes collage as a Picture of tileDimension*collageDimension x
     * tileDimension*collageDimension, where each pixel is black (see all
     * constructors for the Picture class). 4. update collage to be a scaled version
     * of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage(String filename, int td, int cd) {
        collageDimension = cd;
        tileDimension = td;
        original = new Picture(filename);
        collage = new Picture(td * cd, td * cd);

        for (int col = 0; col < cd * td; col++) {
            for (int row = 0; row < cd * td; row++) {
                int col1 = col * original.width() / (cd * td);
                int row1 = row * original.height() / (cd * td);
                Color color = original.get(col1, row1);
                collage.set(col, row, color);
            }
        }

        // WRITE YOUR CODE HERE
    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {
        return collageDimension;
        // WRITE YOUR CODE HERE
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {
        return tileDimension;
        // WRITE YOUR CODE HERE
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {
        return original;
        // WRITE YOUR CODE HERE
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {
        return collage;
        // WRITE YOUR CODE HERE
    }

    /*
     * Display the original image Assumes that original has been initialized
     */
    public void showOriginalPicture() {
        // WRITE YOUR CODE HERE
        original.show();
    }

    /*
     * Display the collage image Assumes that collage has been initialized
     */
    public void showCollagePicture() {
        collage.show();
        // WRITE YOUR CODE HERE
    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename Tile
     * (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * 
     * @param collageCol tile column
     * 
     * @param collageRow tile row
     */
    public void replaceTile(String filename, int collageCol, int collageRow) {
        Picture input = new Picture(filename);

        int height = this.tileDimension;
        int width = this.tileDimension;

        Picture scale = new Picture(width, height);

        for (int coll = 0; coll < width; coll++) {
            for (int ro = 0; ro < height; ro++) {
                int col1 = coll * input.width() / width;
                int row1 = ro * input.height() / height;
                Color color = input.get(col1, row1);
                scale.set(coll, ro, color);
            }
        }

        input = scale;

        int a = this.collageDimension;
        int b = this.collageDimension;

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                Color color = input.get(col, row);
                for (int i = collageCol; i < collageCol + 1; i++) {
                    for (int j = collageRow; j < collageRow + 1; j++) {
                        this.collage.set(width * i + col, height * j + row, color);
                    }
                }
            }

        }
        // WRITE YOUR CODE HERE
    }

    /*
     * Makes a collage of tiles from original Picture original will have
     * collageDimension x collageDimension tiles, each tile has tileDimension X
     * tileDimension pixels
     */
    public void makeCollage() {

        // WRITE YOUR CODE HERE
        int height = this.tileDimension;
        int width = this.tileDimension;

        Picture scale = new Picture(width, height);

        for (int coll = 0; coll < this.tileDimension; coll++) {
            for (int ro = 0; ro < this.tileDimension; ro++) {
                int col1 = coll * this.original.width() / width;
                int row1 = ro * this.original.height() / height;
                Color color = this.original.get(col1, row1);
                scale.set(coll, ro, color);
            }
        }

        int a = this.collageDimension;
        int b = this.collageDimension;

        collage = new Picture(b * scale.width(), a * scale.height());

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                Color color = scale.get(col, row);
                for (int i = 0; i < a; i++) {
                    for (int j = 0; j < b; j++) {
                        this.collage.set(height * j + col, width * i + row, color);
                    }
                }
            }
        }
    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component (see CS111 Week
     * 9 slides, the code for color separation is at the book's website)
     *
     * @param component is either red, blue or green
     * 
     * @param collageCol tile column
     * 
     * @param collageRow tile row
     */
    public void colorizeTile(String component, int collageCol, int collageRow) {

        // WRITE YOUR CODE HERE
        Picture pic = getCollagePicture();
        int tile = getTileDimension();
        for (int h = collageCol * tile; h < tile + (collageCol * tile); h++) {
            for (int x = collageRow * tile; x < tile + (collageRow * tile); x++) {
                Color color = pic.get(h, x);
                int temp = 0;
                if (component.equals("blue")) {
                    temp = color.getBlue();
                    pic.set(h, x, new Color(0, 0, temp));
                }
                if (component.equals("red")) {
                    temp = color.getRed();
                    pic.set(h, x, new Color(temp, 0, 0));
                }
                if (component.equals("green")) {
                    temp = color.getGreen();
                    pic.set(h, x, new Color(0, temp, 0));
                }
            }
        }

        collage = pic;
    }

    /*
     * Grayscale tile at (collageCol, collageRow) (see CS111 Week 9 slides, the code
     * for luminance is at the book's website)
     *
     * @param collageCol tile column
     * 
     * @param collageRow tile row
     */

    public void grayscaleTile(int collageCol, int collageRow) {

        // WRITE YOUR CODE HERE
        Picture pic = getCollagePicture();
        int tile = getTileDimension();

        for (int h = (collageCol * tile); h < (tile + (collageCol * tile)); h++) {
            for (int x = collageRow * tile; x < tile + (collageRow * tile); x++) {
                Color color = pic.get(h, x);
                Color gray = Luminance.toGray(color);
                pic.set(h, x, gray);
            }
        }

        collage = pic;
    }

    /*
     *
     * Test client: use the examples given on the assignment description to test
     * your ArtCollage
     */
    public static void main(String[] args) {

        ArtCollage art = new ArtCollage(args[0], 200, 4);
        art.makeCollage();
        art.grayscaleTile(2, 1);
        art.colorizeTile("red", 0, 0);
        art.showCollagePicture();
    }
}

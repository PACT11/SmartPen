
package view;

/*
 */
public class SheetProcessor {
    // identitifies whether the hand is over the sheet or not
    static boolean hasHand(Bitmap image) {
        System.out.println("SheetProcessor : check for the hand over the sheet");
        return false;
    }
    // compute the transformation to apply to get a straightened image of the sheet
    static Transformation getStraightTransformation(Bitmap sheetImage) {
        System.out.println("SheetProcessor : compute how to transform the sheet picture to get a straight image");
        return null;
    }
    // compute the transformation to apply to get a image of the sheet ready to be displayed from a Straight image
    static Transformation getSheetTransformation(Bitmap sheetImage) {
        System.out.println("SheetProcessor : compute a to transform a straight image to match the sheet when projected");
        return null;
    }
    // apply the specified transformation
    static Bitmap transform(Bitmap sourceImage, Transformation transformation) {
        System.out.println("SheetProcessor : compute a transformation");
        return null;
    }
    /* find the cap (a coloured spot) in the sheet
     * @param image the straightened image of the sheet
     * @return      a Point with coordinates expressed RELATIVELY to the size of the sheet : 
     *         for x, 0 is left border and 1 right border; for y, 0 is upper border and 1 is lower border
     */
    static Point findCap(Bitmap image) {
        System.out.println("SheetProcessor : search a cap above the sheet");
        return null;
    }
}

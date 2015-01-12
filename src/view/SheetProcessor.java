
package view;

/*
 */
public class SheetProcessor {
    // identitifies whether the hand is over the sheet or not
    public static boolean hasHand(Bitmap image) {
        System.out.println("SheetProcessor : check for the hand over the sheet");
        return false;
    }
    // compute the transformation to apply to get a straightened image of the sheet
    public static Transformation getStraightTransformation(Bitmap sheetImage) {
        System.out.println("SheetProcessor : compute how to transform the sheet picture to get a straight image");
        return null;
    }
    // compute the transformation to apply to get a image of the sheet ready to be displayed from a Straight image
    public static Transformation getSheetTransformation(Bitmap sheetImage) {
        System.out.println("SheetProcessor : compute how to transform the sheet picture to get an image that match the sheet when projected");
        return null;
    }
    // apply the specified transformation
    public static Bitmap transform(Bitmap sourceImage, Transformation transformation) {
        System.out.println("SheetProcessor : compute a transformation");
        return null;
    }

}

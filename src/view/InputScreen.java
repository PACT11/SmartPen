
package view;

import shape.ShapeProcessor;

/*
 *
 */
public class InputScreen {
    public interface ImageListener {
        public void newImage(Bitmap image);
    }
    
    private MenuBar menu;
    private ImageListener newImageListener;
    private OutputScreen outputScreen;
    private Camera camera;
    public InputScreen() {
        camera = new Camera();
        camera.addNewImageListener(new ImageListener() {
            @Override
            public void newImage(Bitmap image) {
                onNewImage(image);
            }
        });
    }
    private void onNewImage(Bitmap image) {
        if(!SheetProcessor.hasHand(image)) { // if the user's hand is not over the sheet
            System.out.println("InputScreen : no hand over the sheet");
            outputScreen.blackOut();         // shut down projector shortly
            Bitmap newImage = camera.takePicture();
            outputScreen.updateTransformation(SheetProcessor.getSheetTransformation(newImage)); // compute the new transformation to match th sheet
            outputScreen.restore();          // restart the display
            if(newImageListener!=null) {     // call the new image listener if any
                Transformation t = SheetProcessor.getStraightTransformation(newImage); // and give it a straightened image
                newImageListener.newImage(SheetProcessor.transform(newImage, t));
            }
        } else { // check if user is clicking the menu bar
            Transformation t = SheetProcessor.getStraightTransformation(image);    
            menu.click(ShapeProcessor.findCap(image));
        }
    }
    public void addNewImageListener(ImageListener listener) {
        newImageListener = listener;
    }
    public void setOutputScreen(OutputScreen screen) {
        outputScreen = screen;
    }
    
    //debug
    public void simulateNewImage(Bitmap image) {
        camera.simulateNewImage(image);
    }
}
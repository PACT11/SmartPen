package Morpion;

import android.graphics.Bitmap;
import java.util.ArrayList;
import shape.MainRansac;
import shape.Point;
import shape.ApplicationHomographie;

/**
 * Created by Nicolas on 31/03/2015.
 */
public class ProjectionGrille {
    public Bitmap projectionGrilleRedressee(Bitmap bitmap) {
        Bitmap bitmapRedresse = bitmap ;
        PositionnerGrille.TrouverPlaceLibre(bitmapRedresse) ;
        Grille morpion = new Grille(bitmap,PositionnerGrille.Position[0],PositionnerGrille.Position[1],bitmapRedresse.getWidth()*4/21) ;
        return  ;
    }
}

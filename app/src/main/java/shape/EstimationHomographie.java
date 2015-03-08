package shape;

/**
 * Created by hu on 06/03/15.
 */
public class EstimationHomographie {

    public static Matrix H = new Matrix(8,1) ;

    public Matrix getHomographyCoefficients(Point x0, Point x1, Point x2, Point x3, Point y0, Point y1, Point y2, Point y3) throws ArithmeticException {
        Matrix M = new Matrix(8,8) ;
        Matrix X = new Matrix(8,1) ;
        Matrix M1 = new Matrix (8,8) ;

        // On rentre les coefficients de la matrice d'homographie
        for (int i=0 ; i<=3 ; i++) {
            M.setValueAt(i,2,1) ;
            M.setValueAt(i+4,5,1) ;
            for (int j=0 ; j<=2 ; j++) {
                M.setValueAt(i+4,j,0) ;
                M.setValueAt(i,j+3,0) ;
            }
        }

        M.setValueAt(0,6,-x0.x*y0.x) ;
        M.setValueAt(0,7,-x0.y*y0.y) ;
        M.setValueAt(4,6,-x0.x*y0.y) ;
        M.setValueAt(4,7,-x0.y*y0.y) ;
        M.setValueAt(4,3,x0.x) ;
        M.setValueAt(4,4,x0.y) ;
        M.setValueAt(0,0,x0.x) ;
        M.setValueAt(0,1,x0.y) ;

        M.setValueAt(1,6,-x1.x*y1.x) ;
        M.setValueAt(1,7,-x1.y*y1.x) ;
        M.setValueAt(5,6,-x1.x*y1.y) ;
        M.setValueAt(5,7,-x1.y*y1.y) ;
        M.setValueAt(5,3,x1.x) ;
        M.setValueAt(5,4,x1.y) ;
        M.setValueAt(1,0,x1.x) ;
        M.setValueAt(1,1,x1.y) ;

        M.setValueAt(2,6,-x2.x*y2.x) ;
        M.setValueAt(2,7,-x2.y*y2.x) ;
        M.setValueAt(6,6,-x2.x*y2.y) ;
        M.setValueAt(6,7,-x2.y*y2.y) ;
        M.setValueAt(6,3,x2.x) ;
        M.setValueAt(6,4,x2.y) ;
        M.setValueAt(2,0,x2.x) ;
        M.setValueAt(2,1,x2.y) ;

        M.setValueAt(3,6,-x3.x*y3.x) ;
        M.setValueAt(3,7,-x3.y*y3.x) ;
        M.setValueAt(7,6,-x3.x*y3.y) ;
        M.setValueAt(7,7,-x3.y*y3.y) ;
        M.setValueAt(7,3,x3.x) ;
        M.setValueAt(7,4,x3.y) ;
        M.setValueAt(3,0,x3.x) ;
        M.setValueAt(3,1,x3.y) ;


        // On rentre les coefficients de la matrice des coins d'arrivee
        X.setValueAt(0,0,y0.x) ;
        X.setValueAt(4,0,y0.y) ;
        X.setValueAt(1,0,y1.x) ;
        X.setValueAt(5,0,y1.y) ;
        X.setValueAt(2,0,y2.x) ;
        X.setValueAt(6,0,y2.y) ;
        X.setValueAt(3,0,y3.x) ;
        X.setValueAt(7,0,y3.y) ;

        M1 = Matrix.inverse(M) ;
        H = Matrix.multiply(M1, X) ;

        return H ;
    }

    public double[] phiInverse(Point p,Matrix H) {
        double c;
        double q1, q2 ;
        double[] q = new double[2];
        c = (H.getValueAt(0,0)*H.getValueAt(7,0)*p.y - H.getValueAt(3,0)*H.getValueAt(7,0)*p.x + H.getValueAt(6,0)*H.getValueAt(4,0)*p.x - H.getValueAt(1,0)*H.getValueAt(6,0)*p.y - H.getValueAt(0,0)*H.getValueAt(4,0) + H.getValueAt(1,0)*H.getValueAt(3,0)) ;
        q1 = (int)((H.getValueAt(1,0)*p.y - H.getValueAt(2,0)*H.getValueAt(7,0)*p.y + H.getValueAt(5,0)*H.getValueAt(7,0)*p.x - H.getValueAt(4,0)*p.x + H.getValueAt(4,0)*H.getValueAt(2,0) - H.getValueAt(5,0)*H.getValueAt(1,0)) / c) ;
        q2 = (int)((H.getValueAt(3,0)*p.x - H.getValueAt(6,0)*H.getValueAt(5,0)*p.x + H.getValueAt(2,0)*H.getValueAt(6,0)*p.y - H.getValueAt(0,0)*p.y + H.getValueAt(0,0)*H.getValueAt(5,0) - H.getValueAt(3,0)*H.getValueAt(2,0)) / c) ;
        q[0] = q1;
        q[1] = q2;
        return q ;
    }
}
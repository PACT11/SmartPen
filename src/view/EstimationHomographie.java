package view;

import java.awt.Point;

public class EstimationHomographie {
	
	public static Matrix H = new Matrix(8,1) ;
	
	public static Matrix getHomographyCoefficients(Point x0, Point x1, Point x2, Point x3, Point y0, Point y1, Point y2, Point y3) throws ArithmeticException {
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
		
		M.setValueAt(0,6,-x0.getX()*y0.getX()) ;
		M.setValueAt(0,7,-x0.getY()*y0.getX()) ;
		M.setValueAt(4,6,-x0.getX()*y0.getY()) ;
		M.setValueAt(4,7,-x0.getY()*y0.getY()) ;
		M.setValueAt(4,3,x0.getX()) ;
		M.setValueAt(4,4,x0.getY()) ;
		M.setValueAt(0,0,x0.getX()) ;
		M.setValueAt(0,1,x0.getY()) ;
		
		M.setValueAt(1,6,-x1.getX()*y1.getX()) ;
		M.setValueAt(1,7,-x1.getY()*y1.getX()) ;
		M.setValueAt(5,6,-x1.getX()*y1.getY()) ;
		M.setValueAt(5,7,-x1.getY()*y1.getY()) ;
		M.setValueAt(5,3,x1.getX()) ;
		M.setValueAt(5,4,x1.getY()) ;
		M.setValueAt(1,0,x1.getX()) ;
		M.setValueAt(1,1,x1.getY()) ;
		
		M.setValueAt(2,6,-x2.getX()*y2.getX()) ;
		M.setValueAt(2,7,-x2.getY()*y2.getX()) ;
		M.setValueAt(6,6,-x2.getX()*y2.getY()) ;
		M.setValueAt(6,7,-x2.getY()*y2.getY()) ;
		M.setValueAt(6,3,x2.getX()) ;
		M.setValueAt(6,4,x2.getY()) ;
		M.setValueAt(2,0,x2.getX()) ;
		M.setValueAt(2,1,x2.getY()) ;
		
		M.setValueAt(3,6,-x3.getX()*y3.getX()) ;
		M.setValueAt(3,7,-x3.getY()*y3.getX()) ;
		M.setValueAt(7,6,-x3.getX()*y3.getY()) ;
		M.setValueAt(7,7,-x3.getY()*y3.getY()) ;
		M.setValueAt(7,3,x3.getX()) ;
		M.setValueAt(7,4,x3.getY()) ;
		M.setValueAt(3,0,x3.getX()) ;
		M.setValueAt(3,1,x3.getY()) ;
		
		
		// On rentre les coefficients de la matrice des coins d'arrivee
		X.setValueAt(0,0,y0.getX()) ;
		X.setValueAt(4,0,y0.getY()) ;
		X.setValueAt(1,0,y1.getX()) ;
		X.setValueAt(5,0,y1.getY()) ;
		X.setValueAt(2,0,y2.getX()) ;
		X.setValueAt(6,0,y2.getY()) ;
		X.setValueAt(3,0,y3.getX()) ;
		X.setValueAt(7,0,y3.getY()) ;
			
		M1 = Matrix.inverse(M) ;
		H = Matrix.multiply(M1, X) ;
			
			return H ;
	}
    
	/*
	public double[] phiInverse(Point p,Matrix H) {
        double c, q1, q2 ;
        double[] L = new double[2] ;
        double h0 = H.getValueAt(0,0) ;
        double h1 = H.getValueAt(1,0) ;
        double h2 = H.getValueAt(2,0) ;
        double h3 = H.getValueAt(3,0) ;
        double h4 = H.getValueAt(4,0) ;
        double h5 = H.getValueAt(5,0) ;
        double h6 = H.getValueAt(6,0) ;
        double h7 = H.getValueAt(7,0) ;
        c = (h0*h7*p.y - h3*h7*p.x + h6*h4*p.x - h1*h6*p.y - h0*h4 + h1*h3) ;
        q1 = (h1*p.y - h2*h7*p.y + h5*h7*p.x - h4*p.x + h4*h2 - h5*h1) / c ;
        q2 = (h3*p.x - h6*h5*p.x + h2*h6*p.y - h0*p.y + h0*h5 - h3*h2) / c ;
        L[0]=q1;
        L[1]=q2 ;
        return L ;
    }
   */
	public static double[] phiInverse(Point p,Matrix H) {
		double c, q1, q2 ;
		double[] L = new double[2] ;
		c = (H.getValueAt(0,0)*H.getValueAt(7,0)*p.getY() - H.getValueAt(3,0)*H.getValueAt(7,0)*p.getX() + H.getValueAt(6,0)*H.getValueAt(4,0)*p.getX() - H.getValueAt(1,0)*H.getValueAt(6,0)*p.getY() - H.getValueAt(0,0)*H.getValueAt(4,0) + H.getValueAt(1,0)*H.getValueAt(3,0)) ;
		q1 = (H.getValueAt(1,0)*p.getY() - H.getValueAt(2,0)*H.getValueAt(7,0)*p.getY() + H.getValueAt(5,0)*H.getValueAt(7,0)*p.getX() - H.getValueAt(4,0)*p.getX() + H.getValueAt(4,0)*H.getValueAt(2,0) - H.getValueAt(5,0)*H.getValueAt(1,0)) / c ;
		q2 = (H.getValueAt(3,0)*p.getX() - H.getValueAt(6,0)*H.getValueAt(5,0)*p.getX() + H.getValueAt(2,0)*H.getValueAt(6,0)*p.getY() - H.getValueAt(0,0)*p.getY() + H.getValueAt(0,0)*H.getValueAt(5,0) - H.getValueAt(3,0)*H.getValueAt(2,0)) / c ;
		L[0]=q1;
        L[1]=q2 ;
		return L ;
	}
	

	
}
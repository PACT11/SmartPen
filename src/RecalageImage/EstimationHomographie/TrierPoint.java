import java.awt.Point ;
import java.util.ArrayList ;

public class TrierPoint {
	
	public ArrayList<Point> TrierPoints(Point A, Point B, Point C, Point D) {
		ArrayList<Point> Coins = new ArrayList<Point>(4) ;
		Point P = new Point() ;
		Coins.set(0, A) ; Coins.set(1, B) ; Coins.set(2, C) ; Coins.set(3, D) ;
		if(A.getX() < B.getX()) {
			if(B.getX() < C.getX()) {
				if(C.getX() > D.getX()) {
					if(B.getX() > D.getX()) {
						P = Coins.get(1) ;
						Coins.set(1,Coins.get(3)) ;
						Coins.set(3, P) ;
					}
				}
			}
			else {
				if(A.getX() < C.getX()) {
					if(B.getX() < D.getX()) {
						P = Coins.get(1) ;
						Coins.set(1,Coins.get(2)) ;
						Coins.set(2,P) ;
					}
					else {
						if(C.getX() < D.getX()) {
							P = Coins.get(1) ;
							Coins.set(1,Coins.get(2)) ;
							Coins.set(2,P) ;
						}
						else {
							P = Coins.get(1) ;
							Coins.set(1,Coins.get(3)) ;
							Coins.set(3,P) ;
						}
					}
				}
				else {
					
					
				}
			}
		}
	}
	
	public ArrayList<Point> TrierPoints2(Point A, Point B, Point C, Point D) {
		ArrayList<Point> Coins = new ArrayList<Point>(4) ;
		Point P = new Point() ; 
		Coins.set(0, A) ; Coins.set(1, B) ; Coins.set(2, C) ; Coins.set(3, D) ;
		double x = (A.getX() + B.getX() + C.getX() + D.getX()) / 4.0 ;
		if(A.getX() < x) {
			if(B.getX() < x) ;
			else {
				if(C.getX() < x) {
					P = Coins.get(1) ;
					Coins.set(1,Coins.get(2)) ;
					Coins.set(2,P) ;
				}
				else {
					P = Coins.get(1) ;
					Coins.set(1,Coins.get(3)) ;
					Coins.set(3,P) ;
				}
			}
		}
		else {
			if(B.getX() > x) {
				P = Coins.get(0) ;
				Coins.set(0,Coins.get(2)) ;
				Coins.set(2,P) ;
				P = Coins.get(1) ;
				Coins.set(1,Coins.get(3)) ;
				Coins.set(3,P) ;
			}
			else {
				if(C.getX() < x) {
					P = Coins.get(0) ;
					Coins.set(0,Coins.get(2)) ;
					Coins.set(2,P) ;
				}
				else {
					P = Coins.get(0) ;
					Coins.set(0,Coins.get(3)) ;
					Coins.set(3,P) ;
				}
			}
		}
		if(Coins.get(0).getY() < Coins.get(1).getY()) {
			P = Coins.get(0) ;
			Coins.set(0,Coins.get(1)) ;
			Coins.set(1,P) ;
		}
		if(Coins.get(2).getY() < Coins.get(3).getY()) {
			P = Coins.get(2) ;
			Coins.set(2,Coins.get(3)) ;
			Coins.set(3,P) ;
		}
		return Coins ;
	}

}

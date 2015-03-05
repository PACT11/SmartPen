import java.awt.Point ;
import java.util.ArrayList ;

public class TrierPoint {
	
	public ArrayList<Point> TrierPoints(Point A, Point B, Point C, Point D) {
		ArrayList<Point> Coins = new ArrayList<Point>(4) ;
		Point P = new Point() ;
		Coins.set(0, A) ; Coins.set(1, B) ; Coins.set(2, C) ; Coins.set(3, D) ;
		if(A.getX() < B.getX()) {
			if(B.getX() < C.getX()) {
				if(C.getX() < D.getX()) return Coins ;
				else {
					if(B.getX() < D.getX()) return Coins ;
					else {
						P = Coins.get(1) ;
						Coins.set(1,Coins.get(3)) ;
						Coins.set(3, P) ;
						return Coins ;
					}
				}
			}
			else {
				if(A.getX() < C.getX()) {
					if(B.getX() < D.getX()) {
						P = Coins.get(1) ;
						Coins.set(1,Coins.get(2)) ;
						Coins.set(2,P) ;
						return Coins ;
					}
					else {
						
					}
				}
			}
		}
		
	return Coins;
	}

}

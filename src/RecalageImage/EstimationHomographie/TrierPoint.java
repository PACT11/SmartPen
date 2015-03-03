package RecalageImage.EstimationHomographie;

import java.awt.Point ;
import java.util.ArrayList ;

public class TrierPoint {
		
	public static ArrayList<Point> TrierPoints(Point A, Point B, Point C, Point D) {
		ArrayList<Point> Coins = new ArrayList<Point>(4) ;
		Point P = new Point() ; 
		Coins.add(0, A) ; Coins.add(1, B) ; Coins.add(2, C) ; Coins.add(3, D) ;
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

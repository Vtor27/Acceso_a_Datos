package es.florida.ae03.modelo;

public class Carta {

	String suit, base64;
	int points;
	
	Carta(String suit, int points, String base64) {
		this.suit = suit;
		this.points = points;
		this.base64 = base64;
	}

	public String getSuit() {
		return suit;
	}

	public String getBase64() {
		return base64;
	}

	public int getPoints() {
		return points;
	}
	
}

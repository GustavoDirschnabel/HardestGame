import java.io.Serializable;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Player extends GameObject implements Serializable {
	private int points;
	private TranslateTransition movimento;
	private int velocidade;
	private int deaths;
	public Player(Shape shape, double x, double y, int deaths) {
		super(shape, x, y);
		this.points = 0;
		this.movimento = new TranslateTransition(Duration.millis(167),this.shape);
		this.velocidade = 2;
		this.deaths = deaths;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public TranslateTransition getMovimento() {
		return movimento;
	}
	public void setMovimento(TranslateTransition movimento) {
		this.movimento = movimento;
	}
	public int getVelocidade() {
		return velocidade;
	}
	public void setVelocidade(int velocidade) {
		this.velocidade = velocidade;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
	
}

import java.io.Serializable;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Player extends GameObject implements Serializable {
	private int points;
	private TranslateTransition movimento;
	private int velocidade;
	public Player(Shape shape, double x, double y) {
		super(shape, x, y);
		this.points = 0;
		this.movimento = new TranslateTransition(Duration.millis(167),this.shape);
		this.velocidade = 2;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public void moverDireita() {
		movimento.setToX(this.shape.getLayoutX() + velocidade);
		movimento.play();
	}
	public void moverEsquerda() {
		movimento.setToX(this.shape.getLayoutX() - velocidade);
		movimento.play();
	}
	public void moverCima() {
		movimento.setToX(this.shape.getLayoutY() - velocidade);
		movimento.play();
	}
	public void moverBaixo() {
		movimento.setToX(this.shape.getLayoutY() + velocidade);
		movimento.play();
	}
	
}

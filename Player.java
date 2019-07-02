import javafx.animation.TranslateTransition;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Player extends GameObject {
	private int points;
	private int deaths;
	public Player(Shape shape, double x, double y,int deaths ) {
		super(shape, x, y);
		this.points = 0;
		this.deaths = deaths;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
}

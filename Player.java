
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

public class Player extends GameObject {
	private int points;
	private int deaths;
	public Player(Shape shape, double x, double y,int deaths ) {
		super(shape, x, y);
		this.points = 0;
		this.deaths = deaths;
		this.getShape().setStrokeWidth(5);
		this.getShape().setStrokeType(StrokeType.INSIDE);
		this.getShape().setStroke(Color.BLACK);
		this.getShape().setFill(Color.CRIMSON);
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

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.io.Serializable;

import javafx.animation.PathTransition;
public class Enemy extends GameObject implements Serializable {
	public Enemy(Circle shape, double x, double y) {
		super(shape, x, y);
		// TODO Auto-generated constructor stub
	}
	public void Move(Shape caminho, double duração) {
		PathTransition mover = new PathTransition(Duration.seconds(duração),caminho,this.shape);
		mover.setCycleCount(PathTransition.INDEFINITE);
		mover.play();
	}
}

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import javafx.animation.PathTransition;
public class Enemy extends GameObject {
	public Enemy(Rectangle shape, Double x, Double y) {
		super(shape, x, y);
		// TODO Auto-generated constructor stub
	}
	public void Move(Shape caminho, Double dura��o) {
		PathTransition mover = new PathTransition(Duration.seconds(dura��o),caminho,this.shape);
		mover.setCycleCount(PathTransition.INDEFINITE);
		mover.play();
	}
}

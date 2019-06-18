

public class Player extends GameObject {
	private int points;
	public Player(int pox, int poy, String fileName) {
		super(pox, poy, fileName);
		// TODO Auto-generated constructor stub
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	@Override
	public boolean IsColliding(GameObject obj) {
		if(this.Distancia(obj) <= (this.bounds.getWidth() + obj.getBounds().getWidth())/2 || this.Distancia(obj) <= (this.bounds.getHeight() + obj.getBounds().getHeight())/2) {
			this.collisions.add(obj);
			if(obj.getClass().toString() == "Coin")
				points++;
			return true;
		}
		this.UpdateCollisions(obj);
		return false;
	}
}

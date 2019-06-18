import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public abstract class GameObject implements Collidible {
	protected Dimension bounds;
	protected ArrayList<GameObject> collisions; 
	protected int pox,poy;
	protected ImageIcon sprite;
	public GameObject(int pox, int poy, String fileName) {
		this.sprite = new ImageIcon(fileName);
		this.bounds = new Dimension(sprite.getIconWidth(),sprite.getIconHeight());
		this.collisions = new ArrayList<GameObject>();
		this.pox = pox;
		this.poy = poy;
	}
	
	public Dimension getBounds() {
		return bounds;
	}

	public void setBounds(Dimension bounds) {
		this.bounds = bounds;
	}

	public ArrayList<GameObject> getCollisions() {
		return collisions;
	}

	public void setCollisions(ArrayList<GameObject> collisions) {
		this.collisions = collisions;
	}

	public int getPox() {
		return pox;
	}

	public void setPox(int pox) {
		this.pox = pox;
	}

	public int getPoy() {
		return poy;
	}

	public void setPoy(int poy) {
		this.poy = poy;
	}

	public ImageIcon getSprite() {
		return sprite;
	}

	public void setSprite(ImageIcon sprite) {
		this.sprite = sprite;
	}

	@Override
	public int Distancia(GameObject obj) {
		return (int) Math.round(Math.sqrt(Math.pow(this.pox-obj.getPox(), 2)+Math.pow(this.poy-obj.getPoy(), 2)));
	}

	@Override
	public boolean IsColliding(GameObject obj) {
		if(this.Distancia(obj) <= (this.bounds.getWidth() + obj.getBounds().getWidth())/2 || this.Distancia(obj) <= (this.bounds.getHeight() + obj.getBounds().getHeight())/2) {
			this.collisions.add(obj);
			return true;
		}
		this.UpdateCollisions(obj);
		return false;
	}

	@Override
	public void UpdateCollisions(GameObject obj) {	
			this.collisions.remove(obj);
	}

}

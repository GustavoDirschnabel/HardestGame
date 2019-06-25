import java.io.Serializable;
import java.util.ArrayList;

public class Level implements Serializable {
	private int levelNumber;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Wall> wall;
	private ArrayList<Coin> coins;
	
	public Level(int levelNumber, Player player, ArrayList<Enemy> enemies, ArrayList<Wall> wall,
			ArrayList<Coin> coins) {
		this.levelNumber = levelNumber;
		this.player = player;
		this.enemies = enemies;
		this.wall = wall;
		this.coins = coins;
	}

	public int getLevelNumber() {
		return levelNumber;
	}

	public Player getPlayer() {
		return player;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public ArrayList<Wall> getWall() {
		return wall;
	}

	public ArrayList<Coin> getCoins() {
		return coins;
	}
	
	
}

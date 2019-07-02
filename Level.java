import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

public class Level implements Serializable {
	private int levelNumber;
	private int numberOfDeaths;
	private Vector2 posPlayer;
	private ArrayList<Vector2> posEnemies;
	private ArrayList<Vector2> posWalls;
	private ArrayList<Double> wallPoints;
	private ArrayList<Integer> firstIndexofEachWall;
	private ArrayList<Vector2> posCoins;
	
	public Level(int levelNumber, Player player, ArrayList<Enemy> enemies, ArrayList<Wall> wall,
			ArrayList<Coin> coins) {
		this.levelNumber = levelNumber;
		this.numberOfDeaths = player.getDeaths();
		this.posPlayer = new Vector2(player.getIniX(), player.getIniY());
		this.posEnemies = new ArrayList<Vector2>();
		this.posWalls = new ArrayList<Vector2>();
		this.wallPoints = new ArrayList<Double>();
		this.firstIndexofEachWall = new ArrayList<Integer>();
		this.posCoins = new ArrayList<Vector2>();
		
		for(int i = 0; i < enemies.size(); i ++) {
			Vector2 pos = new Vector2(enemies.get(i).getIniX(), enemies.get(i).getIniY());
			this.posEnemies.add(i, pos);
		}
		
		for(int i = 0; i < wall.size(); i ++) {
			Polyline pol = (Polyline) wall.get(i).getShape();
			int index = 0;
			for(int j = 0; j < i; j++) {
				index += firstIndexofEachWall.get(j);
				if(j == i - 1) {
					index += pol.getPoints().size();
				}
			}
			this.firstIndexofEachWall.add(i,index);
			for(int j = 0; j < pol.getPoints().size(); j++) {
				this.wallPoints.add(j + this.firstIndexofEachWall.get(i), pol.getPoints().get(j));
			}
			Vector2 pos = new Vector2(wall.get(i).getIniX(), wall.get(i).getIniY());
			posWalls.add(i, pos);
		}
		
		for(int i = 0; i < coins.size(); i ++) {
			Vector2 pos = new Vector2(coins.get(i).getIniX(), coins.get(i).getIniY());
			this.posEnemies.add(i, pos);
		}
	}

	public int getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}
	

	public Player getPlayer() {
		Rectangle rect = new Rectangle(50,50);
		return new Player(rect,this.posPlayer.getPosX(), this.posPlayer.getPosY(),this.numberOfDeaths);
	}

	public void setPosPlayer(Vector2 posPlayer) {
		this.posPlayer = posPlayer;
	}

	public ArrayList<Vector2> getPosEnemies() {
		return posEnemies;
	}
	
	public ArrayList<Enemy> getEnemies(){
		Circle circ = new Circle(20,0,0);
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		double x = 0;
		double y = 0;
		
		for(int i = 0; i < posEnemies.size(); i++) {
			x = posEnemies.get(i).getPosX();
		    y = posEnemies.get(i).getPosY();
			circ.setCenterX(x);
			circ.setCenterY(y);
			circ.setFill(Color.DARKBLUE);
			Enemy enemy = new Enemy(circ,x,y);
			enemies.add(enemy);
		}
		
		return enemies;
	}

	public void setPosEnemies(ArrayList<Vector2> posEnemies) {
		this.posEnemies = posEnemies;
	}

	public ArrayList<Vector2> getPosWalls() {
		return posWalls;
	}
	
	public ArrayList<Wall> getWalls(){
		ArrayList<Wall> walls = new ArrayList<Wall>();
		ArrayList<Double> points;
		double[] pointAdapter;
		double x = 0;
		double y = 0;
		
		for(int i = 0; i < posWalls.size(); i++) {
			x = posWalls.get(i).getPosX();
		    y = posWalls.get(i).getPosY();
		    
		    if(i != posWalls.size() - 1)
		    	points = (ArrayList<Double>) wallPoints.subList(firstIndexofEachWall.get(i), firstIndexofEachWall.get(i+1));
		    else
		    	points = (ArrayList<Double>) wallPoints.subList(firstIndexofEachWall.get(i), wallPoints.size());
		    
		    pointAdapter = new double[points.size()];
		    for(int j = 0; i < pointAdapter.length; j++) {
		    	pointAdapter[i] = points.get(i);
		    }
		    
		    Polyline pol = new Polyline(pointAdapter);
		    walls.add(new Wall(pol,x,y));
		}
		
		return walls;
	}

	public void setPosWalls(ArrayList<Vector2> posWalls) {
		this.posWalls = posWalls;
	}

	public ArrayList<Double> getWallPoints() {
		return wallPoints;
	}

	public void setWallPoints(ArrayList<Double> wallPoints) {
		this.wallPoints = wallPoints;
	}

	public ArrayList<Integer> getFirstIndexofEachWall() {
		return firstIndexofEachWall;
	}

	public void setFirstIndexofEachWall(ArrayList<Integer> firstIndexofEachWall) {
		this.firstIndexofEachWall = firstIndexofEachWall;
	}

	public ArrayList<Vector2> getPosCoins() {
		return posCoins;
	}
	
	public ArrayList<Coin> getCoins() {
		Circle circ = new Circle(20,0,0);
		ArrayList<Coin> coins = new ArrayList<Coin>();
		double x = 0;
		double y = 0;
		
		for(int i = 0; i < posCoins.size(); i++) {
			x = posCoins.get(i).getPosX();
		    y = posCoins.get(i).getPosY();
			circ.setCenterX(x);
			circ.setCenterY(y);
			circ.setFill(Color.GOLD);
			Coin coin = new Coin(circ,x,y);
			coins.add(coin);
		}
		
		return coins;
	}

	public void setPosCoins(ArrayList<Vector2> posCoins) {
		this.posCoins = posCoins;
	}	
	
}

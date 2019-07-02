
public class TimeFrameController {
	private long lastUpdatedTime, nextFrame;
	private boolean canRun;
	
	public TimeFrameController() {
		lastUpdatedTime = System.currentTimeMillis();
		nextFrame = lastUpdatedTime + 167;
		canRun = true;
	}
	
	public double getDeltaTime() {
		return (System.currentTimeMillis() - lastUpdatedTime) % 1000;
	}
	
	public void updateRunnable() {
		lastUpdatedTime = System.currentTimeMillis();
		if(lastUpdatedTime >= nextFrame) {
			nextFrame = lastUpdatedTime + 167;
			canRun = true;
		}
	}
	
	public void hasRan() {
		canRun = false;
	}
	
	public boolean isRunnable() {
		return canRun;
	}
}

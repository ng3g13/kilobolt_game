package kiloboltgame;

public class Background {
	private int bgX, bgY, speedX;
	
	public Background(int x, int y){
		bgX = x;
		bgY = y;
		speedX = 0;
	}
	
	// +ve X 'right'. +ve Y 'down'
	//two images 2160x480
	public void update(){
		bgX += speedX;
		
		//1, 2, 1, 2... image loop, images are drawn for top left corner.
		if(bgX <= -2160){
			bgX += 4320;
		}
	}

	public int getBgX() {
		return bgX;
	}

	public int getBgY() {
		return bgY;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setBgX(int bgX) {
		this.bgX = bgX;
	}

	public void setBgY(int bgY) {
		this.bgY = bgY;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
}

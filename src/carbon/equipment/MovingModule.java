package carbon.equipment;

public abstract class MovingModule {
	
	protected int destinationX;
	public int getDestinationX() {
		return destinationX;
	}
	public void setDestinationX(int destinationX) {
		this.destinationX = destinationX;
	}
	public int getDestinationY() {
		return destinationY;
	}
	public void setDestinationY(int destinationY) {
		this.destinationY = destinationY;
	}
	protected int destinationY;
	
	protected int x;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	protected int y;
	protected int speed;
	
	public int getSpeed() {
		return speed;
	}
	public abstract void moveUp();
	public abstract void moveDown();
	public abstract void moveLeft();
	public abstract void moveRight();
	public abstract void moveTo(int toX,  int toY);
	public void setSpeed(int speed)
	{
		this.speed=speed;
	}
	public void speedUp()
	{
		speed++;
	}
	public void speedDown()
	{
		if(speed>5)
		speed--;
	}

}

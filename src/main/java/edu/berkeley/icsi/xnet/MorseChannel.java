package edu.berkeley.icsi.xnet;


public class MorseChannel {
	public static final int NOT_STARTED = 0;
	public static final int ONGOING = 1;
	public static final int ARRIVED = 2;
	private Morse morse;
	private double currentY;
	private double currentX;
	private double targetX;
	private double targetY;
	private double speed;
	private double tolerance;
	private boolean collide;
	private int status = NOT_STARTED; 

	public MorseChannel() {
	}
	public void setMorse(Morse morse) {
		this.morse = morse; 
	}
	public Morse getMorse() {
		return morse;
	}
	public void updateCurrentLocation(double x, double y) {
		this.currentX = x;
		this.currentY = y; 
		System.out.println("MorseChannel.updateCurrentLocation x: "+x+" y: "+y);
	}
	public void updateTargetLocation(double x, double y) {
		this.targetX = x;
		this.targetY = y; 
//		System.out.println("MorseChannel.updateTargetLocation x: "+x+" y: "+y);
	}
	public double getCurrentX() {
		return currentX;
	}
	public double getCurrentY() {
		return currentY;
	}
	public double getTargetX() {
		return targetX;
	}
	public double getTargetY() {
		return targetY;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getTolerance() {
		return tolerance;
	}
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
	public boolean isCollide() {
		return collide;
	}
	public void setCollide(boolean collide) {
		this.collide = collide;
	}
	public int getStatus() {
		return status ;
	}
	public void setStatus(int status) {
		System.out.println("MorseChannel.setStatus: "+status);
		this.status = status;
	}
}

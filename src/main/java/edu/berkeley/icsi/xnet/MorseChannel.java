package edu.berkeley.icsi.xnet;


public class MorseChannel {
	private Morse morse;
	private double currentY;
	private double currentX;
	private double targetX;
	private double targetY;

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
	}
	public void updateTargetLocation(double x, double y) {
		this.targetX = x;
		this.targetY = y; 
		System.out.println("MorseChannel.updateTargetLocation x: "+x+" y: "+y);
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

}

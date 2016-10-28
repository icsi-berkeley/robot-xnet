package edu.berkeley.icsi.xnet;

public class TestingMover implements Mover {

	private boolean called;
	private String command;

	public TestingMover() {
	}

	public boolean wasCalled() {
		return called;
	}

	@Override
	public void callMover(String command) {
		called = true; 
		this.command = command; 
	}

	public String lastCommand() {
		return command;
	}

}

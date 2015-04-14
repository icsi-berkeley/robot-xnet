package edu.berkeley.icsi.xnet;

public class TestingMorse implements Morse {

	private boolean called;
	private String command;

	public TestingMorse() {
	}

	public boolean wasCalled() {
		return called;
	}

	@Override
	public void callMorse(String command) {
		called = true; 
		this.command = command; 
	}

	public String lastCommand() {
		return command;
	}

}

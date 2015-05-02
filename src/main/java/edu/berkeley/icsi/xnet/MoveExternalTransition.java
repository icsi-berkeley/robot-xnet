package edu.berkeley.icsi.xnet;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

import uk.ac.imperial.pipe.exceptions.PetriNetComponentNotFoundException;
import uk.ac.imperial.pipe.models.petrinet.AbstractExternalTransition;
import uk.ac.imperial.pipe.models.petrinet.Place;
import uk.ac.imperial.pipe.runner.InterfaceException;

public class MoveExternalTransition extends AbstractExternalTransition {


	private static final String COMMAND = "command";
	private static final String MOVE = "moveMorse";
	private static final String UPDATE_POSITION = "updateMorsePosition";
	private static final String MOTION = "motion";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String Z = "z";
	private static final String TOLERANCE = "tolerance";
	private static final String SPEED = "speed";
	private static final String COLLIDE = "collide";
	protected static final String ARRIVED = "Arrived";
	protected static final String DEFAULT = "Default";
	private MorseChannel morseChannel;

	@Override
	public void fire() {
		this.morseChannel = (MorseChannel) getExternalTransitionProvider().getContext();
		int status = this.morseChannel.getStatus(); 
		if ((status == MorseChannel.NOT_STARTED) ||
			(status == MorseChannel.RESTARTED)) {
			this.morseChannel.setStatus(MorseChannel.ONGOING); 
			this.morseChannel.getMorse().callMorse(buildMoveCommand()); 
		}
		else if (status == MorseChannel.ONGOING) {
			this.morseChannel.getMorse().callMorse(buildUpdatePositionCommand()); 
		}
		else if (status == MorseChannel.ARRIVED) {
			try {
				getExternalTransitionProvider().getPlaceMarker().markPlace("Arrived", "Default", 2);
			} catch (InterfaceException e) {
				e.printStackTrace();
			}
		}
	}

	private String buildUpdatePositionCommand() {
		JsonObject command = Json.createObjectBuilder()
				.add(COMMAND, UPDATE_POSITION)
						.build();
		return command.toString();
	}

	private String buildMoveCommand() {
		JsonObject command = Json.createObjectBuilder()
				.add(COMMAND, MOVE)
				.add(MOTION, Json.createObjectBuilder()
						.add(X, this.morseChannel.getTargetX())
						.add(Y, this.morseChannel.getTargetY())
						.add(Z, 0.0d)
						.add(COLLIDE, this.morseChannel.isCollide())
						.add(SPEED, this.morseChannel.getSpeed())
						.add(TOLERANCE, this.morseChannel.getTolerance())
						.build())
				.build();
		return command.toString();
	}
}

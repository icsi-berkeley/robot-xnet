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
	private static final String MOVE = "moveMover";
	private static final String UPDATE_POSITION = "updateMoverPosition";
	private static final String MOTION = "motion";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String Z = "z";
	private static final String TOLERANCE = "tolerance";
	private static final String SPEED = "speed";
	private static final String COLLIDE = "collide";
	protected static final String ARRIVED = "Arrived";
	protected static final String DEFAULT = "Default";
	private MotionChannel motionChannel;

	@Override
	public void fire() {
		this.motionChannel = (MotionChannel) getExternalTransitionProvider().getContext();
		int status = this.motionChannel.getStatus(); 
		if ((status == MotionChannel.NOT_STARTED) ||
			(status == MotionChannel.RESTARTED)) {
			this.motionChannel.setStatus(MotionChannel.ONGOING); 
			this.motionChannel.getMover().callMover(buildMoveCommand()); 
		}
		else if (status == MotionChannel.ONGOING) {
			this.motionChannel.getMover().callMover(buildUpdatePositionCommand()); 
		}
		else if (status == MotionChannel.ARRIVED) {
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
						.add(X, this.motionChannel.getTargetX())
						.add(Y, this.motionChannel.getTargetY())
						.add(Z, 0.0d)
						.add(COLLIDE, this.motionChannel.isCollide())
						.add(SPEED, this.motionChannel.getSpeed())
						.add(TOLERANCE, this.motionChannel.getTolerance())
						.build())
				.build();
		return command.toString();
	}
}

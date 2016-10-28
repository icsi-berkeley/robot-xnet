package edu.berkeley.icsi.xnet;

import javax.json.Json;
import javax.json.JsonObject;

import uk.ac.imperial.pipe.models.petrinet.AbstractExternalTransition;
import uk.ac.imperial.pipe.models.petrinet.ExecutablePetriNet;
import uk.ac.imperial.pipe.models.petrinet.ExternalTransition;

public class RestartExternalTransition extends AbstractExternalTransition {

	private static final String COMMAND = "command";
	private static final String RESTART = "restartMover";
	private MotionChannel motionChannel;

	@Override
	public void fire() {
		this.motionChannel = (MotionChannel) getExternalTransitionProvider().getContext();
		this.motionChannel.setStatus(MotionChannel.RESTARTED);
		this.motionChannel.getMover().callMover(buildMoveCommand()); 
	}

	private String buildMoveCommand() {
		JsonObject command = Json.createObjectBuilder()
				.add(COMMAND, RESTART)
				.build();
		return command.toString();
	}


}

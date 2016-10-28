package edu.berkeley.icsi.xnet;

import javax.json.Json;
import javax.json.JsonObject;

import uk.ac.imperial.pipe.models.petrinet.AbstractExternalTransition;

public class SuspendExternalTransition extends AbstractExternalTransition {


	private static final String COMMAND = "command";
	private static final String SUSPEND = "suspendMover";
	private MotionChannel motionChannel;

	@Override
	public void fire() {
		this.motionChannel = (MotionChannel) getExternalTransitionProvider().getContext(); 
		this.motionChannel.getMover().callMover(buildMoveCommand()); 
	}

	private String buildMoveCommand() {
		JsonObject command = Json.createObjectBuilder()
				.add(COMMAND, SUSPEND)
				.build();
		return command.toString();
	}

}

package edu.berkeley.icsi.xnet;

import javax.json.Json;
import javax.json.JsonObject;

import uk.ac.imperial.pipe.models.petrinet.AbstractExternalTransition;

public class SuspendExternalTransition extends AbstractExternalTransition {


	private static final String COMMAND = "command";
	private static final String SUSPEND = "suspendMorse";
	private MorseChannel morseChannel;

	@Override
	public void fire() {
		this.morseChannel = (MorseChannel) getExternalTransitionProvider().getContext(); 
		this.morseChannel.getMorse().callMorse(buildMoveCommand()); 
	}

	private String buildMoveCommand() {
		JsonObject command = Json.createObjectBuilder()
				.add(COMMAND, SUSPEND)
				.build();
		return command.toString();
	}

}

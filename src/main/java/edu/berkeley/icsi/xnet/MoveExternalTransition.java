package edu.berkeley.icsi.xnet;

import javax.json.Json;
import javax.json.JsonObject;

import uk.ac.imperial.pipe.exceptions.PetriNetComponentNotFoundException;
import uk.ac.imperial.pipe.models.petrinet.AbstractExternalTransition;
import uk.ac.imperial.pipe.models.petrinet.Place;

public class MoveExternalTransition extends AbstractExternalTransition {


	private static final String COMMAND = "command";
	private static final String MOVE = "move";
	private static final String LOCATION = "location";
	private static final String X = "x";
	private static final String Y = "y";
	private MorseChannel morseChannel;

	public MoveExternalTransition() {
		System.out.println("MoveExternalTransition constructor");
	}
	
	@Override
	public void fire() {
		System.out.println("MoveExternalTransition fire");
		this.morseChannel = (MorseChannel) context; 
		this.morseChannel.getMorse().callMorse(buildMoveCommand()); 
	}

	private String buildMoveCommand() {
		JsonObject command = Json.createObjectBuilder()
				.add(COMMAND, MOVE)
				.add(LOCATION, Json.createObjectBuilder()
						.add(X, this.morseChannel.getTargetX())
						.add(Y, this.morseChannel.getTargetY())
						.build())
				.build();
		System.out.println("MoveExternalTransition command: "+command.toString());
		return command.toString();
	}
	public void markPlace(String placeId) throws PetriNetComponentNotFoundException {
		Place place = executablePetriNet.getComponent(placeId, Place.class);
		place.setTokenCount("Default", 1);
		System.out.println("MoveExternalTransition.markPlace "+place.getId());
	}

}

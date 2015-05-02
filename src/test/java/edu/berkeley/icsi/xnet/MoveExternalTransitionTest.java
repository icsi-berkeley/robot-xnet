package edu.berkeley.icsi.xnet;

import static org.junit.Assert.*;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import uk.ac.imperial.pipe.dsl.ANormalArc;
import uk.ac.imperial.pipe.dsl.APetriNet;
import uk.ac.imperial.pipe.dsl.APlace;
import uk.ac.imperial.pipe.dsl.AToken;
import uk.ac.imperial.pipe.dsl.AnImmediateTransition;
import uk.ac.imperial.pipe.exceptions.PetriNetComponentNotFoundException;
import uk.ac.imperial.pipe.models.petrinet.ExecutablePetriNet;
import uk.ac.imperial.pipe.models.petrinet.ExternalTransition;
import uk.ac.imperial.pipe.models.petrinet.PetriNet;
import uk.ac.imperial.pipe.models.petrinet.Place;
import uk.ac.imperial.pipe.runner.InterfaceException;
import uk.ac.imperial.pipe.runner.PlaceMarker;

public class MoveExternalTransitionTest extends AbstractExternalTransitionTest implements PlaceMarker {

	@Test
	public void verifyCallsMorseWithMoveCommand() {
		setupInitialMove();
		morseChannel.setMorse(morse); 
		externalTransition.fire();
		assertTrue(morse.wasCalled()); 
		assertEquals("{\"command\":\"moveMorse\",\"motion\":{\"x\":6.7,\"y\":8.9,\"z\":0.0,\"collide\":false,\"speed\":2.0,\"tolerance\":4.0}}", morse.lastCommand()); 
	}
	private void setupInitialMove() {
		morseChannel.updateTargetLocation(6.7, 8.9); 
		morseChannel.setSpeed(2.0); 
		morseChannel.setTolerance(4.0);
		morseChannel.setCollide(false);
	}
	@Test
	public void updatesCurrentAndTargetLocations() {
		assertEquals(0.0, morseChannel.getCurrentX(), 0.01); 
		assertEquals(0.0, morseChannel.getCurrentY(), 0.01); 
		assertEquals(0.0, morseChannel.getTargetX(), 0.01); 
		assertEquals(0.0, morseChannel.getTargetY(), 0.01); 
		morseChannel.updateCurrentLocation(2.4, 3.0); 
		assertEquals(2.4, morseChannel.getCurrentX(), 0.01); 
		assertEquals(3.0, morseChannel.getCurrentY(), 0.01); 
		morseChannel.updateTargetLocation(6.7, 8.9); 
		assertEquals(6.7, morseChannel.getTargetX(), 0.01); 
		assertEquals(8.9, morseChannel.getTargetY(), 0.01); 
	}
	@Test
	public void updatesOngoingStatusAndCommand() {
		assertNull(morse.lastCommand()); 
		assertEquals(MorseChannel.NOT_STARTED, morseChannel.getStatus());
		morseChannel.updateTargetLocation(6.7, 8.9); 
		externalTransition.fire();
		assertEquals(MorseChannel.ONGOING, morseChannel.getStatus());
	}
	@Test
	public void whileOngoingAfterFirstMoveCommandSubsequentCommandsAreToUpdatePosition() {
		morseChannel.updateTargetLocation(6.7, 8.9); 
		externalTransition.fire();
		assertEquals(MorseChannel.ONGOING, morseChannel.getStatus());
		assertEquals("{\"command\":\"moveMorse\",\"motion\":{\"x\":6.7,\"y\":8.9,\"z\":0.0,\"collide\":false,\"speed\":0.0,\"tolerance\":0.0}}", morse.lastCommand()); 
		externalTransition.fire();
		assertEquals(MorseChannel.ONGOING, morseChannel.getStatus());
		assertEquals("{\"command\":\"updateMorsePosition\"}", morse.lastCommand()); 
	}
	@Test
	public void whenStatusIsArrivedMarksArrivedPlace() throws Exception {
		assertEquals(0, epn.getComponent("Arrived", Place.class).getTokenCount("Default")); 
		morseChannel.setStatus(MorseChannel.ARRIVED);
		externalTransitionProvider.setPlaceMarker(this);
		externalTransition.fire();
		assertFalse(morse.wasCalled());
		assertEquals(1, epn.getComponent("Arrived", Place.class).getTokenCount("Default"));
	}
	@Test
	public void whenStatusIsRestartGeneratesNewMoveCommand() throws Exception {
		morseChannel.updateTargetLocation(3.3, 4.4); 
		morseChannel.setSpeed(2.0); 
		morseChannel.setTolerance(4.0);
		morseChannel.setCollide(false);
		morseChannel.setMorse(morse); 
		morseChannel.setStatus(MorseChannel.RESTARTED);
		externalTransition.fire();
		assertTrue(morse.wasCalled()); 
		assertEquals("{\"command\":\"moveMorse\",\"motion\":{\"x\":3.3,\"y\":4.4,\"z\":0.0,\"collide\":false,\"speed\":2.0,\"tolerance\":4.0}}", morse.lastCommand()); 
	}
	@Test
	public void updatedMotionIgnoredUntilStatusChangesToRestart() throws Exception {
		setupInitialMove();
		morseChannel.setMorse(morse); 
		externalTransition.fire();
		assertTrue(morse.wasCalled()); 
		assertEquals("{\"command\":\"moveMorse\",\"motion\":{\"x\":6.7,\"y\":8.9,\"z\":0.0,\"collide\":false,\"speed\":2.0,\"tolerance\":4.0}}", morse.lastCommand()); 
		morseChannel.updateTargetLocation(3.3, 4.4); 
		externalTransition.fire();
		assertEquals("update ignored",MorseChannel.ONGOING, morseChannel.getStatus());
		assertEquals("{\"command\":\"updateMorsePosition\"}", morse.lastCommand()); 
		morseChannel.setStatus(MorseChannel.RESTARTED);
		assertEquals("implies RestartExternalTransition has fired",MorseChannel.RESTARTED, morseChannel.getStatus());
		externalTransition.fire();
		assertEquals("{\"command\":\"moveMorse\",\"motion\":{\"x\":3.3,\"y\":4.4,\"z\":0.0,\"collide\":false,\"speed\":2.0,\"tolerance\":4.0}}", morse.lastCommand()); 
		assertEquals("new motion in progress",MorseChannel.ONGOING, morseChannel.getStatus());
	}
	
	@Override
	protected ExternalTransition buildExternalTransition() {
		return new MoveExternalTransition();
	}
	@Override
	public void markPlace(String placeId, String token, int count)
			throws InterfaceException {
		try {
	epn.getComponent(MoveExternalTransition.ARRIVED, Place.class).setTokenCount(MoveExternalTransition.DEFAULT, 1);
} catch (PetriNetComponentNotFoundException e) {
	throw new RuntimeException("MoveExternalTransition.fire: attempted to mark "+MoveExternalTransition.ARRIVED+" place, but it was not found.");
}

	}

}

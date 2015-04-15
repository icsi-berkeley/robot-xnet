package edu.berkeley.icsi.xnet;

import static org.junit.Assert.*;

import java.awt.Color;

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

public class MoveExternalTransitionTest extends AbstractExternalTransitionTest {

	@Test
	public void verifyCallsMorseWithMoveCommand() {
		TestingMorse morse = new TestingMorse();
		morseChannel.updateTargetLocation(6.7, 8.9); 
		morseChannel.setMorse(morse); 
		externalTransition.fire();
		assertTrue(morse.wasCalled()); 
		assertEquals("{\"command\":\"moveMorse\",\"location\":{\"x\":6.7,\"y\":8.9}}", morse.lastCommand()); 
//		assertEquals("{\"command\":\"moveMorse\",\"location\":{\"x\":6.7,\"y\":8.9,\"z\":0.0}}", morse.lastCommand()); 
		// 'collide':False, 'speed':2.0, 'tolerance':4
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


	@Override
	protected ExternalTransition buildExternalTransition() {
		return new MoveExternalTransition();
	}

}

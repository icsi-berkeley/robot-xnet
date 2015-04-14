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

public class MoveExternalTransitionTest implements Morse {

	private MoveExternalTransition mcet;
	private PetriNet net;
	private ExecutablePetriNet epn;
	private MorseChannel morseChannel;

	@Before
	public void setUp() throws Exception {
		net = buildNet();
		epn = net.getExecutablePetriNet(); 
		mcet = new MoveExternalTransition(); 
		mcet.setExecutablePetriNet(epn);
		morseChannel = new MorseChannel(); 
		mcet.setContext(morseChannel);
	}

	@Test
	public void verifyMarksPlaceWithOneDefaultToken() throws Exception {
		assertEquals(0, epn.getState().getTokens("P1").get("Default").intValue()); 
		mcet.markPlace("P1"); 
		assertEquals(1, epn.getState().getTokens("P1").get("Default").intValue()); 
	}
	@Test
	public void verifyCallsMorseWithMoveCommand() {
		TestingMorse morse = new TestingMorse();
		morseChannel.updateTargetLocation(6.7, 8.9); 
		morseChannel.setMorse(morse); 
		mcet.fire();
		assertTrue(morse.wasCalled()); 
		assertEquals("{\"command\":\"move\",\"location\":{\"x\":6.7,\"y\":8.9}}", morse.lastCommand()); 
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

    private PetriNet buildNet() {
    	PetriNet net = APetriNet.named("testnet").and(AToken.called("Default").withColor(Color.BLACK)).and(APlace.withId("P0").containing(1, "Default").token()).
    			and(APlace.withId("P1").externallyAccessible()).and(AnImmediateTransition.withId("T0")).
    			and(ANormalArc.withSource("P0").andTarget("T0").with("1", "Default").token()).
    			andFinally(ANormalArc.withSource("T0").andTarget("P1").with("1", "Default").token());
    	return net; 
    }

	@Override
	public void callMorse(String command) {
		
	}

}

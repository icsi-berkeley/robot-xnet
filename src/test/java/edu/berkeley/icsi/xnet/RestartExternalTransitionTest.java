package edu.berkeley.icsi.xnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import uk.ac.imperial.pipe.dsl.ANormalArc;
import uk.ac.imperial.pipe.dsl.APetriNet;
import uk.ac.imperial.pipe.dsl.APlace;
import uk.ac.imperial.pipe.dsl.AToken;
import uk.ac.imperial.pipe.dsl.AnImmediateTransition;
import uk.ac.imperial.pipe.models.petrinet.ExecutablePetriNet;
import uk.ac.imperial.pipe.models.petrinet.ExternalTransition;
import uk.ac.imperial.pipe.models.petrinet.PetriNet;

public class RestartExternalTransitionTest  extends AbstractExternalTransitionTest {

	@Test
	public void verifyCallsMorseWithSuspendCommand() {
		externalTransition.fire();
		assertTrue(morse.wasCalled()); 
		assertEquals("{\"command\":\"restartMorse\"}", morse.lastCommand()); 
	}

	@Override
	protected ExternalTransition buildExternalTransition() {
		return new RestartExternalTransition();
	}
}

package edu.berkeley.icsi.xnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.imperial.pipe.models.petrinet.ExternalTransition;

public class ResumeExternalTransitionTest extends AbstractExternalTransitionTest {

	@Test
	public void verifyCallsMorseWithSuspendCommand() {

		morseChannel.setMorse(morse); 
		externalTransition.fire();
		assertTrue(morse.wasCalled()); 
		assertEquals("{\"command\":\"resumeMorse\"}", morse.lastCommand()); 
	}

	@Override
	protected ExternalTransition buildExternalTransition() {
		return new ResumeExternalTransition();
	}
}

package edu.berkeley.icsi.xnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.imperial.pipe.models.petrinet.ExternalTransition;

public class ResumeExternalTransitionTest extends AbstractExternalTransitionTest {

	@Test
	public void verifyCallsMorseWithSuspendCommand() {

		motionChannel.setMover(mover); 
		externalTransition.fire();
		assertTrue(mover.wasCalled()); 
		assertEquals("{\"command\":\"resumeMover\"}", mover.lastCommand()); 
	}

	@Override
	protected ExternalTransition buildExternalTransition() {
		return new ResumeExternalTransition();
	}
}

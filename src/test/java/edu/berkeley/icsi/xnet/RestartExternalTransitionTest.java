package edu.berkeley.icsi.xnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.imperial.pipe.models.petrinet.ExternalTransition;

public class RestartExternalTransitionTest  extends AbstractExternalTransitionTest {

	@Test
	public void verifyCallsMorseWithSuspendCommand() {
		externalTransition.fire();
		assertTrue(mover.wasCalled()); 
		assertEquals("{\"command\":\"restartMover\"}", mover.lastCommand()); 
	}
	@Test
	public void verifySetsMorseChannelStatusToRestart() {
		externalTransition.fire();
		assertEquals(MotionChannel.RESTARTED, motionChannel.getStatus());
	}

	@Override
	protected ExternalTransition buildExternalTransition() {
		return new RestartExternalTransition();
	}
}

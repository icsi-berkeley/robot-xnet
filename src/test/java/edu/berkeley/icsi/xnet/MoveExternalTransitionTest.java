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
	public void verifyCallsMoverWithMoveCommand() {
		setupInitialMove();
		motionChannel.setMover(mover); 
		externalTransition.fire();
		assertTrue(mover.wasCalled()); 
		assertEquals("{\"command\":\"moveMover\",\"motion\":{\"x\":6.7,\"y\":8.9,\"z\":0.0,\"collide\":false,\"speed\":2.0,\"tolerance\":4.0}}", mover.lastCommand()); 
	}
	private void setupInitialMove() {
		motionChannel.updateTargetLocation(6.7, 8.9); 
		motionChannel.setSpeed(2.0); 
		motionChannel.setTolerance(4.0);
		motionChannel.setCollide(false);
	}
	@Test
	public void updatesCurrentAndTargetLocations() {
		assertEquals(0.0, motionChannel.getCurrentX(), 0.01); 
		assertEquals(0.0, motionChannel.getCurrentY(), 0.01); 
		assertEquals(0.0, motionChannel.getTargetX(), 0.01); 
		assertEquals(0.0, motionChannel.getTargetY(), 0.01); 
		motionChannel.updateCurrentLocation(2.4, 3.0); 
		assertEquals(2.4, motionChannel.getCurrentX(), 0.01); 
		assertEquals(3.0, motionChannel.getCurrentY(), 0.01); 
		motionChannel.updateTargetLocation(6.7, 8.9); 
		assertEquals(6.7, motionChannel.getTargetX(), 0.01); 
		assertEquals(8.9, motionChannel.getTargetY(), 0.01); 
	}
	@Test
	public void updatesOngoingStatusAndCommand() {
		assertNull(mover.lastCommand()); 
		assertEquals(MotionChannel.NOT_STARTED, motionChannel.getStatus());
		motionChannel.updateTargetLocation(6.7, 8.9); 
		externalTransition.fire();
		assertEquals(MotionChannel.ONGOING, motionChannel.getStatus());
	}
	@Test
	public void whileOngoingAfterFirstMoveCommandSubsequentCommandsAreToUpdatePosition() {
		motionChannel.updateTargetLocation(6.7, 8.9); 
		externalTransition.fire();
		assertEquals(MotionChannel.ONGOING, motionChannel.getStatus());
		assertEquals("{\"command\":\"moveMover\",\"motion\":{\"x\":6.7,\"y\":8.9,\"z\":0.0,\"collide\":false,\"speed\":0.0,\"tolerance\":0.0}}", mover.lastCommand()); 
		externalTransition.fire();
		assertEquals(MotionChannel.ONGOING, motionChannel.getStatus());
		assertEquals("{\"command\":\"updateMoverPosition\"}", mover.lastCommand()); 
	}
	@Test
	public void whenStatusIsArrivedMarksArrivedPlace() throws Exception {
		assertEquals(0, epn.getComponent("Arrived", Place.class).getTokenCount("Default")); 
		motionChannel.setStatus(MotionChannel.ARRIVED);
		externalTransitionProvider.setPlaceMarker(this);
		externalTransition.fire();
		assertFalse(mover.wasCalled());
		assertEquals(1, epn.getComponent("Arrived", Place.class).getTokenCount("Default"));
	}
	@Test
	public void whenStatusIsRestartGeneratesNewMoveCommand() throws Exception {
		motionChannel.updateTargetLocation(3.3, 4.4); 
		motionChannel.setSpeed(2.0); 
		motionChannel.setTolerance(4.0);
		motionChannel.setCollide(false);
		motionChannel.setMover(mover); 
		motionChannel.setStatus(MotionChannel.RESTARTED);
		externalTransition.fire();
		assertTrue(mover.wasCalled()); 
		assertEquals("{\"command\":\"moveMover\",\"motion\":{\"x\":3.3,\"y\":4.4,\"z\":0.0,\"collide\":false,\"speed\":2.0,\"tolerance\":4.0}}", mover.lastCommand()); 
	}
	@Test
	public void updatedMotionIgnoredUntilStatusChangesToRestart() throws Exception {
		setupInitialMove();
		motionChannel.setMover(mover); 
		externalTransition.fire();
		assertTrue(mover.wasCalled()); 
		assertEquals("{\"command\":\"moveMover\",\"motion\":{\"x\":6.7,\"y\":8.9,\"z\":0.0,\"collide\":false,\"speed\":2.0,\"tolerance\":4.0}}", mover.lastCommand()); 
		motionChannel.updateTargetLocation(3.3, 4.4); 
		externalTransition.fire();
		assertEquals("update ignored",MotionChannel.ONGOING, motionChannel.getStatus());
		assertEquals("{\"command\":\"updateMoverPosition\"}", mover.lastCommand()); 
		motionChannel.setStatus(MotionChannel.RESTARTED);
		assertEquals("implies RestartExternalTransition has fired",MotionChannel.RESTARTED, motionChannel.getStatus());
		externalTransition.fire();
		assertEquals("{\"command\":\"moveMover\",\"motion\":{\"x\":3.3,\"y\":4.4,\"z\":0.0,\"collide\":false,\"speed\":2.0,\"tolerance\":4.0}}", mover.lastCommand()); 
		assertEquals("new motion in progress",MotionChannel.ONGOING, motionChannel.getStatus());
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

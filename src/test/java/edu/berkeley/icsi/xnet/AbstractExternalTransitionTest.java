package edu.berkeley.icsi.xnet;

import java.awt.Color;

import org.junit.Before;

import uk.ac.imperial.pipe.dsl.ANormalArc;
import uk.ac.imperial.pipe.dsl.APetriNet;
import uk.ac.imperial.pipe.dsl.APlace;
import uk.ac.imperial.pipe.dsl.AToken;
import uk.ac.imperial.pipe.dsl.AnImmediateTransition;
import uk.ac.imperial.pipe.exceptions.PetriNetComponentException;
import uk.ac.imperial.pipe.models.petrinet.ExecutablePetriNet;
import uk.ac.imperial.pipe.models.petrinet.ExternalTransition;
import uk.ac.imperial.pipe.models.petrinet.PetriNet;

public abstract class AbstractExternalTransitionTest {

	protected ExternalTransition externalTransition;
	private PetriNet net;
	protected ExecutablePetriNet epn;
	protected MorseChannel morseChannel;
	protected TestingMorse morse;
	protected TestingExternalTransitionProvider externalTransitionProvider;

	@Before
	public void setUp() throws Exception {
		net = buildNet();
		epn = net.getExecutablePetriNet(); 
		externalTransitionProvider = new TestingExternalTransitionProvider();
		externalTransition = buildExternalTransition();
		externalTransitionProvider.setExecutablePetriNet(epn);
		morseChannel = new MorseChannel(); 
		externalTransitionProvider.setContext(morseChannel);
		externalTransition.setExternalTransitionProvider(externalTransitionProvider);
		morse = new TestingMorse();
		morseChannel.setMorse(morse); 
	}

	protected abstract ExternalTransition buildExternalTransition(); 

	private PetriNet buildNet() throws PetriNetComponentException {
		PetriNet net = APetriNet.named("testnet").and(AToken.called("Default").withColor(Color.BLACK)).and(APlace.withId("P0").containing(1, "Default").token()).
				and(APlace.withId("Arrived")).and(APlace.withId("P1").externallyAccessible()).and(AnImmediateTransition.withId("T0")).
				and(ANormalArc.withSource("P0").andTarget("T0").with("1", "Default").token()).
				andFinally(ANormalArc.withSource("T0").andTarget("P1").with("1", "Default").token());
		return net; 
	}

}

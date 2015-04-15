package edu.berkeley.icsi.xnet;

import java.awt.Color;

import org.junit.Before;

import uk.ac.imperial.pipe.dsl.ANormalArc;
import uk.ac.imperial.pipe.dsl.APetriNet;
import uk.ac.imperial.pipe.dsl.APlace;
import uk.ac.imperial.pipe.dsl.AToken;
import uk.ac.imperial.pipe.dsl.AnImmediateTransition;
import uk.ac.imperial.pipe.models.petrinet.ExecutablePetriNet;
import uk.ac.imperial.pipe.models.petrinet.ExternalTransition;
import uk.ac.imperial.pipe.models.petrinet.PetriNet;

public abstract class AbstractExternalTransitionTest {

	protected ExternalTransition externalTransition;
	private PetriNet net;
	private ExecutablePetriNet epn;
	protected MorseChannel morseChannel;
	protected TestingMorse morse;

	@Before
	public void setUp() throws Exception {
		net = buildNet();
		epn = net.getExecutablePetriNet(); 
		externalTransition = buildExternalTransition(); 
		externalTransition.setExecutablePetriNet(epn);
		morseChannel = new MorseChannel(); 
		externalTransition.setContext(morseChannel);
		morse = new TestingMorse();
		morseChannel.setMorse(morse); 
	}

	protected abstract ExternalTransition buildExternalTransition(); 

	private PetriNet buildNet() {
		PetriNet net = APetriNet.named("testnet").and(AToken.called("Default").withColor(Color.BLACK)).and(APlace.withId("P0").containing(1, "Default").token()).
				and(APlace.withId("P1").externallyAccessible()).and(AnImmediateTransition.withId("T0")).
				and(ANormalArc.withSource("P0").andTarget("T0").with("1", "Default").token()).
				andFinally(ANormalArc.withSource("T0").andTarget("P1").with("1", "Default").token());
		return net; 
	}

}

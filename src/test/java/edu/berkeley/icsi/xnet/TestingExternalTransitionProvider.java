package edu.berkeley.icsi.xnet;

import uk.ac.imperial.pipe.models.petrinet.ExecutablePetriNet;
import uk.ac.imperial.pipe.models.petrinet.ExternalTransitionProvider;
import uk.ac.imperial.pipe.runner.PlaceMarker;

public class TestingExternalTransitionProvider implements ExternalTransitionProvider {

	private PlaceMarker placeMarker;
	private ExecutablePetriNet executablePetriNet;
	private Object context;

	@Override
	public PlaceMarker getPlaceMarker() {
		return this.placeMarker;
	}

	@Override
	public ExecutablePetriNet getExecutablePetriNet() {
		return this.executablePetriNet;
	}

	@Override
	public Object getContext() {
		return this.context;
	}

	public void setPlaceMarker(PlaceMarker placeMarker) {
		this.placeMarker = placeMarker;
	}

	public void setExecutablePetriNet(ExecutablePetriNet executablePetriNet) {
		this.executablePetriNet = executablePetriNet;
	}

	public void setContext(Object context) {
		this.context = context;
	}

}

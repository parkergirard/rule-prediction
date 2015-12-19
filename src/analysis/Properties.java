package analysis;

import enums.*;
import java.util.Set;

public class Properties {
	Set<PLACE> places;
	Set<MANNER> manners;
	Set<VOICE> voices;
	
	public Properties(Set<PLACE> places, 
			Set<MANNER> manners, Set<VOICE> voices) {
		this.places = places;
		this.manners = manners;
		this.voices = voices;
	}

	public void addPlace(PLACE place) {
		places.add(place);
	}
	
	public Set<PLACE> getPlaces() {
		return places;
	}
	
	public void addManner(MANNER manner) {
		manners.add(manner);
	}
	
	public Set<MANNER> getManners() {
		return manners;
	}

	public void addVoicing(VOICE voice) {
		voices.add(voice);
	}
	
	public Set<VOICE> getVoices() {
		return voices;
	}
}

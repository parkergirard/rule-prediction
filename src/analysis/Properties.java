package analysis;

import enums.*;

import java.util.HashSet;
import java.util.Set;

public class Properties {
	Set<PLACE> places;
	Set<MANNER> manners;
	Set<VOICE> voices;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Places: ");
		sb.append(places);
		sb.append("\nManners: ");
		sb.append(manners);
		sb.append("\nVoices: ");
		sb.append(voices);
		
		return sb.toString();
	}
	
	public Properties() {
		initMaps();
	}
	
	/**
	 * Init with one of each val
	 * @param p
	 * @param m
	 * @param v
	 */
	public Properties(PLACE p, MANNER m, VOICE v) {
		initMaps();
		add(p, m, v);
	}
	
	private void initMaps() {
		this.places = new HashSet<PLACE>();
		this.manners = new HashSet<MANNER>();
		this.voices = new HashSet<VOICE>();
	}
	
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
	
	public void add(PLACE p, MANNER m, VOICE v) {
		if (p != null) {
			places.add(p);
		}
		if (m != null) {
			manners.add(m);
		}
		if (v != null) {
			voices.add(v);
		}
	}
	
	public Set<VOICE> getVoices() {
		return voices;
	}
}

package analysis;

import enums.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/**
 * NOT defaulted to be global
 *
 */
public class FeatureProperties {
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
	
	public FeatureProperties() {
		initMaps();
	}
	
	/**
	 * Init with one of each val
	 * @param p
	 * @param m
	 * @param v
	 */
	public FeatureProperties(PLACE p, MANNER m, VOICE v) {
		initMaps();
		add(p, m, v);
	}
	
	private void initMaps() {
		this.places = new HashSet<PLACE>();
		this.manners = new HashSet<MANNER>();
		this.voices = new HashSet<VOICE>();
	}
	
	public FeatureProperties(Set<PLACE> places, 
			Set<MANNER> manners, Set<VOICE> voices) {
		this.places = places;
		this.manners = manners;
		this.voices = voices;
	}

	public void addPlace(PLACE place) {
		places.add(place);
	}

	public void removePlace(PLACE place) {
		places.remove(place);
	}
	
	public Set<PLACE> getPlaces() {
		return places;
	}
	
	public void addManner(MANNER manner) {
		manners.add(manner);
	}
	
	public void removeManner(MANNER manner) {
		manners.remove(manner);
	}
	
	public Set<MANNER> getManners() {
		return manners;
	}

	public void addVoicing(VOICE voice) {
		voices.add(voice);
	}

	public Set<VOICE> getVoicing() {
		return voices;
	}

	public void removeVoicing(VOICE voice) {
		voices.remove(voice);
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
	
	/**
	 * Sets all sets to contain every possibility
	 */
	public void makePropertiesGlobal() {
		FEATURE[] vals = PLACE.values();
		for (int i = 0; i < vals.length; i++) {
			places.add((PLACE) vals[i]);
		}
		vals = MANNER.values();
		for (int i = 0; i < vals.length; i++) {
			manners.add((MANNER) vals[i]);
		}
		vals = VOICE.values();
		for (int i = 0; i < vals.length; i++) {
			voices.add((VOICE) vals[i]);
		}
	}
	
	public Set<VOICE> getVoices() {
		return voices;
	}
	
	@Override
	/**
	 * Everything must be the same
	 */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeatureProperties r = (FeatureProperties) o;
        return  Objects.equals(places, r.places) && 
        		Objects.equals(manners, r.manners) &&
        		Objects.equals(voices, r.voices); 
    }
	
	@Override
    public int hashCode() {
        return Objects.hash(places, manners, voices);
    }
}

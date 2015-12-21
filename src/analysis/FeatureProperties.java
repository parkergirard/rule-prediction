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
	
	public FeatureProperties() {
		initEmptyMaps();
	}
	
	public FeatureProperties(PHONEME p) {
		initEmptyMaps();
		add(p);
	}
	
	/**
	 * Init with one of each val
	 * @param p
	 * @param m
	 * @param v
	 */
	public FeatureProperties(PLACE p, MANNER m, VOICE v) {
		initEmptyMaps();
		add(p, m, v);
	}
	
	public FeatureProperties(Set<PLACE> places, 
			Set<MANNER> manners, Set<VOICE> voices) {
		this.places = places;
		this.manners = manners;
		this.voices = voices;
	}

	
	private void initEmptyMaps() {
		this.places = new HashSet<PLACE>();
		this.manners = new HashSet<MANNER>();
		this.voices = new HashSet<VOICE>();
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

	public void addVoice(VOICE voice) {
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
	
	public void add(PHONEME p) {
		add(p.getPlace(), p.getManner(), p.getVoice());
	}
	
	/**
	 * Sets all sets to contain every possibility
	 */
	public void makeAllFeaturesGlobal() {
		makePlaceGlobal();
		makeMannerGlobal();
		makeVoiceGlobal();
	}
	
	public void makePlaceGlobal() {
		// places
		PLACE[] ps = PLACE.values();
		for (int i = 0; i < ps.length; i++) {
			places.add((PLACE) ps[i]);
		}
	}
	
	public void makeMannerGlobal() {
		// manners
		MANNER[] ms = MANNER.values();
		for (int i = 0; i < ms.length; i++) {
			// skip if vowel
			if (ms[i].getGroup().equals(GROUP.VOWEL)) {
				continue;
			}
			manners.add((MANNER) ms[i]);
		}
	}
	
	public void makeVoiceGlobal() {
		// voices
		VOICE[] vs = VOICE.values();
		for (int i = 0; i < vs.length; i++) {
			voices.add((VOICE) vs[i]);
		}
	}
	
	public Set<VOICE> getVoices() {
		return voices;
	}
	
	public boolean isGlobal() {
		return places.size() == PLACE.values().length &&
				manners.size() == MANNER.values().length - 3 &&
				voices.size() == VOICE.values().length;
	}
	
	@Override
	/**
	 * A = B if B's details are exactly A's details
	 */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FeatureProperties r = (FeatureProperties) o;
        return  r.places.equals(places) &&
        		r.manners.equals(manners) &&
        		r.voices.equals(voices);
    }
	
	@Override
    public int hashCode() {
        return Objects.hash(places, manners, voices);
    }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String gap = "    ";
		
		sb.append("Places: ");
		if (places.size() == PLACE.values().length) {
			sb.append("(ALL)");
		}
		sb.append("\n" + gap + places);
		
		sb.append("\nManners: ");
		// ( - 3 for vowel manners)
		if (manners.size() == MANNER.values().length - 3) {
			sb.append("(ALL)");
		}
		sb.append("\n" + gap + manners);
		
		sb.append("\nVoices: ");
		if (voices.size() == VOICE.values().length) {
			sb.append("(ALL)");
		}
		sb.append("\n" + gap +  voices);
		
		return sb.toString();
	}
}

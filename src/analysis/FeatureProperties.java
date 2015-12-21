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

	public PLACE getSinglePlace() {
		if (places.size() == 0 || places.size() > 1) {
			throw new IllegalArgumentException("More than one place.");
		}
		// there's only one place. return it
		for (PLACE p : places) {
			return p;
		}
		return null;
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
	
	public MANNER getSingleManner() {
		if (manners.size() == 0 || manners.size() > 1) {
			throw new IllegalArgumentException("More than one manner.");
		}
		// there's only one Manner. return it
		for (MANNER m : manners) {
			return m;
		}
		return null;
	}

	public void addVoice(VOICE voice) {
		voices.add(voice);
	}

	public void removeVoicing(VOICE voice) {
		voices.remove(voice);
	}
	
	public Set<VOICE> getVoices() {
		return voices;
	}
	

	public VOICE getSingleVoice() {
		if (voices.size() == 0 || voices.size() > 1) {
			throw new IllegalArgumentException("More than one voice.");
		}
		// there's only one Voice. return it
		for (VOICE v : voices) {
			return v;
		}
		return null;
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
	
	public boolean isGlobal() {
		return places.size() == PLACE.values().length &&
				manners.size() == MANNER.values().length &&
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
		if (manners.size() == MANNER.values().length) {
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

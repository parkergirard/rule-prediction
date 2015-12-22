package enums;

import analysis.FeatureProperties;

/**
 * Representations of phonemes corresponding to
 * the Arpabet (https://en.wikipedia.org/wiki/Arpabet, null, null),
 * along with their features (from LING120, 2013)
 * 
 * Phonemes take in a Group (Vowel vs Consonant), a Manner, and a Place
 */

public enum PHONEME {

	// VOWELS

	AO (GROUP.VOWEL, null, null, VOICE.VOICED),
	AA (GROUP.VOWEL, null, null, VOICE.VOICED),
	IY (GROUP.VOWEL, null, null, VOICE.VOICED),
	UW (GROUP.VOWEL, null, null, VOICE.VOICED),
	EH (GROUP.VOWEL, null, null, VOICE.VOICED),
	IH (GROUP.VOWEL, null, null, VOICE.VOICED),
	UH (GROUP.VOWEL, null, null, VOICE.VOICED),
	AH (GROUP.VOWEL, null, null, VOICE.VOICED),
	AX (GROUP.VOWEL, null, null, VOICE.VOICED),
	AE (GROUP.VOWEL, null, null, VOICE.VOICED),


	EY (GROUP.VOWEL, null, null, VOICE.VOICED),
	AY (GROUP.VOWEL, null, null, VOICE.VOICED),
	OW (GROUP.VOWEL, null, null, VOICE.VOICED),
	AW (GROUP.VOWEL, null, null, VOICE.VOICED),
	OY (GROUP.VOWEL, null, null, VOICE.VOICED),


	ER (GROUP.VOWEL, null, null, VOICE.VOICED),
	AXR (GROUP.VOWEL, null, null, VOICE.VOICED),
	EH_R (GROUP.VOWEL, null, null, VOICE.VOICED),
	AO_R (GROUP.VOWEL, null, null, VOICE.VOICED),
	AA_R (GROUP.VOWEL, null, null, VOICE.VOICED),
	IH_R (GROUP.VOWEL, null, null, VOICE.VOICED),
	IY_R (GROUP.VOWEL, null, null, VOICE.VOICED),
	AW_R (GROUP.VOWEL, null, null, VOICE.VOICED),

	// CONSONANTS

	P (GROUP.CONSONANT, MANNER.STOP, PLACE.BILABIAL, VOICE.VOICELESS),
	B (GROUP.CONSONANT, MANNER.STOP, PLACE.BILABIAL, VOICE.VOICED),
	T (GROUP.CONSONANT, MANNER.STOP, PLACE.ALVEOLAR, VOICE.VOICELESS),
	D (GROUP.CONSONANT, MANNER.STOP, PLACE.ALVEOLAR, VOICE.VOICED),
	K (GROUP.CONSONANT, MANNER.STOP, PLACE.VELAR, VOICE.VOICELESS),
	G (GROUP.CONSONANT, MANNER.STOP, PLACE.VELAR, VOICE.VOICED),


	CH (GROUP.CONSONANT, MANNER.AFFRICATE, PLACE.POSTALVEOLAR, VOICE.VOICELESS),
	JH (GROUP.CONSONANT, MANNER.AFFRICATE, PLACE.POSTALVEOLAR, VOICE.VOICED),


	F (GROUP.CONSONANT, MANNER.FRICATIVE, PLACE.LABIODENTAL, VOICE.VOICELESS),
	V (GROUP.CONSONANT, MANNER.FRICATIVE, PLACE.LABIODENTAL, VOICE.VOICED),
	TH (GROUP.CONSONANT, MANNER.FRICATIVE, PLACE.DENTAL, VOICE.VOICELESS),
	DH (GROUP.CONSONANT, MANNER.FRICATIVE, PLACE.DENTAL, VOICE.VOICED),
	S (GROUP.CONSONANT, MANNER.FRICATIVE, PLACE.ALVEOLAR, VOICE.VOICELESS),
	Z (GROUP.CONSONANT, MANNER.FRICATIVE, PLACE.ALVEOLAR, VOICE.VOICED),
	SH (GROUP.CONSONANT, MANNER.FRICATIVE, PLACE.POSTALVEOLAR, VOICE.VOICELESS),
	ZH (GROUP.CONSONANT, MANNER.FRICATIVE, PLACE.POSTALVEOLAR, VOICE.VOICED),
	HH (GROUP.CONSONANT, MANNER.FRICATIVE, PLACE.UVULAR, VOICE.VOICELESS),


	M (GROUP.CONSONANT, MANNER.NASAL, PLACE.BILABIAL, VOICE.VOICED),
	N (GROUP.CONSONANT, MANNER.NASAL, PLACE.ALVEOLAR, VOICE.VOICED),
	NG (GROUP.CONSONANT, MANNER.NASAL, PLACE.VELAR, VOICE.VOICED),

	L (GROUP.CONSONANT, MANNER.LATERAL_APPROXIMANT, PLACE.ALVEOLAR, VOICE.VOICED),
	R (GROUP.CONSONANT, MANNER.APPROXIMANT, PLACE.ALVEOLAR, VOICE.VOICED),
	DX (GROUP.CONSONANT, MANNER.FLAP, PLACE.ALVEOLAR, VOICE.VOICED),


	Y (GROUP.CONSONANT, MANNER.APPROXIMANT, PLACE.PALATAL, VOICE.VOICED),
	W (GROUP.CONSONANT, MANNER.APPROXIMANT, PLACE.VELAR, VOICE.VOICELESS);
	
	// DROP
//	X (GROUP.VOWEL, null, null, null);

	private GROUP group;
	private MANNER manner;
	private PLACE place;
	private VOICE voice;

	PHONEME(GROUP group, MANNER manner, PLACE place, VOICE voice) {
		this.group = group;
		this.manner = manner;
		this.place = place;
		this.voice = voice;
	}

	PHONEME(GROUP group, MANNER manner, PLACE place, VOICE voice, PHONEME contrast) {
		this.group = group;
		this.manner = manner;
		this.place = place;
		this.voice = voice;
	}
	
	public FeatureProperties getProperties() {
		return new FeatureProperties(place, manner, voice);
	}

	public GROUP getGroup() {
		return group;
	}

	public void setGroup(GROUP group) {
		this.group = group;
	}

	public MANNER getManner() {
		return manner;
	}

	public void setManner(MANNER manner) {
		this.manner = manner;
	}

	public PLACE getPlace() {
		return place;
	}

	public void setPlace(PLACE place) {
		this.place = place;
	}

	public VOICE getVoice() {
		return voice;
	}

	public void setVoice(VOICE voice) {
		this.voice = voice;
	}
	
	public boolean isVowel() {
		return group.equals(GROUP.VOWEL);
	}

	@Override
	public String toString() {
		String str = "";
		str += name();
//		str += ": " + group + ", " + manner + ", " + place + ", " + voice;
		return str;
	}

}

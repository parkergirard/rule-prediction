package phonemes;
/**
 * Representations of phonemes corresponding to
 * the Arpabet (https://en.wikipedia.org/wiki/Arpabet, null, null),
 * along with their features (from LING120, 2013)
 * 
 * Phonemes take in a Group (Vowel vs Consonant), a Manner, and a Place
 */

public enum PHONEME {

	AO (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	AA (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	IY (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	UW (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	EH (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	IH (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	UH (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	AH (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	AX (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	AE (GROUP.VOWEL, MANNER.MONOPHTHONG, null, null),
	

	EY (GROUP.VOWEL, MANNER.DIPHTHONG, null, null),
	AY (GROUP.VOWEL, MANNER.DIPHTHONG, null, null),
	OW (GROUP.VOWEL, MANNER.DIPHTHONG, null, null),
	AW (GROUP.VOWEL, MANNER.DIPHTHONG, null, null),
	OY (GROUP.VOWEL, MANNER.DIPHTHONG, null, null),
	

	ER (GROUP.VOWEL, MANNER.RCOLORED, null, null),
	AXR (GROUP.VOWEL, MANNER.RCOLORED, null, null),
	EH_R (GROUP.VOWEL, MANNER.RCOLORED, null, null),
	AO_R (GROUP.VOWEL, MANNER.RCOLORED, null, null),
	AA_R (GROUP.VOWEL, MANNER.RCOLORED, null, null),
	IH_R (GROUP.VOWEL, MANNER.RCOLORED, null, null),
	IY_R (GROUP.VOWEL, MANNER.RCOLORED, null, null),
	AW_R (GROUP.VOWEL, MANNER.RCOLORED, null, null),
	

	P (GROUP.CONSONANT, MANNER.STOP, null, null),
	B (GROUP.CONSONANT, MANNER.STOP, null, null),
	T (GROUP.CONSONANT, MANNER.STOP, null, null),
	D (GROUP.CONSONANT, MANNER.STOP, null, null),
	K (GROUP.CONSONANT, MANNER.STOP, null, null),
	G (GROUP.CONSONANT, MANNER.STOP, null, null),
	
	
	CH (GROUP.CONSONANT, MANNER.AFFRICATE, null, null),
	JH (GROUP.CONSONANT, MANNER.AFFRICATE, null, null),
	
	
	F (GROUP.CONSONANT, MANNER.FRICATIVE, null, null),
	V (GROUP.CONSONANT, MANNER.FRICATIVE, null, null),
	TH (GROUP.CONSONANT, MANNER.FRICATIVE, null, null),
	DH (GROUP.CONSONANT, MANNER.FRICATIVE, null, null),
	S (GROUP.CONSONANT, MANNER.FRICATIVE, null, null),
	Z (GROUP.CONSONANT, MANNER.FRICATIVE, null, null),
	SH (GROUP.CONSONANT, MANNER.FRICATIVE, null, null),
	ZH (GROUP.CONSONANT, MANNER.FRICATIVE, null, null),
	HH (GROUP.CONSONANT, MANNER.FRICATIVE, null, null),
	

	M (GROUP.CONSONANT, MANNER.NASAL, null, null),
	EM (GROUP.CONSONANT, MANNER.NASAL, null, null),
	N (GROUP.CONSONANT, MANNER.NASAL, null, null),
	EN (GROUP.CONSONANT, MANNER.NASAL, null, null),
	NG (GROUP.CONSONANT, MANNER.NASAL, null, null),
	ENG (GROUP.CONSONANT, MANNER.NASAL, null, null),
	

	L (GROUP.CONSONANT, MANNER.APPROXIMANT, null, null),
	R (GROUP.CONSONANT, MANNER.APPROXIMANT, null, null),
	DX (GROUP.CONSONANT, MANNER.APPROXIMANT, null, null),
	NX (GROUP.CONSONANT, MANNER.APPROXIMANT, null, null),
	

	Y (GROUP.CONSONANT, MANNER.SEMIVOWEL, null, null),
	W (GROUP.CONSONANT, MANNER.SEMIVOWEL, null, null),
	Q (GROUP.CONSONANT, MANNER.SEMIVOWEL, null, null);
	
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

	@Override
	public String toString() {
		String str = "";
		str += name() + ": " + group + ", " + manner + ", " + place + ", " + voice;
		return str;
	}
	
	
}

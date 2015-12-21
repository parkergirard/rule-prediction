package enums;

/**
 * Manner for the phoneme
 */
public enum MANNER implements FEATURE {
 // FOLLOWING ARE FOR CONSONANTS
 STOP,
 AFFRICATE,
 FRICATIVE,
 NASAL,
 APPROXIMANT,
 LATERAL_APPROXIMANT,
 FLAP;
 // FOLLOWING ARE FOR VOWELS
// MONOPHTHONG (GROUP.VOWEL),
// DIPHTHONG (GROUP.VOWEL),
// RCOLORED (GROUP.VOWEL);
 
 private GROUP group;

 MANNER(GROUP group) {
	 this.group = group;
 }

 MANNER() {
	 this.group = GROUP.CONSONANT;
 }

 public GROUP getGroup() {
	 return group;
 }
 
}

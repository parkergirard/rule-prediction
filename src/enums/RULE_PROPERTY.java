package enums;

/**
  Rule has information about phonetic environment:
	1. Is it at the beginning or end of a word/syllable?
	2. Is it before, after, or surrounded by vowels?

  Rule has information about what sounds change
	1. What properties does a sound need to have in order to change?
		A. place? or B. manner? or C. voicing?
 */
public enum RULE_PROPERTY {
	
	// PHONETIC ENVIRONMENT
	
	// Is it at the beginning or end of a word/syllable?
	BEGINNING_OF_WORD,
	END_OF_WORD,
	BEGINNING_OF_SYLLABLE,
	END_OF_SYLLABLE,
	
	// Is it before, after, or surrounded by vowels?
	BEFORE_VOWEL,
	AFTER_VOWEL,
	SURROUNDED_BY_VOWELS,
	
	// WHAT SOUNDS CHANGE
	
	// What properties does a sound need to have in order to change?
	PLACE,
	MANNER,
	VOICING;
	
}

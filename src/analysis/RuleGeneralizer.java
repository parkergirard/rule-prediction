package analysis;

import enums.*;
import helpers.Helpers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RuleGeneralizer {

	Set<SpecificRule> givenRules;
	
	Map<DoubleFeatureProperties, GeneralizedRule> featuresToRule;
	
	
	/**
	 * Begin generalizing rules given a set of specific rules
	 * @param rules
	 */
	public RuleGeneralizer(Set<SpecificRule> rules) {
		this.givenRules = rules;
		featuresToRule = new HashMap<DoubleFeatureProperties, GeneralizedRule>();
		generalize();
	}
	
	private void generalize() {
		// loop through all given rules where
		// the rule isn't self transforming
		for (SpecificRule r : givenRules) {
			// skip if transforms to self
			if (r.transformsToSelf()) {
				continue;
			}
			// we will construct a new general rule
			// with these features for its input and output
			// phonemes
			FeatureProperties inputPhonemeFeatures = new FeatureProperties();
			FeatureProperties outputPhonemeFeatures = new FeatureProperties();
			Set<FEATURE_TYPE> remainsSame = new HashSet<FEATURE_TYPE>();
			
			// get target and actual phoneme of the rule
			PHONEME target = r.getTargetPhoneme();
			PHONEME actual = r.getActualPhoneme();

			// VOICES
			
			// look at voices of target/actual phonemes
			VOICE targetVoice = target.getVoice();
			VOICE actualVoice = actual.getVoice();
			
			if (targetVoice.equals(actualVoice)) {
				// rule didn't transform the voice
				
				// make input phoneme's voice = [all]
				inputPhonemeFeatures.makeVoiceGlobal();
				// make output phoneme's voice = [remain same as input]
				remainsSame.add(FEATURE_TYPE.VOICE);
			} else {
				// rule transformed the voice
				
				// Make Input Phoneme’s Voice = [Target Phoneme’s voice]
				inputPhonemeFeatures.addVoice(targetVoice);
				// Make Output Phoneme’s Voice = [Actual Phoneme’s voice]
				outputPhonemeFeatures.addVoice(actualVoice);
			}
			
			
			// PLACES
			

			// look at places of target/actual phonemes
			PLACE targetPlace = target.getPlace();
			PLACE actualPlace = actual.getPlace();
			
			if (targetPlace.equals(actualPlace)) {
				// rule didn't transform the place
				
				// make input phoneme's place = [all]
				inputPhonemeFeatures.makePlaceGlobal();
				// make output phoneme's place = [remain same as input]
				remainsSame.add(FEATURE_TYPE.PLACE);
			} else {
				// rule transformed the place
				
				// Make Input Phoneme’s Place = [Target Phoneme’s place]
				inputPhonemeFeatures.addPlace(targetPlace);
				// Make Output Phoneme’s Place = [Actual Phoneme’s place]
				outputPhonemeFeatures.addPlace(actualPlace);
			}
			
			// MANNERS
			
			// look at manners of target/actual phonemes
			MANNER targetManner = target.getManner();
			MANNER actualManner = actual.getManner();
			
			if (targetManner.equals(actualManner)) {
				// rule didn't transform the manner
				
				// make input phoneme's manner = [all]
				inputPhonemeFeatures.makeMannerGlobal();
				// make output phoneme's place = [remain same as input]
				remainsSame.add(FEATURE_TYPE.MANNER);
			} else {
				// rule transformed the manner
				
				// Make Input Phoneme’s Manner = [Target Phoneme’s manner]
				inputPhonemeFeatures.addManner(targetManner);
				// Make Output Phoneme’s Manner = [Actual Phoneme’s manner]
				outputPhonemeFeatures.addManner(actualManner);
			}
			
			// PHONETIC ENV
			
			DoubleFeatureProperties dfp = new 
					DoubleFeatureProperties(inputPhonemeFeatures,
							outputPhonemeFeatures);
			
			// get any existing rule for these input/output features
			GeneralizedRule existingGenRule = featuresToRule.get(dfp);
			
			// Construct Phonetic Env for the new rule
			PhoneticEnvironment newEnv = new PhoneticEnvironment(false);
			
			// get phonetic env from the current specific rule
			PhoneticEnvironment currentEnv = r.getEnvironment();
			
			if (existingGenRule == null) {
				// gen rule with exact same input/output features doesn't
				// exist yet. make env of the gen rule = the specific rule
				
				newEnv = currentEnv;
			} else {
				// gen rule that has the exact 
				// same input/output features does already exist
				
				// intersect all aspects of the environments
				
				PhoneticEnvironment existingGenRuleEnv = 
						existingGenRule.getPhoneticEnvironment();

				newEnv.setWordPlacement(
						Helpers.getIntersectionOfSets(existingGenRuleEnv.getWordPlacement(),
								currentEnv.getWordPlacement())
						);

				newEnv.setSyllablePlacement(
						Helpers.getIntersectionOfSets(existingGenRuleEnv.getSyllablePlacement(), 
								currentEnv.getSyllablePlacement())
						);

				newEnv.setVowelPlacement(
						Helpers.getIntersectionOfSets(existingGenRuleEnv.getVowelPlacement(), 
								currentEnv.getVowelPlacement())
						);

				newEnv.setComesAfterPhonemes(
						Helpers.getSet1MinusSet2(existingGenRuleEnv.getComesAfterPhonemes(),
								currentEnv.getDoesntComeAfterPhonemes()
						));
				
				/*
				 * For the specific rule, if there is a phoneme the specific 
				 * rule cannot come after/before (P), the general rule cannot 
				 * come after/before any phoneme that has P’s same place and 
				 * manner, and opposite voicing
				 */
				
				newEnv = removeContrastsFromSet(newEnv, newEnv.getComesAfterPhonemes());
				
				newEnv.setComesBeforePhonemes(
						Helpers.getSet1MinusSet2(existingGenRuleEnv.getComesBeforePhonemes(),
								currentEnv.getDoesntComeBeforePhonemes()
						));
				
				// same as above but with comesBefore instead of after
				
				newEnv = removeContrastsFromSet(newEnv, newEnv.getComesBeforePhonemes());
				
				// delete the already existed general rule
				featuresToRule.remove(dfp);
				r = null;
			}
			
			// add the new gen rule
			GeneralizedRule newGenRule = new GeneralizedRule(
					inputPhonemeFeatures, outputPhonemeFeatures,
					remainsSame, newEnv);
			featuresToRule.put(dfp, newGenRule);
		}
	}
	
	/**
	 * Helper functions to remove contrasting phonemes from a set
	 * of phonemes
	 * @param e the phonetic environment to modify
	 * @param set the set of phonemes to look at contrasts of
	 * @return the modified environment
	 */
	private PhoneticEnvironment removeContrastsFromSet(PhoneticEnvironment e,
			Set<PHONEME> set) {

		Set<PHONEME> mustRemove = new HashSet<PHONEME>();
		
		Set<PHONEME> oppositeSet = null;
		if (set.equals(e.getComesAfterPhonemes())) {
			oppositeSet = 
					e.getDoesntComeAfterPhonemes();
		} else if (set.equals(e.getComesBeforePhonemes())) {
			oppositeSet = 
					e.getDoesntComeBeforePhonemes();
		} else {
			throw new IllegalArgumentException(
					"Can only remove from comesAfter/"
					+ "comesBefore phoneme sets.");
		}
		
		for (PHONEME p : oppositeSet) {
			try {
				// get p's counterpart (same with opposite voicing)
				PHONEME contrast = CONTRASTING_PHONEME.
						valueOf(p.name()).getContrastingPhoneme();
				// need to remove p's counterpart
				mustRemove.add(contrast);
			} catch (Exception ex) {
				// ignore, it just means there is no contrasting phoneme
			}
		}
		// remove all of the counterparts
		for (PHONEME contrast : mustRemove) {
			// remove from appropriate set
			if (set.equals(e.getComesAfterPhonemes())) {
				e.removeComesAfterPhoneme(contrast);
			} else if (set.equals(e.getComesBeforePhonemes())) {
				e.removeComesBeforePhoneme(contrast);
			}
		}
		
		return e;
	}

	public Collection<GeneralizedRule> getGeneralizedRules() {
		return featuresToRule.values();
	}
	
	
	/**
	 * Helper class to map input/output features as one key for a map
	 */
	private class DoubleFeatureProperties {
		private FeatureProperties p1;
		private FeatureProperties p2;
		public DoubleFeatureProperties(FeatureProperties p1, FeatureProperties p2) {
			this.p1 = p1;
			this.p2 = p2;
		}
		@Override
		public boolean equals(Object o) {
			if (this == o) {
	            return true;
	        }
	        if (o == null || getClass() != o.getClass()) {
	            return false;
	        }
	        DoubleFeatureProperties r = (DoubleFeatureProperties) o;
	        return p1.equals(r.p1) && p2.equals(r.p2);
		}

	    @Override
	    public int hashCode() {
	        return p1.hashCode() + p2.hashCode();
	    }
	}
	
}

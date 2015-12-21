package analysis;

import enums.*;
import helpers.SetHelpers;

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
						SetHelpers.getIntersectionOfSets(existingGenRuleEnv.getWordPlacement(),
								currentEnv.getWordPlacement())
						);

				newEnv.setSyllablePlacement(
						SetHelpers.getIntersectionOfSets(existingGenRuleEnv.getSyllablePlacement(), 
								currentEnv.getSyllablePlacement())
						);

				newEnv.setVowelPlacement(
						SetHelpers.getIntersectionOfSets(existingGenRuleEnv.getVowelPlacement(), 
								currentEnv.getVowelPlacement())
						);

				newEnv.setComesAfterPhonemes(
						SetHelpers.getSet1MinusSet2(existingGenRuleEnv.getComesAfterPhonemes(),
								currentEnv.getDoesntComeAfterPhonemes()
						));
				
				newEnv.setComesBeforePhonemes(
						SetHelpers.getSet1MinusSet2(existingGenRuleEnv.getComesBeforePhonemes(),
								currentEnv.getDoesntComeBeforePhonemes()
						));
				
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

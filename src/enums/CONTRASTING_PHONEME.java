package enums;

public enum CONTRASTING_PHONEME {
	
	P (PHONEME.B),
	B (PHONEME.P),
	
	T (PHONEME.D),
	D (PHONEME.T),
	
	K (PHONEME.G),
	G (PHONEME.K),
	
	CH (PHONEME.JH),
	JH (PHONEME.CH),
	
	F (PHONEME.V),
	V (PHONEME.F),
	
	TH (PHONEME.DH),
	DH (PHONEME.TH),
	
	S (PHONEME.Z),
	Z (PHONEME.S),
	
	SH (PHONEME.ZH),
	ZH (PHONEME.SH);
	
	private PHONEME contrastingPhoneme = null;
	CONTRASTING_PHONEME(PHONEME contrastsWith) {
		this.contrastingPhoneme = contrastsWith;
	}
	
	public PHONEME getContrastingPhoneme() {
		return contrastingPhoneme;
	}
	
	@Override
	public String toString() {
		String str = name() + ": " + contrastingPhoneme.name();
		return str;
	}
	
}

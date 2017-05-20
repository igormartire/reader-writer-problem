package policy;

public class ReaderWriterPolicyFactory {
	private final ReaderWriterPolicyType policyType;

	public ReaderWriterPolicyFactory(ReaderWriterPolicyType policyType) {
		this.policyType = policyType;
	}

	public ReaderWriterPolicy buildPolicy() {
		switch (this.policyType) {
			case ReadersPreferencePolicy:
				return new ReadersPreferencePolicy();
			case WritersPreferencePolicy:
				return new WritersPreferencePolicy();
			case NoPreferencePolicy:
				return new NoPreferencePolicy();
			default:
				throw new IllegalStateException("Invalid policy type set for ReaderWriterPolicyFactory.");
		}
	}

	public ReaderWriterPolicyType getPolicyType() {
		return this.policyType;
	}
}

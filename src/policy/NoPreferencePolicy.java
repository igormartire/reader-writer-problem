package policy;

public class NoPreferencePolicy implements ReaderWriterPolicy {
	@Override
	public void beforeReading() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void afterReading() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void beforeWriting() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void afterWriting() {
		throw new UnsupportedOperationException();
	}
}

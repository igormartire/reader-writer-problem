package policy;

public interface ReaderWriterPolicy {
	void beforeReading() throws InterruptedException;
	void afterReading() throws InterruptedException;
	void beforeWriting() throws InterruptedException;
	void afterWriting() throws InterruptedException;
}

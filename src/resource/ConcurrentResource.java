package resource;

import policy.ReaderWriterPolicy;

import java.io.FileNotFoundException;

public class ConcurrentResource extends Resource {
	private final ReaderWriterPolicy readerWriterPolicy;

	public ConcurrentResource(int id, ReaderWriterPolicy readerWriterPolicy) {
		super(id);
		this.readerWriterPolicy = readerWriterPolicy;
	}

	public String read(int duration) throws FileNotFoundException, InterruptedException {
		this.readerWriterPolicy.beforeReading();
		final String read;
		try {
			read = super.read(duration);
		}
		finally {
			this.readerWriterPolicy.afterReading();
		}
		return read;
	}

	public void write(String content, int duration) throws FileNotFoundException, InterruptedException {
		this.readerWriterPolicy.beforeWriting();
		try {
			super.write(content, duration);
		}
		finally {
			this.readerWriterPolicy.afterWriting();
		}
	}
}

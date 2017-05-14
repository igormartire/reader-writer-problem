package policy;

import java.util.concurrent.Semaphore;

public class ReadersPreferencePolicy implements ReaderWriterPolicy {
	private final Semaphore rmutex, resource;
	private int readcount;

	public ReadersPreferencePolicy() {
		this.rmutex = new Semaphore(1);
		this.resource = new Semaphore(1);
		this.readcount = 0;
	}

	@Override
	public void beforeReading() throws InterruptedException {
		rmutex.acquire();			//Ensure that no other reader can execute this entry section while you are in it
		readcount++;				//Indicate that you are a reader trying to enter the Critical Section
		if (readcount == 1)			//Checks if you are the first reader trying to enter CS
			resource.acquire();		//If you are the first reader, lock the resource from writers. Resource stays reserved for subsequent readers
		rmutex.release();			//Release
	}

	@Override
	public void afterReading() throws InterruptedException {
		rmutex.acquire();			//Ensure that no other reader can execute this exit section while you are in it
		readcount--;				//Indicate that you are no longer needing the shared resource. One less readers
		if (readcount == 0)			//Checks if you are the last (only) reader who is reading the shared file
			resource.release();		//If you are last reader, then you can unlock the resource. This makes it available to writers.
		rmutex.release();			//Release
	}

	@Override
	public void beforeWriting() throws InterruptedException {
		resource.acquire();			//Lock the shared file for a writer
	}

	@Override
	public void afterWriting() {
		resource.release();			//Release the shared file for use by others
	}
}

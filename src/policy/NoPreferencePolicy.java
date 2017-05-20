package policy;

import java.util.concurrent.Semaphore;

public class NoPreferencePolicy implements ReaderWriterPolicy {
	private int readCount = 0; // number of readers currently accessing resource

	private final Semaphore resourceAccess = new Semaphore(1, true);	// controls access (read/write) to the resource
	private final Semaphore readCountAccess = new Semaphore(1, true);	// for syncing changes to shared variable readCount
	private final Semaphore serviceQueue = new Semaphore(1, true);		// FAIRNESS: preserves ordering of requests (signaling must be FIFO)

	@Override
	public void beforeReading() throws InterruptedException {
		serviceQueue.acquire();           // wait in line to be serviced
		readCountAccess.acquire();        // request exclusive access to readCount
		if (readCount == 0)         	  // if there are no readers already reading:
			resourceAccess.acquire();     // request resource access for readers (writers blocked)
		readCount++;                	  // update count of active readers
		serviceQueue.release();           // let next in line be serviced
		readCountAccess.release();        // release access to readCount
	}

	@Override
	public void afterReading() throws InterruptedException {
		readCountAccess.acquire();        // request exclusive access to readCount
		readCount--;                	  // update count of active readers
		if (readCount == 0)         	  // if there are no readers left:
			resourceAccess.release();     // release resource access for all
		readCountAccess.release();        // release access to readCount
	}

	@Override
	public void beforeWriting() throws InterruptedException {
		serviceQueue.acquire();           // wait in line to be serviced
		resourceAccess.acquire();         // request exclusive access to resource
		serviceQueue.release();           // let next in line be serviced
	}

	@Override
	public void afterWriting() throws InterruptedException {
		resourceAccess.release();         // release resource access for next reader/writer
	}
}

package policy;

import java.util.concurrent.Semaphore;

/*
 * Reference: https://en.wikipedia.org/wiki/Readers%E2%80%93writers_problem
 */
public class WritersPreferencePolicy implements ReaderWriterPolicy {
	private int readcount, writecount;
	private final Semaphore rmutex, wmutex, readTry, resource;

	public WritersPreferencePolicy() {
		readcount = 0;
		writecount = 0;
		rmutex = new Semaphore(1, true);
		wmutex = new Semaphore(1, true);
		readTry= new Semaphore(1, true);
		resource = new Semaphore(1, true);
	}

	@Override
	public void beforeReading() throws InterruptedException {
		readTry.acquire();			//Indicate a reader is trying to enter
		rmutex.acquire();			//lock this entry section to avoid race condition with other readers
		readcount++;				//report yourself as a reader
		if (readcount == 1)			//checks if you are first reader
			resource.acquire();		//if you are first reader, lock  the resource
		rmutex.release();			//release this entry section for other readers
		readTry.release();			//indicate you are done trying to access the resource
	}

	@Override
	public void afterReading() throws InterruptedException {
		rmutex.acquire();			//reserve this exit section - avoids race condition with readers
		readcount--;				//indicate you're leaving
		if (readcount == 0)			//checks if you are last reader leaving
			resource.release();		//if last, you must release the locked resource
		rmutex.release();			//release this exit section for other readers
	}

	@Override
	public void beforeWriting() throws InterruptedException {
		wmutex.acquire();			//reserve this entry section for writers - avoids race conditions
		writecount++;				//report yourself as a writer entering
		if (writecount == 1)		//checks if you're first writer
			readTry.acquire();		//if you're first, then you must lock the readers out. Prevent them from trying to enter CS
		wmutex.release();			//release this entry section
		resource.acquire();			//reserve the resource for yourself - prevents other writers from simultaneously editing the shared resource
	}

	@Override
	public void afterWriting() throws InterruptedException {
		resource.release();			//release file
		wmutex.acquire();			//reserve this exit section
		writecount--;				//indicate you're leaving
		if (writecount == 0)		//checks if you're the last writer
			readTry.release();		//if you're last writer, you must unlock the readers. Allows them to try enter CS for reading
		wmutex.release();			//release this exit section
	}
}

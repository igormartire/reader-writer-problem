package resource;

import java.util.Scanner;
import java.io.*;

public class Resource {
	private final int id;
	private final File file;

	public Resource(int id) {
		this.id = id;
		this.file = new File("resource"+this.id+".txt");
	}

	public String read(int duration) throws InterruptedException, FileNotFoundException {
		this.log("Reading...");

		Thread.sleep(duration * 1000);

		String read;
		try (Scanner scanner = new Scanner(this.file)) {
			read = scanner.useDelimiter("\\Z").next();
		}

		this.log("Finished reading.");
		this.log("Contents: \n" + read);

		return read;
	}

	public void write(String content, int duration) throws InterruptedException, FileNotFoundException {
		this.log("Writing...");

		Thread.sleep(duration * 1000);

		try(PrintWriter out = new PrintWriter(new FileOutputStream(this.file, true))) {
			out.println(content);
		}

		this.log("Writing finished.");
	}

	private void log(String message) {
		System.out.println("\nThread ("+Thread.currentThread().getId()+") --> Resource ("+this.id+"): " + message);
	}
}

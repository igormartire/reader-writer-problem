package server;

import policy.*;
import policy.ReaderWriterPolicyFactory;
import policy.ReaderWriterPolicyType;
import remote.IOController;
import resource.ConcurrentResource;

import java.io.FileNotFoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements IOController {

	private final Map<Integer, ConcurrentResource> resources = new ConcurrentHashMap<>();
	private final ReaderWriterPolicyFactory readerWriterPolicyFactory;

	public Server(ReaderWriterPolicyFactory readerWriterPolicyFactory) {
		this.readerWriterPolicyFactory = readerWriterPolicyFactory;
	}

	public String read(int id, int duration) throws FileNotFoundException, InterruptedException {
		return resources.get(id).read(duration);
	}

	public void write(int id, String content, int duration) throws FileNotFoundException, InterruptedException {
		if (!this.resources.containsKey(id)) {
			this.resources.put(id, newConcurrentResource(id));
		}
		this.resources.get(id).write(content, duration);
	}

	private ConcurrentResource newConcurrentResource(int id) {
		ReaderWriterPolicy policy = this.readerWriterPolicyFactory.buildPolicy();
		return new ConcurrentResource(id, policy);
	}

	public static void main(String args[]) {
		try {
			ReaderWriterPolicyFactory readerWriterPolicyFactory = parseArguments(args);
			Server obj = new Server(readerWriterPolicyFactory);
			IOController stub = (IOController) UnicastRemoteObject.exportObject(obj, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("IOController", stub);
			System.err.println("Server ready with policy: " + readerWriterPolicyFactory.buildPolicy().getClass().getSimpleName());
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

	private static ReaderWriterPolicyFactory parseArguments(String args[]) {
		ReaderWriterPolicyFactory readerWriterPolicyFactory = null;
		if (args.length > 0) {
			switch(args[0]) {
				case "r":
					readerWriterPolicyFactory =
					  new ReaderWriterPolicyFactory(ReaderWriterPolicyType.ReadersPreferencePolicy);
					break;
				case "w":
					readerWriterPolicyFactory =
					  new ReaderWriterPolicyFactory(ReaderWriterPolicyType.WritersPreferencePolicy);
					break;
				case "n":
					readerWriterPolicyFactory =
					  new ReaderWriterPolicyFactory(ReaderWriterPolicyType.NoPreferencePolicy);
					break;
				default:
					throw new IllegalArgumentException("Invalid policy: " + args[0]);
			}
		}
		else {
			readerWriterPolicyFactory = new ReaderWriterPolicyFactory(ReaderWriterPolicyType.WritersPreferencePolicy);
		}
		return readerWriterPolicyFactory;
	}
}

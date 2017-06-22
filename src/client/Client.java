package client;

import remote.IOController;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {
        boolean isReader;
        int fileKey;
        String contentToWrite = "";
        int duration;
        if (args.length == 2) {
            isReader = true;
            fileKey = Integer.parseInt(args[0]);
            duration = Integer.parseInt(args[1]);
        }
        else if (args.length == 3) {
            isReader = false;
            fileKey = Integer.parseInt(args[0]);
            contentToWrite = args[1];
            duration = Integer.parseInt(args[2]);
        }
        else {
            throw new IllegalArgumentException("Invalid number of arguments. Use 2 or 3.");
        }
        try {
            Registry registry = LocateRegistry.getRegistry(System.getProperty("remoteHost"));
            IOController stub = (IOController) registry.lookup("IOController");
            long start, end=0;
            if (isReader) {
                String contentRead;
                for(int i = 0; i < 10; i++){
                    start = System.currentTimeMillis();
                    contentRead = stub.read(fileKey, duration);
                    end += System.currentTimeMillis() - start;
                }
                System.out.println("Average Elapsed Reading Time: "+ end/10000.0 + "s");
                System.out.println("Lines Read: "+duration);
            } else {
                for(int i = 0; i < 10; i++){
                    start = System.currentTimeMillis();                
                    stub.write(fileKey, contentToWrite, duration);
                    end += System.currentTimeMillis() - start;
                }
                System.out.println("Average Elapsed Writing Time: "+ end/10000.0 + "s");
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

package remote;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IOController extends Remote {
    String read(int fd, int duration) throws RemoteException, FileNotFoundException, InterruptedException;
    void write(int fd, String content, int duration) throws RemoteException, FileNotFoundException, InterruptedException;
}

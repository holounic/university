package info.kgeorgiy.ja.samsikova.bank.src;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteAccount extends Remote {
    String getId() throws RemoteException;
    long get() throws RemoteException;
    long add(long n) throws RemoteException;
}

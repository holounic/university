package info.kgeorgiy.ja.samsikova.bank.src;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteBank extends Remote {
    RemotePerson createPerson(String firstName, String lastName, String passport) throws RemoteException;
    RemotePerson getRemotePerson(String passport) throws RemoteException;
    LocalPerson getLocalPerson(String passport) throws RemoteException;
    RemoteAccount getRemoteAccount(String subId, RemotePersonImpl person) throws RemoteException;
    RemoteAccount createAccount(String subId, RemotePersonImpl person) throws RemoteException;
}

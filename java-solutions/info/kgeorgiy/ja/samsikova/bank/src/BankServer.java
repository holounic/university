package info.kgeorgiy.ja.samsikova.bank.src;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class BankServer {

    public static void runRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry(AppUtils.R_PORT).list();
        } catch (ConnectException e) {
            LocateRegistry.createRegistry(AppUtils.R_PORT);
        }
    }

    public static void main(String[] args) {
        try {
            runRegistry();
        } catch (RemoteException e) {
            System.out.println("Failed to create registry :(");
            return;
        }
        RemoteBank bank = new RemoteBankImpl(AppUtils.PORT);
        try {
            UnicastRemoteObject.exportObject(bank, AppUtils.PORT);
            Naming.rebind(AppUtils.NAMING, bank);
        } catch (RemoteException e) {
            System.out.println("Failed to export bank object :(");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.printf("Bad URL: %s%n", AppUtils.NAMING);
        }
    }
}

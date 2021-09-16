package info.kgeorgiy.ja.samsikova.bank.src;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class BankClient {
    private static RemoteBank bank;

    private static RemoteBank getBank() {
        try {
            return (RemoteBank) Naming.lookup(AppUtils.NAMING);
        } catch (final NotBoundException e) {
            System.out.println("The bank is not bound :(");
            return null;
        } catch (final MalformedURLException e) {
            System.out.println("Bad URL :(");
            return null;
        } catch (RemoteException e) {
            System.out.println("Failed to find remote bank");
            return null;
        }
    }

    private static void outputAccountInfo(RemoteAccount account) throws RemoteException {
        System.out.println("====================");
        System.out.printf("Money: %d", account.get());
    }

    private static void personWork(String firstName, String lastName, String passport, String subId, long money) throws RemoteException {
        RemotePerson person = bank.getRemotePerson(passport);
        if (person == null) {
            person = bank.createPerson(firstName, lastName, passport);
            System.out.printf("Creating person: %s %s, passport: %s", firstName, lastName, passport);
        } else {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                System.out.println("User is valid");
            } else {
                System.out.println("Bad user :(");
            }
        }
        RemoteAccount account = person.createAccount(subId);
        outputAccountInfo(account);
        System.out.printf("Adding %d to the account", money);
        account.add(money);
        outputAccountInfo(account);
    }

    public static void main(String[] args) {
        bank = getBank();
        if (bank == null) {
            return;
        }
        try {
            personWork(args[0], args[1], args[2], args[3], Long.parseLong(args[4]));
        } catch (RemoteException e) {
            System.out.println("Failed :(");
        }

    }
}

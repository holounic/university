package info.kgeorgiy.ja.samsikova.bank.src;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RemotePersonImpl extends Person implements RemotePerson {
    private final Set<String> subIds = ConcurrentHashMap.newKeySet();
    private final RemoteBank bank;

    public RemotePersonImpl(String firstName, String lastName, String passport, RemoteBank bank) {
        super(firstName, lastName, passport);
        this.bank = bank;
    }

    @Override
    public RemoteAccount getAccount(String subId) throws RemoteException {
        return bank.getRemoteAccount(subId, this);
    }

    @Override
    public RemoteAccount createAccount(String subId) throws RemoteException {
        return bank.createAccount(subId, this);
    }

    public Set<String> getSubIds() {
        return subIds;
    }
}

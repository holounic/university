package info.kgeorgiy.ja.samsikova.bank.src;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalPerson extends Person {
    private final Map<String, LocalAccount> accounts = new ConcurrentHashMap<>();

    public LocalPerson(RemotePerson person, Map<String, RemoteAccount> accounts) throws RemoteException {
        super(person.getFirstName(), person.getLastName(), person.getPassport());
        for (Map.Entry<String, RemoteAccount> entry : accounts.entrySet()) {
            String key = entry.getKey();
            RemoteAccount value = entry.getValue();
            this.accounts.put(key, new LocalAccount(value));
        }
    }

    public LocalAccount getAccount(String subId) {
        return accounts.get(subId);
    }

    public LocalAccount createAccount(String subId) {
        return accounts.computeIfAbsent(subId, x -> new LocalAccount(getFullId(subId)));
    }
}
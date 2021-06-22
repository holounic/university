package info.kgeorgiy.ja.samsikova.bank.src;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RemoteBankImpl implements RemoteBank {
    private final int port;
    private final Map<String, RemoteAccount> accounts = new ConcurrentHashMap<>();
    private final Map<String, RemotePerson> clients = new ConcurrentHashMap<>();

    public RemoteBankImpl(int port) {
        this.port = port;
    }

    public RemoteBankImpl() {
        this(8088);
    }

    @Override
    public RemotePerson createPerson(String firstName, String lastName, String passport) throws RemoteException {
        return RemoteUtils.create(port, clients, passport,
                x -> new RemotePersonImpl(firstName, lastName, passport, this));
    }

    @Override
    public RemotePerson getRemotePerson(String passport) throws RemoteException {
        return clients.get(passport);
    }

    @Override
    public RemoteAccount getRemoteAccount(String subId, RemotePersonImpl person) {
        return accounts.get(person.getFullId(subId));
    }

    @Override
    public RemoteAccount createAccount(String subId, RemotePersonImpl person) throws RemoteException {
        return RemoteUtils.create(port, accounts, person.getFullId(subId), RemoteAccountImpl::new);
    }

    public Map<String, RemoteAccount> getAccounts(RemotePersonImpl person) {
        return person.getSubIds().stream()
                .collect(Collectors.toConcurrentMap(x -> x, x -> accounts.get(person.getFullId(x))));
    }

    @Override
    public LocalPerson getLocalPerson(String passport) throws RemoteException {
        RemotePersonImpl person = (RemotePersonImpl) getRemotePerson(passport);
        if (person == null) {
            return null;
        }
        return new LocalPerson(person, getAccounts(person));
    }
}

package info.kgeorgiy.ja.samsikova.bank.src;

import java.io.Serializable;
import java.rmi.RemoteException;

public class LocalAccount extends Account implements Serializable {
    public LocalAccount(RemoteAccount remoteAccount) throws RemoteException {
        super(remoteAccount.getId(), remoteAccount.get());
    }
    public LocalAccount(String id) {
        super(id);
    }
}

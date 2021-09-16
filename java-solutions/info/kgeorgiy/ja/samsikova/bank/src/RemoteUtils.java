package info.kgeorgiy.ja.samsikova.bank.src;

import java.io.UncheckedIOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.function.Function;

public class RemoteUtils {

    private RemoteUtils() {}

    private interface RCallable<R> extends Remote {
        R call() throws RemoteException;
    }

    public static  <R extends Remote> R export(int p, R o) {
        try {
            UnicastRemoteObject.exportObject(o, p);
            return o;
        } catch (RemoteException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <R> R perform(RCallable<R> task) throws RemoteException {
        try {
            return task.call();
        } catch (UncheckedIOException e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public static  <R extends Remote> R create(int p, Map<String, R> m, String key, Function<String, R> f)
            throws RemoteException {
        return perform(() -> m.computeIfAbsent(key, k -> export(p, f.apply(k))));
    }
}

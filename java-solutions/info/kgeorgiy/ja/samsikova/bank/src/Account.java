package info.kgeorgiy.ja.samsikova.bank.src;

import java.util.concurrent.atomic.AtomicLong;

public abstract class Account {
    protected final String id;
    protected final AtomicLong balance;

    protected Account(String id, long balance) {
        this.id = id;
        this.balance = new AtomicLong(balance);
    }

    public Account(String id) {
        this(id, 0);
    }

    public String getId() {
        return id;
    }

    public long get() {
        return balance.get();
    }

    public long add(long n) {
        long available = Long.MAX_VALUE - this.get();
        long toAdd = n;
        long toReturn = 0;
        if (available < n) {
            toAdd = n - available;
            toReturn = n - toAdd;
        } else if (this.get() + n < 0) {
            toAdd = this.get();
            toReturn = n - this.get();
        }
        balance.addAndGet(toAdd);
        return toReturn;
    }
}

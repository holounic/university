package info.kgeorgiy.ja.samsikova.bank.test;

import info.kgeorgiy.ja.samsikova.bank.src.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class BankTest {

    RemoteBank bank;

    @BeforeEach
    void init() throws RemoteException, NotBoundException, MalformedURLException {
        BankServer.main(null);
        bank = (RemoteBank) Naming.lookup(AppUtils.NAMING);
    }

    static final String FIRST_NAME = "FIRST NAME";
    static final String LAST_NAME = "LAST NAME";
    static final String PASSPORT = "11111";

    private void addPerson(String firstName, String lastName, String passport) throws RemoteException {
        bank.createPerson(firstName, lastName, passport);
    }

    private void createAccount(RemotePerson person, String subId) throws RemoteException {
        person.createAccount(subId);
    }

    private RemoteAccount createPersonGetAccount(String firstName, String lastName, String passport, String subId)
            throws RemoteException {
        addPerson(firstName, lastName, passport);
        RemotePerson person = bank.getRemotePerson(passport);
        return  person.createAccount(subId);
    }

    @Test
    public void testCreatePerson() throws RemoteException {
        addPerson(FIRST_NAME, LAST_NAME, PASSPORT);
        Person person = bank.getLocalPerson(PASSPORT);
        Assert.assertNotNull(person);
        Assert.assertEquals(FIRST_NAME, person.getFirstName());
        Assert.assertEquals(LAST_NAME, person.getLastName());
        Assert.assertEquals(PASSPORT, person.getPassport());
    }

    private static final String SUB_ID = "100";

    @Test
    public void testLocallyCreatedAccountExists() throws RemoteException {
        addPerson(FIRST_NAME, LAST_NAME, PASSPORT);
        LocalPerson person = bank.getLocalPerson(PASSPORT);
        person.createAccount(SUB_ID);
        Assert.assertNotNull(person.getAccount(SUB_ID));
    }

    @Test
    public void testRemotelyCreatedAccountExists() throws RemoteException {
        addPerson(FIRST_NAME, LAST_NAME, PASSPORT);
        RemotePersonImpl person = new RemotePersonImpl(FIRST_NAME, LAST_NAME, PASSPORT, bank);
        bank.createAccount(SUB_ID, person);
        Assert.assertNotNull(bank.getRemoteAccount(SUB_ID, person));
    }

    private static long ADD = 100;
    private static long GET = -100;
    private LocalAccount createAndGetAccount() throws RemoteException {
        addPerson(FIRST_NAME, LAST_NAME, PASSPORT);
        LocalPerson person = bank.getLocalPerson(PASSPORT);
        person.createAccount(SUB_ID);
        return person.getAccount(SUB_ID);
    }

    @Test
    public void testLocallyCreatedAccountBalanceChange() throws RemoteException {
        LocalAccount account = createAndGetAccount();
        long prevBalance = account.get();
        long n = account.add(ADD);
        long newBalance = account.get();
        Assert.assertEquals(prevBalance + ADD, newBalance);
        Assert.assertEquals(0, n);
    }

    @Test
    public void testLocallyCreatedAccountBalanceDecrease() throws RemoteException {
        LocalAccount account = createAndGetAccount();
        long prevBalance = account.get();
        long n = account.add(GET);
        long newBalance = account.get();
        Assert.assertEquals(GET, n);
        Assert.assertEquals(prevBalance, newBalance);
    }

    private static int NUM_ACCOUNTS = 10;

    @Test
    public void testRemotelyCreatedAccounts() throws RemoteException {
        addPerson(FIRST_NAME, LAST_NAME, PASSPORT);
        RemotePersonImpl person = new RemotePersonImpl(FIRST_NAME, LAST_NAME, PASSPORT, bank);
        IntStream.of(NUM_ACCOUNTS).forEach(x -> {
            try {
                bank.createAccount(Integer.toString(x), person);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        IntStream.of(NUM_ACCOUNTS).forEach(x -> {
            try {
                Assert.assertNotNull(bank.getRemoteAccount(Integer.toString(x), person));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testValidFullId() throws RemoteException {
        addPerson(FIRST_NAME, LAST_NAME, PASSPORT);
        LocalPerson person = bank.getLocalPerson(PASSPORT);
        String fullId = person.getFullId(SUB_ID);
        Assert.assertEquals(PASSPORT + ":" + SUB_ID, fullId);
    }

    private Runnable addMoneyTask(RemoteAccount account, long money, CountDownLatch latch, AtomicLong added) {
        return () -> {
            try {
                account.add(money);
                added.addAndGet(money);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        };
    }

    private static final int THREADS = 10;
    private static final int TASKS = 15;
    @Test
    public void testChangeBalanceConcurrent() throws RemoteException, InterruptedException {
        RemoteAccount account = createPersonGetAccount(FIRST_NAME, LAST_NAME, PASSPORT, SUB_ID);
        long balance = account.get();
        AtomicLong added = new AtomicLong();
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        CountDownLatch count = new CountDownLatch(TASKS);
        IntStream.range(1, TASKS + 1)
                .mapToObj(x -> addMoneyTask(account, x, count, added))
                .forEach(executorService::submit);
        count.await();
        Assert.assertEquals(balance + added.get(), account.get());
    }
}

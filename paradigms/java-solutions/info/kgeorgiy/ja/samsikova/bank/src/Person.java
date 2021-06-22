package info.kgeorgiy.ja.samsikova.bank.src;

import java.io.Serializable;

public abstract class Person implements Serializable {
    protected final String firstName;
    protected final String lastName;
    protected final String passport;

    public Person(String firstName, String lastName, String passport) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passport = passport;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassport() {
        return passport;
    }

    public String getFullId(String subId) {
        return String.format("%s:%s", getPassport(), subId);
    }

}

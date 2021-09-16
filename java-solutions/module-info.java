module info.kgeorgiy.ja.samsikova {
    requires info.kgeorgiy.java.advanced.walk;
    requires info.kgeorgiy.java.advanced.arrayset;
    requires info.kgeorgiy.java.advanced.student;
    requires info.kgeorgiy.java.advanced.implementor;
    requires info.kgeorgiy.java.advanced.concurrent;
    requires info.kgeorgiy.java.advanced.mapper;
    requires info.kgeorgiy.java.advanced.hello;
    requires java.compiler;
    requires java.rmi;
    requires org.junit.jupiter;
    requires org.junit.platform.launcher;

    exports  info.kgeorgiy.ja.samsikova.walk;
    exports  info.kgeorgiy.ja.samsikova.arrayset;
    exports  info.kgeorgiy.ja.samsikova.student;
    exports  info.kgeorgiy.ja.samsikova.implementor;
    exports  info.kgeorgiy.ja.samsikova.concurrent;
    exports  info.kgeorgiy.ja.samsikova.hello;
    exports  info.kgeorgiy.ja.samsikova.bank.src;
    exports  info.kgeorgiy.ja.samsikova.bank.test;
    opens info.kgeorgiy.ja.samsikova.bank.test to org.junit.jupiter, org.junit.platform.commons;
}

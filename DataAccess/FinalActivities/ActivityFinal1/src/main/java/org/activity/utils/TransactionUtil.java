package org.activity.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Function;

public final class TransactionUtil {

    private TransactionUtil() {}

    public static <T> T inTransaction(Function<Session, T> work) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction t = session.beginTransaction();
            try {
                T result = work.apply(session);
                t.commit();
                return result;
            } catch (RuntimeException e) {
                t.rollback();
                throw e;
            }
        }
    }

    public static void inTransactionVoid(java.util.function.Consumer<Session> work) {
        inTransaction(session -> {
            work.accept(session);
            return null;
        });
    }
}
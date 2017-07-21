package db_services;

import db_services.entitys.AdvcashTransaction;
import db_services.entitys.User;

import javax.persistence.*;

public class DbService {
    private static DbService db_service;
    private EntityManager em;

    private DbService() {
        this.em = Persistence.createEntityManagerFactory("eclipsMysql").createEntityManager();
    }

    public static synchronized DbService getInstance(){
        if (db_service==null)
            db_service = new DbService();
        return db_service;
    }

    public synchronized User getUserFromDb(long userId){
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        User user = em.find(User.class,userId);
        em.refresh(user);
        tr.commit();
        return user;
    }

    public synchronized void addAdvcashTransaction(long userId, AdvcashTransaction transaction){
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        User user = getUserFromDb(userId);
        user.getAdvcashTransactions().add(transaction);
        em.persist(user);
        tr.commit();
    }

}

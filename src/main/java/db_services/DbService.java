package db_services;

import entitys.AdvcashTransaction;
import entitys.User;

import javax.persistence.*;

public class DbService {
    private static DbService db_service;
    private EntityManagerFactory managerFactory;

    private DbService() {
        this.managerFactory = Persistence.createEntityManagerFactory("eclipsMysql");
    }

    public static synchronized DbService getInstance(){
        if (db_service==null)
            db_service = new DbService();
        return db_service;
    }

    public synchronized void addUserInDb(User user){
        EntityManager em = managerFactory.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        em.persist(user);
        tr.commit();
        em.close();
    }

    public synchronized User getUserFromDb(long userId){
        EntityManager em = managerFactory.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        User user = em.find(User.class,userId);
        em.refresh(user);
        tr.commit();
        em.close();
        return user;
    }

    public synchronized void addAcTransaction(long userId, AdvcashTransaction transaction){
        EntityManager em = managerFactory.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        User user = em.find(User.class,userId);
        user.addAcTransaction(transaction);
        tr.commit();
        em.close();
    }

    public synchronized boolean checkUser(long id){
        boolean check = false;
        User user = getUserFromDb(id);
        if (user!=null)
            check=true;
        return check;
    }

}

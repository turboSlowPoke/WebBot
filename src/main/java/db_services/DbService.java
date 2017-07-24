package db_services;

import calculates.CalculateOfPayment;
import configs.TypeOfPurchase;
import entitys.AdvcashTransaction;
import entitys.LocalTransaction;
import entitys.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    public synchronized void transactionHandler(long userId, AdvcashTransaction acTransaction, String typeOfParchase){
        EntityManager em = managerFactory.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        TypedQuery<User> queryParentUser = em.createNamedQuery("User.getParent",User.class);
        tr.begin();
        User paidUser = em.find(User.class,userId);
        int paidUserLevel = paidUser.getLevel();
        int lk = paidUser.getLeftKey();
        int rk = paidUser.getRightKey();
        queryParentUser.setParameter("l",paidUserLevel);
        queryParentUser.setParameter("lk",lk);
        queryParentUser.setParameter("rk",rk);

        //вычисляем выплаты
        BigDecimal paymentForFirstLine = CalculateOfPayment.calcForFirstLine(acTransaction.getAc_amount());
        System.out.println("для первой линии "+paymentForFirstLine);
        BigDecimal paymentForSecondLine = CalculateOfPayment.calcForSecondLine(acTransaction.getAc_amount());
        System.out.println("для второй линии "+paymentForSecondLine);
        BigDecimal paymentForThirdLine = CalculateOfPayment.calcForThirdLine(acTransaction.getAc_amount());
        System.out.println("для третьей линии" + paymentForThirdLine);
        //создаем транзакции для истрии выплат
        LocalTransaction localTransaction1 = new LocalTransaction(LocalDateTime.now(), paymentForFirstLine, paidUser);
        LocalTransaction localTransaction2 = new LocalTransaction(LocalDateTime.now(), paymentForSecondLine, paidUser);
        LocalTransaction localTransaction3 = new LocalTransaction(LocalDateTime.now(), paymentForThirdLine, paidUser);
        //находим пригластителей, добавляем тразакции и пополняем кошелёк
        List<User> parentUsers = queryParentUser.getResultList();
        System.out.println("из базы получено "+ parentUsers.size());
        if (parentUsers!=null&&parentUsers.size()>0) {
            for (User u : parentUsers) {
                System.out.println(u);
                int parentLevel = u.getLevel();
                switch (paidUserLevel - parentLevel) {
                    case 1:
                        u.addLocalTransactions(localTransaction1);
                        u.setLocalWallet(u.getLocalWallet().add(paymentForFirstLine));
                        break;
                    case 2:
                        u.addLocalTransactions(localTransaction2);
                        u.setLocalWallet(u.getLocalWallet().add(paymentForSecondLine));
                        break;
                    case 3:
                        u.addLocalTransactions(localTransaction3);
                        u.setLocalWallet(u.getLocalWallet().add(paymentForThirdLine));
                        break;
                    default:
                        System.out.println("ERROR: Dbservice transactionHandler проценты не начислены, не правильный уровень родителя:\n" + u);
                }
            }
            System.out.println("проценты начислены");
        } else {
            System.out.println("пригласители не найдены");
        }

        paidUser.addAcTransaction(acTransaction);
        System.out.println("транзакция добавлена");
        //продляем подписку
        if (paidUser.getEndDate()==null)
            paidUser.setEndDate(LocalDateTime.now());
        if (typeOfParchase.equals(TypeOfPurchase.ONE_MONTH))
            paidUser.setEndDate(paidUser.getEndDate().plusMonths(1));
        else if (typeOfParchase.equals(TypeOfPurchase.TWO_MONTH))
            paidUser.setEndDate(paidUser.getEndDate().plusMonths(2));
        else if (typeOfParchase.equals(TypeOfPurchase.THREE_MONTH))
            paidUser.setEndDate(paidUser.getEndDate().plusMonths(3));
        else
            System.out.println("!!! EROR подписка не продлена для userid"+userId);

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

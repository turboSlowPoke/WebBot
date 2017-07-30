package db_services;

import calculates.CalculateOfPayment;
import configs.TypeOfPurchase;
import entitys.AdvcashTransaction;
import entitys.LocalTransaction;
import entitys.User;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DbService {
    private static final Logger log = Logger.getLogger(DbService.class);
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

    public synchronized User getUserFromDb(long userId) throws NoUserInDbException {
        EntityManager em = managerFactory.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        User user = em.find(User.class,userId);
        em.refresh(user);
        tr.commit();
        em.close();
        if (user==null)
            throw new NoUserInDbException(userId);
        return  user;
    }

    public synchronized void transactionHandler(long userId, AdvcashTransaction acTransaction, String typeOfParchase){
        EntityManager em = managerFactory.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        TypedQuery<User> queryParentUser = em.createNamedQuery("User.getParent",User.class);

        tr.begin();

        User paidUser = em.find(User.class,userId);
        em.refresh(paidUser);
        int paidUserLevel = paidUser.getLevel();
        int lk = paidUser.getLeftKey();
        int rk = paidUser.getRightKey();
        queryParentUser.setParameter("l",paidUserLevel);
        queryParentUser.setParameter("lk",lk);
        queryParentUser.setParameter("rk",rk);
        //вычисляем размеры выплат для 3 линий
        BigDecimal paymentForFirstLine = CalculateOfPayment.calcForFirstLine(acTransaction.getAc_amount());
        BigDecimal paymentForSecondLine = CalculateOfPayment.calcForSecondLine(acTransaction.getAc_amount());
        BigDecimal paymentForThirdLine = CalculateOfPayment.calcForThirdLine(acTransaction.getAc_amount());
        //создаем транзакции для истрии выплат
        LocalTransaction localTransaction1 = new LocalTransaction(LocalDateTime.now(), paymentForFirstLine, paidUser);
        LocalTransaction localTransaction2 = new LocalTransaction(LocalDateTime.now(), paymentForSecondLine, paidUser);
        LocalTransaction localTransaction3 = new LocalTransaction(LocalDateTime.now(), paymentForThirdLine, paidUser);
        //находим пригластителей, если они еть то добавляем тразакции и пополняем кошелёк
        List<User> parentUsers = queryParentUser.getResultList();
        if (parentUsers!=null&&parentUsers.size()>0) {
            for (User u : parentUsers) {
                if (u.getServices().getEndDateOfSubscription().toLocalDate().isAfter(LocalDate.now())
                        ||u.getServices().getUnlimit()) {
                    int parentLevel = u.getLevel();
                    switch (paidUserLevel - parentLevel) {
                        case 1:
                                log.info("записываем локальную транзакцию");
                                u.addLocalTransactions(localTransaction1);
                                log.info("увеличиваем кошелек");
                                u.getPersonalData().setLocalWallet(u.getPersonalData().getLocalWallet().add(paymentForFirstLine));
                                //u.setLocalWallet(u.getLocalWallet().add(paymentForFirstLine));
                                //log.info("Проценты начислены сумма="+paymentForFirstLine+" для пользователя " +u+" итого в кошельке "+u.getPersonalData().getLocalWallet());
                                if (!u.getPersonalData().getReferalsForPrize().contains(paidUser.getUserID())){
                                    u.getPersonalData().addReferalForPrize(paidUser.getUserID());
                                    log.info("пользователю "+u.getPersonalData().getFirstName()+"добавлен реферал для премии");
                                }
                            break;
                        case 2:
                                u.addLocalTransactions(localTransaction2);
                                u.setLocalWallet(u.getLocalWallet().add(paymentForSecondLine));
                                log.info("Проценты начислены сумма="+paymentForSecondLine+" для пользователя " +u+" итого в кошельке "+u.getPersonalData().getLocalWallet());
                            break;
                        case 3:
                                u.addLocalTransactions(localTransaction3);
                                u.setLocalWallet(u.getLocalWallet().add(paymentForThirdLine));
                                log.info("Проценты начислены сумма="+paymentForThirdLine+" для пользователя " +u+" итого в кошельке "+u.getPersonalData().getLocalWallet());
                            break;
                        default:
                            log.error("проценты не начислены, не правильный уровень родителя:\n" + u);
                    }
                }
            }

        } else {
            log.info("пригласители для userId="+userId+" не найдены");
        }
        paidUser.addAcTransaction(acTransaction);
        if (paidUser.getPersonalData().getAdvcashWallet()==null)
            paidUser.setAdvcashWallet(acTransaction.getAc_src_wallet());
        log.info("транзакция добавлена для userId="+userId);
        //продляем подписку
        if (paidUser.getEndDateOfSubscription()==null)
            paidUser.setEndDateOfSubscription(LocalDateTime.now());
        if (typeOfParchase.equals(TypeOfPurchase.ONE_MONTH))
            paidUser.setEndDateOfSubscription(paidUser.getEndDateOfSubscription().plusMonths(1));
        else if (typeOfParchase.equals(TypeOfPurchase.TWO_MONTH))
            paidUser.setEndDateOfSubscription(paidUser.getEndDateOfSubscription().plusMonths(2));
        else if (typeOfParchase.equals(TypeOfPurchase.THREE_MONTH))
            paidUser.setEndDateOfSubscription(paidUser.getEndDateOfSubscription().plusMonths(3));
        else if (typeOfParchase.equals(TypeOfPurchase.PRIVATE_CHAT))
            paidUser.getServices().setOnetimeConsultation(true);
        else if (typeOfParchase.equals(TypeOfPurchase.UNLIMIT))
            paidUser.getServices().setUnlimit(true);
        else 
            log.error("!!!подписка не продлена для userid "+userId);

        tr.commit();
        em.close();
    }

    public synchronized boolean hasUser(long id) throws NoUserInDbException {
        boolean check = false;
        User user = getUserFromDb(id);
        if (user!=null)
            check=true;
        return check;
    }

}

package entitys;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    private  long userID;
    private long chatID;
    private int level;
    private int rightKey;
    private int leftKey;

    private String userName;
    private String firstName;
    private String LastName;
    private int phoneNumber;
    private String email;

    private String typeUser = "customer";

    private LocalDate endDate;
    private float localWallet;
    private String advcashWallet;

    @OneToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private List<AdvcashTransaction> advcashTransactions;
    //@OneToMany(cascade = CascadeType.PERSIST)
    //private List<LocalTransaction> localTransactions;



    User() {}

    public User(long userID) {
        this.userID = userID;
    }

    public long getUserID() {
        return userID;
    }

    public long getChatID() {
        return chatID;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRightKey() {
        return rightKey;
    }

    public void setRightKey(int rightKey) {
        this.rightKey = rightKey;
    }

    public int getLeftKey() {
        return leftKey;
    }

    public void setLeftKey(int leftKey) {
        this.leftKey = leftKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public float getLocalWallet() {
        return localWallet;
    }

    public void setLocalWallet(float localWallet) {
        this.localWallet = localWallet;
    }

    public String getAdvcashWallet() {
        return advcashWallet;
    }

    public void setAdvcashWallet(String advcashWallet) {
        this.advcashWallet = advcashWallet;
    }

    public List<AdvcashTransaction> getAdvcashTransactions() {
        return advcashTransactions;
    }

    public void addAcTransaction(AdvcashTransaction transaction){
        if (advcashTransactions==null)
            advcashTransactions=new ArrayList<>();
        advcashTransactions.add(transaction);
    }

    @Override
    public String toString() {
        return "Имя: "+getFirstName()
                +"| Фамилия: "+getLastName()
                +"| UserName: "+getUserName()
                +"| UserID: "+getUserID()
                +"| Тип: "+getTypeUser()
                +"| конец подписки: "+getEndDate();
    }
}

package db_services.entitys;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
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

    @OneToMany(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<AdvcashTransaction> advcashTransactions;
    //@OneToMany(cascade = CascadeType.PERSIST)
    //private List<LocalTransaction> localTransactions;



    User() {}

    public User(long userID, String userName, String firstName, String lastName,long chatID) {
        this.userID = userID;
        this.userName = userName;
        this.firstName = firstName;
        this.LastName = lastName;
        this.chatID = chatID;
    }

    public User(long userID) {
        this.userID = userID;
    }

    public long getChatID() {
        return chatID;
    }

    public void setChatID(long chatID) {
        this.chatID = chatID;
    }

    public long getUserID() {
        return userID;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
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

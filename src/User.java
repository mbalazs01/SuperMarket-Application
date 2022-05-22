public class User {
    public User(String firstname, String lastname, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.balance = 0;
    }

    public User(String firstname, String lastname, String username, int balance) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.balance = balance;
    }
    private String firstname;
    private String lastname;
    private String username;
    private int balance;

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public int getBalance() {
        return balance;
    }
}

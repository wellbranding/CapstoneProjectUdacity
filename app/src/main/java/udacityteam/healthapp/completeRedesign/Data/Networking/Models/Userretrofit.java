package udacityteam.healthapp.completeRedesign.Data.Networking.Models;


public class Userretrofit {

    private int id;
    private String name;
    private String email;
    private String uid;

    public String getUid() {
        return uid;
    }

    public Userretrofit(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}

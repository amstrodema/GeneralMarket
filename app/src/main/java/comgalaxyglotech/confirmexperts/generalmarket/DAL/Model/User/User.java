package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.User;


public class User {
    private String id;
    private String name;
    private String describe;
    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public User(String id, String name, String Describe) {
        this.id = id;
        this.name = name;
        this.describe = Describe;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}

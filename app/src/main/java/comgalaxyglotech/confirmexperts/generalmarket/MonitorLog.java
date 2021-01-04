package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 08/13/2020.
 */

public class MonitorLog {
    private String id,title, description, dateTime, priority, fromUser, toUser, isAcct;
    private double amount;

    public MonitorLog() {
    }

    public MonitorLog(String id, String title, String description, String dateTime, String priority, String fromUser, String toUser, String isAcct, double amount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.priority = priority;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.isAcct = isAcct;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getIsAcct() {
        return isAcct;
    }

    public void setIsAcct(String isAcct) {
        this.isAcct = isAcct;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

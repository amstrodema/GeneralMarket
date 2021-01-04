package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 09/23/2019.
 */

public class FarmDisplayModel {
    float ratings;
    String name,status,closeTime,deliveryMode,stockAvailable,id;
    private String desc,loc;
private  String gps;
private boolean ban;
private String creator;

    public FarmDisplayModel(float ratings, String name, String status, String closeTime, String deliveryMode, String stockAvailable, String id, String desc, String loc, String gps, boolean ban, String creator) {
        this.ratings = ratings;
        this.name = name;
        this.status = status;
        this.closeTime = closeTime;
        this.deliveryMode = deliveryMode;
        this.stockAvailable = stockAvailable;
        this.id = id;
        this.desc = desc;
        this.loc = loc;
        this.gps = gps;
        this.ban = ban;
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public float getRatings() {
        return ratings;
    }
    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(String stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

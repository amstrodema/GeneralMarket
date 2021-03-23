package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Store;

/**
 * Created by ELECTRON on 03/19/2019.
 */

public class StoreDisplayModel {
    private String id,storeName,tradeDays,totalItemz,delivery;
    private float ratin;
    private String desc,loc;
    private String gps;
    private boolean ban;
    private String creatorId;

    public StoreDisplayModel(String id, String storeName, String tradeDays, String totalItemz, String delivery, float ratin, String desc, String loc, String gps, boolean ban, String creatorId) {
        this.id = id;
        this.storeName = storeName;
        this.tradeDays = tradeDays;
        this.totalItemz = totalItemz;
        this.delivery = delivery;
        this.ratin = ratin;
        this.desc = desc;
        this.loc = loc;
        this.gps = gps;
        this.ban = ban;
        this.creatorId = creatorId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getTradeDays() {
        return tradeDays;
    }

    public void setTradeDays(String tradeDays) {
        this.tradeDays = tradeDays;
    }

    public float getRatin() {
        return ratin;
    }

    public void setRatin(float ratin) {
        this.ratin = ratin;
    }

    public String getTotalItemz() {
        return totalItemz;
    }

    public void setTotalItemz(String totalItemz) {
        this.totalItemz = totalItemz;
    }
}
package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Farm;

/**
 * Created by ELECTRON on 09/23/2019.
 */

public class FarmMainModel {
    float ratings;
    String id,farmName,creatorId,farmDesc,farmLocation,farmPhone,farmOpeningDays,farmOpeningTime,farmClosingTime,delivery;
    private double longitude;
    private double latitude;
    private boolean ban;
    public FarmMainModel() {
    }

    public FarmMainModel(float ratings, String id, String farmName, String creatorId, String farmDesc, String farmLocation, String farmPhone, String farmOpeningDays, String farmOpeningTime, String farmClosingTime, String delivery, double longitude, double latitude, boolean ban) {
        this.ratings = ratings;
        this.id = id;
        this.farmName = farmName;
        this.creatorId = creatorId;
        this.farmDesc = farmDesc;
        this.farmLocation = farmLocation;
        this.farmPhone = farmPhone;
        this.farmOpeningDays = farmOpeningDays;
        this.farmOpeningTime = farmOpeningTime;
        this.farmClosingTime = farmClosingTime;
        this.delivery = delivery;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ban = ban;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getFarmDesc() {
        return farmDesc;
    }

    public void setFarmDesc(String farmDesc) {
        this.farmDesc = farmDesc;
    }

    public String getFarmLocation() {
        return farmLocation;
    }

    public void setFarmLocation(String farmLocation) {
        this.farmLocation = farmLocation;
    }

    public String getFarmPhone() {
        return farmPhone;
    }

    public void setFarmPhone(String farmPhone) {
        this.farmPhone = farmPhone;
    }

    public String getFarmOpeningDays() {
        return farmOpeningDays;
    }

    public void setFarmOpeningDays(String farmOpeningDays) {
        this.farmOpeningDays = farmOpeningDays;
    }

    public String getFarmOpeningTime() {
        return farmOpeningTime;
    }

    public void setFarmOpeningTime(String farmOpeningTime) {
        this.farmOpeningTime = farmOpeningTime;
    }

    public String getFarmClosingTime() {
        return farmClosingTime;
    }

    public void setFarmClosingTime(String farmClosingTime) {
        this.farmClosingTime = farmClosingTime;
    }
}


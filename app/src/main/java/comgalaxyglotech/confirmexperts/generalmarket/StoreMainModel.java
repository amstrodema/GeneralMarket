package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 03/19/2019.
 */

public class StoreMainModel {
    private String id,creatorId,storeName,storeDesc,storeLocation,storePhone,storeOpeningDays,storeOpeningTime,storeClosingTime,marketId, delievery;
    private Float rating;
    private double longitude;
    private double latitude;
    private boolean ban;
    private String regDate, lastPaymentDate,rentDuration;
    public StoreMainModel() {
    }

    public StoreMainModel(String id, String creatorId, String storeName, String storeDesc, String storeLocation, String storePhone, String storeOpeningDays, String storeOpeningTime, String storeClosingTime, String marketId, String delievery, Float rating, double longitude, double latitude, boolean ban, String regDate, String lastPaymentDate, String rentDuration) {
        this.id = id;
        this.creatorId = creatorId;
        this.storeName = storeName;
        this.storeDesc = storeDesc;
        this.storeLocation = storeLocation;
        this.storePhone = storePhone;
        this.storeOpeningDays = storeOpeningDays;
        this.storeOpeningTime = storeOpeningTime;
        this.storeClosingTime = storeClosingTime;
        this.marketId = marketId;
        this.delievery = delievery;
        this.rating = rating;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ban = ban;
        this.regDate = regDate;
        this.lastPaymentDate = lastPaymentDate;
        this.rentDuration = rentDuration;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(String lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getRentDuration() {
        return rentDuration;
    }

    public void setRentDuration(String rentDuration) {
        this.rentDuration = rentDuration;
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

    public String getDelievery() {
        return delievery;
    }

    public void setDelievery(String delievery) {
        this.delievery = delievery;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDesc() {
        return storeDesc;
    }

    public void setStoreDesc(String storeDesc) {
        this.storeDesc = storeDesc;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreOpeningDays() {
        return storeOpeningDays;
    }

    public void setStoreOpeningDays(String storeOpeningDays) {
        this.storeOpeningDays = storeOpeningDays;
    }

    public String getStoreOpeningTime() {
        return storeOpeningTime;
    }

    public void setStoreOpeningTime(String storeOpeningTime) {
        this.storeOpeningTime = storeOpeningTime;
    }

    public String getStoreClosingTime() {
        return storeClosingTime;
    }

    public void setStoreClosingTime(String storeClosingTime) {
        this.storeClosingTime = storeClosingTime;
    }
}

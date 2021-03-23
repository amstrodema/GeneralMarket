package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Market;

/**
 * Created by ELECTRON on 02/22/2019.
 */

public class newMarketModel {
    private String marketName;
    private String marketDescription;
    private String Location;
    private String marketTradeFreq;
    private String marketLastTradingDay;
    private String id;
    private String creatorId;
    private Float rating;
    private double longitude;
    private double latitude;
    public newMarketModel(){

    }

    public newMarketModel(String marketName, String marketDescription, String location, String marketTradeFreq, String marketLastTradingDay, String id, String creatorId, Float rating, double longitude, double latitude) {
        this.marketName = marketName;
        this.marketDescription = marketDescription;
        Location = location;
        this.marketTradeFreq = marketTradeFreq;
        this.marketLastTradingDay = marketLastTradingDay;
        this.id = id;
        this.creatorId = creatorId;
        this.rating = rating;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
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

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketDescription() {
        return marketDescription;
    }

    public void setMarketDescription(String marketDescription) {
        this.marketDescription = marketDescription;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getMarketTradeFreq() {
        return marketTradeFreq;
    }

    public void setMarketTradeFreq(String marketTradeFreq) {
        this.marketTradeFreq = marketTradeFreq;
    }

    public String getMarketLastTradingDay() {
        return marketLastTradingDay;
    }

    public void setMarketLastTradingDay(String marketLastTradingDay) {
        this.marketLastTradingDay = marketLastTradingDay;
    }
}

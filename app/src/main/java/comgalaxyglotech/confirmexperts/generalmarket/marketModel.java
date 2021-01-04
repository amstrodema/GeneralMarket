package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 03/03/2019.
 */

public class marketModel {
    private String marketName,LastTradingDay,marketId, marketFreq,mktRecy_Loc;
    private float rating;
    private String marketDesc;
    private String gps;

    public marketModel(String marketName, String lastTradingDay, String marketId, String marketFreq, String mktRecy_Loc, float rating, String marketDesc, String gps) {
        this.marketName = marketName;
        LastTradingDay = lastTradingDay;
        this.marketId = marketId;
        this.marketFreq = marketFreq;
        this.mktRecy_Loc = mktRecy_Loc;
        this.rating = rating;
        this.marketDesc = marketDesc;
        this.gps = gps;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getMarketDesc() {
        return marketDesc;
    }

    public void setMarketDesc(String marketDesc) {
        this.marketDesc = marketDesc;
    }

    public String getMktRecy_Loc() {
        return mktRecy_Loc;
    }

    public void setMktRecy_Loc(String mktRecy_Loc) {
        this.mktRecy_Loc = mktRecy_Loc;
    }

    public String getMarketFreq() {
        return marketFreq;
    }

    public void setMarketFreq(String marketFreq) {
        this.marketFreq = marketFreq;
    }

    public  void modifyName(){
        marketName=getMarketId();
    }
    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getLastTradingDay() {
        return LastTradingDay;
    }

    public void setLastTradingDay(String lastTradingDay) {
        LastTradingDay = lastTradingDay;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

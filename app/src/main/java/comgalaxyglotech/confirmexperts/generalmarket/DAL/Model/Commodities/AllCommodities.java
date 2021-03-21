package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Commodities;

/**
 * Created by ELECTRON on 03/28/2019.
 */

public class AllCommodities {
    String itemName,noUser,marketOrStoreName,metric,storeOrMarket,marketorStoreId,itemId;

    public AllCommodities(String itemName, String noUser, String marketOrStoreName, String metric, String storeOrMarket, String marketorStoreId, String itemId) {
        this.itemName = itemName;
        this.noUser = noUser;
        this.marketOrStoreName = marketOrStoreName;
        this.metric = metric;
        this.storeOrMarket = storeOrMarket;
        this.marketorStoreId = marketorStoreId;
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getNoUser() {
        return noUser;
    }

    public void setNoUser(String noUser) {
        this.noUser = noUser;
    }

    public String getMarketOrStoreName() {
        return marketOrStoreName;
    }

    public void setMarketOrStoreName(String marketOrStoreName) {
        this.marketOrStoreName = marketOrStoreName;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getStoreOrMarket() {
        return storeOrMarket;
    }

    public void setStoreOrMarket(String storeOrMarket) {
        this.storeOrMarket = storeOrMarket;
    }

    public String getMarketorStoreId() {
        return marketorStoreId;
    }

    public void setMarketorStoreId(String marketorStoreId) {
        this.marketorStoreId = marketorStoreId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}

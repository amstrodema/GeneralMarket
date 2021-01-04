package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 03/29/2019.
 */

public class StoreItemMainModel {
    String storeItemId,storeId, storeItemName,storeItemDesc,storeItemAvailQty,storeItemMetric,storeItemQtyMetric,storeItemPriceLow,storeItemPriceHigh,creatorId,date;

    public StoreItemMainModel() {
    }

    public StoreItemMainModel(String storeItemId, String storeId, String storeItemName,String storeItemDesc, String storeItemAvailQty, String storeItemMetric, String storeItemQtyMetric, String storeItemPriceLow, String storeItemPriceHigh, String creatorId, String date) {
        this.storeItemId = storeItemId;
        this.storeId = storeId;
        this.storeItemName = storeItemName;
        this.storeItemDesc = storeItemDesc;
        this.storeItemAvailQty = storeItemAvailQty;
        this.storeItemMetric = storeItemMetric;
        this.storeItemQtyMetric = storeItemQtyMetric;
        this.storeItemPriceLow = storeItemPriceLow;
        this.storeItemPriceHigh = storeItemPriceHigh;
        this.creatorId = creatorId;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getStoreItemQtyMetric() {
        return storeItemQtyMetric;
    }

    public void setStoreItemQtyMetric(String storeItemQtyMetric) {
        this.storeItemQtyMetric = storeItemQtyMetric;
    }

    public String getStoreItemId() {
        return storeItemId;
    }

    public void setStoreItemId(String storeItemId) {
        this.storeItemId = storeItemId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreItemName() {
        return storeItemName;
    }

    public void setStoreItemName(String storeItemName) {
        this.storeItemName = storeItemName;
    }

    public String getStoreItemDesc() {
        return storeItemDesc;
    }

    public void setStoreItemDesc(String storeItemDesc) {
        this.storeItemDesc = storeItemDesc;
    }

    public String getStoreItemAvailQty() {
        return storeItemAvailQty;
    }

    public void setStoreItemAvailQty(String storeItemAvailQty) {
        this.storeItemAvailQty = storeItemAvailQty;
    }

    public String getStoreItemMetric() {
        return storeItemMetric;
    }

    public void setStoreItemMetric(String storeItemMetric) {
        this.storeItemMetric = storeItemMetric;
    }

    public String getStoreItemPriceLow() {
        return storeItemPriceLow;
    }

    public void setStoreItemPriceLow(String storeItemPriceLow) {
        this.storeItemPriceLow = storeItemPriceLow;
    }

    public String getStoreItemPriceHigh() {
        return storeItemPriceHigh;
    }

    public void setStoreItemPriceHigh(String storeItemPriceHigh) {
        this.storeItemPriceHigh = storeItemPriceHigh;
    }
}

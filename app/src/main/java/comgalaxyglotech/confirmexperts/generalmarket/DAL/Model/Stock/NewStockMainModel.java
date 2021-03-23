package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock;

/**
 * Created by ELECTRON on 03/18/2019.
 */
//this model is used for stock
public class NewStockMainModel {
    private String id,advertName,creatorId,itemCost,commonNameId,itemDesc,itemQty,category,date;
    private String marketId, storeId;
    private Float rating;
    private boolean ban;
    private String isEditorsPick;
    public NewStockMainModel() {

    }

    public NewStockMainModel(String id, String advertName, String creatorId, String itemCost, String commonNameId, String itemDesc, String itemQty, String category, String date, String marketId, String storeId, Float rating, boolean ban, String isEditorsPick) {
        this.id = id;
        this.advertName = advertName;
        this.creatorId = creatorId;
        this.itemCost = itemCost;
        this.commonNameId = commonNameId;
        this.itemDesc = itemDesc;
        this.itemQty = itemQty;
        this.category = category;
        this.date = date;
        this.marketId = marketId;
        this.storeId = storeId;
        this.rating = rating;
        this.ban = ban;
        this.isEditorsPick = isEditorsPick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvertName() {
        return advertName;
    }

    public void setAdvertName(String advertName) {
        this.advertName = advertName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getItemCost() {
        return itemCost;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public String getCommonNameId() {
        return commonNameId;
    }

    public void setCommonNameId(String commonNameId) {
        this.commonNameId = commonNameId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public String getIsEditorsPick() {
        return isEditorsPick;
    }

    public void setIsEditorsPick(String isEditorsPick) {
        this.isEditorsPick = isEditorsPick;
    }


/*
*
*
* */


 /*   String metricId,lowPrice,highPrice,traderName,traderDesc,traderLoc,traderPhone;
    String variant1Name,variant1LowPrice, variant1HighPrice;
    String variant2Name,variant2LowPrice, variant2HighPrice;
    String variant3Name,variant3LowPrice, variant3HighPrice;
    String variant4Name,variant4LowPrice, variant4HighPrice;

    public NewStockMainModel(String id, String metricId, String creatorId, String lowPrice, String highPrice, String traderName, String traderDesc, String traderLoc, String traderPhone, String date, String marketId, String storeId, String variant1Name, String variant1LowPrice, String variant1HighPrice, String variant2Name, String variant2LowPrice, String variant2HighPrice, String variant3Name, String variant3LowPrice, String variant3HighPrice, String variant4Name, String variant4LowPrice, String variant4HighPrice, Float rating, boolean ban, String isEditorsPick) {
        this.id = id;
        this.metricId = metricId;
        this.creatorId = creatorId;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
        this.traderName = traderName;
        this.traderDesc = traderDesc;
        this.traderLoc = traderLoc;
        this.traderPhone = traderPhone;
        this.date = date;
        this.marketId = marketId;
        this.storeId = storeId;
        this.variant1Name = variant1Name;
        this.variant1LowPrice = variant1LowPrice;
        this.variant1HighPrice = variant1HighPrice;
        this.variant2Name = variant2Name;
        this.variant2LowPrice = variant2LowPrice;
        this.variant2HighPrice = variant2HighPrice;
        this.variant3Name = variant3Name;
        this.variant3LowPrice = variant3LowPrice;
        this.variant3HighPrice = variant3HighPrice;
        this.variant4Name = variant4Name;
        this.variant4LowPrice = variant4LowPrice;
        this.variant4HighPrice = variant4HighPrice;
        this.rating = rating;
        this.ban = ban;
        this.isEditorsPick = isEditorsPick;
    }



    public String getMetricId() {
        return metricId;
    }

    public void setMetricId(String metricId) {
        this.metricId = metricId;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public String getTraderDesc() {
        return traderDesc;
    }

    public void setTraderDesc(String traderDesc) {
        this.traderDesc = traderDesc;
    }

    public String getTraderLoc() {
        return traderLoc;
    }

    public void setTraderLoc(String traderLoc) {
        this.traderLoc = traderLoc;
    }

    public String getTraderPhone() {
        return traderPhone;
    }

    public void setTraderPhone(String traderPhone) {
        this.traderPhone = traderPhone;
    }


    public String getVariant1Name() {
        return variant1Name;
    }

    public void setVariant1Name(String variant1Name) {
        this.variant1Name = variant1Name;
    }

    public String getVariant1LowPrice() {
        return variant1LowPrice;
    }

    public void setVariant1LowPrice(String variant1LowPrice) {
        this.variant1LowPrice = variant1LowPrice;
    }

    public String getVariant1HighPrice() {
        return variant1HighPrice;
    }

    public void setVariant1HighPrice(String variant1HighPrice) {
        this.variant1HighPrice = variant1HighPrice;
    }

    public String getVariant2Name() {
        return variant2Name;
    }

    public void setVariant2Name(String variant2Name) {
        this.variant2Name = variant2Name;
    }

    public String getVariant2LowPrice() {
        return variant2LowPrice;
    }

    public void setVariant2LowPrice(String variant2LowPrice) {
        this.variant2LowPrice = variant2LowPrice;
    }

    public String getVariant2HighPrice() {
        return variant2HighPrice;
    }

    public void setVariant2HighPrice(String variant2HighPrice) {
        this.variant2HighPrice = variant2HighPrice;
    }

    public String getVariant3Name() {
        return variant3Name;
    }

    public void setVariant3Name(String variant3Name) {
        this.variant3Name = variant3Name;
    }

    public String getVariant3LowPrice() {
        return variant3LowPrice;
    }

    public void setVariant3LowPrice(String variant3LowPrice) {
        this.variant3LowPrice = variant3LowPrice;
    }

    public String getVariant3HighPrice() {
        return variant3HighPrice;
    }

    public void setVariant3HighPrice(String variant3HighPrice) {
        this.variant3HighPrice = variant3HighPrice;
    }

    public String getVariant4Name() {
        return variant4Name;
    }

    public void setVariant4Name(String variant4Name) {
        this.variant4Name = variant4Name;
    }

    public String getVariant4LowPrice() {
        return variant4LowPrice;
    }

    public void setVariant4LowPrice(String variant4LowPrice) {
        this.variant4LowPrice = variant4LowPrice;
    }

    public String getVariant4HighPrice() {
        return variant4HighPrice;
    }

    public void setVariant4HighPrice(String variant4HighPrice) {
        this.variant4HighPrice = variant4HighPrice;
    }  */
}

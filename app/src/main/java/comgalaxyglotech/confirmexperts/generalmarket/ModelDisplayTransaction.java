package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 02/15/2020.
 */

public class ModelDisplayTransaction {
    private String id,itemName,buyerName,isConfirmed,time,date, storeName;
    private Double itmQty, itmCost;
    private String type;
    private String payerId;

    public ModelDisplayTransaction(String id, String itemName, String buyerName, String isConfirmed, String time, String date, String storeName, Double itmQty, Double itmCost, String type, String payerId) {
        this.id = id;
        this.itemName = itemName;
        this.buyerName = buyerName;
        this.isConfirmed = isConfirmed;
        this.time = time;
        this.date = date;
        this.storeName = storeName;
        this.itmQty = itmQty;
        this.itmCost = itmCost;
        this.type = type;
        this.payerId = payerId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(String isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Double getItmQty() {
        return itmQty;
    }

    public void setItmQty(Double itmQty) {
        this.itmQty = itmQty;
    }

    public Double getItmCost() {
        return itmCost;
    }

    public void setItmCost(Double itmCost) {
        this.itmCost = itmCost;
    }
}

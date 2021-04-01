package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Transaction;

/**
 * Created by ELECTRON on 02/01/2020.
 */

public class Model_Transaction {
    private String id,itemId,buyerId,payerId,isConfirmed,isBarred,time,date,isNew;
    private Double itmQty, itemCost;
    private String storeId;
    private Double mainWalet, bonusWallet, referalWallet;
    private String payerWalletType, storeOwnerId;
    public Model_Transaction() {
    }

    public Model_Transaction(String id, String itemId, String buyerId, String payerId, String isConfirmed, String isBarred, String time, String date, String isNew, Double itmQty, Double itemCost, String storeId, Double mainWalet, Double bonusWallet, Double referalWallet, String payerWalletType, String storeOwnerId) {
        this.id = id;
        this.itemId = itemId;
        this.buyerId = buyerId;
        this.payerId = payerId;
        this.isConfirmed = isConfirmed;
        this.isBarred = isBarred;
        this.time = time;
        this.date = date;
        this.isNew = isNew;
        this.itmQty = itmQty;
        this.itemCost = itemCost;
        this.storeId = storeId;
        this.mainWalet = mainWalet;
        this.bonusWallet = bonusWallet;
        this.referalWallet = referalWallet;
        this.payerWalletType = payerWalletType;
        this.storeOwnerId = storeOwnerId;
    }

    public String getStoreOwnerId() {
        return storeOwnerId;
    }

    public void setStoreOwnerId(String storeOwnerId) {
        this.storeOwnerId = storeOwnerId;
    }

    public String getPayerWalletType() {
        return payerWalletType;
    }

    public void setPayerWalletType(String payerWalletType) {
        this.payerWalletType = payerWalletType;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public Double getMainWalet() {
        return mainWalet;
    }

    public void setMainWalet(Double mainWalet) {
        this.mainWalet = mainWalet;
    }

    public Double getBonusWallet() {
        return bonusWallet;
    }

    public void setBonusWallet(Double bonusWallet) {
        this.bonusWallet = bonusWallet;
    }

    public Double getReferalWallet() {
        return referalWallet;
    }

    public void setReferalWallet(Double referalWallet) {
        this.referalWallet = referalWallet;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Double getItemCost() {
        return itemCost;
    }

    public void setItemCost(Double itemCost) {
        this.itemCost = itemCost;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(String isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getIsBarred() {
        return isBarred;
    }

    public void setIsBarred(String isBarred) {
        this.isBarred = isBarred;
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

    public Double getItmQty() {
        return itmQty;
    }

    public void setItmQty(Double itmQty) {
        this.itmQty = itmQty;
    }
}

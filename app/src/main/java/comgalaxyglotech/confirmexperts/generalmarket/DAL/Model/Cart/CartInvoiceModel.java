package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Cart;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 06/19/2020.
 */

public class CartInvoiceModel {
    private String id, buyerId, status,payerId, dateTime;
    private double cost;
    private int noItems;
    public CartInvoiceModel() {
    }

    public CartInvoiceModel(String id, String buyerId, String status, String payerId, String dateTime, double cost, int noItems) {
        this.id = id;
        this.buyerId = buyerId;
        this.status = status;
        this.payerId = payerId;
        this.dateTime = dateTime;
        this.cost = cost;
        this.noItems = noItems;
    }

    public int getNoItems() {
        return noItems;
    }

    public void setNoItems(int noItems) {
        this.noItems = noItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}

package comgalaxyglotech.confirmexperts.generalmarket;

import java.util.ArrayList;

/**
 * Created by ELECTRON on 06/18/2020.
 */

public class CartInvoiceDisplayModel {
    private String id, buyerId, status,payerId, dateTime,invoiceName;
    private double cost;
    private int cartNo;

    public CartInvoiceDisplayModel(String id, String buyerId, String status, String payerId, String dateTime, String invoiceName, double cost, int cartNo) {
        this.id = id;
        this.buyerId = buyerId;
        this.status = status;
        this.payerId = payerId;
        this.dateTime = dateTime;
        this.invoiceName = invoiceName;
        this.cost = cost;
        this.cartNo = cartNo;
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

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getCartNo() {
        return cartNo;
    }

    public void setCartNo(int cartNo) {
        this.cartNo = cartNo;
    }
}

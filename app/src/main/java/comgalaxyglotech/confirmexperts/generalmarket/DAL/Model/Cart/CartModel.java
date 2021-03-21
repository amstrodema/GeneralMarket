package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Cart;

import comgalaxyglotech.confirmexperts.generalmarket.NewStockMainModel;

/**
 * Created by ELECTRON on 06/18/2020.
 */

public class CartModel {
    private NewStockMainModel stockToBuy;
    private float itemQty;
    private String id, invoiceId;

    public CartModel(NewStockMainModel stockToBuy, float itemQty, String id, String invoiceId) {
        this.stockToBuy = stockToBuy;
        this.itemQty = itemQty;
        this.id = id;
        this.invoiceId = invoiceId;
    }

    public CartModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public NewStockMainModel getStockToBuy() {
        return stockToBuy;
    }

    public void setStockToBuy(NewStockMainModel stockToBuy) {
        this.stockToBuy = stockToBuy;
    }

    public float getItemQty() {
        return itemQty;
    }

    public void setItemQty(float itemQty) {
        this.itemQty = itemQty;
    }
}

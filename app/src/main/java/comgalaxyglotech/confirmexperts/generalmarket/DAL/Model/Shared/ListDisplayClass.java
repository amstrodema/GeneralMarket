package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Shared;

/**
 * Created by ELECTRON on 10/11/2019.
 */

public class ListDisplayClass {
    private String itemName,itemID,qty;

    public ListDisplayClass(String itemName, String itemID, String qty) {
        this.itemName = itemName;
        this.itemID = itemID;
        this.qty = qty;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
}

package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 03/11/2019.
 */

public class itemModel {
    private String id, itemName;
    private int numberOfSeller;
    private double lowest,highest;
    private String commonId;

    public itemModel(String id, String itemName, int numberOfSeller, double lowest, double highest, String commonId) {
        this.id = id;
        this.itemName = itemName;
        this.numberOfSeller = numberOfSeller;
        this.lowest = lowest;
        this.highest = highest;
        this.commonId = commonId;
    }

    public String getCommonId() {
        return commonId;
    }

    public void setCommonId(String commonId) {
        this.commonId = commonId;
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

    public int getNumberOfSeller() {
        return numberOfSeller;
    }

    public void setNumberOfSeller(int numberOfSeller) {
        this.numberOfSeller = numberOfSeller;
    }

    public double getLowest() {
        return lowest;
    }

    public void setLowest(double lowest) {
        this.lowest = lowest;
    }

    public double getHighest() {
        return highest;
    }

    public void setHighest(double highest) {
        this.highest = highest;
    }
}

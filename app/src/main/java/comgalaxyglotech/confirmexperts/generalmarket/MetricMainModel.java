package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 03/17/2019.
 */

public class MetricMainModel {
    private String id, itemID, metric,creatorID;
    private int quantity;

    public MetricMainModel() {
    }

    public MetricMainModel(String id, String itemID, String metric, String creatorID, int quantity) {
        this.id = id;
        this.itemID = itemID;
        this.metric = metric;
        this.creatorID = creatorID;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

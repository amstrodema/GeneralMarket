package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Metric;

/**
 * Created by ELECTRON on 03/17/2019.
 */

public class MetricDisplayModel {
    private String itemName, metric, noTrader,lowestPrice,highestPrice,id;

    public MetricDisplayModel(String itemName, String metric, String noTrader, String lowestPrice, String highestPrice) {
        this.itemName = itemName;
        this.metric = metric;
        this.noTrader = noTrader;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
    }

    public MetricDisplayModel(String itemName, String metric, String noTrader, String lowestPrice, String highestPrice, String Id) {
        this.itemName = itemName;
        this.metric = metric;
        this.noTrader = noTrader;
        this.lowestPrice = lowestPrice;
        this.highestPrice = highestPrice;
        id = Id;
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

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getNoTrader() {
        return noTrader;
    }

    public void setNoTrader(String noTrader) {
        this.noTrader = noTrader;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(String highestPrice) {
        this.highestPrice = highestPrice;
    }
}

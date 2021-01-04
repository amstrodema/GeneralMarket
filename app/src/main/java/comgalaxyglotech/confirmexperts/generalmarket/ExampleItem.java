package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 03/02/2019.
 */

public class ExampleItem {
    private String itemName,itemId, marketId, creatorId,newItemClass;
    public ExampleItem(){

    }

    public ExampleItem(String itemName, String itemId, String marketId, String creatorId, String newItemClass) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.marketId = marketId;
        this.creatorId = creatorId;
        this.newItemClass = newItemClass;
    }

    public String getNewItemClass() {
        return newItemClass;
    }

    public void setNewItemClass(String newItemClass) {
        this.newItemClass = newItemClass;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}

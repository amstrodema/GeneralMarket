package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Stock;

/**
 * Created by ELECTRON on 03/18/2019.
 */

public class NewStockDisplayModel {
    private String id, advertName, storeId, category, description, price, creatorID;
    private float rating;
    private boolean ban;
    private String fav;

    public NewStockDisplayModel(String id, String advertName, String storeId, String category, String description, String price, String creatorID, float rating, String fav, boolean ban) {
        this.id = id;
        this.advertName = advertName;
        this.storeId = storeId;
        this.category = category;
        this.description = description;
        this.price = price;
        this.creatorID = creatorID;
        this.rating = rating;
        this.ban = ban;
        this.fav = fav;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvertName() {
        return advertName;
    }

    public void setAdvertName(String advertName) {
        this.advertName = advertName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }
}

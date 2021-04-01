package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Favorite;

/**
 * Created by ELECTRON on 02/24/2020.
 */

public class ModelFavey {
    private String id, userId, itemId;
    public ModelFavey() {
    }

    public ModelFavey(String id, String userId, String itemId) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}

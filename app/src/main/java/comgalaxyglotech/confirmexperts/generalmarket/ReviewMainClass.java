package comgalaxyglotech.confirmexperts.generalmarket;

/**
 * Created by ELECTRON on 10/23/2019.
 */

public class ReviewMainClass {
    String id, reviewerId,review, storeId;
    float rating;

    public ReviewMainClass() {
    }

    public ReviewMainClass(String id, String reviewerId, String review, String storeId, float rating) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.review = review;
        this.storeId = storeId;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

package comgalaxyglotech.confirmexperts.generalmarket.DAL.Model.Wallet;

/**
 * Created by ELECTRON on 02/01/2020.
 */

public class Model_Wallet {
    private String id, userId;
    private Double mainWallet, bonusWallet, referralWallet, upfrontWallet;

    public Model_Wallet() {
    }

    public Model_Wallet(String id, String userId, Double mainWallet, Double bonusWallet, Double referralWallet, Double upfrontWallet) {
        this.id = id;
        this.userId = userId;
        this.mainWallet = mainWallet;
        this.bonusWallet = bonusWallet;
        this.referralWallet = referralWallet;
        this.upfrontWallet = upfrontWallet;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getUpfrontWallet() {
        return upfrontWallet;
    }

    public void setUpfrontWallet(Double upfrontWallet) {
        this.upfrontWallet = upfrontWallet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getMainWallet() {
        return mainWallet;
    }

    public void setMainWallet(Double mainWallet) {
        this.mainWallet = mainWallet;
    }

    public Double getBonusWallet() {
        return bonusWallet;
    }

    public void setBonusWallet(Double bonusWallet) {
        this.bonusWallet = bonusWallet;
    }

    public Double getReferralWallet() {
        return referralWallet;
    }

    public void setReferralWallet(Double referralWallet) {
        this.referralWallet = referralWallet;
    }
}

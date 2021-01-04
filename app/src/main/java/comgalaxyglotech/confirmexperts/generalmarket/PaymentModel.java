package comgalaxyglotech.confirmexperts.generalmarket;

import co.paystack.android.model.Card;

public class PaymentModel {
    private Card card;
    private String walletId, type;
    private int amount;

    public PaymentModel(Card card, String walletId, String type, int amount) {
        this.card = card;
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

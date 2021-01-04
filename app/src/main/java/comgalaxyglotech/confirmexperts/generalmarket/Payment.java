package comgalaxyglotech.confirmexperts.generalmarket;

import co.paystack.android.model.Card;

public class Payment {

    public boolean getCard(String cardNumber, int expiryMonth, int expiryYear, String cvv) {
        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
        return card.isValid();
    }

}

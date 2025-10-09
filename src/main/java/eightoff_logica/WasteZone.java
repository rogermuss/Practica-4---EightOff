package eightoff_logica;

import DeckOfCards.CartaInglesa;

public class WasteZone {

    CartaInglesa carta;

    public WasteZone(CartaInglesa carta) {
        this.carta = carta;
    }

    public void setCarta(CartaInglesa carta) {
        this.carta = carta;
    }

    public CartaInglesa getCarta() {
        return carta;
    }

    public void removerCarta() {
        carta = null;
    }
}

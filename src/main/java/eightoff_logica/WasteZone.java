package eightoff_logica;

import DeckOfCards.CartaInglesa;

public class WasteZone {

    private CartaInglesa carta;

    public WasteZone(CartaInglesa carta) {
        this.carta = carta;
    }
    public WasteZone[] clonarWasteZones(WasteZone[] originales) {
        WasteZone[] copia = new WasteZone[originales.length];
        for (int i = 0; i < originales.length; i++) {
            if (originales[i] != null) {
                copia[i] = new WasteZone(originales[i].getCarta()); // SOLO si original != null
            } else {
                copia[i] = new WasteZone(); // constructor vacío
            }
        }
        return copia;
    }



    public WasteZone() {
        carta = null;
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

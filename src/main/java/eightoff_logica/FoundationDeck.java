package eightoff_logica;

import DeckOfCards.Carta;
import DeckOfCards.CartaInglesa;
import lista_circular_simple.ListaCircularSimple;

public class FoundationDeck {

    ListaCircularSimple<CartaInglesa> foundation;

    public FoundationDeck() {
        foundation = new ListaCircularSimple<>();
    }
    public FoundationDeck(FoundationDeck original) {
        this.foundation = new ListaCircularSimple<>();
        for (int i = 0; i < original.getFoundation().size(); i++) {
            this.foundation.insertaFin(new CartaInglesa(original.getFoundation().get(i)));
        }
    }

    public boolean ingresarCarta(CartaInglesa card) {
        if(foundation.isEmpty()){
            if(card.getValorEnum() == Carta.ValorEnum.AS){
                foundation.insertaFin(card);
                return true;
            }
        }
        else{
            if(foundation.get(foundation.size() - 1).compareTo(card) == -1){
                foundation.insertaFin(card);
                return  true;
            }
        }
        return false;
    }

    public CartaInglesa removerUltimaCarta(){
        if(foundation.isEmpty()){
            return null;
        }else{
            return foundation.eliminaFin();
        }
    }

    public ListaCircularSimple<CartaInglesa> getFoundation() {
        return foundation;
    }

    public void setFoundation(ListaCircularSimple<CartaInglesa> foundation) {
        this.foundation = foundation;
    }
}

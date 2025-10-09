package eightoff_logica;

import DeckOfCards.Carta;
import DeckOfCards.CartaInglesa;
import lista_circular_simple.ListaCircularSimple;

public class FoundationDeck {

    ListaCircularSimple<CartaInglesa> foundation;

    public FoundationDeck() {
        foundation = new ListaCircularSimple<>();
    }

    public void ingresarCarta(CartaInglesa card) {
        if(foundation.isEmpty()){
            if(card.getValorEnum() == Carta.ValorEnum.AS){
                foundation.insertaFin(card);
            }
        }
        else{
            if(foundation.get(foundation.size() - 1).compareTo(card) == -1){
                foundation.insertaFin(card);
            }
        }
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

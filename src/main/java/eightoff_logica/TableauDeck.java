package eightoff_logica;

import DeckOfCards.Carta;
import DeckOfCards.CartaInglesa;
import lista_circular_simple.ListaCircularSimple;


public class TableauDeck {

    private ListaCircularSimple<CartaInglesa> tableau;

    public TableauDeck(){
        tableau = new ListaCircularSimple<>();
    }

    public boolean insertarCartas(ListaCircularSimple<CartaInglesa> cartas){
        if(tableau.isEmpty()){
            if(cartas.get(0).getValorEnum() == Carta.ValorEnum.K){
                for (int i = 0; i < cartas.size(); i++){
                    tableau.insertaFin(cartas.get(i));
                }
                return true;
            }
        } else {
            if(tableau.get(tableau.size() - 1).compareTo(cartas.get(0)) == 1) {
                for (int i = 0; i < cartas.size(); i++){
                    tableau.insertaFin(cartas.get(i));
                }
                return true;
            }
        }
        return false;
    }

    public void insertarCarta(CartaInglesa carta){
        tableau.insertaFin(carta);
    }

    public boolean contains(CartaInglesa carta){
        for(int i = 0; i<tableau.size(); i++){
            if(tableau.get(i).compareTo(carta) == 0){
                return true;
            }
        }
        return false;
    }

    public int getCardIndex(CartaInglesa carta){
        for(int i = 0; i<tableau.size(); i++){
            if(tableau.get(i).compareTo(carta) == 0){
                return i;
            }
        }
        return -1;
    }

    public ListaCircularSimple<CartaInglesa> removerCartas(CartaInglesa carta){
        if(contains(carta)){
            ListaCircularSimple<CartaInglesa> cartas = new ListaCircularSimple<>();
            int indexCartaSeleccionada = getCardIndex(carta);

            // Copiar cartas desde la seleccionada hasta el final
            for(int i = indexCartaSeleccionada; i < tableau.size(); i++){
                cartas.insertaFin(tableau.get(i));
            }

            // Eliminar desde el final hasta la carta seleccionada (INCLUSIVE)
            for(int i = tableau.size()-1; i >= indexCartaSeleccionada; i--){
                tableau.eliminaFin();
            }

            // Si quedan cartas, voltear la última
            if(!tableau.isEmpty()){
                tableau.get(tableau.size()-1).makeFaceUp();
            }

            return cartas;
        }
        return null;
    }

    public ListaCircularSimple<CartaInglesa> getTableau() {
        return tableau;
    }

    public void setTableau(ListaCircularSimple<CartaInglesa> tableau) {
        this.tableau = tableau;
    }
}

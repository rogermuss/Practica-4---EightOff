package eightoff_logica;

import DeckOfCards.Carta;
import DeckOfCards.CartaInglesa;
import lista_circular_simple.ListaCircularSimple;


public class TableauDeck {

    private ListaCircularSimple<CartaInglesa> tableau;

    public TableauDeck(){
        tableau = new ListaCircularSimple<>();
    }

    public void insertarCartas(ListaCircularSimple<CartaInglesa> cartas){
        if(tableau.isEmpty()){
            if(cartas.get(cartas.size()-1).getValorEnum() == Carta.ValorEnum.K){
                for (int i = cartas.size()-1; i >= 0; i--){
                    tableau.insertaFin(cartas.get(i));
                }
            }
        } else{
            if(tableau.get(tableau.size() - 1).compareTo(cartas.get(cartas.size()-1)) == 1) {
                for (int i = cartas.size()-1; i >= 0; i--){
                    tableau.insertaFin(cartas.get(i));
                }
            }
        }
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
           for(int i = indexCartaSeleccionada; i<tableau.size(); i++){
               cartas.insertaFin(tableau.get(i));
           }
           for(int i = tableau.size()-1; i>indexCartaSeleccionada; i--){
               tableau.eliminaFin();
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

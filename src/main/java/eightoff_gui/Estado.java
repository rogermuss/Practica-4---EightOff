package eightoff_gui;

import DeckOfCards.CartaInglesa;
import eightoff_logica.FoundationDeck;
import eightoff_logica.TableauDeck;
import eightoff_logica.WasteZone;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lista_circular_simple.ListaCircularSimple;

import java.util.ArrayList;

public class Estado {
    // Atributos que guardan índices y estados
    private FoundationDeck[]  foundationDecks =  new FoundationDeck[4];
    private TableauDeck[] tableauDecks =  new TableauDeck[8];
    private WasteZone[]  wasteZones = new WasteZone[8];


    // Constructor que captura el estado actual del juego
    //Se ingresa el tableau, los foundation, las pilas y las cartas graficas
    //para obtener el indice y visibilidad de todas las cartas.


    public Estado(TableauDeck[] tableaus, FoundationDeck[] foundations,
                  WasteZone[] wasteZones) {

        ArrayList<ListaCircularSimple<CartaInglesa>> t = new ArrayList<>();
        ArrayList<ListaCircularSimple<CartaInglesa>> f  = new ArrayList<>();


        CartaInglesa[] wzCartas =  new CartaInglesa[8];


        for (int i = 0; i < wasteZones.length; i++) {
            CartaInglesa carta = wasteZones[i].getCarta();
            this.wasteZones[i] = new WasteZone();
            if (carta != null) {
                this.wasteZones[i].setCarta(new CartaInglesa(carta.getValor(),
                        carta.getPalo(), carta.getColor()));
            }
        }

        // Copiar TableauDecks
        for (int i = 0; i < tableaus.length; i++) {
            this.tableauDecks[i] = new TableauDeck();
            ListaCircularSimple<CartaInglesa> original = tableaus[i].getTableau();
            if (original != null) {
                this.tableauDecks[i].setTableau(new ListaCircularSimple<>(original));
            }
        }

        // Copiar FoundationDecks
        for (int i = 0; i < foundations.length; i++) {
            this.foundationDecks[i] = new FoundationDeck();
            ListaCircularSimple<CartaInglesa> original = foundations[i].getFoundation();
            if (original != null) {
                this.foundationDecks[i].setFoundation(new ListaCircularSimple<>(original));
            }
        }


    }

    public WasteZone[] getWasteZones() {
        return wasteZones;
    }

    public TableauDeck[] getTableauDecks() {
        return tableauDecks;
    }

    public FoundationDeck[] getFoundationDecks() {
        return foundationDecks;
    }
}
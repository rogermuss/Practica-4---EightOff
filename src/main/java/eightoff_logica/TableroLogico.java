package eightoff_logica;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import lista_circular_doble.ListaCircularDoble;
import lista_circular_simple.ListaCircularSimple;

import java.util.ArrayList;

public class TableroLogico {
    private Mazo mazo;
    private FoundationDeck[]  foundationDecks =  new FoundationDeck[4];
    private TableauDeck[] tableauDecks =  new TableauDeck[8];
    private WasteZone[]  wasteZones = new WasteZone[8];
    private ListaCircularSimple<CartaInglesa> cartasMazo = new ListaCircularSimple<>();
    private ArrayList<CartaInglesa> cartasGraficas = new ArrayList<>();

    public TableroLogico() {
        this.mazo = new Mazo();
        convertirListaDobleASimple();

        initFoundations();

        makeAllVisible();

        repartirCartasTableau();

        repartirCartasWasteZones();

        crearMazoGrafico();
    }

    public void makeAllVisible(){
        for(int i=0; i<cartasMazo.size(); i++ ){
            cartasMazo.get(i).makeFaceUp();
        }
    }

    public void initFoundations(){
        for(int i = 0; i < foundationDecks.length; i++){
            foundationDecks[i] = new FoundationDeck();
        }
    }

    public void convertirListaDobleASimple() {
        ListaCircularDoble<CartaInglesa> m = mazo.getCartas();
        while (!m.isEmpty()) {
            CartaInglesa c = m.eliminaFin();
            if (c != null) {
                cartasMazo.insertaInicio(c);
            }
        }
    }

    public void repartirCartasTableau(){
        for(int i = 0; i < tableauDecks.length; i++){
            tableauDecks[i] = new TableauDeck();
            for(int j = 0; j < 6; j++){
                CartaInglesa c = cartasMazo.eliminaFin();
                if(j!=5){c.makeFaceDown();}
                tableauDecks[i].insertarCarta(c);
            }
        }
    }

    public void repartirCartasWasteZones(){
        for(int i = 0; i < wasteZones.length; i++){
            CartaInglesa c = cartasMazo.eliminaFin();
            if( c != null) {
                wasteZones[i] = new WasteZone(c);
            }else{
                wasteZones[i] = new WasteZone();
            }
        }
    }

    public void crearMazoGrafico(){
        for (TableauDeck tableauDeck : tableauDecks) {
            int cantidad = tableauDeck.getTableau().size();
            for (int j = 0; j < cantidad; j++) {
                cartasGraficas.add(tableauDeck.getTableau().get(j));
            }
        }
        for (WasteZone wz : wasteZones) {
            if(wz.getCarta()!=null){
                cartasGraficas.add(wz.getCarta());
            }
            else {
                return;
            }
        }
    }
    public FoundationDeck[] clonarFoundationDecks(FoundationDeck[] originales) {
        FoundationDeck[] copia = new FoundationDeck[originales.length];
        for (int i = 0; i < originales.length; i++) {
            copia[i] = new FoundationDeck(originales[i]);
        }
        return copia;
    }

    public TableauDeck[] clonarTableauDecks(TableauDeck[] originales) {
        TableauDeck[] copia = new TableauDeck[originales.length];
        for (int i = 0; i < originales.length; i++) {
            copia[i] = new TableauDeck(originales[i]);
        }
        return copia;
    }



    public void setFoundationDecks(FoundationDeck[] foundationDecks) {
        this.foundationDecks = foundationDecks;
    }

    public void setTableauDecks(TableauDeck[] tableauDecks) {
        this.tableauDecks = tableauDecks;
    }

    public void setWasteZones(WasteZone[] wasteZones) {
        this.wasteZones = wasteZones;
    }

    public void setCartasGraficas(ArrayList<CartaInglesa> cartasGraficas) {
        this.cartasGraficas = cartasGraficas;
    }

    public ArrayList<CartaInglesa> getCartasGraficas() {
        return cartasGraficas;
    }

    public FoundationDeck[] getFoundationDecks() {
        return foundationDecks;
    }

    public TableauDeck[] getTableauDecks() {
        return tableauDecks;
    }

    public WasteZone[] getWasteZones() {
        return wasteZones;
    }

    public static void main(String[] args){
        TableroLogico tableroLogico = new TableroLogico();
        System.out.println(tableroLogico.cartasGraficas);
    }
}

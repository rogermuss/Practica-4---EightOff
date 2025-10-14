package eightoff_logica;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import lista_circular_doble.ListaCircularDoble;
import lista_circular_simple.ListaCircularSimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableroLogico {
    private Mazo mazo;
    private FoundationDeck[]  foundationDecks =  new FoundationDeck[4];
    private TableauDeck[] tableauDecks =  new TableauDeck[8];
    private WasteZone[]  wasteZones = new WasteZone[8];
    private ListaCircularSimple<CartaInglesa> cartasMazo = new ListaCircularSimple<>();
    private ArrayList<CartaInglesa> cartasGraficas = new ArrayList<>();
    private HashMap<CartaInglesa, CartaInglesa> movimientosDisponibles = new HashMap<>();

    public TableroLogico() {
        this.mazo = new Mazo();
        convertirListaDobleASimple();

        initFoundations();

        makeAllVisible();

        repartirCartasTableau();

        repartirCartasWasteZones();

        crearMazoGrafico();
    }

    public ListaCircularSimple<Boolean> guardarVisibilidadCartas() {
        ListaCircularSimple<Boolean> visibilidadCartas = new ListaCircularSimple<>();

        for (int i = 0; i < cartasGraficas.size(); i++) {
            CartaInglesa carta = cartasGraficas.get(i);
            visibilidadCartas.insertaFin(carta.isFaceup());
        }

        return visibilidadCartas;
    }

    public void restaurarVisibilidadCartas(ListaCircularSimple<Boolean> visibilidadCartas) {
        int total = Math.min(cartasGraficas.size(), visibilidadCartas.size());

        for (int i = 0; i < total; i++) {
            CartaInglesa carta = cartasGraficas.get(i);
            boolean visible = visibilidadCartas.get(i);

            if (visible) {
                carta.makeFaceUp();
            } else {
                carta.makeFaceDown();
            }
        }
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
            copia[i] = new FoundationDeck();
            ListaCircularSimple<CartaInglesa> listaOriginal = originales[i].getFoundation();
            for(int j = 0; j < listaOriginal.size(); j++) {
                // Buscar la carta en cartasGraficas en lugar de crear una nueva
                CartaInglesa cartaOriginal = listaOriginal.get(j);
                for(CartaInglesa cartaGrafica : cartasGraficas) {
                    if(cartaGrafica.toString().equals(cartaOriginal.toString())) {
                        copia[i].getFoundation().insertaFin(cartaGrafica);
                        break;
                    }
                }
            }
        }
        return copia;
    }

    public TableauDeck[] clonarTableauDecks(TableauDeck[] originales) {
        TableauDeck[] copia = new TableauDeck[originales.length];
        for (int i = 0; i < originales.length; i++) {
            copia[i] = new TableauDeck();
            ListaCircularSimple<CartaInglesa> listaOriginal = originales[i].getTableau();
            for(int j = 0; j < listaOriginal.size(); j++) {
                // Buscar la carta en cartasGraficas en lugar de crear una nueva
                CartaInglesa cartaOriginal = listaOriginal.get(j);
                for(CartaInglesa cartaGrafica : cartasGraficas) {
                    if(cartaGrafica.toString().equals(cartaOriginal.toString())) {
                        copia[i].getTableau().insertaFin(cartaGrafica);
                        break;
                    }
                }
            }
        }
        return copia;
    }

    public WasteZone[] clonarWasteZones(WasteZone[] originales) {
        WasteZone[] copia = new WasteZone[originales.length];
        for (int i = 0; i < originales.length; i++) {
            copia[i] = new WasteZone();
            if (originales[i].getCarta() != null) {
                CartaInglesa cartaOriginal = originales[i].getCarta();
                // Buscar la carta en cartasGraficas
                for(CartaInglesa cartaGrafica : cartasGraficas) {
                    if(cartaGrafica.toString().equals(cartaOriginal.toString())) {
                        copia[i].setCarta(cartaGrafica);
                        break;
                    }
                }
            }
        }
        return copia;
    }

    public boolean canMove() {
        movimientosDisponibles.clear();

        ArrayList<ListaCircularSimple<CartaInglesa>> cartasJugablesTableau = new ArrayList<>();
        ArrayList<ListaCircularSimple<CartaInglesa>> cartasJugablesFoundation = new ArrayList<>();

        for (int i = 0; i < tableauDecks.length; i++) cartasJugablesTableau.add(new ListaCircularSimple<>());
        for (int i = 0; i < foundationDecks.length; i++) cartasJugablesFoundation.add(new ListaCircularSimple<>());

        CartaInglesa cTemp;

        // Recolectar cartas jugables de los tableaus
        for (int indexArregloTableaus = 0; indexArregloTableaus < tableauDecks.length; indexArregloTableaus++) {
            TableauDeck tableauDeck = tableauDecks[indexArregloTableaus];
            if (!tableauDeck.getTableau().isEmpty()) {
                int indexInferiorASuperior = tableauDeck.getTableau().size() - 1;
                while (indexInferiorASuperior >= 0 && tableauDeck.getTableau().get(indexInferiorASuperior).isFaceup()) {
                    cTemp = tableauDeck.getTableau().get(indexInferiorASuperior);
                    if (cTemp != null) cartasJugablesTableau.get(indexArregloTableaus).insertaFin(cTemp);
                    indexInferiorASuperior--;
                }
            }
        }

        // Movimientos tableau - foundation
        for (ListaCircularSimple<CartaInglesa> listaCartasTableau : cartasJugablesTableau) {
            for (int i = 0; i < listaCartasTableau.size(); i++) {
                CartaInglesa cartaTableau = listaCartasTableau.get(i);
                for (FoundationDeck f : foundationDecks) {
                    if (f.getFoundation().isEmpty() && cartaTableau.getValor() == 1) {
                        movimientosDisponibles.put(cartaTableau, null);
                    } else if (!f.getFoundation().isEmpty()) {
                        CartaInglesa topeFoundation = f.getFoundation().get(f.getFoundation().size() - 1);
                        if (cartaTableau.getPalo().equals(topeFoundation.getPalo()) &&
                                cartaTableau.getValor() == topeFoundation.getValor() + 1) {
                            movimientosDisponibles.put(cartaTableau, topeFoundation);
                        }
                    }
                }
            }
        }

        // Movimientos tableau - tableau
        for (int i = 0; i < tableauDecks.length; i++) {
            TableauDeck origen = tableauDecks[i];
            if (!origen.getTableau().isEmpty()) {
                // Construir secuencia descendente del mismo palo desde el tope
                List<CartaInglesa> secuencia = new ArrayList<>();
                CartaInglesa c = origen.getTableau().get(origen.getTableau().size() - 1);
                secuencia.add(c);

                for (int k = origen.getTableau().size() - 2; k >= 0; k--) {
                    CartaInglesa anterior = origen.getTableau().get(k);
                    if (anterior.getPalo().equals(c.getPalo()) && anterior.getValor() == c.getValor() + 1) {
                        secuencia.add(0, anterior); // agregar al inicio de la secuencia
                        c = anterior;
                    } else {
                        break;
                    }
                }

                // Carta inferior de la secuencia
                CartaInglesa cartaInferior = secuencia.get(0);

                // Verificar movimientos hacia otros tableau
                for (int j = 0; j < tableauDecks.length; j++) {
                    if (i == j) continue;
                    TableauDeck destino = tableauDecks[j];
                    if (destino.getTableau().isEmpty()) {
                        movimientosDisponibles.put(cartaInferior, null);
                    } else {
                        CartaInglesa cartaDestino = destino.getTableau().get(destino.getTableau().size() - 1);
                        if (cartaInferior.getPalo().equals(cartaDestino.getPalo()) &&
                                cartaInferior.getValor() == cartaDestino.getValor() - 1) {
                            movimientosDisponibles.put(cartaInferior, cartaDestino);
                        }
                    }
                }
            }
        }



        // Movimientos desde waste zones
        boolean hayEspacios = false;
        for (WasteZone wz : wasteZones) {
            if (wz.getCarta() != null) {
                CartaInglesa cartaWaste = wz.getCarta();

                // Hacia tableau
                for (TableauDeck t : tableauDecks) {
                    if (t.getTableau().isEmpty()) {
                        movimientosDisponibles.put(cartaWaste, null);
                    } else {
                        CartaInglesa topeCarta = t.getTableau().get(t.getTableau().size() - 1);
                        if (cartaWaste.getPalo().equals(topeCarta.getPalo()) &&
                                cartaWaste.getValor() == topeCarta.getValor() - 1) {
                            movimientosDisponibles.put(cartaWaste, topeCarta);
                        }
                    }
                }

                // Hacia foundation
                for (FoundationDeck f : foundationDecks) {
                    if (f.getFoundation().isEmpty() && cartaWaste.getValor() == 1) {
                        movimientosDisponibles.put(cartaWaste, null);
                    } else if (!f.getFoundation().isEmpty()) {
                        CartaInglesa topeFoundation = f.getFoundation().get(f.getFoundation().size() - 1);
                        if (cartaWaste.getPalo().equals(topeFoundation.getPalo()) &&
                                cartaWaste.getValor() == topeFoundation.getValor() + 1) {
                            movimientosDisponibles.put(cartaWaste, topeFoundation);
                        }
                    }
                }
            }
           else{
               hayEspacios = true;
            }
        }



        // Si hay al menos un movimiento registrado, retornamos true
        return !movimientosDisponibles.isEmpty() || hayEspacios;
    }




    public CartaInglesa[] tomarMovimientoDisponible() {
        CartaInglesa[] movimientoSeleccionado = new CartaInglesa[2];

        if (!movimientosDisponibles.isEmpty()) {
            // Obtener el primer par de movimientos disponibles
            for (HashMap.Entry<CartaInglesa, CartaInglesa> entry : movimientosDisponibles.entrySet()) {
                movimientoSeleccionado[0] = entry.getKey();      // Carta origen
                movimientoSeleccionado[1] = entry.getValue();    // Carta destino
                return movimientoSeleccionado;
            }
        }

        return null;
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

    public void setMovimientosDisponibles(HashMap<CartaInglesa, CartaInglesa> movimientosDisponibles) {
        this.movimientosDisponibles = movimientosDisponibles;
    }

    public HashMap<CartaInglesa, CartaInglesa> getMovimientosDisponibles() {
        return movimientosDisponibles;
    }

    public static void main(String[] args){
        TableroLogico tableroLogico = new TableroLogico();
        System.out.println(tableroLogico.cartasGraficas);
    }
}

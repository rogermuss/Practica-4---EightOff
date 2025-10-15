package eightoff_logica;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Mazo;
import lista_circular_doble.ListaCircularDoble;
import lista_circular_simple.ListaCircularSimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Clase que representa la logica del juego Eight Off (una variante del solitario).
 * Se encarga de inicializar el mazo, repartir cartas, manejar fundaciones, tableau,
 * zonas de desecho y determinar los movimientos posibles.
 */
public class TableroLogico {
    private Mazo mazo;
    private FoundationDeck[] foundationDecks = new FoundationDeck[4];
    private TableauDeck[] tableauDecks = new TableauDeck[8];
    private WasteZone[] wasteZones = new WasteZone[8];
    private ListaCircularSimple<CartaInglesa> cartasMazo = new ListaCircularSimple<>();
    private ArrayList<CartaInglesa> cartasGraficas = new ArrayList<>();
    private HashMap<CartaInglesa, CartaInglesa> movimientosDisponibles = new HashMap<>();

    /**
     * Constructor principal del tablero.
     * Inicializa el mazo, convierte las cartas del mazo doble a una lista simple,
     * crea las fundaciones, pone todas las cartas boca arriba, reparte las cartas
     * en los tableau y las zonas de desecho, y genera la lista grafica de cartas.
     */
    public TableroLogico() {
        this.mazo = new Mazo();
        convertirListaDobleASimple();
        initFoundations();
        makeAllVisible();
        repartirCartasTableau();
        repartirCartasWasteZones();
        crearMazoGrafico();
    }

    /**
     * Guarda el estado de visibilidad (boca arriba / boca abajo) de todas las cartas graficas.
     * return Lista circular simple con valores booleanos que representan si cada carta esta visible.
     */
    public ListaCircularSimple<Boolean> guardarVisibilidadCartas() {
        ListaCircularSimple<Boolean> visibilidadCartas = new ListaCircularSimple<>();
        for (CartaInglesa carta : cartasGraficas) {
            visibilidadCartas.insertaFin(carta.isFaceup());
        }
        return visibilidadCartas;
    }

    /**
     * Restaura el estado de visibilidad de las cartas a partir de una lista guardada.
     */
    public void restaurarVisibilidadCartas(ListaCircularSimple<Boolean> visibilidadCartas) {
        int total = Math.min(cartasGraficas.size(), visibilidadCartas.size());
        for (int i = 0; i < total; i++) {
            CartaInglesa carta = cartasGraficas.get(i);
            boolean visible = visibilidadCartas.get(i);
            if (visible) carta.makeFaceUp();
            else carta.makeFaceDown();
        }
    }

    /**
     * Pone todas las cartas del mazo boca arriba.
     */
    public void makeAllVisible() {
        for (int i = 0; i < cartasMazo.size(); i++) {
            cartasMazo.get(i).makeFaceUp();
        }
    }

    /**
     * Inicializa las cuatro fundaciones del tablero.
     */
    public void initFoundations() {
        for (int i = 0; i < foundationDecks.length; i++) {
            foundationDecks[i] = new FoundationDeck();
        }
    }

    /**
     * Convierte la lista circular doble del mazo a una lista circular simple,
     * extrayendo las cartas una por una desde el final.
     */
    public void convertirListaDobleASimple() {
        ListaCircularDoble<CartaInglesa> m = mazo.getCartas();
        while (!m.isEmpty()) {
            CartaInglesa c = m.eliminaFin();
            if (c != null) {
                cartasMazo.insertaInicio(c);
            }
        }
    }

    /**
     * Reparte 6 cartas a cada una de las 8 pilas del tableau.
     * Solo la carta superior de cada pila queda boca arriba.
     */
    public void repartirCartasTableau() {
        for (int i = 0; i < tableauDecks.length; i++) {
            tableauDecks[i] = new TableauDeck();
            for (int j = 0; j < 6; j++) {
                CartaInglesa c = cartasMazo.eliminaFin();
                if (j != 5) c.makeFaceDown();
                tableauDecks[i].insertarCarta(c);
            }
        }
    }

    /**
     * Coloca una carta en cada una de las 8 zonas de desecho.
     * Si no quedan cartas, la zona se inicializa vacia.
     */
    public void repartirCartasWasteZones() {
        for (int i = 0; i < wasteZones.length; i++) {
            CartaInglesa c = cartasMazo.eliminaFin();
            if (c != null) wasteZones[i] = new WasteZone(c);
            else wasteZones[i] = new WasteZone();
        }
    }

    /**
     * Crea una lista grafica de todas las cartas visibles en el tablero,
     * incluyendo las cartas de tableau y las de las zonas de desecho.
     */
    public void crearMazoGrafico() {
        for (TableauDeck tableauDeck : tableauDecks) {
            int cantidad = tableauDeck.getTableau().size();
            for (int j = 0; j < cantidad; j++) {
                cartasGraficas.add(tableauDeck.getTableau().get(j));
            }
        }
        for (WasteZone wz : wasteZones) {
            if (wz.getCarta() != null) {
                cartasGraficas.add(wz.getCarta());
            } else {
                return;
            }
        }
    }

    /**
     * Crea una copia de las fundaciones usando referencias a las cartas graficas existentes.
     */
    public FoundationDeck[] clonarFoundationDecks(FoundationDeck[] originales) {
        FoundationDeck[] copia = new FoundationDeck[originales.length];
        for (int i = 0; i < originales.length; i++) {
            copia[i] = new FoundationDeck();
            ListaCircularSimple<CartaInglesa> listaOriginal = originales[i].getFoundation();
            for (int j = 0; j < listaOriginal.size(); j++) {
                CartaInglesa cartaOriginal = listaOriginal.get(j);
                for (CartaInglesa cartaGrafica : cartasGraficas) {
                    if (cartaGrafica.toString().equals(cartaOriginal.toString())) {
                        copia[i].getFoundation().insertaFin(cartaGrafica);
                        break;
                    }
                }
            }
        }
        return copia;
    }

    /**
     * Crea una copia de los tableau usando las cartas graficas existentes.
     */
    public TableauDeck[] clonarTableauDecks(TableauDeck[] originales) {
        TableauDeck[] copia = new TableauDeck[originales.length];
        for (int i = 0; i < originales.length; i++) {
            copia[i] = new TableauDeck();
            ListaCircularSimple<CartaInglesa> listaOriginal = originales[i].getTableau();
            for (int j = 0; j < listaOriginal.size(); j++) {
                CartaInglesa cartaOriginal = listaOriginal.get(j);
                for (CartaInglesa cartaGrafica : cartasGraficas) {
                    if (cartaGrafica.toString().equals(cartaOriginal.toString())) {
                        copia[i].getTableau().insertaFin(cartaGrafica);
                        break;
                    }
                }
            }
        }
        return copia;
    }

    /**
     * Crea una copia de las zonas de desecho (WasteZone) usando las cartas graficas existentes.
     */
    public WasteZone[] clonarWasteZones(WasteZone[] originales) {
        WasteZone[] copia = new WasteZone[originales.length];
        for (int i = 0; i < originales.length; i++) {
            copia[i] = new WasteZone();
            if (originales[i].getCarta() != null) {
                CartaInglesa cartaOriginal = originales[i].getCarta();
                for (CartaInglesa cartaGrafica : cartasGraficas) {
                    if (cartaGrafica.toString().equals(cartaOriginal.toString())) {
                        copia[i].setCarta(cartaGrafica);
                        break;
                    }
                }
            }
        }
        return copia;
    }

    /**
     * Determina si existen movimientos validos en el tablero.
     * Analiza las cartas visibles en tableau y waste zones para verificar
     * posibles movimientos hacia fundaciones o entre pilas de tableau.
     * Tambien revisa si hay espacios vacios en las waste zones (que permiten mover una carta).
     * return true si hay al menos un movimiento posible o hay huecos en las zonas, false si el jugador no puede mover.
     */
    public boolean canMove() {
        movimientosDisponibles.clear();
        ArrayList<ListaCircularSimple<CartaInglesa>> cartasJugablesTableau = new ArrayList<>();
        for (int i = 0; i < tableauDecks.length; i++) cartasJugablesTableau.add(new ListaCircularSimple<>());

        CartaInglesa cTemp;

        // Recolectar cartas visibles de los tableau
        for (int i = 0; i < tableauDecks.length; i++) {
            TableauDeck tableau = tableauDecks[i];
            if (!tableau.getTableau().isEmpty()) {
                int idx = tableau.getTableau().size() - 1;
                while (idx >= 0 && tableau.getTableau().get(idx).isFaceup()) {
                    cTemp = tableau.getTableau().get(idx);
                    if (cTemp != null) cartasJugablesTableau.get(i).insertaFin(cTemp);
                    idx--;
                }
            }
        }

        // Movimientos desde tableau hacia fundaciones
        for (ListaCircularSimple<CartaInglesa> listaCartas : cartasJugablesTableau) {
            for (int i = 0; i < listaCartas.size(); i++) {
                CartaInglesa carta = listaCartas.get(i);
                for (FoundationDeck f : foundationDecks) {
                    if (f.getFoundation().isEmpty() && carta.getValor() == 1) {
                        movimientosDisponibles.put(carta, null);
                    } else if (!f.getFoundation().isEmpty()) {
                        CartaInglesa tope = f.getFoundation().get(f.getFoundation().size() - 1);
                        if (carta.getPalo().equals(tope.getPalo()) &&
                                carta.getValor() == tope.getValor() + 1) {
                            movimientosDisponibles.put(carta, tope);
                        }
                    }
                }
            }
        }

        // Movimientos entre tableau
        for (int i = 0; i < tableauDecks.length; i++) {
            TableauDeck origen = tableauDecks[i];
            if (!origen.getTableau().isEmpty()) {
                // Crear secuencia descendente del mismo palo
                List<CartaInglesa> secuencia = new ArrayList<>();
                CartaInglesa c = origen.getTableau().get(origen.getTableau().size() - 1);
                secuencia.add(c);
                for (int k = origen.getTableau().size() - 2; k >= 0; k--) {
                    CartaInglesa anterior = origen.getTableau().get(k);
                    if (anterior.getPalo().equals(c.getPalo()) && anterior.getValor() == c.getValor() + 1) {
                        secuencia.add(0, anterior);
                        c = anterior;
                    } else break;
                }

                CartaInglesa cartaInferior = secuencia.get(0);

                // Verificar hacia otros tableau
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
                        CartaInglesa tope = t.getTableau().get(t.getTableau().size() - 1);
                        if (cartaWaste.getPalo().equals(tope.getPalo()) &&
                                cartaWaste.getValor() == tope.getValor() - 1) {
                            movimientosDisponibles.put(cartaWaste, tope);
                        }
                    }
                }

                // Hacia fundacion
                for (FoundationDeck f : foundationDecks) {
                    if (f.getFoundation().isEmpty() && cartaWaste.getValor() == 1) {
                        movimientosDisponibles.put(cartaWaste, null);
                    } else if (!f.getFoundation().isEmpty()) {
                        CartaInglesa tope = f.getFoundation().get(f.getFoundation().size() - 1);
                        if (cartaWaste.getPalo().equals(tope.getPalo()) &&
                                cartaWaste.getValor() == tope.getValor() + 1) {
                            movimientosDisponibles.put(cartaWaste, tope);
                        }
                    }
                }
            } else {
                hayEspacios = true;
            }
        }

        // Devuelve true si hay movimientos o si hay huecos disponibles
        return !movimientosDisponibles.isEmpty() || hayEspacios;
    }

    /**
     * Retorna el primer movimiento disponible detectado por canMove().
     */
    public CartaInglesa[] tomarMovimientoDisponible() {
        CartaInglesa[] movimientoSeleccionado = new CartaInglesa[2];
        if (!movimientosDisponibles.isEmpty()) {
            for (HashMap.Entry<CartaInglesa, CartaInglesa> entry : movimientosDisponibles.entrySet()) {
                movimientoSeleccionado[0] = entry.getKey();
                movimientoSeleccionado[1] = entry.getValue();
                return movimientoSeleccionado;
            }
        }
        return null;
    }

    // --- Metodos getters y setters ---

    public void setFoundationDecks(FoundationDeck[] foundationDecks) { this.foundationDecks = foundationDecks; }
    public void setTableauDecks(TableauDeck[] tableauDecks) { this.tableauDecks = tableauDecks; }
    public void setWasteZones(WasteZone[] wasteZones) { this.wasteZones = wasteZones; }
    public void setCartasGraficas(ArrayList<CartaInglesa> cartasGraficas) { this.cartasGraficas = cartasGraficas; }
    public ArrayList<CartaInglesa> getCartasGraficas() { return cartasGraficas; }
    public FoundationDeck[] getFoundationDecks() { return foundationDecks; }
    public TableauDeck[] getTableauDecks() { return tableauDecks; }
    public WasteZone[] getWasteZones() { return wasteZones; }
    public void setMovimientosDisponibles(HashMap<CartaInglesa, CartaInglesa> movimientosDisponibles) { this.movimientosDisponibles = movimientosDisponibles; }
    public HashMap<CartaInglesa, CartaInglesa> getMovimientosDisponibles() { return movimientosDisponibles; }

    /**
     * Metodo de prueba: crea un tablero y muestra las cartas graficas cargadas.
     */
    public static void main(String[] args) {
        TableroLogico tableroLogico = new TableroLogico();
        System.out.println(tableroLogico.cartasGraficas);
    }
}

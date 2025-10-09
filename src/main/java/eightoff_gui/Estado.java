package eightoff_gui;

import DeckOfCards.CartaInglesa;
import eightoff_logica.FoundationDeck;
import eightoff_logica.TableauDeck;
import eightoff_logica.WasteZone;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Estado {
    // Atributos que guardan índices y estados
    private ArrayList<ArrayList<Integer>> tableaus;
    private ArrayList<ArrayList<Integer>> foundations;
    private ArrayList<ArrayList<Integer>> wasteZones;
    private ArrayList<Boolean> cartasVisibles;

    // Constructor que captura el estado actual del juego
    //Se ingresa el tableau, los foundation, las pilas y las cartas graficas
    //para obtener el indice y visibilidad de todas las cartas.

    /*
    public Estado(TableauDeck[] tableaus, FoundationDeck[] foundations,
                  WasteZone[] wasteZones, ArrayList<CartaInglesa> cartasGraficas) {

        // inicializo las listas que copiaran los elementos
        this.tableaus = new ArrayList<>();
        this.foundations = new ArrayList<>();
        this.wasteZones = new ArrayList<>();
        this.cartasVisibles = new ArrayList<>();

        // Saco las cartas por cada tableau y guardo su respectivo indice asociado
        // al mazo general de cartas graficas, cada tableau tiene su arreglo de indices
        for (TableauDeck t:tableaus) {
            ArrayList<Integer> indicesTableau = new ArrayList<>();
            for (Node node : t.getChildren()) {
                if (node instanceof StackPane) {
                    StackPane carta = (StackPane) node;
                    int indice = cartasGraficas.indexOf(carta);
                    if (indice != -1) {
                        indicesTableau.add(indice);
                    }
                }
            }
            this.tableaus.add(indicesTableau);
        }

        // Saco las cartas por cada foundation y guardo su respectivo indice asociado
        // al mazo general de cartas graficas, cada foundation tiene su arreglo de indices
        for (VBox foundation : foundations) {
            ArrayList<Integer> indicesFoundation = new ArrayList<>();
            for (Node node : foundation.getChildren()) {
                if (node instanceof StackPane) {
                    StackPane carta = (StackPane) node;
                    int indice = cartasGraficas.indexOf(carta);
                    if (indice != -1) {
                        indicesFoundation.add(indice);
                    }
                }
            }
            this.foundations.add(indicesFoundation);
        }

        // Capturo los indices de los elementos de la pila
        // (Se necesitara ingresar de forma inversa para tener un orden correcto)
        //capturarPila(pilaPozo, this.pilaPozoIndices, cartasGraficas);
        //capturarPila(pilaDescarte, this.pilaDescarteIndices, cartasGraficas);

        // Capturar visibilidad de todas las cartas
        for (StackPane carta : cartasGraficas) {
            StackPane visibleCard = (StackPane) carta.lookup("#VisibleCard");
            //Agrega false si no es visible, true si es visible
            this.cartasVisibles.add(visibleCard != null && visibleCard.isVisible());
        }
    }

    /*Captura los indices de las pilas
    private void capturarPila(Pila<StackPane> pilaOriginal, ArrayList<Integer> destino,
                              ArrayList<StackPane> cartasGraficas) {
        if (pilaOriginal.isEmpty()) {
            return;
        }

        // Crear pila temporal para no modificar la original
        Pila<StackPane> pilaTemp = new Pila<>(pilaOriginal.getCapacidad());
        ArrayList<Integer> indicesTemp = new ArrayList<>();

        // Sacar los elementos de la  pila original y guardarlo temporalmente en un arreglo
        while (!pilaOriginal.isEmpty()) {
            StackPane carta = pilaOriginal.pop();
            int indice = cartasGraficas.indexOf(carta);
            if (indice != -1) {
                indicesTemp.add(indice);
            }
            pilaTemp.push(carta);
        }

        // Restaurar la pila original
        while (!pilaTemp.isEmpty()) {
            pilaOriginal.push(pilaTemp.pop());
        }

        //CUIDADDOODODOD estan en orden inverso
        destino.addAll(indicesTemp);
    }

     */


    // Getters y Setters
    public ArrayList<ArrayList<Integer>> getTableaus() {
        return tableaus;
    }


    public ArrayList<ArrayList<Integer>> getFoundations() {
        return foundations;
    }



    public ArrayList<Boolean> getCartasVisibles() {
        return cartasVisibles;
    }

}
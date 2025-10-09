package DeckOfCards;
/**
 * Write a description of class Mazo here.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
import lista_circular_doble.ListaCircularDoble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//Implementar ListaDobleCircular
public class Mazo {
    private ListaCircularDoble<CartaInglesa> cartas = new ListaCircularDoble<>();

    public Mazo() {
        llenar(); // crea todas las cartas, excluyendo Jokers
        mezclar();
    }

    /**
     * Obtiene todas las cartas del mazo.
     * @return
     */
    public ListaCircularDoble<CartaInglesa> getCartas() {
        return cartas;
    }

    public CartaInglesa obtenerUnaCarta() {
        if (cartas.size() > 0) {
            return cartas.eliminaInicio();
        }
        return null;
    }
    private void mezclar() {
        Random rand = new Random();
        int total = cartas.size(); // Guardamos el tamaño antes de eliminar
        CartaInglesa[] aux = new CartaInglesa[total];

        // Extrae todas las cartas del mazo y las guarda en el arreglo
        for (int i = 0; i < total; i++) {
            CartaInglesa c = cartas.eliminaInicio();
            aux[i] = c;
        }

        // Inserta las cartas de nuevo en orden aleatorio
        for (int i = 0; i < aux.length; i++) {
            int numRandom = rand.nextInt(aux.length);
            if (aux[numRandom] != null) {
                cartas.insertaInicio(aux[numRandom]);
                aux[numRandom] = null;
            } else {
                i--; // Repite si ya se uso esa posicion
            }
        }
    }


    private void llenar() {
        for (int i = 1; i <=13 ; i++) {
            for (Palo palo : Palo.values()) {
                CartaInglesa c = new CartaInglesa(i,palo, palo.getColor());
                cartas.insertaFin(c);
            }
        }
    }

    public void ordenar() {
        ListaCircularDoble<CartaInglesa> ref = new ListaCircularDoble<>(cartas);
        for(int i = 0; i < cartas.size(); i++) {
            return;
        }
    }

    @Override
    public String toString() {
        return cartas.toString();
    }
}

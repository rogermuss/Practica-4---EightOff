package DeckOfCards;
/**
 * Write a description of class CartaInglesa here.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class CartaInglesa extends Carta {

    public CartaInglesa(int valor, Palo figura, String color) {
        super(valor, figura, color);
    }
    public CartaInglesa(CartaInglesa original) {
        super(original);
    }

    @Override
    public int compareTo(Carta o) {
        if(getColor().equals(o.getColor())) {
            if(getPalo().equals(o.getPalo())) {
                if(getValor() == o.getValor()) {
                    return 0;
                }
                else {
                    return getValor() - o.getValor();
                }
            }
        }
        return Integer.MIN_VALUE;
    }
}

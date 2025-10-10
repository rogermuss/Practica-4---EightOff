package DeckOfCards;

/**
 * Write a description of class Carta here.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public abstract class Carta implements Comparable<Carta> {
    private int valor;
    protected Palo palo;
    private String color;
    private int valorBajo;
    private boolean faceup;
    public enum ValorEnum {
        AS,
        DOS,
        TRES,
        CUATRO,
        CINCO,
        SEIS,
        SIETE,
        OCHO,
        NUEVE,
        DIEZ,
        J,
        Q,
        K
    }

    ValorEnum valorEnum;

    /**
     * Crea una carta que no se ve su contenido.
     * @param valor
     * @param palo
     * @param color
     */
    public Carta(int valor, Palo palo, String color) {

        this.valor = valor;
        setValorEnum(valor);
        this.palo = palo;
        this.color = color;
        if (valor == 14) {
            valorBajo = 1;
        } else {
            valorBajo = valor;
        }

        faceup = false;
    }

    public Carta(Carta original) {
        this.valorEnum = original.getValorEnum();
        this.palo = original.getPalo();
        this.color = original.getColor();
        this.faceup = original.isFaceup();
        this.valor = original.getValor();
        this.valorBajo = original.getValorBajo();

        // Copiar cualquier otro atributo necesario
    }

    /**
     * Voltea una carta para que NO se vea su contenido.
     */
    public void makeFaceDown() {
        faceup = false;
    }
    /**
     * Voltea una carta para que se vea su contenido.
     */
    public void makeFaceUp() {
        faceup = true;
    }
    /**
     * Indica si se ve la cara de la carta
     * @return true si se ve, false si está volteada
     */
    public boolean isFaceup() {
        return faceup;
    }
    public String toString() {
        return switch (valor) {
            case 1 -> "A" + palo.getFigura();
            case 11 -> "J"+ palo.getFigura();
            case 12 -> "Q" + palo.getFigura();
            case 13 -> "K" + palo.getFigura();
            case 15-> "Joker";
            default -> ""+ valor + palo.getFigura();
        };
    }

    public boolean tieneElMismoValor(Carta carta) {
        return valor == carta.valor;
    }
    public boolean tieneElMismoPalo(Palo palo) {
        return this.palo.equals(palo);
    }
    public boolean esLaSiguiente(Carta carta) {
        if (valor+1 == carta.valor) {
            return true;
        }
        // verificar si es un As y un 2
        if (valor == 14 && carta.valor == 2) {
            return true;
        }
        return false;
    }


    public void setValorEnum(int valor) {
        switch (valor) {
            case 1 -> valorEnum = ValorEnum.AS;
            case 2 -> valorEnum = ValorEnum.DOS;
            case 3 -> valorEnum = ValorEnum.TRES;
            case 4 -> valorEnum = ValorEnum.CUATRO;
            case 5 -> valorEnum = ValorEnum.CINCO;
            case 6 -> valorEnum = ValorEnum.SEIS;
            case 7 -> valorEnum = ValorEnum.SIETE;
            case 8 -> valorEnum = ValorEnum.OCHO;
            case 9 -> valorEnum = ValorEnum.NUEVE;
            case 10 -> valorEnum = ValorEnum.DIEZ;
            case 11 -> valorEnum = ValorEnum.J;
            case 12 -> valorEnum = ValorEnum.Q;
            case 13 -> valorEnum = ValorEnum.K;
        }
    }

    public ValorEnum getValorEnum() {
        return valorEnum;
    }

    public int getValor() {
        return valor;
    }
    public Palo getPalo() {
        return palo;
    }
    public String getColor() {
        return color;
    }
    public int getValorBajo() {
        return valorBajo;
    }

    public void setValorEnum(ValorEnum valorEnum) {
        this.valorEnum = valorEnum;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFaceup(boolean faceup) {
        this.faceup = faceup;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public void setPalo(Palo palo) {
        this.palo = palo;
    }

    public void setValorBajo(int valorBajo) {
        this.valorBajo = valorBajo;
    }
}

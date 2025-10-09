package lista_circular_doble;

import java.util.List;

public class ListaCircularDoble<T> {
    private NodoDoble<T> inicio, fin;

    public ListaCircularDoble() {
        inicio = null;
        fin = null;
    }

    public ListaCircularDoble(ListaCircularDoble<T> l) {
        if (l.inicio == null) {
            inicio = fin = null;
            return;
        }

        NodoDoble<T> n = l.inicio;
        do {
            this.insertaFin(n.getData()); // cada nodo nuevo
            n = n.getSiguiente();
        } while (n != l.inicio);
    }

    public boolean isEmpty(){
        return inicio == null;
    }


    public void insertaInicio(T data) {
        NodoDoble<T> n = new NodoDoble<>(data);
        if (inicio == null) {
            inicio = fin = n;
            n.setSiguiente(inicio);
            n.setAnterior(inicio);
        } else {
            n.setSiguiente(inicio);
            n.setAnterior(fin);
            inicio.setAnterior(n);
            fin.setSiguiente(n);
            inicio = n;
        }
    }

    public void insertaFin(T data) {
        NodoDoble<T> n = new NodoDoble<>(data);
        if (inicio == null) {
            inicio = fin = n;
            n.setSiguiente(inicio);
            n.setAnterior(inicio);
        } else {
            n.setSiguiente(inicio);
            n.setAnterior(fin);
            fin.setSiguiente(n);
            inicio.setAnterior(n);
            fin = n;
        }
    }

    public T eliminaInicio() {
        if (inicio == null) {
            return null;
        }
        T data = inicio.getData();

        if (inicio == fin) {
            inicio = fin = null;
        } else {
            fin.setSiguiente(inicio.getSiguiente());
            inicio = inicio.getSiguiente();
            inicio.setAnterior(fin);
        }
        return data;
    }

    public T eliminaFin() {
        if (inicio == null) {
            return null;
        }
        T data = fin.getData();

        if (inicio == fin) {
            inicio = fin = null;
        } else {
            NodoDoble<T> r = fin.getAnterior();
            r.setSiguiente(inicio);
            inicio.setAnterior(r);
            fin = r;
        }
        return data;
    }

    public int size(){
        if (inicio == null) {
            return 0;
        }
        int size = 1;
        NodoDoble<T> n = inicio;
        while(n != fin) {
            n = n.getSiguiente();
            size++;
        }
        return size;
    }


}

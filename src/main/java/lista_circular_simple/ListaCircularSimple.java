package lista_circular_simple;

public class ListaCircularSimple<T> {
    Nodo<T> inicio;
    Nodo<T> fin;

    public ListaCircularSimple() {
        inicio = null;
        fin = null;
    }

    public void insertaInicio(T dato) {
        Nodo<T> n = new Nodo<>(dato, null);
        if (inicio == null) {
            inicio = n;
            fin = n;
            n.setSiguiente(n); // apunta a sí mismo
        } else {
            n.setSiguiente(inicio);
            fin.setSiguiente(n);
            inicio = n;
        }
    }

    public void insertaFin(T dato) {
        Nodo<T> n = new Nodo<>(dato, null);
        if (inicio == null) {
            inicio = n;
            fin = n;
            n.setSiguiente(n);
        } else {
            fin.setSiguiente(n);
            n.setSiguiente(inicio);
            fin = n;
        }
    }

    public T eliminaInicio() {
        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        }

        T t = inicio.getInfo();

        if (inicio == fin) { // solo un nodo
            inicio = null;
            fin = null;
        } else {
            inicio = inicio.getSiguiente();
            fin.setSiguiente(inicio);
        }

        return t;
    }

    public T eliminaFin() {
        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        }

        T t = fin.getInfo();

        if (inicio == fin) { // un solo nodo
            inicio = null;
            fin = null;
        } else {
            Nodo<T> r = inicio;
            while (r.getSiguiente() != fin) {
                r = r.getSiguiente();
            }
            fin = r;
            fin.setSiguiente(inicio);
        }

        return t;
    }

    public void mostrar() {
        if (inicio == null) {
            System.out.println("Lista vacia");
            return;
        }

        Nodo<T> r = inicio;
        do {
            System.out.print(r.getInfo() + " -> ");
            r = r.getSiguiente();
        } while (r != inicio);
        System.out.println("(inicio)");
    }
}

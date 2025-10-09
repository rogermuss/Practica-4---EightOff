package lista_circular_simple;

public class ListaCircularSimple<T> {
    Nodo<T> inicio;
    Nodo<T> fin;

    public ListaCircularSimple() {
        inicio = null;
        fin = null;
    }
    public ListaCircularSimple(ListaCircularSimple<T> copia) {
        this.inicio = null;
        this.fin = null;

        if (copia.inicio != null) {
            Nodo<T> actual = copia.inicio;
            do {
                this.insertaFin(actual.getInfo()); // copia el valor, no el nodo
                actual = actual.getSiguiente();
            } while (actual != copia.inicio);
        }
    }

    public int size(){
        if(inicio == null){
            return 0;
        }
        else{
            int size = 0;
            Nodo<T> r = inicio;
            do {
                r = r.getSiguiente();
                size++;
            } while (r != inicio);
            return size;
        }
    }

    public boolean isEmpty(){
        return inicio == null;
    }

    public T get(int index){
        if(isEmpty()){
            return null;
        }
        else{
            Nodo<T> r = inicio;
            for(int i = 0; i < index; i++){
                r = r.getSiguiente();
            }
            return r.getInfo();
        }
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

        T data = inicio.getInfo();

        if (inicio == fin) { // solo un nodo
            inicio = null;
            fin = null;
        } else {
            inicio = inicio.getSiguiente();
            fin.setSiguiente(inicio);
        }

        return data;
    }

    public T eliminaFin() {
        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        }

        T data = fin.getInfo();

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

        return data;
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

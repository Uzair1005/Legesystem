import java.util.NoSuchElementException;
import java.util.Iterator;

interface Liste<E> extends Iterable<E> {
    int stoerrelse();
    void leggTil(E x);
    E hent();
    E fjern();
}

public class Lenkeliste<E> implements Liste<E> {
    protected Node foran;
    protected Node bak;
    protected int antall;
    
    protected class Node {
        public E data;
        public Node neste;
        public Node(E data) {
            this.data = data;
        }
    }
    
    public int stoerrelse() {
        return antall;
    }
    public void leggTil(E x) {
        Node ny = new Node(x);
        if (bak == null) {
            foran = ny;
            bak = ny;
        } else {
            bak.neste = ny;
            bak = ny;
        }
        antall++;
    }
    public E hent() {
        if (foran == null) {
            throw new NoSuchElementException();
       }
        return foran.data;
    }
    public E fjern() {
       if (foran == null) {
            throw new NoSuchElementException();
        }
        E resultat = foran.data;
        foran = foran.neste;
        if (foran == null) {
            bak = null;
        }
        antall--;
        return resultat;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node gjeldende = foran;
        while (gjeldende != null) {
            sb.append(gjeldende.data.toString());
            gjeldende = gjeldende.neste;
            if (gjeldende != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    class LenkelisteIterator implements Iterator<E> {
        private Node node = foran;
        public boolean hasNext() {
            return node != null;
        }
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E data = node.data;
            node = node.neste;
            return data;
        }
    }
    
    public Iterator<E> iterator() {
        return new LenkelisteIterator();
    }
    
    

}






// Exceção
class BoundaryViolationException extends RuntimeException {
    public BoundaryViolationException(String erro) {
        super(erro);
    }
}

// Interface
interface Vetor {
    int size();
    boolean isEmpty();
    Object elemAtRank(int r);
    Object replaceAtRank(int r, Object o);
    void insertAtRank(int r, Object o);
    Object removeAtRank(int r);
}

// Implementação com array
class ArrayVetor implements Vetor {
    private Object[] A;
    private int capacity;
    private int size;

    public ArrayVetor(int capacity) {
        this.capacity = capacity;
        this.A = new Object[capacity];
        this.size = 0;
    }

    public ArrayVetor() {
        this(10);
    }

    private void conferirIndice(int r, int n) { // Método auxiliar
        if (r < 0 || r >= n) {
            throw new BoundaryViolationException("Rank inválido");
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object elemAtRank(int r) {
        conferirIndice(r, size);
        return A[r];
    }

    @Override
    public Object replaceAtRank(int r, Object o) {
        conferirIndice(r, size);
        Object temp = A[r];
        A[r] = o;
        return temp;
    }

    @Override
    public void insertAtRank(int r, Object o) {
        conferirIndice(r, size + 1);

        // Aumento de capacidade
        if (size == capacity) {
            capacity *= 2;
            Object[] B = new Object[capacity];
            for (int i = 0; i < size; i++) {
                B[i] = A[i];
            }
            A = B;
        }

        // Deslocamento
        for (int i = size - 1; i >= r; i--) {
            A[i + 1] = A[i];
        }

        A[r] = o;
        size++;
    }

    @Override
    public Object removeAtRank(int r) {
        conferirIndice(r, size);
        Object temp = A[r];

        // Deslocamento
        for (int i = r; i < size - 1; i++) {
            A[i] = A[i + 1];
        }

        A[size - 1] = null;
        size--;
        return temp;
    }
}

// Implementação com lista duplamente ligada
class DuplamenteLigada implements Vetor {

    private static class No {
        Object elemento;
        No anterior;
        No proximo;

        public No(Object elemento, No anterior, No proximo) {
            this.elemento = elemento;
            this.anterior = anterior;
            this.proximo = proximo;
        }
    }

    private No inicio;
    private No fim;
    private int size;

    public DuplamenteLigada() {
        size = 0;
        inicio = new No(null, null, null);
        fim = new No(null, inicio, null);
        inicio.proximo = fim;
    }

    private void conferirIndice(int r, int n) {
        if (r < 0 || r >= n) {
            throw new BoundaryViolationException("Rank inválido");
        }
    }

    private No atRank(int rank) {
        No node;
        if (rank <= size / 2) {
            node = inicio.proximo;
            for (int i = 0; i < rank; i++) {
                node = node.proximo;
            }
        } else {
            node = fim.anterior;
            for (int i = 0; i < size - rank - 1; i++) {
                node = node.anterior;
            }
        }
        return node;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object elemAtRank(int r) {
        conferirIndice(r, size);
        return atRank(r).elemento;
    }

    @Override
    public Object replaceAtRank(int r, Object o) {
        conferirIndice(r, size);
        No node = atRank(r);
        Object temp = node.elemento;
        node.elemento = o;
        return temp;
    }

    @Override
    public void insertAtRank(int r, Object o) {
        conferirIndice(r, size + 1);

        No p;
        if (r == 0) {
            p = inicio;
        } else if (r == size) {
            p = fim.anterior;
        } else {
            p = atRank(r).anterior;
        }

        No q = new No(o, p, p.proximo);
        p.proximo.anterior = q;
        p.proximo = q;

        size++;
    }

    @Override
    public Object removeAtRank(int r) {
        conferirIndice(r, size);
        No node = atRank(r);

        Object temp = node.elemento;

        node.anterior.proximo = node.proximo;
        node.proximo.anterior = node.anterior;

        node.anterior = null;
        node.proximo = null;

        size--;
        return temp;
    }
}

// Main
public class Main {
    public static void main(String[] args) {
        // Teste array
        Vetor v1 = new ArrayVetor(3);
        teste(v1);

        // Teste lista duplamente ligada
        Vetor v2 = new DuplamenteLigada();
        teste(v2);
    }

    // Método auxiliar para imprimir
    private static void imprimirVetor(Vetor v) {
        System.out.print("[");
        for (int i = 0; i < v.size(); i++) {
            System.out.print(v.elemAtRank(i));
            if (i < v.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    private static void teste(Vetor v) {
        System.out.println("Está vazio? " + v.isEmpty());

        // Inserções
        v.insertAtRank(0, "A");
        v.insertAtRank(1, "C");
        v.insertAtRank(1, "B");
        v.insertAtRank(3, "D");

        System.out.print("Após inserções: ");
        imprimirVetor(v);
        System.out.println("Tamanho: " + v.size());

        // Acesso
        System.out.println("Elemento no rank 2: " + v.elemAtRank(2));

        // Substituição
        Object antigo = v.replaceAtRank(2, "X");
        System.out.print("Substituído " + antigo + " por X: ");
        imprimirVetor(v);

        // Remoção
        Object removido = v.removeAtRank(1);
        System.out.print("Removido no rank 1: " + removido + " -> ");
        imprimirVetor(v);

        // Teste de exceção
        try {
            v.elemAtRank(10);
        } catch (BoundaryViolationException e) {
            System.out.println("Exceção capturada com sucesso: " + e.getMessage());
        }
    }
}

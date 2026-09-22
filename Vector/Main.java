// Implementa TAD Vector array e lista duplamente encadeada

// Em vetor array, inserção/remoção no início ou meio é O(N), pois precisa deslocar os elementos.
// Em lista duplamente encadeada a inserção nas extremidades é O(1) (usa os nós sentinel diretamente); remoção só é O(1) no início, pois remover 
// no fim ainda exige percorrer a lista até o penúltimo nó (O(N)). Acesso, substituição e inserção/remoção no meio são O(N).

// Interface
interface Vetor {
    int size();
    boolean isEmpty();
    Object elemAtRank(int r);
    Object replaceAtRank(int r, Object o);
    void insertAtRank(int r, Object o);
    Object removeAtRank(int r);
}

// Implementação com Array

class VetorArray implements Vetor {

    private Object[] array;
    private int capacidade;
    private int size;

    public VetorArray(int capacidade) {
        this.capacidade = capacidade;
        this.array = new Object[capacidade];
        this.size = 0;
    }

    public VetorArray() {
        this(10);
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
        if (r < 0 || r >= size) { // Deve estar dentro do intervalo (o até size -1)
            throw new IndexOutOfBoundsException("Informe uma colocação válida.");
        }
        return array[r];
    }

    @Override
    public Object replaceAtRank(int r, Object o) {
        if (r < 0 || r >= size) {
            throw new IndexOutOfBoundsException("Informe uma colocação válida.");
        }
        Object aux = array[r];
        array[r] = o;
        return aux;
    }

    @Override
    public void insertAtRank(int r, Object o) {
        if (r < 0 || r > size) { // O intervalo é diferente, pois pode inserir uma posição depois do último elemento, ao fim do vetor
            throw new IndexOutOfBoundsException("Informe uma colocação válida.");
        }

        if (size == capacidade) { // Redimensionamento
            capacidade *= 2;
            Object[] novoArray = new Object[capacidade];
            for (int i = 0; i < size; i++) {
                novoArray[i] = array[i];
            }
            array = novoArray;
        }

        for (int i = size; i > r; i--) { // Deslocamento para a direita
            array[i] = array[i - 1];
        }
        array[r] = o;
        size++;
    }

    @Override
    public Object removeAtRank(int r) {
        if (r < 0 || r >= size) {
            throw new IndexOutOfBoundsException("Informe uma colocação válida.");
        }
        Object aux = array[r]; // Guarda o elemento no rank r

        for (int i = r; i < size - 1; i++) { // Deslocamento para a esquerda
            array[i] = array[i + 1];
        }
        array[size - 1] = null; // libera a referência do último

        size--;
        return aux;
    }

    public void print() {
        System.out.print("(");
        for (int i = 0; i < size; i++) {
            System.out.print(array[i]);
            if (i < size - 1) System.out.print(", ");
        }
        System.out.println(")");
    }
}

// Implementação com Lista Duplamente Encadeada

class VetorListaDuplamenteEncadeada implements Vetor {

    private static class No { // Classe de criação do nó
        Object elemento;
        No prev, next;
        No(Object elemento) {
            this.elemento = elemento;
        }
    }

    private No inicio; // sentinela de início
    private No fim;  // sentinela de fim
    private int size;

    public VetorListaDuplamenteEncadeada() {
        inicio = new No(null);
        fim = new No(null);
        // Sentinelas ligadas desde o começo
        inicio.next = fim;
        fim.prev = inicio;
        size = 0;
    }

    // Retorna o nó atualmente no rank r (r deve estar em [0, size))
    private No noAtRank(int r) {
        No atual = inicio.next;
        for (int i = 0; i < r; i++) {
            atual = atual.next;
        }
        return atual;
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
        if (r < 0 || r >= size) {
            throw new IndexOutOfBoundsException("Informe uma colocação válida.");
        }
        return noAtRank(r).elemento;
    }

    @Override
    public Object replaceAtRank(int r, Object o) {
        if (r < 0 || r >= size) {
            throw new IndexOutOfBoundsException("Informe uma colocação válida.");
        }
        No no = noAtRank(r);
        Object aux = no.elemento;
        no.elemento = o;
        return aux;
    }

    @Override
    public void insertAtRank(int r, Object o) {
        // Intervalo válido: [0, size], pois o nó pode vir depois do último
        if (r < 0 || r > size) {
            throw new IndexOutOfBoundsException("Informe uma colocação válida.");
        }

        // Vizinhos que vão ficar antes e depois do novo nó
        No sucessor;
        if (r == size) {
            sucessor = fim;
        } else {
            sucessor = noAtRank(r);
        }
        
        No predecessor = sucessor.prev;

        No novo = new No(o);
        novo.prev = predecessor;
        novo.next = sucessor;
        predecessor.next = novo;
        sucessor.prev = novo;

        size++;
    }

    @Override
    public Object removeAtRank(int r) {
        if (r < 0 || r >= size) {
            throw new IndexOutOfBoundsException("Informe uma colocação válida.");
        }
        No no = noAtRank(r);
        Object aux = no.elemento;

        // Desreferência o nó ligando o anterior direto com o próximo (tira o que quero remover da cadeia)
        no.prev.next = no.next;
        no.next.prev = no.prev;
        no.prev = null;
        no.next = null;

        size--;
        return aux;
    }


    public void print() {
        System.out.print("(");
        No atual = inicio.next;
        while (atual != fim) {
            System.out.print(atual.elemento);
            if (atual.next != fim) System.out.print(", ");
            atual = atual.next;
        }
        System.out.println(")");
    }
}


// Testes
    public class Main {
        public static void main(String[] args) {
            System.out.println("Teste VetorArray");
            teste(new VetorArray(3));

            System.out.println("\nTeste VetorListaDuplamenteEncadeada");
            teste(new VetorListaDuplamenteEncadeada());
        }

        private static void teste(Vetor v) {
            System.out.println("Vazio? " + v.isEmpty());

            v.insertAtRank(0, "A");
            v.insertAtRank(1, "C");
            v.insertAtRank(1, "B"); // insere no meio
            v.insertAtRank(3, "D"); // insere no fim (r == size)

            printVetor(v, "Após inserções");
            System.out.println("Tamanho: " + v.size());

            System.out.println("Elemento no rank 2: " + v.elemAtRank(2));

            Object antigo = v.replaceAtRank(2, "X");
            printVetor(v, "Substituído " + antigo + " por X");

            Object removido = v.removeAtRank(1);
            printVetor(v, "Removido no rank 1 (" + removido + ")");

            try {
                v.elemAtRank(10);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Exceção capturada corretamente: " + e.getMessage());
            }
        }

        private static void printVetor(Vetor v, String rotulo) {
            System.out.print(rotulo + ": [");
            for (int i = 0; i < v.size(); i++) {
                System.out.print(v.elemAtRank(i));
                if (i < v.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
    }

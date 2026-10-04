interface Sequencia {
    // Métodos de vetor (acesso por índice)
    Object elemAtRank(int r);
    Object replaceAtRank(int r, Object o);
    void insertAtRank(int r, Object o);
    Object removeAtRank(int r);

    // Métodos de lista (acesso por nó)
    boolean isFirst(No n);
    boolean isLast(No n);
    No first();
    No last();
    No before(No n);
    No after(No n);
    Object replaceElement(No n, Object o);
    void swapElements(No n, No m);
    void insertBefore(No n, Object o);
    void insertAfter(No n, Object o);
    void insertFirst(Object o);
    void insertLast(Object o);
    void remove(No n);

    // Métodos ponte (faz a conversão de uma visão para a outra)
    No atRank(int rank); // do índice para o nó
    int rankOf(No n);    // do nó para o índice

    // Métodos de sequência
    void print();
    int size();
    boolean isEmpty();
}

// TAD Posição
class No {
    public Object elemento;
    protected No next, prev; // usado pela lista duplamente ligada
    protected int rank;       // usado pelo array

    // Construtor usado pela lista duplamente encadeada
    public No(Object elemento) {
        this.elemento = elemento;
        this.next = null;
        this.prev = null;
    }

    // Construtor usado pelo array
    public No(Object elemento, int rank) {
        this.elemento = elemento;
        this.rank = rank;
    }
}

// Exceção
class SequenciaExcecao extends RuntimeException {
    public SequenciaExcecao(String mensagem) {
        super(mensagem);
    }
}

// Implementação Sequência com lista duplamente encadeada
class SequenciaDuplamente implements Sequencia {
    private int size;
    private No first, last;

    public SequenciaDuplamente() {
        this.first = new No(null);
        this.last = new No(null);
        first.next = last;
        last.prev = first;
        this.size = 0;
    }

    // Métodos do vetor
    // Retorna o elemento na colocação r
    public Object elemAtRank(int r) {
        if (r < 0 || r >= size) {
            throw new SequenciaExcecao("Posição informada é inválida: " + r);
        }
        No atual = first.next; // Primeiro nó
        for (int i = 0; i < r; i++) { // Caminha até achar o atual
            atual = atual.next;
        }
        return atual.elemento;
    }

    // Substitui o elemento na colocação r por o e retorna o antigo elemento
    public Object replaceAtRank(int r, Object o) {
        if (r < 0 || r >= size) {
            throw new SequenciaExcecao("Posição informada é inválida: " + r);
        }
        No atual = first.next;
        for (int i = 0; i < r; i++) {
            atual = atual.next;
        }
        Object aux = atual.elemento; // Nó antigo
        atual.elemento = o;
        return aux;
    }

    // Insere um novo elemento na colocação r
    public void insertAtRank(int r, Object o) {
        No novoNo = new No(o);
        No antigoNo = first.next;
        if (r < 0) {
            throw new SequenciaExcecao("Posição informada é inválida: " + r);
        }
        if (r > size) { // r == size é permitido, inserindo depois do último
            throw new SequenciaExcecao("Não é possível inserir um elemento numa posição que é maior que o número de elementos atual da lista");
        }
        if (isEmpty()) { // Lista vazia
            first.next = novoNo;
            last.prev = novoNo;
            novoNo.prev = first;
            novoNo.next = last;
        } else if (r == 0) { // Inserir na posição 0 (entrando antes do antigoNo)
            novoNo.next = antigoNo;
            novoNo.prev = first;
            antigoNo.prev = novoNo;
            first.next = novoNo;
        } else { // Inserir no meio ou no fim (entrando também antes do antigoNo)
            for (int i = 0; i < r; i++) {
                antigoNo = antigoNo.next;
            }
            novoNo.prev = antigoNo.prev;
            novoNo.next = antigoNo;
            antigoNo.prev.next = novoNo;
            antigoNo.prev = novoNo;
        }
        size++;
    }

    // Remove e retorna o elemento na posição r
    public Object removeAtRank(int r) {
        No antigoNo = first.next;
        Object elemento;
        if (isEmpty()) {
            throw new SequenciaExcecao("Vetor já está vazio");
        }
        if (r < 0) {
            throw new SequenciaExcecao("Posição informada é inválida: " + r);
        }
        if (r >= size) {
            throw new SequenciaExcecao("Não é possível remover um elemento numa posição que é maior que o número de elementos atual da lista");
        }
        if (r == 0) { // Remover o primeiro elemento
            elemento = antigoNo.elemento;
            antigoNo.next.prev = first;
            first.next = antigoNo.next;
        } else { // Remover do meio ou do fim
            for (int i = 0; i < r; i++) {
                antigoNo = antigoNo.next;
            }
            antigoNo.prev.next = antigoNo.next;
            antigoNo.next.prev = antigoNo.prev;
            elemento = antigoNo.elemento;
        }
        size--;
        return elemento;
    }

    // Métodos de lista
    // Verifica se o nó passado é o primeiro
    public boolean isFirst(No n) {
        return n == first.next;
    }

    // Verifica se o nó é o último
    public boolean isLast(No n) {
        return n == last.prev;
    }

    // Retorna o primeiro elemento
    public No first() {
        if (isEmpty()) {
            throw new SequenciaExcecao("A sequência está vazia");
        }
        return first.next;
    }

    // Retorna o último elemento
    public No last() {
        if (isEmpty()) {
            throw new SequenciaExcecao("A sequência está vazia");
        }
        return last.prev;
    }

    // Retorna o nó antes do nó passado
    public No before(No n) {
        if (isFirst(n)) {
            throw new SequenciaExcecao("Não existe posição anterior ao primeiro elemento");
        }
        return n.prev;
    }

    // Retorna o nó depois do nó passado
    public No after(No n) {
        if (isLast(n)) {
            throw new SequenciaExcecao("Não existe posição posterior ao último elemento");
        }
        return n.next;
    }

    // Substitui o elemento do nó pelo objeto o
    public Object replaceElement(No n, Object o) {
        Object aux = n.elemento;
        n.elemento = o;
        return aux;
    }

    // Troca os elementos internos dos nós n e m
    public void swapElements(No n, No m) {
        Object aux = n.elemento;
        n.elemento = m.elemento;
        m.elemento = aux;
    }

    // Insere o nó antes do nó informado
    public void insertBefore(No n, Object o) {
        No novoNo = new No(o);
        n.prev.next = novoNo;
        novoNo.prev = n.prev;
        n.prev = novoNo;
        novoNo.next = n;
        size++;
    }

    // Insere o nó depois do nó informado
    public void insertAfter(No n, Object o) {
        No novoNo = new No(o);
        n.next.prev = novoNo;
        novoNo.next = n.next;
        n.next = novoNo;
        novoNo.prev = n;
        size++;
    }

    // Insere o nó no início da lista
    public void insertFirst(Object o) {
        No novoNo = new No(o);
        if (isEmpty()) {
            first.next = novoNo;
            last.prev = novoNo;
            novoNo.prev = first;
            novoNo.next = last;
        } else {
            novoNo.next = first.next;
            novoNo.prev = first;
            first.next.prev = novoNo;
            first.next = novoNo;
        }
        size++;
    }

    // Insere o nó no final da lista
    public void insertLast(Object o) {
        No novoNo = new No(o);
        if (isEmpty()) {
            last.prev = novoNo;
            first.next = novoNo;
            novoNo.prev = first;
            novoNo.next = last;
        } else {
            novoNo.next = last;
            novoNo.prev = last.prev;
            last.prev.next = novoNo;
            last.prev = novoNo;
        }
        size++;
    }

    // Remove o nó passado
    public void remove(No n) {
        n.next.prev = n.prev;
        n.prev.next = n.next;
        n.next = null; // o nó removido não mantém mais referências para a lista
        n.prev = null;
        size--;
    }

    // Métodos ponte
    // Acessa um nó em uma sequência com base em sua posição
    public No atRank(int r) {
        if (r < 0 || r >= size)
            throw new SequenciaExcecao("Posição informada é inválida: " + r);
        No atual;
        if (r <= size / 2) { // Se r está na metade da frente, começa a partir de first
            atual = first.next;
            for (int i = 0; i < r; i++) {
                atual = atual.next;
            }
        } else { // Se r está na metade de trás, anda a partir de last
            atual = last.prev;
            for (int i = 0; i < size - r - 1; i++) {
                atual = atual.prev;
            }
        }
        return atual;
    }

    // Determina a posição de um nó específico na sequência
    public int rankOf(No n) {
        No atual = first.next;
        int r = 0;
        while (atual != last) {
            if (atual == n) return r;
            atual = atual.next;
            r++;
        }
        throw new SequenciaExcecao("Nó não encontrado.");
    }

    // Métodos de sequência
    public void print() {
        No atual = first.next;
        while (atual != null && atual != last) {
            System.out.print(atual.elemento);
            System.out.print(" ");
            atual = atual.next;
        }
        System.out.println();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

// Implementação Sequência Array
class SequenciaArray implements Sequencia {
    private static final int capacidade_inicial = 10;
    private No[] V; // array v de posições (No)
    private int size;

    public SequenciaArray() {
        this.V = new No[capacidade_inicial];
        this.size = 0;
    }

    // Métodos auxiliares
    // Garante que existe espaço no array; se não houver, dobra a capacidade
    private void aumentaCapacidade() {
        if (size == V.length) {
            No[] novoV = new No[V.length * 2];
            System.arraycopy(V, 0, novoV, 0, size);
            V = novoV;
        }
    }

    // Valida um rank de acesso (acesso/remoção exige 0 <= r < size, devendo ter elemento ali)
    private void checarRankAcesso(int r) {
        if (r < 0 || r >= size) {
            throw new SequenciaExcecao("Posição informada é inválida: " + r);
        }
    }

    // Valida um rank de inserção (aceita 0 <= r <= size, podendo inserir logo depois do último elemento)
    private void checarRankInsercao(int r) {
        if (r < 0 || r > size) {
            throw new SequenciaExcecao("Não é possível inserir um elemento numa posição que é maior que o número de elementos atual da lista");
        }
    }

    // Métodos do vetor
    // Retorna o elemento na colocação r
    public Object elemAtRank(int r) {
        checarRankAcesso(r);
        return V[r].elemento;
    }

    // Substitui o elemento na colocação r por o e retorna o antigo elemento
    public Object replaceAtRank(int r, Object o) {
        checarRankAcesso(r);
        Object aux = V[r].elemento;
        V[r].elemento = o;
        return aux;
    }

    // Insere um novo elemento na colocação r
    public void insertAtRank(int r, Object o) {
        checarRankInsercao(r);
        aumentaCapacidade();
        for (int i = size; i > r; i--) {
            V[i] = V[i - 1];
            V[i].rank = i;
        }
        V[r] = new No(o, r);
        size++;
    }

    // Remove e retorna o elemento na posição r
    public Object removeAtRank(int r) {
        if (isEmpty()) {
            throw new SequenciaExcecao("Vetor já está vazio");
        }
        checarRankAcesso(r);
        Object elemento = V[r].elemento;
        V[r].rank = -1; // marca o nó como removido
        for (int i = r; i < size - 1; i++) {
            V[i] = V[i + 1];
            V[i].rank = i;
        }
        V[size - 1] = null;
        size--;
        return elemento;
    }

    // Métodos de lista
    // Verifica se o nó passado é o primeiro
    public boolean isFirst(No n) {
        return n.rank == 0;
    }

    // Verifica se o nó é o último
    public boolean isLast(No n) {
        return n.rank == size - 1;
    }

    // Retorna o primeiro elemento
    public No first() {
        if (isEmpty()) {
            throw new SequenciaExcecao("A sequência está vazia");
        }
        return V[0];
    }

    // Retorna o último elemento
    public No last() {
        if (isEmpty()) {
            throw new SequenciaExcecao("A sequência está vazia");
        }
        return V[size - 1];
    }

    // Retorna o nó antes do nó passado
    public No before(No n) {
        if (isFirst(n)) {
            throw new SequenciaExcecao("Não existe posição anterior ao primeiro elemento");
        }
        return V[n.rank - 1];
    }

    // Retorna o nó depois do nó passado
    public No after(No n) {
        if (isLast(n)) {
            throw new SequenciaExcecao("Não existe posição posterior ao último elemento");
        }
        return V[n.rank + 1];
    }

    // Substitui o elemento do nó pelo objeto o
    public Object replaceElement(No n, Object o) {
        Object aux = n.elemento;
        n.elemento = o;
        return aux;
    }

    // Troca os elementos (conteúdo) dos nós n e m, sem mexer no rank de cada um
    public void swapElements(No n, No m) {
        Object aux = n.elemento;
        n.elemento = m.elemento;
        m.elemento = aux;
    }

    // Insere o elemento antes do nó informado
    public void insertBefore(No n, Object o) {
        if (n.rank < 0 || n.rank >= size || V[n.rank] != n) {
            throw new SequenciaExcecao("Nó inválido");
        }
        insertAtRank(n.rank, o);
    }

    // Insere o elemento depois do nó informado
    public void insertAfter(No n, Object o) {
        if (n.rank < 0 || n.rank >= size || V[n.rank] != n) {
            throw new SequenciaExcecao("Nó inválido");
        }
        insertAtRank(n.rank + 1, o);
    }

    // Insere o elemento no início da sequência
    public void insertFirst(Object o) {
        insertAtRank(0, o);
    }

    // Insere o elemento no final da sequência
    public void insertLast(Object o) {
        insertAtRank(size, o);
    }

    // Remove o nó passado
    public void remove(No n) {
        if (n.rank < 0 || n.rank >= size || V[n.rank] != n) {
            throw new SequenciaExcecao("Nó inválido");
        }
        removeAtRank(n.rank);
    }

    // Métodos ponte
    // Acessa o nó (posição) em uma sequência com base em sua posição
    public No atRank(int r) {
        checarRankAcesso(r);
        return V[r];
    }

    // Determina a posição (rank) de um nó específico na sequência
    public int rankOf(No n) {
        return n.rank;
    }

    // Métodos de sequência
    public void print() {
        for (int i = 0; i < size; i++) {
            System.out.print(V[i].elemento);
            System.out.print(" ");
        }
        System.out.println();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

// Testes
public class Main {
    public static void main(String[] args) {
        System.out.println("Teste Sequencia Duplamente Ligada");
        testarSequencia(new SequenciaDuplamente());

        System.out.println();
        System.out.println("Teste SequenciaArray");
        testarSequencia(new SequenciaArray());
    }

    private static void testarSequencia(Sequencia sequencia) {
        System.out.println("Teste métodos do vetor");
        sequencia.print();

        System.out.println("Tamanho da sequencia");
        System.out.println(sequencia.size());

        System.out.println("Inserindo em posição específica: ");
        sequencia.insertAtRank(0, 1);
        sequencia.insertAtRank(1, 2);
        sequencia.insertAtRank(2, 3);
        sequencia.print();

        System.out.println("Retornando o elemento na posição escolhida (1)");
        System.out.println(sequencia.elemAtRank(1));

        System.out.println("Substituindo o elemento da posição 2");
        System.out.println(sequencia.replaceAtRank(2, 100));
        sequencia.print();

        System.out.println("Removendo elemento na posição escolhida (2)");
        System.out.println(sequencia.removeAtRank(2));
        sequencia.print();

        System.out.println();
        System.out.println("Teste métodos de lista");

        System.out.println("Inserindo um nó antes do nó informado (1)");
        sequencia.insertBefore(sequencia.atRank(1), 33);
        sequencia.print();

        System.out.println("Inserindo um nó depois do nó informado (1)");
        sequencia.insertAfter(sequencia.atRank(2), 55);
        sequencia.print();

        System.out.println("Inserindo o nó no inicio da sequencia");
        sequencia.insertFirst(21);
        sequencia.print();

        System.out.println("Inserindo o nó no fim da sequencia");
        sequencia.insertLast(23);
        sequencia.print();

        System.out.println("Substituindo o elemento do nó pelo objeto passado");
        sequencia.replaceElement(sequencia.atRank(1), 10);
        sequencia.print();

        System.out.println("Trocando os elementos de dois nós");
        sequencia.swapElements(sequencia.atRank(1), sequencia.last());
        sequencia.print();

        System.out.println("Removendo um nó");
        sequencia.remove(sequencia.atRank(0));
        sequencia.print();

        System.out.println("Tamanho da sequencia");
        System.out.println(sequencia.size());

        System.out.println("Testando método rankOf");
        System.out.println(sequencia.rankOf(sequencia.last()));
    }
}

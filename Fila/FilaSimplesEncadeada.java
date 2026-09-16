// Em fila (diferente de pilha) os elementos entram pelo fim e saem pelo inicio
// Com o uso do ponteiro auxiliar 'fim' ela não precisa percorrer toda lista até o final, sendo O(1)

class FilaVaziaExcecao extends RuntimeException {
    public FilaVaziaExcecao(String erro) {
        super(erro);
    }
}

class Node {
    private Object elemento;
    private Node proximo;

    public Node(Object elemento) {
        this.elemento = elemento;
        this.proximo = null;
    }

    public Object getElemento() {
        return this.elemento;
    }

    public void setElemento(Object o) {
        this.elemento = o;
    }

    public Node getProximo() {
        return this.proximo;
    }

    public void setProximo(Node proximo) {
        this.proximo = proximo;
    }
}

interface Fila {
    void enqueue(Object o); // Insere no fim 
    Object dequeue(); // Remove o começo 
    Object primeiro();
    int tamanho();
    boolean estaVazio();
}

public class FilaSimplesEncadeada implements Fila {
    private Node inicio;
    private Node fim;
    private int tamanho;

    public FilaSimplesEncadeada() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    @Override
    public void enqueue(Object o) {
        Node novoNo = new Node(o);
        if (estaVazio()) {
            this.inicio = novoNo;
        } else {
            this.fim.setProximo(novoNo); // O nó que estava no fim passa a apontar para o novo nó
        }
        this.fim = novoNo;
        this.tamanho++;
    }

    @Override
    public Object dequeue() {
        if (estaVazio()) {
            throw new FilaVaziaExcecao("A fila está vazia.");
        }
        Object elementoRemovido = this.inicio.getElemento(); // Salva o elemento do início em elementoRemovido
        this.inicio = this.inicio.getProximo(); // Inicio vira o próximo da fila
        this.tamanho--;
        if (this.inicio == null) { //  Se o último item da fila for removido, inicio vira null
            this.fim = null; 
        }
        return elementoRemovido;
    }

    @Override
    public Object primeiro() {
        if (estaVazio()) {
            throw new FilaVaziaExcecao("A fila está vazia.");
        }
        return this.inicio.getElemento();
    }

    @Override
    public int tamanho() {
        return this.tamanho;
    }

    @Override
    public boolean estaVazio() {
        return this.tamanho == 0;
    }
}

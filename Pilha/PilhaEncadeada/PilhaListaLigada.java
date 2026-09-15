interface Pilha {
    int size();
    boolean isEmpty();
    Object top() throws PilhaVaziaExcecao;
    void push(Object elemento);
    Object pop() throws PilhaVaziaExcecao;
}

class PilhaVaziaExcecao extends RuntimeException {
    public PilhaVaziaExcecao(String erro) {
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

    public void setElemento(Object elemento) {
        this.elemento = elemento;
    }

    public Node getProximo() {
        return this.proximo;
    }

    public void setProximo(Node proximo) {
        this.proximo = proximo;
    }
}

public class PilhaListaLigada implements Pilha {
    protected Node topo;
    protected int tamanho;

    public PilhaListaLigada() {
        this.topo = null;
        this.tamanho = 0;
    }

    @Override
    public void push(Object elemento) {
        Node node = new Node(elemento);
        node.setProximo(this.topo);
        this.topo = node;
        this.tamanho++;
    }

    @Override
    public Object pop() {
        if (isEmpty()) {
            throw new PilhaVaziaExcecao("A pilha está vazia");
        }
        Object elementoRetirado = this.topo.getElemento();
        this.topo = this.topo.getProximo();
        this.tamanho--;
        return elementoRetirado;
    }

    @Override
    public Object top() {
        if (isEmpty()) {
            throw new PilhaVaziaExcecao("A pilha está vazia");
        }
        return this.topo.getElemento();
    }

    @Override
    public boolean isEmpty() {
        return this.topo == null;
    }

    @Override
    public int size() {
        return this.tamanho;
    }
}

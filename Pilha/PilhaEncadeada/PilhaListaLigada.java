// Em vez de alocar um bloco contínuo de memória pré-fixado (como um array), cada elemento é encapsulado em um nó independente que aponta para o elemento anterior (funcionando em O(1))

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
    private Object elemento; // Guarda o dado inserido 
    private Node proximo; // Armazena o ponteiro de memória para o próximo dele na pilha

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
    protected Node topo; // Inicio da pilha 
    protected int tamanho; // Contador sincronizado a cada remoção/adição para que não precise percorrer a lista

    public PilhaListaLigada() { // Pilha vazia
        this.topo = null;
        this.tamanho = 0;
    }

    @Override
    public void push(Object elemento) {
        Node node = new Node(elemento); // Novo nó contendo o objeto
        node.setProximo(this.topo); // Faz o proximo desse novo nó apontar para quem era o antigo topo
        this.topo = node;
        this.tamanho++;
    }

    @Override
    public Object pop() {
        if (isEmpty()) {
            throw new PilhaVaziaExcecao("A pilha está vazia");
        }
        Object elementoRetirado = this.topo.getElemento(); // Guarda o dado contido no nó do topo em elementoRetirado
        this.topo = this.topo.getProximo(); // Muda novo topo
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

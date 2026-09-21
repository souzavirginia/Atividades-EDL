// Em fila (diferente de pilha) os elementos entram pelo fim e saem pelo inicio

public class FilaListaLigada {

    // Nó da lista encadeada
    static class No {
        Object elemento;   // valor armazenado no nó
        No proximo;        // referência para o próximo nó da lista
 
        public No(Object elemento) {
            this.elemento = elemento;
            this.proximo = null; // todo nó novo nasce sem sucessor
        }
 
        @Override
        public String toString() {
            return " " + this.elemento;
        }
    }
 
    //Exceção para fila vazia
    static class FilaVaziaExcecao extends RuntimeException {
        public FilaVaziaExcecao(String erro) {
            super(erro);
        }
    }

    // TAD Fila com lista simplesmente encadeada
    // Usa ponteiros para o início e para o fim, de modo que inserir no fim (enqueue) e remover do início (dequeue) são O(1) - não precisa percorrer tudo
    static class Fila {
        private No inicio;   // referência para o primeiro nó da fila
        private No fim;      // referência para o último nó da fila
        private int tamanho; // quantidade de elementos na fila
 
        // Construtor: fila começa vazia
        public Fila() {
            this.inicio = null;
            this.fim = null;
            this.tamanho = 0;
        }
 
        // enqueue: insere um novo elemento no fim 
        public void enqueue(Object elemento) {
            No novoNo = new No(elemento); // cria o nó que vai entrar na fila
 
            if (inicio == null) {
                // fila estava vazia: o novo nó é o início E o fim
                inicio = novoNo;
                fim = novoNo;
            } else {
                // liga o antigo último nó ao novo nó, e atualiza "fim"
                fim.proximo = novoNo;
                fim = novoNo;
            }
            tamanho++;
        }
 
        // dequeue: remove e retorna o elemento do início da fila
        public Object dequeue() {
            if (isEmpty()) {
                throw new FilaVaziaExcecao("A fila está vazia");
            }
 
            No temp = inicio;          // guarda o nó que será removido
            inicio = inicio.proximo;   // início passa a ser o segundo nó
 
            // se a fila ficou vazia, "fim" também deve voltar a ser null
            if (inicio == null) {
                fim = null;
            }
 
            tamanho--;
            return temp.elemento; // retorna o valor guardado no nó removido
        }
 
        // isEmpty: indica se a fila não possui elementos
        public boolean isEmpty() {
            return inicio == null;
        }
 
        // first: retorna (sem remover) o elemento do início da fila
        public Object first() {
            if (isEmpty()) {
                throw new FilaVaziaExcecao("A fila está vazia");
            }
            return inicio.elemento;
        }
 
        // size: retorna a quantidade de elementos na fila
        public int size() {
            return tamanho;
        }
 
        // imprimirFila: percorre a lista do início ao fim, imprimindo cada elemento
        public void imprimirFila() {
            No atual = inicio;
            while (atual != null) {
                System.out.print(atual.elemento + " ");
                atual = atual.proximo;
            }
            System.out.println();
        }
    }
 
    // Teste
    public static void main(String[] args) {
        Fila fila = new Fila();
 
        System.out.println("Inserindo nós na fila:");
        for (int i = 0; i < 10; i++) {
            fila.enqueue(i); // insere 0,1,2,...,9
        }
        fila.imprimirFila();
 
        System.out.println("Removendo nós na fila:");
        for (int i = 0; i < 6; i++) {
            System.out.println(fila.dequeue()); // remove os 6 primeiros
        }
        fila.imprimirFila();
 
        System.out.println("O primeiro elemento da fila é: " + fila.first());
        System.out.println("O tamanho da fila é: " + fila.size());
    }
}

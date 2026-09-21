// Diferente do vetor que acessa o elemento por número (indice/rank), a lista identifica por posição (referência de nó) - sendo uma refêrencia de memória que não muda

public class ListaArray {
 
    // Exceção
    static class ListaExcecao extends RuntimeException {
        public ListaExcecao(String erro) {
            super(erro);
        }
    }
 

    // Interface - Navegação por posição numérica (int n)
    interface ListaInterface {
        int size();
        boolean isEmpty();
        boolean isFirst(Object o);
        boolean isLast(Object o);
        Object first();
        Object last();
        Object before(int n);
        Object after(int n);
        Object replaceElement(int n, Object o);
        void swapElements(int n, int m);
        void insertBefore(int n, Object o);
        void insertAfter(int n, Object o);
        void insertFirst(Object o);
        void insertLast(Object o);
        void remove(int n);
    }
 
    // TAD Lista com Array
    static class Lista implements ListaInterface {
        private Object[] lista;
        private int size = 0;
        private int capacidade;
 
        public Lista(int capacidade) {
            this.capacidade = capacidade;
            lista = new Object[capacidade];
        }
 
        @Override
        public int size() {
            return size;
        }
 
        @Override
        public boolean isEmpty() {
            return size == 0;
        }
 
        // isFirst: verifica se o elemento passado é igual ao primeiro
        @Override
        public boolean isFirst(Object o) {
            return lista[0] == o;
        }
 
        // isLast: verifica se o elemento passado é igual ao último
        @Override
        public boolean isLast(Object o) {
            return lista[size - 1] == o;
        }
 
        // first: retorna o primeiro elemento
        @Override
        public Object first() {
            return lista[0];
        }
 
        // last: retorna o último elemento
        @Override
        public Object last() {
            return lista[size - 1];
        }
 
        // before: retorna o elemento imediatamente antes da posição n
        @Override
        public Object before(int n) {
            return lista[n - 1];
        }
 
        // after: retorna o elemento imediatamente depois da posição n
        @Override
        public Object after(int n) {
            return lista[n + 1];
        }
 
        // replaceElement: substitui o elemento na posição n, retorna o antigo
        @Override
        public Object replaceElement(int n, Object o) {
            Object aux = lista[n];
            lista[n] = o;
            return aux;
        }
 
        // swapElements: troca de lugar os elementos das posições n e m
        @Override
        public void swapElements(int n, int m) {
            Object aux = lista[n];
            lista[n] = lista[m];
            lista[m] = aux;
        }
 
        // insertBefore: insere o objeto o IMEDIATAMENTE ANTES da posição n
        @Override
        public void insertBefore(int n, Object o) {
            if (capacidade == size) {
                aumentaCapacidade();
            }
 
            if (n == 0 || n > size + 1) {
                // OBS: n == 0 é bloqueado aqui — para inserir na posição 0
                // use insertFirst, não insertBefore.
                throw new ListaExcecao("Índice informado ultrapassa os limites da lista");
            } else if (isEmpty()) {
                System.out.println("Lista vazia, elemento inserido no início da lista");
                lista[0] = o;
            } else {
                // desloca os elementos para a direita, abrindo espaço na posição n-1
                for (int i = size; i > n - 1; i--) {
                    lista[i] = lista[i - 1];
                }
                lista[n - 1] = o;
            }
            size++;
        }
 
        // insertAfter: insere o objeto o depois da posição n
        @Override
        public void insertAfter(int n, Object o) {
            if (capacidade == size) {
                aumentaCapacidade();
            }
 
            if (n > size) {
                throw new ListaExcecao("Índice informado ultrapassa os limites da lista");
            } else {
                // desloca os elementos para a direita, abrindo espaço na posição n+1
                for (int i = size; i > n + 1; i--) {
                    lista[i] = lista[i - 1];
                }
                lista[n + 1] = o;
                size++;
            }
        }
 
        // insertFirst: insere o objeto o no início da lista
        @Override
        public void insertFirst(Object o) {
            if (capacidade == size) {
                aumentaCapacidade();
            }
 
            if (size > 0) {
                // abre espaço na posição 0, empurrando tudo para a direita
                for (int i = size; i > 0; i--) {
                    lista[i] = lista[i - 1];
                }
                lista[0] = o;
                size++;
            } else {
                lista[0] = o;
                size++;
            }
        }
 
        // insertLast: insere o objeto o no final da lista
        @Override
        public void insertLast(Object o) {
            if (capacidade == size) {
                aumentaCapacidade();
            }
            lista[size] = o;
            size++;
        }
 
        // remove: remove o elemento na posição n, deslocando os seguintes
        @Override
        public void remove(int n) {
            for (int i = n; i < size; i++) {
                lista[i] = lista[i + 1];
            }
            size--;
        }
 
        // aumentaCapacidade: dobra o tamanho do array interno quando necessário
        public void aumentaCapacidade() {
            capacidade *= 2;
            Object[] novaLista = new Object[capacidade];
            for (int i = 0; i < size; i++) {
                novaLista[i] = lista[i];
            }
            lista = novaLista;
        }
 
        // print: imprime todos os elementos em ordem
        public void print() {
            for (int i = 0; i < size; i++) {
                System.out.print(this.lista[i]);
                System.out.print(' ');
            }
            System.out.println();
        }
    }
 
    // Teste
    public static void main(String[] args) {
        Lista lista = new Lista(10);
 
        for (int i = 0; i < 4; i++) {
            lista.insertFirst(i); // insere 0,1,2,3  -> [3,2,1,0]
        }
        lista.print();
 
        lista.insertBefore(5, 9);
        lista.print();
 
        lista.insertBefore(5, 100);
        lista.print();
 
        lista.insertAfter(1, 33);
        lista.print();
 
        lista.insertLast(55);
        lista.print();
 
        lista.remove(2);
        lista.print();
    }
}
 

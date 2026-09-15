// Exceções
class ExcecaoListaVazia extends RuntimeException {
    public ExcecaoListaVazia(String mensagem) {
        super(mensagem);
    }
}

class ExcecaoLimiteViolado extends RuntimeException {
    public ExcecaoLimiteViolado(String mensagem) {
        super(mensagem);
    }
}

// Interface
interface Lista<P> {
    int size();
    boolean isEmpty();
    boolean isFirst(P p);
    boolean isLast(P p);
    P first();
    P last();
    P before(P p);
    P after(P p);
    Object elemento(P p);
    Object replaceElement(P p, Object o);
    void swapElements(P p, P q);
    P insertBefore(P p, Object o);
    P insertAfter(P p, Object o);
    P insertFirst(Object o);
    P insertLast(Object o);
    Object remove(P p);
}

// Lista Duplamente Encadeada
class No {
    Object elemento;
    No anterior;
    No proximo;

    public No(Object elemento, No anterior, No proximo) {
        this.elemento = elemento;
        this.anterior = anterior;
        this.proximo = proximo;
    }
}

class ListaDuplamenteEncadeada implements Lista<No> {
    private final No cabeca;
    private final No cauda;
    private int tamanho;

    public ListaDuplamenteEncadeada() {
        cabeca = new No(null, null, null);
        cauda = new No(null, null, null);
        cabeca.proximo = cauda;
        cauda.anterior = cabeca;
        tamanho = 0;
    }

    @Override
    public int size() {
        return tamanho;
    }

    @Override
    public boolean isEmpty() {
        return tamanho == 0;
    }

    @Override
    public No first() {
        if (isEmpty()) {
            throw new ExcecaoListaVazia("Lista vazia");
        }
        return cabeca.proximo;
    }

    @Override
    public No last() {
        if (isEmpty()) {
            throw new ExcecaoListaVazia("Lista vazia");
        }
        return cauda.anterior;
    }

    @Override
    public boolean isFirst(No n) {
        return n.anterior == cabeca;
    }

    @Override
    public boolean isLast(No n) {
        return n.proximo == cauda;
    }

    @Override
    public No before(No n) {
        if (isFirst(n)) {
            throw new ExcecaoLimiteViolado("Não há nó anterior ao primeiro");
        }
        return n.anterior;
    }

    @Override
    public No after(No n) {
        if (isLast(n)) {
            throw new ExcecaoLimiteViolado("Não há nó posterior ao último");
        }
        return n.proximo;
    }

    @Override
    public Object elemento(No n) {
        return n.elemento;
    }

    private No inserirEntre(Object o, No anterior, No proximo) {
        No novo = new No(o, anterior, proximo);
        anterior.proximo = novo;
        proximo.anterior = novo;
        tamanho++;
        return novo;
    }

    @Override
    public No insertFirst(Object o) {
        return inserirEntre(o, cabeca, cabeca.proximo);
    }

    @Override
    public No insertLast(Object o) {
        return inserirEntre(o, cauda.anterior, cauda);
    }

    @Override
    public No insertBefore(No n, Object o) {
        return inserirEntre(o, n.anterior, n);
    }

    @Override
    public No insertAfter(No n, Object o) {
        return inserirEntre(o, n, n.proximo);
    }

    @Override
    public Object replaceElement(No n, Object o) {
        Object antigo = n.elemento;
        n.elemento = o;
        return antigo;
    }

    @Override
    public void swapElements(No n, No q) {
        Object temp = n.elemento;
        n.elemento = q.elemento;
        q.elemento = temp;
    }

    @Override
    public Object remove(No n) {
        Object elem = n.elemento;
        n.anterior.proximo = n.proximo;
        n.proximo.anterior = n.anterior;
        n.anterior = null;
        n.proximo = null;
        tamanho--;
        return elem;
    }
}

// Lista Array
class ItemArray {
    Object elemento;
    int rank;

    ItemArray(Object elemento, int rank) {
        this.elemento = elemento;
        this.rank = rank;
    }
}

class ListaArray implements Lista<ItemArray> {
    private ItemArray[] dados;
    private int capacidade;
    private int tamanho;

    public ListaArray(int capacidadeInicial) {
        this.capacidade = (capacidadeInicial < 1) ? 1 : capacidadeInicial;
        this.dados = new ItemArray[capacidade];
        this.tamanho = 0;
    }

    public ListaArray() {
        this(10);
    }

    @Override
    public int size() {
        return tamanho;
    }

    @Override
    public boolean isEmpty() {
        return tamanho == 0;
    }

    private void duplicar() {
        capacidade *= 2;
        ItemArray[] novo = new ItemArray[capacidade];
        for (int i = 0; i < tamanho; i++) {
            novo[i] = dados[i];
        }
        dados = novo;
    }

    @Override
    public ItemArray first() {
        if (isEmpty()) {
            throw new ExcecaoListaVazia("Lista vazia");
        }
        return dados[0];
    }

    @Override
    public ItemArray last() {
        if (isEmpty()) {
            throw new ExcecaoListaVazia("Lista vazia");
        }
        return dados[tamanho - 1];
    }

    @Override
    public boolean isFirst(ItemArray p) {
        return p.rank == 0;
    }

    @Override
    public boolean isLast(ItemArray p) {
        return p.rank == tamanho - 1;
    }

    @Override
    public ItemArray before(ItemArray p) {
        if (isFirst(p)) {
            throw new ExcecaoLimiteViolado("Não há posição anterior");
        }
        return dados[p.rank - 1];
    }

    @Override
    public ItemArray after(ItemArray p) {
        if (isLast(p)) {
            throw new ExcecaoLimiteViolado("Não há posição posterior");
        }
        return dados[p.rank + 1];
    }

    @Override
    public Object elemento(ItemArray p) {
        return p.elemento;
    }

    private ItemArray inserirNoRank(int r, Object o) {
        if (tamanho == capacidade) {
            duplicar();
        }
        for (int i = tamanho; i > r; i--) {
            dados[i] = dados[i - 1];
            dados[i].rank = i;
        }
        ItemArray novo = new ItemArray(o, r);
        dados[r] = novo;
        tamanho++;
        return novo;
    }

    @Override
    public ItemArray insertFirst(Object o) {
        return inserirNoRank(0, o);
    }

    @Override
    public ItemArray insertLast(Object o) {
        return inserirNoRank(tamanho, o);
    }

    @Override
    public ItemArray insertBefore(ItemArray p, Object o) {
        return inserirNoRank(p.rank, o);
    }

    @Override
    public ItemArray insertAfter(ItemArray p, Object o) {
        return inserirNoRank(p.rank + 1, o);
    }

    @Override
    public Object replaceElement(ItemArray p, Object o) {
        Object antigo = p.elemento;
        p.elemento = o;
        return antigo;
    }

    @Override
    public void swapElements(ItemArray p, ItemArray q) {
        Object temp = p.elemento;
        p.elemento = q.elemento;
        q.elemento = temp;
    }

    @Override
    public Object remove(ItemArray p) {
        Object removido = p.elemento;
        int r = p.rank;
        for (int i = r; i < tamanho - 1; i++) {
            dados[i] = dados[i + 1];
            dados[i].rank = i;
        }
        dados[--tamanho] = null;
        p.rank = -1;
        return removido;
    }
}

// Testes
public class Main {
    static <P> void imprimir(String rotulo, Lista<P> lista) {
        System.out.print(rotulo + " [");
        if (!lista.isEmpty()) {
            P atual = lista.first();
            while (true) {
                System.out.print(lista.elemento(atual));
                if (lista.isLast(atual)) {
                    break;
                }
                System.out.print(", ");
                atual = lista.after(atual);
            }
        }
        System.out.println("] (Tamanho: " + lista.size() + ")");
    }

    static <P> void testarEstrutura(String nomeEstrutura, Lista<P> lista) {
        System.out.println("TESTANDO: " + nomeEstrutura);

        // 1. Testes de estado inicial
        System.out.println("A lista recém-criada está vazia? " + lista.isEmpty());
        System.out.println("Tamanho inicial: " + lista.size());

        // 2. Inserções (First e Last)
        P pBanana = lista.insertFirst("banana");
        P pMaca = lista.insertFirst("maçã");
        P pUva = lista.insertLast("uva");
        imprimir("Após insertFirst e insertLast:", lista);

        // 3. Inserções (Before e After)
        P pPera = lista.insertAfter(pMaca, "pera");
        P pMelao = lista.insertBefore(pUva, "melão");
        imprimir("Após insertAfter(maçã, pera) e insertBefore(uva, melão):", lista);

        // 4. Navegação (first, last, before, after)
        System.out.println("Primeiro elemento (first): " + lista.elemento(lista.first()));
        System.out.println("Último elemento (last): " + lista.elemento(lista.last()));
        System.out.println("Elemento antes de 'banana': " + lista.elemento(lista.before(pBanana)));
        System.out.println("Elemento depois de 'banana': " + lista.elemento(lista.after(pBanana)));

        // 5. Verificações booleanas (isFirst e isLast)
        System.out.println("pMaca é o primeiro? " + lista.isFirst(pMaca));
        System.out.println("pMaca é o último? " + lista.isLast(pMaca));
        System.out.println("pUva é o último? " + lista.isLast(pUva));

        // 6. Atualização de elementos (replaceElement e swapElements)
        Object antigo = lista.replaceElement(pPera, "morango");
        System.out.println("Substituído '" + antigo + "' por 'morango'.");
        imprimir("Após replaceElement:", lista);

        System.out.println("Trocando posições de 'maçã' com 'uva' via swapElements...");
        lista.swapElements(pMaca, pUva);
        imprimir("Após swapElements:", lista);

        // 7. Remoções
        Object removido = lista.remove(pBanana);
        System.out.println("Elemento removido: " + removido);
        imprimir("Após remover 'banana':", lista);

        // 8. Testes de Exceções e Limites
        try {
            lista.after(lista.last());
        } catch (ExcecaoLimiteViolado e) {
            System.out.println("Sucesso - Capturado limite posterior: " + e.getMessage());
        }

        try {
            lista.before(lista.first());
        } catch (ExcecaoLimiteViolado e) {
            System.out.println("Sucesso - Capturado limite anterior: " + e.getMessage());
        }

        // Esvaziando a lista por completo
        while (!lista.isEmpty()) {
            lista.remove(lista.first());
        }
        imprimir("Lista totalmente esvaziada:", lista);

        try {
            lista.first();
        } catch (ExcecaoListaVazia e) {
            System.out.println("Sucesso - Capturada lista vazia: " + e.getMessage());
        }

        System.out.println();
    }

    public static void main(String[] args) {
        // Testes na Lista Encadeada
        testarEstrutura("LISTA DUPLAMENTE ENCADEADA", new ListaDuplamenteEncadeada());

        // Testes na Lista Array
        testarEstrutura("LISTA COM ARRAY", new ListaArray(2));
    }
}

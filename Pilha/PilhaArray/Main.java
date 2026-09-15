class PilhaVaziaExcecao extends RuntimeException {
    public PilhaVaziaExcecao(String erro) {
        super(erro);
    }
}

interface DuasPilhas {
    // Operações Pilha Vermelha
    void pushVermelha(Object o);
    Object popVermelha() throws PilhaVaziaExcecao;
    Object topVermelha() throws PilhaVaziaExcecao;
    boolean isEmptyVermelha();
    int sizeVermelha();

    // Operações Pilha Preta
    void pushPreta(Object o);
    Object popPreta() throws PilhaVaziaExcecao;
    Object topPreta() throws PilhaVaziaExcecao;
    boolean isEmptyPreta();
    int sizePreta();
}

class DuasPilhasArray implements DuasPilhas {
    private static final int CAPACIDADE_MIN = 4;
    private int capacidade;
    private Object[] a;
    private int topov;
    private int topop;

    public DuasPilhasArray(int capacidadeInicial) {
        if (capacidadeInicial < CAPACIDADE_MIN) {
            this.capacidade = CAPACIDADE_MIN;
        } else {
            this.capacidade = capacidadeInicial;
        }
        this.a = new Object[this.capacidade];
        this.topov = -1;
        this.topop = this.capacidade;
    }

    // Pilha Vermelha
    @Override
    public void pushVermelha(Object o) {
        if (topov + 1 == topop) {
            dobrarCapacidade();
        }
        a[++topov] = o;
    }

    @Override
    public Object popVermelha() throws PilhaVaziaExcecao {
        if (isEmptyVermelha()) {
            throw new PilhaVaziaExcecao("A pilha vermelha está vazia");
        }
        Object r = a[topov];
        a[topov--] = null;
        reduzirCapacidadeSeNecessario();
        return r;
    }

    @Override
    public Object topVermelha() throws PilhaVaziaExcecao {
        if (isEmptyVermelha()) {
            throw new PilhaVaziaExcecao("A pilha vermelha está vazia");
        }
        return a[topov];
    }

    @Override
    public boolean isEmptyVermelha() {
        return topov == -1;
    }

    @Override
    public int sizeVermelha() {
        return topov + 1;
    }

    // Pilha Preta
    @Override
    public void pushPreta(Object o) {
        if (topov + 1 == topop) {
            dobrarCapacidade();
        }
        a[--topop] = o;
    }

    @Override
    public Object popPreta() throws PilhaVaziaExcecao {
        if (isEmptyPreta()) {
            throw new PilhaVaziaExcecao("A pilha preta está vazia");
        }
        Object r = a[topop];
        a[topop++] = null;
        reduzirCapacidadeSeNecessario();
        return r;
    }

    @Override
    public Object topPreta() throws PilhaVaziaExcecao {
        if (isEmptyPreta()) {
            throw new PilhaVaziaExcecao("A pilha preta está vazia");
        }
        return a[topop];
    }

    @Override
    public boolean isEmptyPreta() {
        return topop == capacidade;
    }

    @Override
    public int sizePreta() {
        return capacidade - topop;
    }

    // Aumento e Redução
    public int sizeTotal() {
        return sizeVermelha() + sizePreta();
    }

    private void dobrarCapacidade() {
        redimensionar(capacidade * 2);
    }

    private void reduzirCapacidadeSeNecessario() {
        int total = sizeTotal();
        if (capacidade / 2 >= CAPACIDADE_MIN && total <= capacidade / 3) {
            redimensionar(capacidade / 2);
        }
    }

    private void redimensionar(int novaCapacidade) {
        Object[] b = new Object[novaCapacidade];

        // Copia a Pilha Vermelha
        int tamVermelha = sizeVermelha();
        for (int i = 0; i < tamVermelha; i++) {
            b[i] = a[i];
        }

        // Copia a Pilha Preta
        int tamPreta = sizePreta();
        int novoTopop = novaCapacidade - tamPreta;
        for (int i = 0; i < tamPreta; i++) {
            b[novoTopop + i] = a[topop + i];
        }

        this.a = b;
        this.topop = novoTopop;
        this.capacidade = novaCapacidade;
    }
}

public class Main {
    public static void main(String[] args) {
        DuasPilhasArray p = new DuasPilhasArray(4);

        // Está vazia?
        System.out.println("Vermelha vazia? " + p.isEmptyVermelha());
        System.out.println("Preta vazia? " + p.isEmptyPreta());

        // Inserir
        p.pushVermelha("A");
        p.pushVermelha("B");
        p.pushPreta(1);
        p.pushPreta(2);

        // Topo e Tamanho
        System.out.println("Topo Vermelha: " + p.topVermelha());
        System.out.println("Topo Preta: " + p.topPreta());
        System.out.println("Tamanho Total: " + p.sizeTotal());

        // Duplicar o tamanho do array
        p.pushVermelha("C");
        System.out.println("Novo topo Vermelha: " + p.topVermelha());

        // Remover
        System.out.println("Removeu em Vermelha: " + p.popVermelha());
        System.out.println("Removeu em Preta: " + p.popPreta());
    }
}

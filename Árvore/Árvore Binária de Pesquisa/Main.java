import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// Interface
interface ArvorePesquisa {

  // Genérica
  int tamanho();
  int altura(no no);
  int profundidade(no no);
  boolean ehVazia();
  Iterator<no> nos();
  Iterator<Object> elementos();

  // Compara tamanho
  void setComparador(Comparador c);
  Comparador getComparador();

  // Raiz
  no getRaiz();
  void setRaiz(no p);
  no pai(no no);
  Iterator<no> filhos(no no);

  // Consulta
  boolean ehInterno(no no);
  boolean ehExterno(no no);
  boolean ehRaiz(no no);

  // Atualização
  Object substituir(no no, Object o);

  // Busca, inserção, remoção
  no pesquisar(no no, Object key);
  no incluir(Object key);
  Object remover(Object key);

  // Travessia
  void emOrdem(no no);
  void preOrdem(no no);
  void posOrdem(no no);

  // Desenhar hieararquia na tela
  void mostrar();
}

// Nó
class no {
  private Object chave; // Valor
  private no pai;
  private no filhoEsquerdo; // Menor
  private no filhoDireito; // Maior

  // Construtores
  // Árvore binária de pesquisa tem chave em seus nós internos
  // Nós externos -> null (a ausência de filho é o nó externo)
  public no() {
    chave = null;
    filhoDireito = null;
    filhoEsquerdo = null;
    pai = null;
  }

  public no(Object ch, no pai) { // Nó com chave e já com pai
    this.chave = ch;
    this.pai = pai;
    this.filhoDireito = null;
    this.filhoEsquerdo = null;
  }

  public Object getChave() {
    return chave;
  }

  public void setChave(Object chave) {
    this.chave = chave;
  }

  public no getPai() {
    return pai;
  }

  public void setPai(no pai) {
    this.pai = pai;
  }

  public no getFilhoEsquerdo() {
    return filhoEsquerdo;
  }

  public void setFilhoEsquerdo(no filhoEsquerdo) {
    this.filhoEsquerdo = filhoEsquerdo;
  }

  public no getFilhoDireito() {
    return filhoDireito;
  }

  public void setFilhoDireito(no filhoDireito) {
    this.filhoDireito = filhoDireito;
  }

  public boolean ehInterno() {
    return filhoEsquerdo != null || filhoDireito != null;
  }

  public boolean ehExterno() { // Sem filho, são folhas
    return !ehInterno();
  }

  public boolean ehRaiz() {
    return pai == null;
  }

  @Override
  public String toString() {
    return String.format("no<%s>", chave);
  }
}

// Comparador (Para decidir quem é o maior e o menor)
class Comparador {
  public Comparador() {}

  @SuppressWarnings("unchecked")
  public int comparar(Object n1, Object n2) { // Devolve 1, 0 ou -1
    int r = ((Comparable<Object>) n1).compareTo(n2);
    if (r > 0) return 1;  // n1 é maior
    if (r < 0) return -1; // n1 é menor
    return 0;             // São iguais
  }
}

// Árvore Binária de Pesquisa
class ArvoreBinariaPesquisa implements ArvorePesquisa {
  private no raiz; // Nó no topo (se igual a null, árvore vazia)
  private Comparador comp; // Compara as chaves
  private int tamanho; // Quantos nós

  public ArvoreBinariaPesquisa(Comparador comp) {
    this.comp = comp;
    this.raiz = null;
    this.tamanho = 0;
  }

  public void setComparador(Comparador c) {
    this.comp = c;
  }

  public Comparador getComparador() {
    return this.comp;
  }

  // Pesquisa (chave a partir de nó)
  public no pesquisar(no no, Object key) {
    int res = 0; // Resultado da comparação (vale 1, 0 ou -1)

    if (no != null) // Se existe o nó
      res = comp.comparar(key, no.getChave());

    if (res == 0) { // Achei a chave
      return no; // Retorna o nó que contém ela (retorna null se árvore está vazia)

    } else if (res < 0) { // Chave menor (-1), pesquisar no lado esquerdo
      if (no.getFilhoEsquerdo() != null)
        return pesquisar(no.getFilhoEsquerdo(), key); // Continua descendo
      else
        return no; // Não tem mais filho esquerdo, é o ponto de inserção do nó

    } else if (res > 0) { // Chave maior (1), pesquisar no lado direito
      if (no.getFilhoDireito() != null)
        return pesquisar(no.getFilhoDireito(), key);
      else
        return no;
    }
    return null;
  }

  // Incluir (comparar para chegar na folha enquanto for diferente de null)
  public no incluir(Object key) {
    int res = 1;

    no temp = pesquisar(raiz, key); // Primeiro usa o pesquisar
    if (temp != null)
      res = this.getComparador().comparar(key, temp.getChave());

    if (res == 0) {
      System.out.println("a chave já existe! " + temp.getChave());
      return null;

    } else {
      no novoNo = new no(key, temp); // Nó com chave e ainda sem pai
      if (res > 0) { // Novo nó vira filho direito de temp
        if (temp != null)
          temp.setFilhoDireito(novoNo);

      } else { // Novo nó vira filho esquerdo de temp
        if (temp != null)
          temp.setFilhoEsquerdo(novoNo);
      }
      if (temp == null) // Árvore vazia: o novo nó é a raiz
        raiz = novoNo;
      tamanho++;
      System.out.println("Nó incluído com sucesso! " + novoNo.getChave());
      return novoNo;
    }
  }

  // Remover
  public Object remover(Object key) {
    no temp = pesquisar(raiz, key); // Pode devolver o nó com a chave, o último nó visitado (se chave não existe) ou null (árvore vazia)
    Object retorna = null; // Guarda a chave removida
    if (temp == null || comp.comparar(key, temp.getChave()) != 0) { // Arvore vazia OU nó que não tem a chave
      System.out.println("chave " + key + " não existe!");
      return null;
    }
    tamanho--;

    // Caso 1: Folha (Basta o pai esquecer o filho)
    if ((temp.getFilhoEsquerdo() == null) && (temp.getFilhoDireito() == null)) {
      retorna = temp.getChave();
      if (temp.getPai() != null) {
        if (comp.comparar(temp.getPai().getChave(), temp.getChave()) > 0) {
          temp.getPai().setFilhoEsquerdo(null); // O filho removido estava à esquerda
        } else {
          temp.getPai().setFilhoDireito(null); // Estava à direta
        }
      }
      if (temp.getPai() == null) // Se o nó não tem pai, ele é a raiz
        this.setRaiz(null);
      temp.setPai(null);
      temp = null;
      return retorna;

      // Caso 2: Um filho (O avô adota o neto)
    } else if (((temp.getFilhoEsquerdo() != null) && (temp.getFilhoDireito() == null)) ||
            ((temp.getFilhoEsquerdo() == null) && (temp.getFilhoDireito() != null))) {
      retorna = temp.getChave();
      no filho;
      if (temp.getFilhoEsquerdo() != null) // Onde está o filho?
        filho = temp.getFilhoEsquerdo();
      else
        filho = temp.getFilhoDireito();
      filho.setPai(temp.getPai()); // Filho passa a apontar para avô e vice-versa
      if (temp.getPai() == null) {
        this.setRaiz(filho); // O filho passa a ser a raiz
      } else if (comp.comparar(temp.getPai().getChave(), temp.getChave()) > 0) {
        temp.getPai().setFilhoEsquerdo(filho);
      } else {
        temp.getPai().setFilhoDireito(filho);
      }
      temp.setPai(null);
      temp.setFilhoEsquerdo(null);
      temp.setFilhoDireito(null);
      temp = null;
      return retorna;

    } else {
      retorna = temp.getChave();
      // Caso 3: Dois filhos (Ao invés de tirar o nó, troco a chave dele pela chave do próximo maior e apago o nó desse próximo maior
      no sucessor = temp.getFilhoDireito();
      while (sucessor.getFilhoEsquerdo() != null)
        sucessor = sucessor.getFilhoEsquerdo();

      // O sucessor tem, no máximo, o filho direito
      no filhoSucessor = sucessor.getFilhoDireito();
      if (filhoSucessor != null)
        filhoSucessor.setPai(sucessor.getPai());
      if (sucessor.getPai() == temp)
        temp.setFilhoDireito(filhoSucessor);
      else
        sucessor.getPai().setFilhoEsquerdo(filhoSucessor);

      // A chave do sucessor ocupa o lugar da chave removida
      temp.setChave(sucessor.getChave());
      sucessor.setPai(null);
      sucessor.setFilhoDireito(null);
      sucessor = null;
      return retorna;
    }
  }

  public no getRaiz() {
    return this.raiz;
  }

  // Define uma nova raiz (a subárvore inteira passa a ser a árvore)
  public void setRaiz(no p) {
    this.raiz = p;
    if (p != null)
      p.setPai(null);
    this.tamanho = contar(p);
  }

  private int contar(no n) {
    if (n == null) return 0;
    return 1 + contar(n.getFilhoEsquerdo()) + contar(n.getFilhoDireito()); // Função recursiva que conta os nós
  }

  // Retorna o pai de um nó (null se for a raiz)
  public no pai(no n) {
    return n.getPai();
  }

  // Iterador sobre os filhos de um nó (esquerdo primeiro, depois o direito)
  public Iterator<no> filhos(no n) {
    List<no> lista = new ArrayList<no>();
    if (n.getFilhoEsquerdo() != null)
      lista.add(n.getFilhoEsquerdo());
    if (n.getFilhoDireito() != null)
      lista.add(n.getFilhoDireito());
    return lista.iterator();
  }

  // Consultas
  public boolean ehInterno(no n) {
    return n.ehInterno();
  }

  public boolean ehExterno(no n) {
    return n.ehExterno();
  }

  public boolean ehRaiz(no n) {
    return n.ehRaiz();
  }

  // Altera a chave guardada em um nó e devolve a antiga
  public Object substituir(no n, Object o) {
    Object antigo = n.getChave();
    n.setChave(o);
    return antigo;
  }

  // Travessias
  public void emOrdem(no no) { // Imprime entre esquerda e direita
    if (no == null) return;
    if (no.getFilhoEsquerdo() != null) {
      emOrdem(no.getFilhoEsquerdo());
    }
    System.out.print(no.getChave() + " ");
    if (no.getFilhoDireito() != null) {
      emOrdem(no.getFilhoDireito());
    }
  }

  public void preOrdem(no no) { // Imprime antes dos filhos
    if (no == null) return;
    System.out.print(no.getChave() + " ");
    if (no.getFilhoEsquerdo() != null) {
      preOrdem(no.getFilhoEsquerdo());
    }
    if (no.getFilhoDireito() != null) {
      preOrdem(no.getFilhoDireito());
    }
  }

  public void posOrdem(no no) { // Imprime depois dos filhos
    if (no == null) return;
    if (no.getFilhoEsquerdo() != null) {
      posOrdem(no.getFilhoEsquerdo());
    }
    if (no.getFilhoDireito() != null) {
      posOrdem(no.getFilhoDireito());
    }
    System.out.print(no.getChave() + " ");
  }

  // Altura
  public int altura(no no) {
    if (no == null) return -1;
    if (no.ehExterno()) return 0;
    int h = 0;
    if (no.getFilhoEsquerdo() != null)
      h = Math.max(h, altura(no.getFilhoEsquerdo()));
    if (no.getFilhoDireito() != null)
      h = Math.max(h, altura(no.getFilhoDireito()));
    return 1 + h;
  }

  // Profundidade
  public int profundidade(no no) {
    if (no == null || no.ehRaiz())
      return 0;
    return 1 + profundidade(no.getPai());
  }

  // Mostrar a árvore
  public void mostrar() {
    if (raiz == null) {
      System.out.println("(árvore vazia)");
      return;
    }
    int h = altura(raiz);
    int w = Math.max(3, maiorChave(raiz) + 2); // Largura de cada célula
    int nColunas = (1 << (h + 1)) - 1;
    int nLinhas = 2 * h + 1; // Linha de chaves + linha de ligações
    char[][] grade = new char[nLinhas][nColunas * w];
    for (char[] linha : grade)
      Arrays.fill(linha, ' ');
    desenhar(raiz, grade, 0, (1 << h) - 1, h, w);
    for (char[] linha : grade)
      System.out.println(new String(linha).replaceAll("\\s+$", ""));
  }

  private int maiorChave(no n) {
    if (n == null) return 0;
    int m = String.valueOf(n.getChave()).length();
    return Math.max(m, Math.max(maiorChave(n.getFilhoEsquerdo()), maiorChave(n.getFilhoDireito())));
  }

  private void desenhar(no n, char[][] grade, int nivel, int coluna, int h, int w) {
    int x = coluna * w + w / 2;
    String s = String.valueOf(n.getChave());
    int inicio = x - s.length() / 2;
    for (int i = 0; i < s.length(); i++)
      grade[2 * nivel][inicio + i] = s.charAt(i);
    int offset = (nivel < h) ? (1 << (h - nivel - 1)) : 0;
    if (n.getFilhoEsquerdo() != null) {
      int cx = (coluna - offset) * w + w / 2;
      grade[2 * nivel + 1][(x + cx) / 2] = '/';
      desenhar(n.getFilhoEsquerdo(), grade, nivel + 1, coluna - offset, h, w);
    }
    if (n.getFilhoDireito() != null) {
      int cx = (coluna + offset) * w + w / 2;
      grade[2 * nivel + 1][(x + cx) / 2] = '\\';
      desenhar(n.getFilhoDireito(), grade, nivel + 1, coluna + offset, h, w);
    }
  }

  public Iterator<no> nos() { // Entrega a lista de nós como iterador
    List<no> lista = new ArrayList<no>();
    coletarNos(raiz, lista);
    return lista.iterator();
  }

  private void coletarNos(no n, List<no> lista) { // Colocando os nós em uma lista
    if (n == null) return;
    coletarNos(n.getFilhoEsquerdo(), lista);
    lista.add(n);
    coletarNos(n.getFilhoDireito(), lista);
  }

  public Iterator<Object> elementos() { // Guarda só as chaves dos nós da lista
    List<Object> lista = new ArrayList<Object>();
    Iterator<no> it = nos();
    while (it.hasNext())
      lista.add(it.next().getChave());
    return lista.iterator();
  }

  public int tamanho() {
    return this.tamanho;
  }

  public boolean ehVazia() {
    return this.tamanho == 0;
  }
}

// Testes
class Main {
  private static void percursos(ArvoreBinariaPesquisa t) {
    System.out.print("Em ordem : "); t.emOrdem(t.getRaiz());
    System.out.println();
    System.out.print("Pré-ordem: "); t.preOrdem(t.getRaiz());
    System.out.println();
    System.out.print("Pós-ordem: "); t.posOrdem(t.getRaiz());
    System.out.println();
  }

  private static void titulo(String s) {
    System.out.println("\n=== " + s + " ===");
  }

  private static void situacao(ArvoreBinariaPesquisa t) {
    System.out.println("tamanho: " + t.tamanho() + " | altura: " + t.altura(t.getRaiz()));
  }

  public static void main(String[] args) {
    ArvoreBinariaPesquisa arvore = new ArvoreBinariaPesquisa(new Comparador());

    titulo("Árvore vazia");
    System.out.println("ehVazia: " + arvore.ehVazia() + " | tamanho: " + arvore.tamanho());
    arvore.mostrar();

    titulo("Inclusão (6, 2, 9, 1, 4, 8)");
    int[] chaves = {6, 2, 9, 1, 4, 8};
    for (int k : chaves)
      arvore.incluir(k);
    arvore.mostrar();
    situacao(arvore);

    titulo("Percursos");
    percursos(arvore);

    titulo("Pesquisa");
    no achado = arvore.pesquisar(arvore.getRaiz(), 4);
    System.out.println("pesquisar(4) -> " + achado + " | profundidade: " + arvore.profundidade(achado));
    no ultimo = arvore.pesquisar(arvore.getRaiz(), 5);
    System.out.println("pesquisar(5) -> " + ultimo + " (chave ausente: retorna o futuro pai)");

    titulo("Incluir 5 (e uma chave repetida)");
    arvore.incluir(5);
    arvore.incluir(5);
    arvore.mostrar();
    situacao(arvore);

    titulo("Iteradores");
    System.out.print("elements(): ");
    Iterator<Object> el = arvore.elementos();
    while (el.hasNext()) System.out.print(el.next() + " ");
    System.out.print("\nnos(): ");
    Iterator<no> ns = arvore.nos();
    while (ns.hasNext()) System.out.print(ns.next() + " ");
    System.out.println();

    titulo("Métodos do TAD (pai, filhos, ehInterno, ehExterno, ehRaiz, substituir)");
    no n2 = arvore.pesquisar(arvore.getRaiz(), 2);
    System.out.println("pai(" + n2 + ") -> " + arvore.pai(n2));
    System.out.print("filhos(" + n2 + ") -> ");
    Iterator<no> fs = arvore.filhos(n2);
    while (fs.hasNext()) System.out.print(fs.next() + " ");
    System.out.println();
    System.out.println("ehInterno(" + n2 + "): " + arvore.ehInterno(n2)
            + " | ehExterno(no<1>): " + arvore.ehExterno(arvore.pesquisar(arvore.getRaiz(), 1))
            + " | ehRaiz(raiz): " + arvore.ehRaiz(arvore.getRaiz()));
    Object velho = arvore.substituir(arvore.pesquisar(arvore.getRaiz(), 1), 0);
    System.out.println("substituir(1 por 0) devolveu " + velho);
    arvore.mostrar();
    arvore.substituir(arvore.pesquisar(arvore.getRaiz(), 0), 1); // desfaz a troca

    titulo("Remover 8 (nó folha - 1o caso)");
    arvore.remover(8);
    arvore.mostrar();

    titulo("Remover 4 (um filho - 2o caso)");
    arvore.remover(4);
    arvore.mostrar();

    titulo("Remover 2 (dois filhos - 3o caso)");
    arvore.remover(2);
    arvore.mostrar();

    titulo("Remover 6 (raiz com dois filhos)");
    arvore.remover(6);
    arvore.mostrar();

    titulo("Remover chave inexistente (99)");
    System.out.println("remover(99) -> " + arvore.remover(99));
    situacao(arvore);

    titulo("Remover 9 (raiz com um filho)");
    arvore.remover(9);
    arvore.mostrar();

    titulo("Percursos finais");
    percursos(arvore);
    situacao(arvore);

    titulo("Esvaziando a árvore");
    arvore.remover(1);
    arvore.remover(5); // última chave: a raiz é uma folha
    System.out.println("ehVazia: " + arvore.ehVazia() + " | tamanho: " + arvore.tamanho());
    arvore.mostrar();

    titulo("Árvore maior (50, 30, 70, 20, 40, 60, 80, 10, 45, 65, 100)");
    for (int k : new int[]{50, 30, 70, 20, 40, 60, 80, 10, 45, 65, 100})
      arvore.incluir(k);
    arvore.mostrar();
    situacao(arvore);
  }
}

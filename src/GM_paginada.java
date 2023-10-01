public class GM_paginada {
    private int framesLivres;
    private int tamPg;
    private int tamMem;
    private int numFrames;
    private Word[] m; // Acesso direto à memória
    private boolean[] tabelaPaginas;

    public GM_paginada(Memory mem, int tamPagina) {
        this.tamMem = mem.tamMem;
        this.tamPg = tamPagina;
        this.numFrames = tamMem/tamPagina;
        this.m = mem.m;
        this.tabelaPaginas = new boolean[this.numFrames];
        this.framesLivres = this.numFrames;

        // Inicializa a tabela de páginas com -1 (indicando que nenhuma página está alocada)
        for (int i = 0; i < tabelaPaginas.length; i++) {
            tabelaPaginas[i] = false;
        }
    }

    public boolean aloca(int nroPaginas, int[] tabelaPaginas) {
        int paginasAlocadas = 0;

        for (int i = 0; i < tabelaPaginas.length; i++) {
            if (paginasAlocadas == nroPaginas) {
                // Todas as páginas foram alocadas com sucesso
                return true;
            }

            if (tabelaPaginas[i] == -1) {
                // Encontra a primeira página livre e a aloca
                tabelaPaginas[i] = alocaPagina();
                paginasAlocadas++;
            }
        }

        // Se não conseguiu alocar todas as páginas solicitadas, libera as que foram alocadas
        desaloca(tabelaPaginas);
        return false;
    }

    public void desaloca(int[] tabelaPaginas) {
        for (int pagina : tabelaPaginas) {
            if (pagina != -1) {
                desalocaPagina(pagina);
            }
        }
    }

    private int alocaPagina() {
        // Encontra a primeira página livre e a aloca
        for (int i = 0; i < tabelaPaginas.length; i++) {
            if (tabelaPaginas[i] == -1) {
                tabelaPaginas[i] = i;
                return i;
            }
        }
        System.out.println("Nenhuma página disponível!");
        return -1;
    }

    private void desalocaPagina(int pagina) {
        if (pagina >= 0 && pagina < tabelaPaginas.length) {
            tabelaPaginas[pagina] = -1;
            // Você pode adicionar aqui a lógica para limpar a memória associada à página desalocada, se necessário.
        }
    }
}

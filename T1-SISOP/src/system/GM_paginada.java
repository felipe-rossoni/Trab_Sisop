package system;

public class GM_paginada {
    private int framesLivres;
    private int tamPg;
    private int tamMem;
    private int numFrames;
    private Word[] m; // Acesso direto à memória
    private Memory mem;
    private boolean[] tabelaPaginas;

    public GM_paginada(Memory mem, int tamPagina) {
        this.tamMem = mem.tamMem;
        this.tamPg = tamPagina;
        this.numFrames = tamMem/tamPagina;
        this.m = mem.m;
        this.mem = mem;
        this.tabelaPaginas = new boolean[this.numFrames];
        this.framesLivres = this.numFrames;

        for (int i = 0; i < tabelaPaginas.length; i++) {
            tabelaPaginas[i] = false;
        }
    }

    public int[] aloca(int tamProg) {
        int paginasNecessarias = (tamProg/tamPg);
        int[] indexs;
        if(tamProg%tamPg>0)
            paginasNecessarias++;
        indexs = new int[paginasNecessarias];
        if(framesLivres >= paginasNecessarias){
            int i = 0;
            int j = 0;
            while(true){
                if (j >= paginasNecessarias)
                    break;
                if (tabelaPaginas[i] == false){
                    tabelaPaginas[i] = true;
                    indexs[j] = i;
                    j++;
                    framesLivres--;
                }   
                i++; 
            }
            return indexs;
        }
        return null;
    }

    public void desaloca(int[] paginasAlocadas) {
        for (int i = 0; i < paginasAlocadas.length; i++) {
            tabelaPaginas[paginasAlocadas[i]] = false;
            framesLivres++;
            for (int j=paginasAlocadas[i]*tamPg; j<paginasAlocadas[i]*tamPg + tamPg; j++) { m[j] = new Word(Opcode.___,-1,-1,-1); }
        }
    }

    public int get_tamPg() {
        return tamPg;
    }

    public Word[] get_mem(){
        return m;
    }
    
    public void dumpProcess(int[] pags){
        for (int i = 0; i < pags.length; i++) {
            mem.dump(pags[i]*tamPg, pags[i]*tamPg + tamPg);
        }
    }
}

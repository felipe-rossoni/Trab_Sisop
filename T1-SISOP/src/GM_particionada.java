

public class GM_particionada {
    private int tamPart;
    private int tamMem;
    private Word[] m;   // Acesso direto a memória
    private Memory mem;
    private boolean[] particao;

    public GM_particionada(Memory mem, int tamPart){
        this.tamMem = mem.tamMem;
        this.tamPart = tamPart;
        this.mem = mem;
        this.m = mem.m;

        // Define o número de partições
        if(tamMem % tamPart > 0)
            this.particao = new boolean[tamMem/tamPart + 1];
        else
            this.particao = new boolean[tamMem/tamPart];
        
        for (int i = 0; i < particao.length; i++) {
            particao[i] = false;
        }
    }

    public int get_tamPart() {
        return tamPart;
    }

    public Word[] get_mem(){
        return m;
    }

    public int aloca(int tamProg) {
        if(tamProg > tamPart){
            System.out.println("Espaço insuficiente! tamanho do progama maior que temanho da partição.");
            return -1;
        }
        // Encontra a primeira partição livre e a aloca
        for (int i = 0; i < particao.length; i++) {
            if (!particao[i]) {
                particao[i] = true;
                return i;
            }
        }
        System.out.println("Nenhuma partição disponível!");
        return -1;
    }

    public void desaloca(int part) {
        // Libera a partição especificada
        if (part >= 0 && part < particao.length) {
            particao[part] = false;
            if((part*tamPart + tamPart)> tamMem)
                for (int i=part*tamPart; i<tamMem; i++) { m[i] = new Word(Opcode.___,-1,-1,-1); }
            else
                for (int i=part*tamPart; i<part*tamPart + tamPart; i++) { m[i] = new Word(Opcode.___,-1,-1,-1); }
        }
    }

    public void dumpProcess(int part){
        mem.dump(part*tamPart, part*tamPart + tamPart);
    }
}

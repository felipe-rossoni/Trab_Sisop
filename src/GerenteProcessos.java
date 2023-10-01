public class GerenteProcessos {
    private boolean tipoGm;  // true para paginação e false para fixa
    private PCB ready[];
    private PCB running;
    private GM_paginada gerentePaginado;
    private GM_particionada gerenteParticionado;
    private int id = 0;

    public GerenteProcessos(GM_paginada gm, int numProgamas){
        this.gerentePaginado = gm;
        this.ready = new PCB[numProgamas];
    }

    public GerenteProcessos(GM_particionada gm){
        this.gerenteParticionado= gm;
    }

    public boolean criaProcesso(Word[] programa){
        int tamProg = programa.length;
        if(tipoGm){
            int[] mem = gerentePaginado.aloca(tamProg);
            if(mem == null){
                System.out.println("Sem memória disponível!");
                return false;
            }
            PCB newProg = new PCB(id, 0, mem);
            id++;
            carregaPrograma(programa, mem);
            ready[id--] = newProg;
            return true;
        }
        int particao = gerenteParticionado.aloca(tamProg);
        if(particao == -1)
            return false;
        PCB newProg = new PCB(id, 0, particao);
        id++;
        carregaPrograma(programa, particao);
        ready[id--] = newProg;
        return true;
    }

    public void desalocaProcesso(int id){
        if(tipoGm)
            gerentePaginado.desaloca(ready[id].getPaginas());
        else
            gerenteParticionado.desaloca(ready[id].getPartUsada());
        ready[id] = null;
    }

    private void carregaPrograma(Word[] programa,int[] mem){
        int tamPag = gerentePaginado.get_tamPg();
        Word[] memTemp = gerentePaginado.get_mem();
        int k = 0;
        int j;
        for (int i = 0; i < mem.length; i++) {
            j = 0;
            while (true){
                memTemp[mem[i] * tamPag + j].opc = programa[k].opc;     
                memTemp[mem[i] * tamPag + j].r1 = programa[k].r1;     
                memTemp[mem[i] * tamPag + j].r2 = programa[k].r2;     
                memTemp[mem[i] * tamPag + j].p = programa[k].p;
                j++;
                k++;
                if (j == tamPag){
                    break;
                }
            }
        }
    }

    private void carregaPrograma(Word[] programa,int particao){
        int tamPart = gerenteParticionado.get_tamPart();
        Word[] memTemp = gerenteParticionado.get_mem();
        int j = 0;
        for (int i = particao * tamPart; i < particao * tamPart + tamPart; i++) {
			memTemp[i].opc = programa[j].opc;     
            memTemp[i].r1 = programa[j].r1;     
            memTemp[i].r2 = programa[j].r2;     
            memTemp[i].p = programa[j].p;
            j++;
            if (j > programa.length){
                break;
            }
		}
    }
}

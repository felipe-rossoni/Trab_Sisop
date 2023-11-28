public class PCB {
    private int id;
    private int pc;
    private int[] reg;
    private int[] paginas;
    private int partUsada;
    private estadoCPU esCPU;
    

    public PCB(int id, int pc, int[] paginas){
        this.id = id;
        this.pc = pc;
        this.paginas = paginas;
        esCPU = null;
    }

    public PCB(int id, int pc, int particao){
        this.id = id;
        this.pc = pc;
        this.partUsada = particao;
    }

    public int[] getPaginas() {
        return paginas;
    }

    public int getPartUsada() {
        return partUsada;
    }

    public int getId(){
        return id;
    }

    public int getPc(){
        return pc;
    }

    public estadoCPU getEstadoCPU(){
        return esCPU;
    }

    public void setEstadoCPU(estadoCPU esCPU){
        this.esCPU = esCPU;
    }
}

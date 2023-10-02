package system;
public class PCB {
    private int id;
    private int pc;
    private int[] reg;
    private int[] paginas;
    private int partUsada;

    public PCB(int id, int pc, int[] paginas){
        this.id = id;
        this.pc = pc;
        this.paginas = paginas;
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

}

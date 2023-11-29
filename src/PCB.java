

public class PCB {
    private int id;
    private int pc;
    private int[] reg;
    private int[] paginas;
    private int partUsada;
    private estadoCPU esCPU;
    private ProcessState state;
    

    public PCB(int id, int pc, int[] paginas){
        this.id = id;
        this.pc = pc;
        this.paginas = paginas;
        this.esCPU = null;
        this.state = ProcessState.READY;
    }

    public PCB(int id, int pc, int particao){
        this.id = id;
        this.pc = pc;
        this.partUsada = particao;
        this.state = ProcessState.READY;
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
    
    public ProcessState getState() {
		return state;
	}
    
    public void setState(ProcessState state) {
		this.state = state;
	}
}

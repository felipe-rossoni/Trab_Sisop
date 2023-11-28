public class estadoCPU {
        private boolean isPag;
	    private int pc; 
	    private Word ir;
	    private int[] reg; 
	    private Interrupts irpt; 
	    private int base; 
	    private int limite; 

        public estadoCPU(boolean isPag, int pc, Word ir, int[] reg, Interrupts irpt, int base, int limite){
            this.isPag = isPag;
	        this.pc = pc;
	        this.ir = ir;
	        this.reg = reg; 
	        this.irpt = irpt; 
	        this.base = base; 
	        this.limite = limite;
        }

        public boolean isPag() {
            return isPag;
        }

        public int getPc() {
            return pc;
        }

        public Word getIr() {
            return ir;
        }

        public int[] getReg() {
            return reg;
        }

        public Interrupts getIrpt() {
            return irpt;
        }

        public int getBase() {
            return base;
        }

        public int getLimite() {
            return limite;
        }
        
    }

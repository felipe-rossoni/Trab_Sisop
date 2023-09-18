// PUCRS - Escola Politécnica - Sistemas Operacionais
// Prof. Fernando Dotti
// Código fornecido como parte da solução do projeto de Sistemas Operacionais
//
// VM
//    HW = memória, cpu
//    SW = tratamento int e chamada de sistema
// Funcionalidades de carga, execução e dump de memória

public class Sistema {
	
	private void loadProgram(Word[] p, Word[] m) {
		for (int i = 0; i < p.length; i++) {
			m[i].opc = p[i].opc;     m[i].r1 = p[i].r1;     m[i].r2 = p[i].r2;     m[i].p = p[i].p;
		}
	}

	private void loadProgram(Word[] p) {
		loadProgram(p, vm.m);
	}

	private void loadAndExec(Word[] p){
		loadProgram(p);    // carga do programa na memoria
				System.out.println("---------------------------------- programa carregado na memoria");
				vm.mem.dump(0, p.length);            // dump da memoria nestas posicoes				
		vm.cpu.setContext(0, vm.tamMem - 1, 0);      // seta estado da cpu ]
				System.out.println("---------------------------------- inicia execucao ");
		vm.cpu.run();                                // cpu roda programa ate parar	
				System.out.println("---------------------------------- memoria após execucao ");
				vm.mem.dump(0, p.length);            // dump da memoria com resultado
	}

	// -------------------------------------------------------------------------------------------------------
    // -------------------  S I S T E M A --------------------------------------------------------------------

	public VM vm;
	public InterruptHandling ih;
	public SysCallHandling sysCall;
	public static Programas progs;

    public Sistema(){   // a VM com tratamento de interrupções
		 ih = new InterruptHandling();
         sysCall = new SysCallHandling();
		 vm = new VM(ih, sysCall);
		 sysCall.setVM(vm);
		 progs = new Programas();
	}

    // -------------------  S I S T E M A - fim --------------------------------------------------------------
    // -------------------------------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------------------------------
    // ------------------- instancia e testa sistema
	public static void main(String args[]) {
		Sistema s = new Sistema();			
		//s.loadAndExec(progs.fibonacci10);
		//s.loadAndExec(progs.progMinimo);
		s.loadAndExec(progs.fatorial);
		//s.loadAndExec(progs.fatorialTRAP); // saida
		//s.loadAndExec(progs.fibonacciTRAP); // entrada
		//s.loadAndExec(progs.PC); // bubble sort
			
	}


   // -------------------------------------------------------------------------------------------------------
   // -------------------------------------------------------------------------------------------------------
   // -------------------------------------------------------------------------------------------------------
   // --------------- P R O G R A M A S  - não fazem parte do sistema
   // esta classe representa programas armazenados (como se estivessem em disco) 
   // que podem ser carregados para a memória (load faz isto)

   
}


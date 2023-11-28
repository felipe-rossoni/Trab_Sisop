public class VM {
		public int tamMem;    
        public Word[] m;  
		public Memory mem;   
        public CPU cpu;    
		public GM_paginada GM_pag;
		public GM_particionada GM_part;
		public boolean isPag;
		public GerenteProcessos gp;
		public Device[] devices;

        public VM(InterruptHandling ih, SysCallHandling sysCall, boolean pag){   
		 // vm deve ser configurada com endereço de tratamento de interrupcoes e de chamadas de sistema
	     // cria memória
		     tamMem = 1024;
  		 	 mem = new Memory(tamMem);
			 m = mem.m;
			 isPag = pag;
			 if(isPag){
			 	GM_pag = new GM_paginada(mem, 16);
				gp = new GerenteProcessos(GM_pag, 100);
			 }
			 else{
				GM_part = new GM_particionada(mem, 128);
				gp = new GerenteProcessos(GM_part,100);
			 }
	  	 // cria cpu
			 cpu = new CPU(mem,ih,sysCall, true);                   // true liga debug
			 
			 loadDevices();
	    }
        
        private void loadDevices() {
        	System.out.println("Loading devices....");
        	devices = new Device[2];
        	
        	System.out.println("Loading screen....");
        	devices[0] = new Screen(0, 9);
        	
        	System.out.println("Loading keyboard....");
        	devices[1] = new Keyboard(1, 9);
        }

		public int criaProcesso(Word[] process){
			return gp.criaProcesso(process);
		}

		public boolean apagaProcesso(int procesId){
			return gp.desalocaProcesso(procesId);
		}

		public void executaProcesso(int id){
			PCB[] processos = gp.getPcbs();
			if (processos[id] != null){
				if(isPag){
					int[] pags = processos[id].getPaginas();
					int tamPag = GM_pag.get_tamPg();
					cpu.setContext(0, pags.length*tamPag -1, 0, isPag);
					cpu.setPags(pags, tamPag);
					if (processos[id].getEstadoCPU() != null){
						cpu.setEstado(processos[id].getEstadoCPU());
					}
					cpu.run();
					processos[id].setEstadoCPU(cpu.getEstadoCPU());

				}else{
					int part = processos[id].getPartUsada();
					int tamPart = GM_part.get_tamPart();
					cpu.setContext(part*tamPart,((part+1)*tamPart) -1, 0, isPag);
					if (processos[id].getEstadoCPU() != null){
						cpu.setEstado(processos[id].getEstadoCPU());
					}
					cpu.run();
					processos[id].setEstadoCPU(cpu.getEstadoCPU());
				}
			}
			else{
				System.out.println("Processo não existe!");
			}
			
		}
	}
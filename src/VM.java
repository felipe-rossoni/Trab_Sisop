public class VM implements Runnable {


	private class DeviceRunner implements Runnable{
		@Override
		public void run() {

			while (true) {
				try {
					for(Device device : devices) {
						if(device.state == ProcessState.READY) {
							cpu.signalDeviceReady();
						}
					}
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public int tamMem;    
	public Word[] m;  
	public Memory mem;   
	public CPU cpu;    
	public GM_paginada GM_pag;
	public GM_particionada GM_part;
	public boolean isPag;
	public GerenteProcessos gp;
	public Device[] devices;
	private DeviceRunner deviceRunner;

	public VM(InterruptHandling ih, SysCallHandling sysCall, boolean pag){   
		// vm deve ser configurada com endereco de tratamento de interrupcoes e de chamadas de sistema
		// cria memoria
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
		cpu = new CPU(mem,ih,sysCall, true); // true liga debug
		new Thread(cpu).start();

		loadDevices();
		deviceRunner = new DeviceRunner();

		new Thread(deviceRunner).start();
	}

	private void loadDevices() {
		System.out.println("Loading devices....");
		devices = new Device[2];

		System.out.println("Loading screen....");
		devices[0] = new Screen(0, 9);

		System.out.println("Loading keyboard....");
		devices[1] = new Keyboard(1, 9, mem);

		for(Device device : devices) {
			new Thread(device).start();
		}
	}

	public int criaProcesso(Word[] process){
		return gp.criaProcesso(process);
	}

	public boolean apagaProcesso(int procesId){
		return gp.desalocaProcesso(procesId);
	}

	public void executaProcesso(int id){

		PCB process = gp.getReadyProcess(id);
		gp.setRunning(process);
		if (process != null){
			if(isPag){
				int[] pags = process.getPaginas();
				int tamPag = GM_pag.get_tamPg();
				
				//caso o estado da cpu esteja null significa que ainda nao executou 
				int pc = 0;
				if(process.getEstadoCPU() != null) {
					pc = process.getEstadoCPU().getPc();
				}
				
				cpu.setContext(0, pags.length*tamPag -1, pc, isPag);
				cpu.setPags(pags, tamPag);
				if (process.getEstadoCPU() != null){
					cpu.setEstado(process.getEstadoCPU());
				}
				cpu.run();
				process.setEstadoCPU(cpu.getEstadoCPU());

			}else{
				int part = process.getPartUsada();
				int tamPart = GM_part.get_tamPart();
				cpu.setContext(part*tamPart,((part+1)*tamPart) -1, 0, isPag);
				if (process.getEstadoCPU() != null){
					cpu.setEstado(process.getEstadoCPU());
				}
				cpu.run();
				process.setEstadoCPU(cpu.getEstadoCPU());
			}
		}
		else{
			System.out.println("Processo nao existe!");
		}

	}

	public void run() {
		while(true) {
			for(PCB pcb : gp.getReadyPCBs().values()) {
				if(pcb.getState() == ProcessState.READY && cpu.wait) {
					cpu.wait = false;
					executaProcesso(pcb.getId());
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}




}
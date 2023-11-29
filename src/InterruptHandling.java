

public class InterruptHandling {
	
	private Device device;
	
	private VM vm;

	public void setVM(VM _vm) {
		vm = _vm;
	}
	
	public void setDevice(Device device) {
		this.device = device;
	}
	
	public void handle(Interrupts irpt, int pc) { // apenas avisa - todas interrupcoes neste momento finalizam o programa
		System.out.println("                                               Interrupcao " + irpt + "   pc: " + pc);
		
		//em caso de interrupcao de device:
		//cpu recebe notificacao do device que o trabalho ja esta pronto
		//remove o processo da fila de bloqueados e coloca na fila de ready
		//restaura o processo informado pelo device na cpu
		switch(irpt) {
			case intDeviceReady:
				handleDevice();
		}
		
	}
	
	private void handleDevice() {
		
		//bloqueia o processo que esta na cpu
		estadoCPU estadoCPU = vm.cpu.getEstadoCPU();
		PCB pcb = vm.gp.getRunning();
		vm.gp.blockProcess(pcb.getId(), estadoCPU);
		
		//tira da fila de bloqueados o proceso que estava aguardando pelo device
		vm.gp.unBlockProcess(device.processId);	
	}
}

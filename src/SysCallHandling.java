public class SysCallHandling {
	private VM vm;

	public void setVM(VM _vm) {
		vm = _vm;
	}

	public void handle(int baseAddr) { // apenas avisa - todas interrupcoes neste momento finalizam o programa
		System.out.println("                                               Chamada de Sistema com op  /  par:  "
				+ vm.cpu.getReg()[8] + " / " + vm.cpu.getReg()[9]);
		
		//dentro do syscall.handle:
		//salva o estado da cpu
		//coloca o processo na fila de bloqueados
		//informa o device requisitado que ele precisa executar uma tarefa
		//pega o proximo processo da fila de disponiveis 
		
		estadoCPU estadoCPU = vm.cpu.getEstadoCPU();
		PCB pcb = vm.gp.getRunning();
		vm.gp.blockProcess(pcb.getId(), estadoCPU);
		
		int deviceAddress = vm.cpu.getReg()[7];
		int operation = vm.cpu.getReg()[8];
		int address = vm.cpu.getReg()[9];
		
		//ajusta o id do processo que acionou o device
		vm.devices[deviceAddress].processId = pcb.getId();
		vm.cpu.wait = true;
		
		
		switch(operation) {
			//leitura
			case 1:
				//acessa o endereco da memoria que foi definido no registrador 9 para buscar o periferico que deve ser acessado
				//le o device e escreve o conteudo na memoria
				vm.devices[deviceAddress].bufferInit = address+baseAddr;
				vm.devices[deviceAddress].state = ProcessState.RUNNING;
				break;
				
			//escrita
			case 2:
				//coloca o conteudo no registrador
				vm.cpu.getReg()[0] = vm.mem.m[address+baseAddr].p;
				int data = vm.cpu.getReg()[0];
				//grava o conteudo do registrador na posicao de memoria especificado no registrador 9
				//word = new Word(Opcode.DATA, -1, -1, data);
				//vm.mem.m[address+baseAddr] = word;
				//envia para o device 
				//vm.devices[deviceAddress].write(word);
				break;
		}
		
	}

}
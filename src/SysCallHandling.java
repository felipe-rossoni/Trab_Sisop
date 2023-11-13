package system;

public class SysCallHandling {
	private VM vm;

	public void setVM(VM _vm) {
		vm = _vm;
	}

	public void handle(int baseAddr) { // apenas avisa - todas interrupcoes neste momento finalizam o programa
		System.out.println("                                               Chamada de Sistema com op  /  par:  "
				+ vm.cpu.getReg()[8] + " / " + vm.cpu.getReg()[9]);
		
		int deviceAddress = vm.cpu.getReg()[7];
		int operation = vm.cpu.getReg()[8];
		int address = vm.cpu.getReg()[9];
		
		switch(operation) {
			//leitura
			case 1:
				//acessa o endereco da memoria que foi definido no registrador 9 para buscar o periferico que deve ser acessado
				//le o device e escreve o conteudo na memoria
				Word word = vm.devices[deviceAddress].read();
				vm.mem.m[address+ baseAddr] = word;
				
				break;
				
			//escrita
			case 2:
				//coloca o conteudo no registrador
				vm.cpu.getReg()[0] = vm.mem.m[address+baseAddr].p;
				int data = vm.cpu.getReg()[0];
				//grava o conteudo do registrador na posicao de memoria especificado no registrador 9
				word = new Word(Opcode.DATA, -1, -1, data);
				vm.mem.m[address+baseAddr] = word;
				//envia para o device 
				vm.devices[deviceAddress].write(word);
				break;
		}
		
	}

}
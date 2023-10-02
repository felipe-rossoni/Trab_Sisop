package system;

public class SysCallHandling {
	private VM vm;

	public void setVM(VM _vm) {
		vm = _vm;
	}

	public void handle() { // apenas avisa - todas interrupcoes neste momento finalizam o programa
		System.out.println("                                               Chamada de Sistema com op  /  par:  "
				+ vm.cpu.reg[8] + " / " + vm.cpu.reg[9]);
		
		int deviceAddress = vm.cpu.reg[7];
		int operation = vm.cpu.reg[8];
		int address = vm.cpu.reg[9];
		
		
		switch(operation) {
			//leitura
			case 1:
				//acessa o endereco da memoria que foi definido no registrador 9 para buscar o periferico que deve ser acessado
				//le o device e escreve o conteudo na memoria
				Word word = vm.devices[deviceAddress].read();
				vm.mem.m[address] = word;
				
				break;
				
			//escrita
			case 2:
				//coloca o conteudo no registrador
				vm.cpu.reg[0] = vm.mem.m[address].p;
				int data = vm.cpu.reg[0];
				//grava o conteudo do registrador na posicao de memoria especificado no registrador 9
				word = new Word(Opcode.DATA, -1, -1, data);
				vm.mem.m[address] = word;
				//envia para o device 
				vm.devices[deviceAddress].write(word);
				break;
		}
		
	}

}
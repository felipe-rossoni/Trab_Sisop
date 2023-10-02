package system.syscall;

import java.util.Scanner;

import system.Opcode;
import system.Word;

public class Keyboard extends Device {
	
	
	public Keyboard(int address, int r0) {
		this.address = address;
		this.r0 = r0;
	}
	
	@Override
	public Word read() {
		try (Scanner sc = new Scanner(System.in)) {
			int input = sc.nextInt();
			
			Word word = new Word(Opcode.DATA, this.r0, -1, input);
			return word;
		}
	}

	@Override
	public void write(Word word) {
		
	}

}

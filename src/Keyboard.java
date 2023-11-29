import java.util.Scanner;

public class Keyboard extends Device {

	private Scanner scanner;

	public Keyboard(int address, int r0, Memory memory) {
		this.address = address;
		this.r0 = r0;
		this.scanner = new Scanner(System.in);
		this.state = ProcessState.BLOCKED;
		this.memory = memory;
	}

	@Override
	public Word read() {
		System.out.println("Device Keyboard: ");
		int input = scanner.nextInt();
		Word word = new Word(Opcode.DATA, this.r0, -1, input);
		return word;
	}

	@Override
	public void write(Word word) {

	}

	@Override
	public void run() {
		while(true) {
			try {
				if(this.state == ProcessState.RUNNING) {
					Word word = read();
					this.memory.m[this.bufferInit] = word;
					this.state = ProcessState.READY;
				}
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}

import java.util.Scanner;

public class Keyboard extends Device {

	private Scanner scanner;

	public Keyboard(int address, int r0) {
		this.address = address;
		this.r0 = r0;
		this.scanner = new Scanner(System.in);
		this.state = ProcessState.BLOCKED;
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
				if(this.state == ProcessState.READY) {
					this.state = ProcessState.RUNNING;
					read();
					this.state = ProcessState.BLOCKED;
				}
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}

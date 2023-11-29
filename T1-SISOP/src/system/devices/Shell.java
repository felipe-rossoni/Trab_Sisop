package system.devices;

import system.Word;

public class Shell extends Device {
	
	public Shell(int address, int r0) {
		this.address = address;
		this.r0 = r0;
		this.state = State.READY;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public Word read() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(Word word) {
		// TODO Auto-generated method stub

	}

}

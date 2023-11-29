

public class Console extends Device {

	
	public Console(int address, int r0) {
		this.address = address;
		this.r0 = r0;
		this.state = State.READY;
	}
	
	@Override
	public void run() {
		while(true) {
			Word word = read();
		}
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

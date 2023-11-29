public class Screen extends Device {
	
	public Screen(int address, int r0) {
		this.address = address;
		this.r0 = r0;
	}
	
	@Override
	public Word read() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(Word word) {
		System.out.println("Screen output: " + word.p);

	}

	@Override
	public void run() {
		while(true) {
			try {
				read();
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}

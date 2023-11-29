
public class Console extends Device {

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

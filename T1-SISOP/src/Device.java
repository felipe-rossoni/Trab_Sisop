

public abstract class Device implements Runnable{
	
	public enum State{
		RUNNING, READY, BLOCKED
	}
	
	public int address;
	public int r0;
	public State state;
	
	private void stateControl(State nextState) {
		switch (this.state) {
		case BLOCKED: 
			if(nextState == State.READY || nextState == State.BLOCKED) {
				this.state = nextState;
			}
			break;
		case READY:
			if(nextState == State.RUNNING || nextState == State.READY) {
				this.state = nextState;
			}
			break;
		case RUNNING:
			if(nextState == State.BLOCKED || nextState == State.RUNNING) {
				this.state = nextState;
			}
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + this.state);
		}
	}
	
	public abstract Word read();
	public abstract void write(Word word);
	
}

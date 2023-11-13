package system.syscall;

import system.Word;

public abstract class Device {
	
	public int address;
	public int r0;
	
	
	public abstract Word read();
	public abstract void write(Word word);
	
}

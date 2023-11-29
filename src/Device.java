public abstract class Device implements Runnable {
	
	//endereco do dispositivo
	public int address;
	
	//registradores de instrucao
	public int r0;
	public int r1;
	
	//endereco de memoria inicio e fim para o buffer do dispositivo
	public int bufferInit;
	public int bufferEnd;
	
	public ProcessState state;
	
	public abstract Word read();
	public abstract void write(Word word);
	
}

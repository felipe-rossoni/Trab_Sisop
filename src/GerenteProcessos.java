import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class GerenteProcessos {
	
	private boolean tipoGm;  // true para paginacao e false para fixa
	private PCB running;
	private GM_paginada gerentePaginado;
	private GM_particionada gerenteParticionado;
	private int id = 0;
	
	private Map<Integer, PCB> readyQueue;
	private Map<Integer, PCB> bloquedQueue;

	public GerenteProcessos(GM_paginada gm, int numProgamas){
		this.gerentePaginado = gm;
		this.tipoGm = true;
		this.readyQueue = new ConcurrentSkipListMap<Integer, PCB>();
		this.bloquedQueue = new ConcurrentSkipListMap<Integer, PCB>();
	}

	public GerenteProcessos(GM_particionada gm, int numProgamas){
		this.gerenteParticionado= gm;
		this.tipoGm = false;
		this.readyQueue = new ConcurrentSkipListMap<Integer, PCB>();
		this.bloquedQueue = new ConcurrentSkipListMap<Integer, PCB>();
	}
	
	public Map<Integer, PCB> getReadyPCBs(){
		return readyQueue;
	}
	
	public void setRunning(PCB running) {
		this.running = running;
	}
	
	public PCB getRunning() {
		return running;
	}
	
	public PCB getReadyProcess(int id) {
		
		PCB process = null;
		if(readyQueue.containsKey(id)) {
			process = readyQueue.get(id);
		}
		else {
			System.out.println("GP: Processo " + id + " nao encontrado na fila de prontos.");
			System.out.println("GP: Verificando Processo " + id + " na fila de bloqueados: " + bloquedQueue.containsKey(id));
		}
		
		return process;
	}

	public int criaProcesso(Word[] programa){
		int tamProg = programa.length;
		if(tipoGm){
			int[] mem = gerentePaginado.aloca(tamProg);
			if(mem == null){
				System.out.println("Sem memoria disponivel!");
				return -1;
			}
			PCB newProg = new PCB(id++, 0, mem);
			carregaPrograma(programa, mem);
			readyQueue.put(newProg.getId(), newProg);
			
			return newProg.getId();
		}
		
		int particao = gerenteParticionado.aloca(tamProg);
		if(particao == -1) {
			return -1;
		}
		
		PCB newProg = new PCB(id++, 0, particao);
		carregaPrograma(programa, particao);
		readyQueue.put(newProg.getId(), newProg);
		return newProg.getId();
	}

	public boolean desalocaProcesso(int id){
		if(readyQueue.containsKey(id)){
			PCB pcb = readyQueue.remove(id);
			if(tipoGm) {
				gerentePaginado.desaloca(pcb.getPaginas());
			}
			else {
				gerenteParticionado.desaloca(pcb.getPartUsada());
			}
			
			return true;
		}
		return false;
	}

	private void carregaPrograma(Word[] programa,int[] mem){
		int tamPag = gerentePaginado.get_tamPg();
		Word[] memTemp = gerentePaginado.get_mem();
		int k = 0;
		int j;
		for (int i = 0; i < mem.length; i++) {
			j = 0;
			while (true){
				memTemp[mem[i] * tamPag + j].opc = programa[k].opc;     
				memTemp[mem[i] * tamPag + j].r1 = programa[k].r1;     
				memTemp[mem[i] * tamPag + j].r2 = programa[k].r2;     
				memTemp[mem[i] * tamPag + j].p = programa[k].p;
				j++;
				k++;
				if(k == programa.length)
					break;
				if (j == tamPag){
					break;
				}
				if(k == programa.length)
					break;
			}
		}
	}

	private void carregaPrograma(Word[] programa,int particao){
		int tamPart = gerenteParticionado.get_tamPart();
		Word[] memTemp = gerenteParticionado.get_mem();
		int j = 0;
		for (int i = particao * tamPart; i < particao * tamPart + tamPart; i++) {
			memTemp[i].opc = programa[j].opc;     
			memTemp[i].r1 = programa[j].r1;     
			memTemp[i].r2 = programa[j].r2;     
			memTemp[i].p = programa[j].p;
			j++;
			if (j >= programa.length){
				break;
			}
		}
	}

	public void listaProcessos(){
		boolean var = false;
		if (readyQueue.isEmpty()) {
			System.out.println("Nenhum processo alocado!");
			System.out.println("Fila de bloqueados: " + bloquedQueue.size());
		}else{
			System.out.println("Lista de processos: ");
			for (int i = 0; i < readyQueue.size(); i++) {
				if (readyQueue.containsKey(id))
					System.out.println("id do processo: " + readyQueue.get(id).getId());
				var = true;
			}
			if(!var)
				System.out.println("Nenhum processo alocado!");
		}
	}

	public void dumpProcess(int id){
		if (readyQueue.containsKey(id)){
			PCB process = readyQueue.get(id);
			if(tipoGm)
				gerentePaginado.dumpProcess(process.getPaginas());
			else
				gerenteParticionado.dumpProcess(process.getPartUsada());
			System.out.printf("PC: %d\n", readyQueue.get(id).getPc());
		}else{
			System.out.println("Processo nao existe!");
		}
	}
	
	public void blockProcess(int id, estadoCPU estado) {
		if(readyQueue.containsKey(id)) {
			PCB process = readyQueue.remove(id);
			process.setState(ProcessState.BLOCKED);
			process.setEstadoCPU(estado);
			bloquedQueue.put(process.getId(), process);
			
		}else{
			System.out.println("Processo nao existe!");
		}
	}
	
	public void unBlockProcess(int id) {
		if(bloquedQueue.containsKey(id)) {
			PCB process = bloquedQueue.remove(id);
			process.setState(ProcessState.READY);
			readyQueue.put(process.getId(), process);
			
		}else{
			System.out.println("Processo nao existe!");
		}
	}
	
	public PCB getBlockedProcess(int processId) {
		return bloquedQueue.get(processId);
	}
}

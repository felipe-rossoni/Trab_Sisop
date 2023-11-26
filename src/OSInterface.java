import java.util.Scanner;

public class OSInterface {
	private boolean traceMode;
	private Sistema Sys;

	public OSInterface(Sistema s) {
		this.Sys = s;
		traceMode = false;
	}

	public void run() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			try{
				System.out.print("> ");
				String input = scanner.nextLine();

				String[] tokens = input.split(" ");

				if (tokens.length > 0) {
					String command = tokens[0];
					switch (command) {
					case "new":
						if (tokens.length > 1) {
							String programName = tokens[1];
							Word[] programa;
							switch (programName) {
							case "fatorial":
								programa = Sys.progs.fatorial;
								break;
							case "progMinimo":
								programa = Sys.progs.progMinimo;
								break;
							case "fibonacci10":
								programa = Sys.progs.fibonacci10;
								break;
							case "fatorialTRAP":
								programa = Sys.progs.fatorialTRAP;
								break;
							case "fibonacciTRAP":
								programa = Sys.progs.fibonacciTRAP;
								break;
							case "PB":
								programa = Sys.progs.PB;
								break;
							case "PC":
								programa = Sys.progs.PC;
								break;
							case "readKeyboard":
								programa = Sys.progs.readKeyboard;
								break;
							case "writeScreen":
								programa = Sys.progs.writeScreen;
								break;
							case "testInOut":
								programa = Sys.progs.testInOut;
								break;
							default :
								programa = null;
								System.out.println("Programa inválido.");
								break;
							}
							if(programa != null){
								int processId = Sys.vm.criaProcesso(programa);
								if (processId >= 0)
									System.out.printf("Processo criado: " + programName + "\nid: %d\n", processId);
							}
						} else {
							System.out.println("Uso: new <programName>");
						}
						break;
					case "rm":
						if (tokens.length > 1) {
							int processId = Integer.parseInt(tokens[1]);
							boolean var = Sys.vm.apagaProcesso(processId);
							if(var)
								System.out.printf("Processo de id %d removido.\n", processId);
							else
								System.out.println("Falha: processo não existe!");
						} else {
							System.out.println("uso: rm <processId>");
						}
						break;
					case "ps":
						Sys.vm.gp.listaProcessos();
						break;
					case "dump":
						if (tokens.length > 1) {
							int processId = Integer.parseInt(tokens[1]);
							System.out.println("Dump do processo: " + processId);
							Sys.vm.gp.dumpProcess(processId);
						} else {
							System.out.println("uso: dump <processId>");
						}
						break;
					case "dumpM":
						if (tokens.length > 2) {
							int start = Integer.parseInt(tokens[1]);
							int end = Integer.parseInt(tokens[2])+1;
							System.out.println("Dumping da memória de " + start + " até " + (end-1));
							Sys.vm.mem.dump(start, end);
						} else {
							System.out.println("uso: dumpM <start> <end>");
						}
						break;
					case "run":
						if (tokens.length > 1) {
							int processId = Integer.parseInt(tokens[1]);
							System.out.println("Executando processo: " + processId);
							Sys.vm.executaProcesso(processId);
						} else {
							System.out.println("uso: executa <processId>");
						}
						break;
					case "traceOn":
						traceMode = true;
						Sys.vm.cpu.traceMode(traceMode);
						System.out.println("Trace mode ON");
						break;
					case "traceOff":
						traceMode = false;
						Sys.vm.cpu.traceMode(traceMode);
						System.out.println("Trace mode OFF");
						break;
					case "exit":
						scanner.close();
						System.exit(0);
						break;
					case "h":
						// Implemente a lógica para listar todos os processos existentes
						System.out.println("Comandos possíveis:\nnew <nomeDePrograma>\nrm <id>\nps\ndump <id>\ndumpM <inicio, fim>\nexecuta <id>");
						System.out.print("traceOn\ntraceOff\nexit\n");
						break;
					default:
						System.out.println("Comando desconhecido: " + command);
						break;
					}
				}
			}catch(Exception e) {}
		}
	}
}




package system;

public class CPU {
		private int maxInt; // valores maximo e minimo para inteiros nesta cpu
		private int minInt;
							// característica do processador: contexto da CPU ...
		private boolean isPag;
		private int pc; 			// ... composto de program counter,
		private Word ir; 			// instruction register,
		private int[] reg;       	// registradores da CPU
		private Interrupts irpt; 	// durante instrucao, interrupcao pode ser sinalizada
		private int base;   		// base e limite de acesso na memoria
		private int limite; // por enquanto toda memoria pode ser acessada pelo processo rodando
							// ATE AQUI: contexto da CPU - tudo que precisa sobre o estado de um processo para executa-lo
							// nas proximas versoes isto pode modificar
		private int pc_calc;

		private Memory mem;               // mem tem funcoes de dump e o array m de memória 'fisica' 
		private Word[] m;                 // CPU acessa MEMORIA, guarda referencia a 'm'. m nao muda. semre será um array de palavras
		private int[] pags;
		private int tamPag;

		private InterruptHandling ih;     // significa desvio para rotinas de tratamento de  Int - se int ligada, desvia
        private SysCallHandling sysCall;  // significa desvio para tratamento de chamadas de sistema - trap 
		private boolean debug;            // se true entao mostra cada instrucao em execucao
						
		public CPU(Memory _mem, InterruptHandling _ih, SysCallHandling _sysCall, boolean _debug) {     // ref a MEMORIA e interrupt handler passada na criacao da CPU
			maxInt =  32767;        // capacidade de representacao modelada
			minInt = -32767;        // se exceder deve gerar interrupcao de overflow
			mem = _mem;	            // usa mem para acessar funcoes auxiliares (dump)
			m = mem.m; 				// usa o atributo 'm' para acessar a memoria.
			reg = new int[10]; 		// aloca o espaço dos registradores - regs 8 e 9 usados somente para IO
			ih = _ih;               // aponta para rotinas de tratamento de int
            sysCall = _sysCall;     // aponta para rotinas de tratamento de chamadas de sistema
			debug =  _debug;        // se true, print da instrucao em execucao
		}
		
		private boolean legal(int e) {                             // todo acesso a memoria tem que ser verificado
			if(!isPag){
				if((e+base < base)||(e+base > limite))
					return false;
			}
			return true;
		}

		private boolean testOverflow(int v) {                       // toda operacao matematica deve avaliar se ocorre overflow                      
			if ((v < minInt) || (v > maxInt)) {                       
				irpt = Interrupts.intOverflow;             
				return true;
			};
			return false;
		}
		
		public void setContext(int _base, int _limite, int _pc, boolean pag) {  // no futuro esta funcao vai ter que ser 
			base = _base;                                          // expandida para setar todo contexto de execucao,
			limite = _limite;									   // agora,  setamos somente os registradores base,
			pc = _pc;                                              // limite e pc (deve ser zero nesta versao)
			irpt = Interrupts.noInterrupt;
			isPag = pag;                         // reset da interrupcao registrada
		}

		public void setPags(int[] pags, int pag) {
			this.pags = pags;
			this.tamPag = pag;
		}

		public void traceMode(boolean var){
			debug = var;
		}
		
		public void run() { 		// execucao da CPU supoe que o contexto da CPU, vide acima, esta devidamente setado			
			while (true) { 			// ciclo de instrucoes. acaba cfe instrucao, veja cada caso.
			   // --------------------------------------------------------------------------------------------------
			   // FETCH
				if (legal(pc)) { 
					if(isPag){
						pc_calc= (pags[pc/tamPag]*tamPag) + (pc%tamPag);
						base = (pags[pc/tamPag]*tamPag);
						ir = m[pc_calc];
					}else{
						ir = m[pc+base]; 	// <<<<<<<<<<<<           busca posicao da memoria apontada por pc, guarda em ir
					}	
					if (debug) { 
						System.out.print("                               pc: "+pc+"       exec: ");  
						mem.dump(ir); 
					}
			   // --------------------------------------------------------------------------------------------------
			   // EXECUTA INSTRUCAO NO ir
					switch (ir.opc) {   // conforme o opcode (código de operação) executa

					// Instrucoes de Busca e Armazenamento em Memoria
					    case LDI: // Rd ← k
						if (!legal(ir.p)) {
							irpt = Interrupts.intEnderecoInvalido;
						}
						else{
							reg[ir.r1] = ir.p;
							pc++;
							break;
						}
		
						case LDD: // Rd <- [A]
						    if (legal(ir.p)) {
							   reg[ir.r1] = m[ir.p+base].p;
							   pc++;
						    }
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
						    break;

						case LDX: // RD <- [RS] // NOVA
							if (legal(reg[ir.r2])) {
								reg[ir.r1] = m[reg[ir.r2]+base].p;
								pc++;
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;

						case STD: // [A] ← Rs
						    if (legal(ir.p)) {
							    m[ir.p+base].opc = Opcode.DATA;
							    m[ir.p+base].p = reg[ir.r1];
							    
							    System.out.println(m[ir.p+base]);
							    
							    pc++;
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
						    break;

						case STX: // [Rd] ←Rs
						    if (legal(reg[ir.r1])) {
							    m[reg[ir.r1]+base].opc = Opcode.DATA;      
							    m[reg[ir.r1]+base].p = reg[ir.r2];          
								pc++;
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;
						
						case MOVE: // RD <- RS
							if (ir.r2 > 8) {
							irpt = Interrupts.intEnderecoInvalido;
							}
							else{
								reg[ir.r1] = reg[ir.r2];
								pc++;
							}
							break;	
							
					// Instrucoes Aritmeticas
						case ADD: // Rd ← Rd + Rs
							if(legal(reg[ir.r2]) && legal(reg[ir.r1])){
								reg[ir.r1] = reg[ir.r1] + reg[ir.r2];
								if (testOverflow(reg[ir.r1])){
									irpt = Interrupts.intOverflow;
								}
							pc++;
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;

						case ADDI: // Rd ← Rd + k
							if(legal(reg[ir.r1])){
							reg[ir.r1] = reg[ir.r1] + ir.p;
							if (testOverflow(reg[ir.r1])){
								irpt = Interrupts.intOverflow;
							}
							pc++;
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;

						case SUB: // Rd ← Rd - Rs
							if(legal(reg[ir.r2]) && legal(reg[ir.r1])){
							reg[ir.r1] = reg[ir.r1] - reg[ir.r2];
							if (testOverflow(reg[ir.r1])){
								irpt = Interrupts.intOverflow;
							}
							pc++;
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;

						case SUBI: // RD <- RD - k // NOVA
							if(legal(reg[ir.r1])){
							reg[ir.r1] = reg[ir.r1] - ir.p;
							if (testOverflow(reg[ir.r1])){
								irpt = Interrupts.intOverflow;
							}
							pc++;
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;

						case MULT: // Rd <- Rd * Rs
							if(legal(reg[ir.r2]) && legal(reg[ir.r1])){
							reg[ir.r1] = reg[ir.r1] * reg[ir.r2];  
							if (testOverflow(reg[ir.r1])){
								irpt = Interrupts.intOverflow;
							}
							pc++;
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;

					// Instrucoes JUMP
						case JMP: // PC <- k
							if(legal(reg[ir.p])){
							pc = ir.p;
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;
						
						case JMPIG: // If Rc > 0 Then PC ← Rs Else PC ← PC +1
							if(legal(reg[ir.r2]) && legal(reg[ir.r1])){
							if (reg[ir.r2] > 0) {
								pc = reg[ir.r1];
							} else {
								pc++;
							}
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;

						case JMPIGK: // If RC > 0 then PC <- k else PC++
							if(legal(reg[ir.r2]) && legal(reg[ir.p])){
							if (reg[ir.r2] > 0) {
								pc = ir.p;
							} else {
								pc++;
							}
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;
	
						case JMPILK: // If RC < 0 then PC <- k else PC++
							if(legal(reg[ir.r2]) && legal(reg[ir.p])){
							 if (reg[ir.r2] < 0) {
								pc = ir.p;
							} else {
								pc++;
							}
							}
							else{
								irpt = Interrupts.intEnderecoInvalido;
							}
							break;
	
						case JMPIEK: // If RC = 0 then PC <- k else PC++
								if(legal(reg[ir.r2]) && legal(reg[ir.p])){
								if (reg[ir.r2] == 0) {
									pc = ir.p;
								} else {
									pc++;
								}
								}
								else{
								irpt = Interrupts.intEnderecoInvalido;
								}
							break;
	
	
						case JMPIL: // if Rc < 0 then PC <- Rs Else PC <- PC +1
								if(legal(reg[ir.r2]) && legal(reg[ir.r1])){
								 if (reg[ir.r2] < 0) {
									pc = reg[ir.r1];
								} else {
									pc++;
								}
								}
								else{
								irpt = Interrupts.intEnderecoInvalido;
								}
							break;
		
						case JMPIE: // If Rc = 0 Then PC <- Rs Else PC <- PC +1
								if(legal(reg[ir.r2]) && legal(reg[ir.r1])){
								 if (reg[ir.r2] == 0) {
									pc = reg[ir.r1];
								} else {
									pc++;
								}
								}
								else{
								irpt = Interrupts.intEnderecoInvalido;
								}
							break; 
	
						case JMPIM: // PC <- [A]
							if(legal(reg[ir.p]))
								 pc = m[ir.p + base].p;
							else
								irpt = Interrupts.intEnderecoInvalido;
							 break; 
	
						case JMPIGM: // If RC > 0 then PC <- [A] else PC++
								if(legal(reg[ir.r2]) && legal(reg[ir.p])){
								 if (reg[ir.r2] > 0) {
									pc = m[ir.p + base].p;
								} else {
									pc++;
								}
								}
								else{
								irpt = Interrupts.intEnderecoInvalido;
								}
							 break;  
	
						case JMPILM: // If RC < 0 then PC <- k else PC++
								if(legal(reg[ir.r2]) && legal(reg[ir.p])){
								 if (reg[ir.r2] < 0) {
									pc = m[ir.p + base].p;
								} else {
									pc++;
								}
								}
								else{
								irpt = Interrupts.intEnderecoInvalido;
								}
							 break; 
	
						case JMPIEM: // If RC = 0 then PC <- k else PC++
								if(legal(reg[ir.r2]) && legal(reg[ir.p])){
								if (reg[ir.r2] == 0) {
									pc = m[ir.p + base].p;
								} else {
									pc++;
								}
								}
								else{
								irpt = Interrupts.intEnderecoInvalido;
								}
							 break; 
	
						case JMPIGT: // If RS>RC then PC <- k else PC++
								if(legal(reg[ir.r2]) && legal(reg[ir.r1])){
								if (reg[ir.r1] > reg[ir.r2]) {
									pc = ir.p;
								} else {
									pc++;
								}
								}
								else{
								irpt = Interrupts.intEnderecoInvalido;
								}
							 break; 

					// outras
						case STOP: // por enquanto, para execucao
							irpt = Interrupts.intSTOP;
							break;

						case DATA:
							irpt = Interrupts.intInstrucaoInvalida;
							break;

					// Chamada de sistema
					    case TRAP:
						     sysCall.handle(base);            // <<<<< aqui desvia para rotina de chamada de sistema, no momento so temos IO
							 pc++;
						     break;

					// Inexistente
						default:
							irpt = Interrupts.intInstrucaoInvalida;
							break;
					}
				}
			   // --------------------------------------------------------------------------------------------------
			   // VERIFICA INTERRUPÇÃO !!! - TERCEIRA FASE DO CICLO DE INSTRUÇÕES
				if (!(irpt == Interrupts.noInterrupt)) {   // existe interrupção
					ih.handle(irpt,pc);                       // desvia para rotina de tratamento
					break; // break sai do loop da cpu
				}
			}  // FIM DO CICLO DE UMA INSTRUÇÃO
		} 
		
		public int[] getReg() {
			return reg;
		}
	}
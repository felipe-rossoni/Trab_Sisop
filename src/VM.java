public class VM {
		public int tamMem;    
        public Word[] m;  
		public Memory mem;   
        public CPU cpu;    

        public VM(InterruptHandling ih, SysCallHandling sysCall){   
		 // vm deve ser configurada com endereço de tratamento de interrupcoes e de chamadas de sistema
	     // cria memória
		     tamMem = 1024;
  		 	 mem = new Memory(tamMem);
			 m = mem.m;
	  	 // cria cpu
			 cpu = new CPU(mem,ih,sysCall, true);                   // true liga debug
	    }	
	}
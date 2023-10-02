package system;

import system.syscall.Device;
import system.syscall.Keyboard;
import system.syscall.Screen;

public class VM {
		public int tamMem;    
        public Word[] m;  
		public Memory mem;   
        public CPU cpu;
        public Device[] devices;

        public VM(InterruptHandling ih, SysCallHandling sysCall){   
		 // vm deve ser configurada com endereço de tratamento de interrupcoes e de chamadas de sistema
	     // cria memória
		     tamMem = 1024;
  		 	 mem = new Memory(tamMem);
			 m = mem.m;
	  	 // cria cpu
			 cpu = new CPU(mem,ih,sysCall, true);                   // true liga debug
			 
			 loadDevices();
	    }
        
        private void loadDevices() {
        	System.out.println("Loading devices....");
        	devices = new Device[2];
        	
        	System.out.println("Loading screen....");
        	devices[0] = new Screen(0, 9);
        	
        	System.out.println("Loading keyboard....");
        	devices[1] = new Keyboard(1, 9);
        }
        
	}
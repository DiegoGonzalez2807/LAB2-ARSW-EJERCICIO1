package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	private long inicial;
	private final List<Integer> primes;
        private final static int TMILISECONDS = 5000;
       private boolean boolWait;
        private long fin;
	
	public PrimeFinderThread(int a, int b, List<Integer> primes) {
		super();
                this.primes = primes;
		this.a = a;
		this.b = b;
	}

        /**
         * Funcion generada para correr el hilo. Esta funcion abarca los procesos
         * de sincronizar la lista de primos de tal manera que no haya errores por
         * los hilos. Si no se cumple el tiempo, revisa si el numero es primo y lo inserta
         */
        @Override
	public void run(){
          
            //Se dejan dos variables de tiempo, la variable fin se va actualizando
            //hasta que lleguemos a N segundos. 
            this.inicial = System.currentTimeMillis();
            this.fin = System.currentTimeMillis();
            synchronized(this.primes){
                for (int i= a;i < b;i++){ 
                    /**Aun no cumple los N segundos*/
                    if(this.fin - this.inicial <= TMILISECONDS){
                        this.boolWait = false;
                       // System.out.println(this.fin-this.inicial);
                        if(isPrime(i)){
                            this.primes.add(i);
                            //System.out.println("NO LLEGO AL LIMITE DE TIEMPO "+(this.fin-this.inicial));
                            System.out.println("TAMAÑO DE LA LISTA"+getPrimes().size());
                            //System.out.println(i);
                            this.fin = System.currentTimeMillis();
                        }
                    }
                    /**Cumple los N segundos */
                    else{
                        System.out.println("LLEGO AL LIMITE DE TIEMPO "+(this.fin-this.inicial));
                        this.boolWait = true;
                        try{
                            /**No deja que inserten mas valores a la lista de primos*/
                            this.primes.wait();
                        } catch (InterruptedException ex){}
                    }  
                }
            }  
	}
        
        public boolean waiting(){
            return boolWait;
        }
        
        public void reRun(){
            this.boolWait = false;
        }
        
        public void setInicial(long num){
            this.inicial = num;
        }
        
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	
}


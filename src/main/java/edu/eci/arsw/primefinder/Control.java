/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 10000000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    
    private List<Integer> primes = new LinkedList<>();
    
   
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, this.primes);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, this.primes);
    }
    
    public static Control newControl() {
        return new Control();
    }
    
    public void reRun(){
        for(PrimeFinderThread thread: this.pft){
            thread.reRun();
            thread.setInicial(System.currentTimeMillis());
            System.out.println("Hilo queda en "+thread.waiting());
        }
    }
    
    public boolean isFinish(){
        boolean bFinish = false;
        for(PrimeFinderThread thread:pft){
            bFinish |= thread.isAlive();
        }
        return !bFinish;
    }

    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();   
        }
        /**Mientras que siga corriendo el codigo*/
        while(true &&
                !this.pft[pft.length-1].waiting()){
            synchronized(this.primes){
                
                if(isFinish()){break;}
                if(this.pft[pft.length-1].waiting()){
                    System.out.println("Primos encontrados hasta el momento:"+" "+this.primes.size());
                    System.out.println("Hilos suspendidos, por favor pulse la tecla ENTER");
                    String next = scan.nextLine();
                    if(next.isEmpty()){
                        System.out.println("Corren de nuevo");
                        //Como estamos manejando una variable de revision si corren o esperan, en esta parte
                        //decimos que la espera es falsa para cada hilo y asi no haya problemas corriendo
                        reRun();
                        this.primes.notifyAll();
                        
                        System.out.println("Primos encontrados hasta el momento:"+" "+this.primes.size());
                    }
                }
            }
        }
        System.out.println("Primos totales: "+this.primes.size());
    }
    
}

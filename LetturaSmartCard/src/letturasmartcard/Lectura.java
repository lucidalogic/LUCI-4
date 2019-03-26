/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package letturasmartcard;



/**
 *
 * @author Lucida
 */
public class Lectura {
    boolean flag =false;
    static int time =1000;
     public static void main(String[] args) throws InterruptedException{
         while (time <10000){
             
             try {
                // LetturaSmartCard lectura = new LetturaSmartCard();
                LogginCardTerminal prueba = new LogginCardTerminal();
                //terminal.System.out.println("Private key: " + )(
               // LogginCardTerminal.
                System.out.println("Private key: "  );  
                 if (prueba.isCardPresent()){
                     System.out.println("nombre: "+ prueba.getName());
                     break;
                 }
                 else{
                 //lectura.main(args);
                     
                 LogginCardTerminal sistema = new LogginCardTerminal();
                 sistema.getPassword(args);
                 System.out.println("S.O: "+sistema.getOperativeSystem());
                 System.out.println("Esperando...");   
                 Thread.sleep(1000);
                 }
                 
             } catch (Exception e) {
                 LogginCardTerminal sistema = new LogginCardTerminal();
                 System.out.println("S.O: "+sistema.getOperativeSystem());
                 System.out.println("Esperando...");
                 Thread.sleep(1000);
                // break;
             }
             time+=1000;
             
         }
     }
   
}

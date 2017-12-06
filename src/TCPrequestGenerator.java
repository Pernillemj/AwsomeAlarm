/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author irl
 */
public class TCPrequestGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        for(int i=1;i<=1;i++){
             
            ClientThread temp= new ClientThread();
            temp.start();
            System.out.println("Started client:" + i);
       }
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author irl
 */
public class ClientThread extends Thread{
  
    @Override
    public void run(){
    
        Socket socket = null;
        try {
            socket = new Socket("localhost",9999);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        PrintStream pr = null;
        try {
            pr = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        // register user
        //String tmp = "01;Emil;Emil@mig.dk;password";

        // LogIn
        //String tmp = "02;emil@mig.dk;password";

        // register device
        // TODO: update med register to user
        //String tmp = "03;0987654321;54.7657,23.4567;Gangen;29";

        // Request devices for user ID
        // String tmp = "04;29";

        // Get current state
        //String tmp = "05;1234567890";

        // Get logged state
        String tmp = "06;0987654321";

        // Turnoff alarm
        //String tmp = "07;0987654321";

        // Register device to user
        //String tmp = "08;0987654321;29";


        pr.println(tmp);
        
        System.out.println("sending: " + tmp + "\r");
        
        //check for reply
        
        Reader rs = null;
        try {
            rs = new InputStreamReader(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedReader br =  new BufferedReader(rs);
        
        try {
            System.out.println("receiving: " + br.readLine() + "\r");
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

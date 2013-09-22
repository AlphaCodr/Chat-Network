/**
 *
 * @author Guest
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    
    static String username, ip;
    static Socket clientSocket;
    static ObjectInputStream inputStream;
    static ObjectOutputStream outputStream;
    
    static DataPackage dataPackage;
    
    static Runnable connectToServer = new Runnable(){
        @Override
        public void run(){
            try{
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                dataPackage.setState(0);
                outputStream.writeObject(dataPackage);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, 
                        "Error: Could not connect to Server", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            updateDataPackage();
        }
    };
    
    public static void main(String[] args){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error: (Could not set Look And Feel)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        try{
            username = System.getProperty("user.name");
            ip = InetAddress.getLocalHost().getHostAddress();
            clientSocket = new Socket(ip, 27011);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error: ", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        DataPackage dataPackage = new DataPackage(username, ip, 1);
        new Thread(connectToServer).start();
    }
    
    public static void updateDataPackage(){
        try{
            outputStream.writeObject(dataPackage);
            Thread.sleep(60000);
            updateDataPackage();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void changeName(String newName){
        username = newName;
    }
    
}

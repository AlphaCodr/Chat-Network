/**
 *
 * @author Guest
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    
    static String serverUsername;
    
    static String serverIP;
    static ServerSocket serverSocket;
    static Socket clientSocket;
    static InetAddress addressVariable;
    static byte[] addressByte = {(byte) 192, (byte) 168, (byte) 1, (byte) 3};
    
    static ArrayList<Socket> clientSocketList;
    static ArrayList<Integer> clientStatesList;
    static ArrayList<DataPackage> clientDataPackageList;
    static ArrayList<String> blockedList;
    static DefaultListModel listModel;
    
    static ObjectOutputStream outputStream;
    static ObjectInputStream inputStream;
    
    static Runnable acceptClients = new Runnable(){
        @Override
        public void run(){
            while(true){
                try{
                    clientSocket = serverSocket.accept();
                    inputStream = new ObjectInputStream(clientSocket.getInputStream());
                    outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    
                    DataPackage dataPackage = (DataPackage) inputStream.readObject();
                    if(!checkIfBlocked(dataPackage)){
                        int state = dataPackage.getState();
                        clientSocketList.add(clientSocket);
                        clientDataPackageList.add(dataPackage);
                        clientStatesList.add(state);
                        listModel.addElement(dataPackage.getUsername());
                    }else if(checkIfBlocked(dataPackage)){
                        
                    }
                    
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Error: Could not Accept Client", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    };
    
    static Runnable updateClientStates = new Runnable(){
        @Override
        public void run(){
            while(true){
                try{
                    for(int index = 0; index < clientStatesList.size(); index++){
                        int currentState = clientStatesList.get(index);
                        String clientUsername = clientDataPackageList.get(index).getUsername();
                        if(currentState == 0){
                            //Client Online
                        }else if(currentState == 1){
                            removeClient(index);
                        }else if(currentState == 2){
                            removeClient(index);
                            addToBlockedList(clientDataPackageList.get(index));
                        }else{
                            JOptionPane.showMessageDialog(null, 
                                    "Error: " + clientUsername + " State Unrecognized", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Error: Update Thread (1)");
                }
            }
        }
    };
    
    static Runnable updateCientData = new Runnable(){
        @Override
        public void run(){
            while(true){
                try{
                    for(int index = 0; index < clientDataPackageList.size(); index++){
                        int currentState = clientStatesList.get(index);
                        if(currentState == 1 || currentState == 2){
                            removeClient(index);
                        }
                        Socket socket = clientSocketList.get(index);
                        inputStream = new ObjectInputStream(socket.getInputStream());
                        DataPackage dataPackage = (DataPackage) inputStream.readObject();
                        clientDataPackageList.add(index, dataPackage);
                        
                        
                    }
                }catch(Exception ex){
                    
                }
            }
        }
    };
    
    public static void main(String[] args){
        //Setting the Look and Feel to the System Look And Feel
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error: (Could Not set Look And Feel)", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        //Setting up Server Settings and socket Settings 
        try{
            serverUsername = System.getProperty("user.name");
            serverIP = InetAddress.getLocalHost().getHostAddress();
            try{
                addressVariable = InetAddress.getByAddress(addressByte);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Error: Could not get Address Variable", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            serverSocket = new ServerSocket(27011);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        MiniServerFrame serverFrame = new MiniServerFrame("Server Frame: " + serverUsername, 400, 500, serverIP){
            @Override
            public void blockClient(int index) {
                String blockedClientIP = clientDataPackageList.get(index).getIP();
                blockedList.add(index, blockedClientIP);
                removeClient(index);
            }
        };
        serverFrame.setVisible(true);
        listModel = (DefaultListModel) serverFrame.getClientListModel();
        
        new Thread(acceptClients).start();
        
        
    }
    
    public static void removeClient(int index){
        clientDataPackageList.remove(index);
        clientStatesList.remove(index);
        clientSocketList.remove(index);
    }
    
    public static void addToBlockedList(DataPackage dataPackage){
        blockedList = new ArrayList<String>();
        blockedList.add(dataPackage.getIP());
    }
    
    public static boolean checkIfBlocked(DataPackage dataPackage){
        for(String clientIP : blockedList){
            if(clientIP == dataPackage.getIP()){
                return true;
            }
        }
        return false;
    }
    
    public byte[] getAddressByte(){
        return null;
    }
    
}

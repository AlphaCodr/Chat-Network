/**
 *
 * @author Guest
 */

import java.awt.*;

public class DataPackage {
    
    private String clientName, clientIP;
    private int state;
    private Image profilePhoto;
    
    public DataPackage(String name, String ip, int currentState){
        this.clientName = name;
        this.clientIP = ip;
        this.state = currentState;
    }
    
    public void addProfilePhoto(Image photo){
        this.profilePhoto = photo;
    }
    
    public void setUsername(String name){
        this.clientName = name;
    }
    
    public void setIP(String ip){
        this.clientIP = ip;
    }
    
    public void setState(int currentState){
        this.state = currentState;
    }
    
    public Image getProfilePhoto(){
        return this.profilePhoto;
    }
    
    public String getUsername(){
        return this.clientName;
    }
    
    public String getIP(){
        return this.clientIP;
    }
    
    public int getState(){
        return this.state;
    }
    
}

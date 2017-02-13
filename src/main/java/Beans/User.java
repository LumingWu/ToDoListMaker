package Beans;

import java.io.Serializable;

/**
 * Editor: Luming Wu
 */
public class User implements Serializable{

    private String key;
    private String username;
    private String password;

    public User(){

    }

    public String getKey(){
        return key;
    }

    public void setKey(String i){
        key = i;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String u){
        username = u;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String p){
        password = p;
    }

}

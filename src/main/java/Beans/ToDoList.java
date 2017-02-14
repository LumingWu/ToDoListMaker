package Beans;

import java.io.Serializable;

/**
 * Editor: Luming Wu
 */
public class ToDoList implements Serializable {
    
    private String key;
    private String name;
    private String owner;
    private String type;
    private String editable;
    private String useremail;
    
    public ToDoList() {
        
    }
    
    public String getKey(){
        return key;
    }
    
    public void setKey(String i){
        key = i;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String n){
        name = n;
    }
    
    public String getOwner(){
        return owner;
    }
    
    public void setOwner(String o){
        owner = o;
    }
    
    public String getType(){
        return type;
    }
    
    public void setType(String t){
        type = t;
    }
    
    public String getEditable(){
        return editable;
    }
    
    public void setEditable(String e){
        editable = e;
    }
    
    public String getUserEmail(){
        return useremail;
    }
    
    public void setUserEmail(String u){
        useremail = u;
    }
    
}

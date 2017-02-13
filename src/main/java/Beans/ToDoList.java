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
    
}

package Beans;

import java.io.Serializable;

/**
 *
 * Editor: Luming Wu
 */
public class ToDo implements Serializable {
    
    private String key;
    private String category;
    private String description;
    private String startdate;
    private String enddate;
    private String complete;
    private String todolistkey;
    
    public ToDo() {
        
    }
    
    public String getKey(){
        return key;
    }
    
    public void setKey(String i){
        key = i;
    }
    
    public String getCategory(){
        return category;
    }
    
    public void setCategory(String c){
        category = c;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setDescription(String d){
        description = d;
    }
    
    public String getStartDate(){
        return startdate;
    }
    
    public void setStartDate(String s){
        startdate = s;
    }
    
    public String getEndDate(){
        return enddate;
    }
    
    public void setEndDate(String e){
        enddate = e;
    }
    
    public String getComplete(){
        return complete;
    }
    
    public void setComplete(String c){
        complete = c;
    }
    
    public String getToDoListKey(){
        return todolistkey;
    }
    
    public void setToDoListKey(String t){
        todolistkey = t;
    }
    
}

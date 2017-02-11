package Beans;

import com.google.appengine.api.datastore.Entity;

/**
 * Editor: Luming Wu
 */
public class User {

    private String index;
    private String username;
    private String password;

    public User(){

    }

    public String getIndex(){
        return index;
    }

    public void setIndex(String i){
        index = i;
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

    public void fromEntity(Entity e){

    }

    public Entity toEntity(){
        Entity e = new Entity("User");
        e.setProperty("username", username);
        e.setProperty("password", password);
        return e;
    }

}

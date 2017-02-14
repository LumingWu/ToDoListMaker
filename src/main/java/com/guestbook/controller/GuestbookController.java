package com.guestbook.controller;

import Beans.ToDo;
import Beans.ToDoList;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
public class GuestbookController {

    @RequestMapping("/")
    public String home(HttpServletRequest request) {
        // Get user
        UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        HttpSession session = request.getSession();
        // Login or empty
        if (currentUser == null) {
            session.setAttribute("loginstatus", false);
            session.setAttribute("authurl", userService.createLoginURL("/"));
        } else {
            // Load Head
            session.setAttribute("user", currentUser);
            session.setAttribute("loginstatus", true);
            session.setAttribute("authurl", userService.createLogoutURL("/logout"));
            // Load Public ToDoLists
            ToDoList tdl1 = new ToDoList();
            ToDoList tdl2 = new ToDoList();
            tdl1.setName("ToDoList1");
            tdl2.setName("ToDoList2");
            tdl1.setOwner("John");
            tdl2.setOwner("Daniel");
            tdl1.setEditable("true");
            tdl2.setEditable("true");
            ToDoList[] publictdls = new ToDoList[]{tdl1, tdl2};
            session.setAttribute("public", publictdls);
            // Load Private ToDoLists
            ToDoList tdl3 = new ToDoList();
            ToDoList tdl4 = new ToDoList();
            tdl3.setName("ToDoList3");
            tdl4.setName("ToDoList4");
            tdl3.setOwner("John");
            tdl4.setOwner("John");
            tdl3.setEditable("true");
            tdl4.setEditable("true");
            ToDoList[] privatetdls = new ToDoList[]{tdl3, tdl4};
            session.setAttribute("private", privatetdls);
        }
        return "ToDoListMaker";
    }
    
    @RequestMapping("/logout")
    public String signout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
    
    @RequestMapping(value="/viewtodolist", produces="text/html")
    @ResponseBody
    public String gettodolist(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            HttpSession session){
        ToDo td1 = new ToDo();
        td1.setCategory("Sample1");
        td1.setDescription("Sample2");
        td1.setStartDate("Sample3");
        td1.setEndDate("Sample4");
        td1.setComplete("true");
        ToDo td2 = new ToDo();
        td2.setCategory("Sample5");
        td2.setDescription("Sample6");
        td2.setStartDate("Sample7");
        td2.setEndDate("Sample8");
        td2.setComplete("true");
        ToDo[] todos = new ToDo[]{td1, td2};
        session.setAttribute("todos", todos);
        String html = "";
        for(int i = 0; i < todos.length; i++){
            html =  html + "<tr>"
                    + "<td>" + todos[i].getCategory() + "</td>"
                    + "<td>" + todos[i].getDescription() + "</td>"
                    + "<td>" + todos[i].getStartDate() + "</td>"
                    + "<td>" + todos[i].getEndDate() + "</td>"
                    + "<td>" + todos[i].getComplete() + "</td>"
                    + "<input type='hidden' value='" + i + "'>"
                    + "</tr>";
        }
        return html;
    }
    // Return '' if failed, ok if success.
    @RequestMapping(value="/deletetodolist", produces="text/plain")
    @ResponseBody
    public String deletetodolist(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            HttpSession session){
        ToDoList[] publictdl = (ToDoList[])session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[])session.getAttribute("private");
        /* need to change later */
        return "ok";
    }
    //Return '' if failed, index if success.
    @RequestMapping(value="/addtodolist", produces="text/plain")
    @ResponseBody
    public String addtodolist(
            @RequestParam("type") String type,
            HttpSession session){
        ToDoList[] publictdl = (ToDoList[])session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[])session.getAttribute("private");
        // Get user
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        // Get datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Create todolist
        ToDoList tdl = new ToDoList();
        tdl.setName("");
        tdl.setOwner(user.getNickname());
        tdl.setEditable("true");
        tdl.setUserEmail(user.getEmail());
        // Check public or private
        int length = 0;
        if(type.equals("public")){
            ToDoList[] newlist = Arrays.copyOf(publictdl, publictdl.length + 1);
            length = newlist.length;
            tdl.setType("public");
            newlist[length - 1] = tdl;
            session.setAttribute("public", newlist);
        }
        if(type.equals("private")){
            ToDoList[] newlist = Arrays.copyOf(publictdl, privatetdl.length + 1);
            length = newlist.length;
            tdl.setType("private");
            newlist[length - 1] = tdl;
            session.setAttribute("private", newlist);
        }
        Entity todolist = new Entity("ToDoList");
        todolist.setProperty("name", tdl.getName());
        todolist.setProperty("owner", tdl.getOwner());
        todolist.setProperty("type", tdl.getType());
        todolist.setProperty("useremail", tdl.getUserEmail());
        datastore.put(todolist);
        /* need to change later */
        return "" + length;
    }
    //Return '' if failed, index if success.
    @RequestMapping(value="/addtodo", produces="text/plain")
    @ResponseBody
    public String addtodo(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            HttpSession session){
        ToDoList[] publictdl = (ToDoList[])session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[])session.getAttribute("private");
        ToDo[] todos = (ToDo[])session.getAttribute("todos");
        // Get user
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        // Get datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Create todo
        ToDo td = new ToDo();
        td.setCategory("");
        td.setDescription("");
        td.setStartDate("");
        td.setEndDate("");
        td.setComplete("");
        // Fill ToDoList key
        if(type.equals("public") && publictdl[index].getEditable().equals("true")){
            td.setToDoListKey(publictdl[index].getKey());
        }
        else if(type.equals("private") && privatetdl[index].getEditable().equals("true")){
            td.setToDoListKey(privatetdl[index].getKey());
        }
        else{
            return "";
        }
        // Renew the todolist
        ToDo[] newlist = Arrays.copyOf(todos, todos.length + 1);
        newlist[newlist.length - 1] = td;
        session.setAttribute("todos", newlist);
        // Create Entity
        Entity todo = new Entity("ToDo");
        todo.setProperty("category", td.getCategory());
        todo.setProperty("description", td.getDescription());
        todo.setProperty("startdate", td.getStartDate());
        todo.setProperty("enddate", td.getEndDate());
        todo.setProperty("complete", td.getComplete());
        todo.setProperty("todolistkey", td.getToDoListKey());
        /* need to change later */
        return "" + newlist.length;
    }
    // Return '' if failed, ok if success.
    @RequestMapping(value="/deletetodo", produces="text/plain")
    @ResponseBody
    public String deletetodo(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            @RequestParam("index2") int index2,
            HttpSession session){
        ToDoList[] publictdl = (ToDoList[])session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[])session.getAttribute("private");
        /* need to change later */
        return "ok";
    }
    
    @RequestMapping(value="/modifytodolist", produces="text/plain")
    @ResponseBody
    public String modifytodolist(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            HttpSession session
        ){
        ToDoList[] publictdl = (ToDoList[])session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[])session.getAttribute("private");
        
        return "ok";
    }
    
    @RequestMapping(value="/modifytodo", produces="text/plain")
    @ResponseBody
    public String modifytodo(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            @RequestParam("index2") int index2,
            HttpSession session
        ){
        ToDoList[] publictdl = (ToDoList[])session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[])session.getAttribute("private");
        
        return "ok";
    }
    
}

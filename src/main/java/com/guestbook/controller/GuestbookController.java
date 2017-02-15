package com.guestbook.controller;

import Beans.ToDo;
import Beans.ToDoList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entities;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.Arrays;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
            // Load datastore
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            // Set up query filters
            Filter emailfilter = new FilterPredicate("useremail", FilterOperator.EQUAL, currentUser.getEmail());
            Filter publicfilter = new FilterPredicate("type", FilterOperator.EQUAL, "public");
            Filter privatefilter = new FilterPredicate("type", FilterOperator.EQUAL, "private");
            CompositeFilter privateemailfilter = CompositeFilterOperator.and(emailfilter, privatefilter);
            // Load Public ToDoLists
            Query publictdlquery = new Query("ToDoList").setFilter(publicfilter);
            PreparedQuery publictdlpquery = datastore.prepare(publictdlquery);
            LinkedList<ToDoList> publictdls = new LinkedList<ToDoList>();
            for (Entity publictdl : publictdlpquery.asIterable()) {
                ToDoList tdl = new ToDoList();
                tdl.setKey(KeyFactory.keyToString(publictdl.getKey()));
                if (((String) publictdl.getProperty("useremail")).equals(currentUser.getEmail())) {
                    tdl.setEditable("true");
                } else {
                    tdl.setEditable("false");
                }
                tdl.setName((String) publictdl.getProperty("name"));
                tdl.setOwner((String) publictdl.getProperty("owner"));
                tdl.setUserEmail((String) publictdl.getProperty("useremail"));
                publictdls.add(tdl);
            }
            session.setAttribute("public", publictdls.toArray(new ToDoList[publictdls.size()]));
            // Load Private ToDoLists
            Query privatetdlquery = new Query("ToDoList").setFilter(privateemailfilter);
            PreparedQuery privatetdlpquery = datastore.prepare(privatetdlquery);
            LinkedList<ToDoList> privatetdls = new LinkedList<ToDoList>();
            for (Entity privatetdl : privatetdlpquery.asIterable()) {
                ToDoList tdl = new ToDoList();
                tdl.setKey(KeyFactory.keyToString(privatetdl.getKey()));
                tdl.setEditable("true");
                tdl.setName((String) privatetdl.getProperty("name"));
                tdl.setOwner((String) privatetdl.getProperty("owner"));
                tdl.setUserEmail((String) privatetdl.getProperty("useremail"));
                privatetdls.add(tdl);
            }
            session.setAttribute("private", privatetdls.toArray(new ToDoList[privatetdls.size()]));
        }
        return "ToDoListMaker";
    }

    @RequestMapping("/logout")
    public String signout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(value = "/viewtodolist", produces = "text/html")
    @ResponseBody
    public String gettodolist(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            HttpSession session) {
        // Get datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Set up queryfilters
        ToDoList tdl;
        if (type.equals("public")) {
            tdl = ((ToDoList[]) session.getAttribute("public"))[index];
        } else if (type.equals("private")) {
            tdl = ((ToDoList[]) session.getAttribute("private"))[index];
        } else {
            return "";
        }
        Filter todolistkeyfilter = new FilterPredicate("todolistkey", FilterOperator.EQUAL, tdl.getKey());
        // Query
        Query todolistquery = new Query("ToDo").setFilter(todolistkeyfilter);
        PreparedQuery todolistpquery = datastore.prepare(todolistquery);
        LinkedList<ToDo> todos = new LinkedList<ToDo>();
        for (Entity todo : todolistpquery.asIterable()) {
            ToDo td = new ToDo();
            td.setKey(KeyFactory.keyToString(todo.getKey()));
            td.setToDoListKey(tdl.getKey());
            td.setCategory((String) todo.getProperty("category"));
            td.setDescription((String) todo.getProperty("description"));
            td.setStartDate((String) todo.getProperty("startdate"));
            td.setEndDate((String) todo.getProperty("enddate"));
            td.setComplete((String) todo.getProperty("complete"));
            todos.add(td);
        }
        ToDo[] tds = todos.toArray(new ToDo[todos.size()]);
        session.setAttribute("todos", tds);
        String html = "";
        for (int i = 0; i < tds.length; i++) {
            html = html + "<tr>"
                    + "<td>" + tds[i].getCategory() + "</td>"
                    + "<td>" + tds[i].getDescription() + "</td>"
                    + "<td>" + tds[i].getStartDate() + "</td>"
                    + "<td>" + tds[i].getEndDate() + "</td>"
                    + "<td>" + tds[i].getComplete() + "</td>"
                    + "<input type='hidden' value='" + i + "'>"
                    + "</tr>";
        }
        return html;
    }

    // Return '' if failed, ok if success.
    @RequestMapping(value = "/deletetodolist", produces = "text/plain")
    @ResponseBody
    public String deletetodolist(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            HttpSession session) {
        // Get datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Set up queryfilters
        ToDoList tdl;
        if (type.equals("public")) {
            datastore.delete(KeyFactory.stringToKey(((ToDoList[]) session.getAttribute("public"))[index].getKey()));
            return "ok";
        } else if (type.equals("private")) {
            datastore.delete(KeyFactory.stringToKey(((ToDoList[]) session.getAttribute("private"))[index].getKey()));
            return "ok";
        }
        return "";
    }

    //Return '' if failed, index if success.
    @RequestMapping(value = "/addtodolist", produces = "text/plain")
    @ResponseBody
    public String addtodolist(
            @RequestParam("type") String type,
            HttpSession session) {
        ToDoList[] publictdl = (ToDoList[]) session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[]) session.getAttribute("private");
        // Get user
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        // Get datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Create todolist
        Key generatekey = KeyFactory.createKey("ToDoList", "" + user.getEmail() + System.nanoTime());
        System.out.println("------Key kind: " + generatekey.getKind() + ", name: " + generatekey.getName());
        Entity todolist = new Entity("ToDoList", generatekey);
        ToDoList tdl = new ToDoList();
        tdl.setKey(KeyFactory.keyToString(generatekey));
        tdl.setName("");
        tdl.setOwner("");
        tdl.setEditable("true");
        tdl.setUserEmail(user.getEmail());
        // Check public or private
        int length = 0;
        if (type.equals("public")) {
            ToDoList[] newlist = Arrays.copyOf(publictdl, publictdl.length + 1);
            length = newlist.length;
            tdl.setType("public");
            newlist[length - 1] = tdl;
            session.setAttribute("public", newlist);
        }
        else if (type.equals("private")) {
            ToDoList[] newlist = Arrays.copyOf(privatetdl, privatetdl.length + 1);
            length = newlist.length;
            tdl.setType("private");
            newlist[length - 1] = tdl;
            session.setAttribute("private", newlist);
        }
        else{
            System.out.println("-----Type: " + type);
            return "";
        }
        todolist.setProperty("name", tdl.getName());
        todolist.setProperty("owner", tdl.getOwner());
        todolist.setProperty("type", tdl.getType());
        todolist.setProperty("useremail", tdl.getUserEmail());
        System.out.println("-----About to add");
        datastore.put(todolist);
        System.out.println("-----Add data base success");
        /* need to change later */
        return "" + (length - 1);
    }

    //Return '' if failed, index if success.
    @RequestMapping(value = "/addtodo", produces = "text/plain")
    @ResponseBody
    public String addtodo(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            HttpSession session) {
        ToDoList[] publictdl = (ToDoList[]) session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[]) session.getAttribute("private");
        ToDo[] todos = (ToDo[]) session.getAttribute("todos");
        // Get user
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        // Get datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Create todo
        Key generatekey = KeyFactory.createKey("ToDo", "" + user.getEmail() + System.nanoTime());
        Entity todo = new Entity("ToDo", generatekey);
        ToDo td = new ToDo();
        td.setKey(KeyFactory.keyToString(generatekey));
        td.setCategory("");
        td.setDescription("");
        td.setStartDate("");
        td.setEndDate("");
        td.setComplete("");
        // Fill ToDoList key
        if (type.equals("public") && publictdl[index].getEditable().equals("true")) {
            td.setToDoListKey(publictdl[index].getKey());
        } else if (type.equals("private") && privatetdl[index].getEditable().equals("true")) {
            td.setToDoListKey(privatetdl[index].getKey());
        } else {
            return "";
        }
        // Renew the todolist
        ToDo[] newlist = Arrays.copyOf(todos, todos.length + 1);
        newlist[newlist.length - 1] = td;
        session.setAttribute("todos", newlist);
        // Create Entity
        todo.setProperty("category", td.getCategory());
        todo.setProperty("description", td.getDescription());
        todo.setProperty("startdate", td.getStartDate());
        todo.setProperty("enddate", td.getEndDate());
        todo.setProperty("complete", td.getComplete());
        todo.setProperty("todolistkey", td.getToDoListKey());
        datastore.put(todo);
        /* need to change later */
        return "" + (newlist.length - 1);
    }

    // Return '' if failed, ok if success.
    @RequestMapping(value = "/deletetodo", produces = "text/plain")
    @ResponseBody
    public String deletetodo(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            @RequestParam("index2") int index2,
            HttpSession session) {
        // Get datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Get useful attributes
        ToDoList[] publictdl = (ToDoList[]) session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[]) session.getAttribute("private");
        // Remove
        if((type.equals("public") && publictdl[index].getEditable().equals("true"))
                || (type.equals("private") && privatetdl[index].getEditable().equals("true"))){
            ToDo[] tdl = (ToDo[])session.getAttribute("todos");
            ToDo td = tdl[index2];
            datastore.delete(KeyFactory.stringToKey(td.getKey()));
            tdl[index2] = null;
            session.setAttribute("todos", tdl);
            return "ok";
        }
        return "";
    }

    @RequestMapping(value = "/modifytodolist", produces = "text/plain")
    @ResponseBody
    public String modifytodolist(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            @RequestParam("name") String name,
            @RequestParam("owner") String owner, 
            HttpSession session
    ) throws EntityNotFoundException {
        System.out.println("/modifytodolist" + ", index=" + index + ", name=" + name + ", owner=" + owner);
        // Get datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Useful attributes
        ToDoList[] publictdl = (ToDoList[]) session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[]) session.getAttribute("private");
        if(type.equals("public")){
            ToDoList tdl = publictdl[index];
            tdl.setName(name);
            tdl.setOwner(owner);
            session.setAttribute("public", publictdl);
            // Store
            System.out.println("-----ToDoListKey: " + KeyFactory.stringToKey(tdl.getKey()));
            Entity todolist = datastore.get(KeyFactory.stringToKey(tdl.getKey()));
            todolist.setProperty("name", tdl.getName());
            todolist.setProperty("owner", tdl.getOwner());
            datastore.put(todolist);
            return "ok";
        }
        else if(type.equals("private")){
            ToDoList tdl = privatetdl[index];
            tdl.setName(name);
            tdl.setOwner(owner);
            session.setAttribute("private", privatetdl);
            // Store
            System.out.println("-----ToDoListKey: " + KeyFactory.stringToKey(tdl.getKey()));
            Entity todolist = datastore.get(KeyFactory.stringToKey(tdl.getKey()));
            todolist.setProperty("name", tdl.getName());
            todolist.setProperty("owner", tdl.getOwner());
            datastore.put(todolist);
            return "ok";
        }
        return "";
    }

    @RequestMapping(value = "/modifytodo", produces = "text/plain")
    @ResponseBody
    public String modifytodo(
            @RequestParam("type") String type,
            @RequestParam("index") int index,
            @RequestParam("index2") int index2,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("startdate") String startdate,
            @RequestParam("enddate") String enddate,
            @RequestParam("complete") String complete,
            HttpSession session
    ) throws EntityNotFoundException {
        System.out.println("/modifytodo" + ", index=" + index + ", index2=" + index2 + ", category=" + category
        + ", description=" + description
        + ", startdate=" + startdate
        + ", complete=" + complete
        + ", enddate=" + enddate);
        // Get datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        // Useful attributes
        ToDoList[] publictdl = (ToDoList[]) session.getAttribute("public");
        ToDoList[] privatetdl = (ToDoList[]) session.getAttribute("private");
        if((type.equals("public") && publictdl[index].getEditable().equals("true"))
                || (type.equals("private") && privatetdl[index].getEditable().equals("true"))){
            ToDo[] todos = (ToDo[])session.getAttribute("todos");
            ToDo todo = todos[index2];
            todo.setCategory(category);
            todo.setDescription(description);
            todo.setStartDate(startdate);
            todo.setEndDate(enddate);
            todo.setComplete(complete);
            session.setAttribute("todos", todos);
            // Store
            System.out.println("-----ToDoKey: " + KeyFactory.stringToKey(todo.getKey()));
            Entity td = datastore.get(KeyFactory.stringToKey(todo.getKey()));
            td.setProperty("category", todo.getCategory());
            td.setProperty("description", todo.getDescription());
            td.setProperty("startdate", todo.getStartDate());
            td.setProperty("enddate", todo.getEndDate());
            td.setProperty("complete", todo.getComplete());
            td.setProperty("todolistkey", todo.getToDoListKey());
            datastore.put(td);
            return "ok";
        }
        return "";
    }

}

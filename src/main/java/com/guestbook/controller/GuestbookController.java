package com.guestbook.controller;

import Beans.ToDo;
import Beans.ToDoList;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"user", "loginstatus", "public", "private", "todos"})
public class GuestbookController {

    @RequestMapping("/")
    public ModelAndView home() {
        // Get user
        UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        ModelAndView mav = new ModelAndView("ToDoListMaker");
        // Login or empty
        if (currentUser == null) {
            mav.getModelMap().addAttribute("loginstatus", false);
            mav.getModelMap().addAttribute("authurl", userService.createLoginURL("/"));
        } else {
            // Load Head
            mav.getModelMap().addAttribute("user", currentUser);
            mav.getModelMap().addAttribute("loginstatus", true);
            mav.getModelMap().addAttribute("authurl", userService.createLogoutURL("/logout"));
            // Load Public ToDoLists
            ToDoList tdl1 = new ToDoList();
            ToDoList tdl2 = new ToDoList();
            tdl1.setName("ToDoList1");
            tdl2.setName("ToDoList2");
            tdl1.setOwner("John");
            tdl2.setOwner("Daniel");
            ToDoList[] publictdls = new ToDoList[]{tdl1, tdl2};
            mav.getModelMap().addAttribute("public", publictdls);
            // Load Private ToDoLists
            ToDoList tdl3 = new ToDoList();
            ToDoList tdl4 = new ToDoList();
            tdl3.setName("ToDoList3");
            tdl4.setName("ToDoList4");
            tdl3.setOwner("John");
            tdl4.setOwner("John");
            ToDoList[] privatetdls = new ToDoList[]{tdl3, tdl4};
            mav.getModelMap().addAttribute("private", privatetdls);
        }
        return mav;
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
            @RequestParam("index") int index
            ){
        ModelAndView mav = new ModelAndView("ToDoList");
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
        mav.getModelMap().addAttribute("todos", todos);
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
    
    @RequestMapping(value="/deletetodolist", produces="text/html")
    @ResponseBody
    public String deletetodolist(
            @ModelAttribute("public") ToDoList[] publictdl,
            @ModelAttribute("private") ToDoList[] privatetdl,
            @RequestParam("type") String type,
            @RequestParam("index") int index){
        // Return '' if failed, ok if success.
        return "ok";
    }
    
    @RequestMapping(value="/addtodolist", produces="text/html")
    @ResponseBody
    public String addtodolist(
            @ModelAttribute("public") ToDoList[] publictdl,
            @ModelAttribute("private") ToDoList[] privatetdl,
            @RequestParam("type") String type){
        //Return '' if failed, index if success.
        return "3";
    }
    
    @RequestMapping("/sign")
    public String signGuestbook(
            @RequestParam(required = true, value = "guestbookName") String guestbookName,
            @RequestParam(required = true, value = "content") String content,
            Model model) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
        Date date = new Date();
        Entity greeting = new Entity("Greeting", guestbookKey);
        greeting.setProperty("user", user);
        greeting.setProperty("date", date);
        greeting.setProperty("content", content);

        DatastoreService datastore = DatastoreServiceFactory
                .getDatastoreService();
        datastore.put(greeting);

        model.addAttribute("guestbookName", guestbookName);
        return "guestbook";
    }
}

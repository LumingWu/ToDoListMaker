package com.guestbook.controller;

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

@Controller
public class GuestbookController {
	@RequestMapping("/")
	public ModelAndView home() {
		// Get user
		UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();
		currentUser = null;
		ModelAndView mav = new ModelAndView("ToDoListMaker");
		// Login or empty
		if(currentUser == null){
			mav.getModelMap().addAttribute("username", "");
		}
		else{
			// Static testing user
			mav.getModelMap().addAttribute("username", "John");
		}

		return mav;
	}

	@RequestMapping("/logout")
	public void logout() {
		UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();

	}

	@RequestMapping("/login")
	@ResponseBody
	public String listGuestbook(
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password
	) {
		// Confirm the data is correct
		User currentUser = null;

		return "{'username':'John'," +
				"'publictodolist':[" +
				"	{'name':'ToDo1', 'owner':'John'}," +
				"	{'name':'ToDo2', 'owner':'Daniel'}" +
				"	]," +
				"'privatetodolist':[" +
				"	{'name':'ToDo3', 'owner':'John'}," +
				"	{'name':'ToDo4', 'owner':'John'}" +
				"	]" +
				"}";
	}

	@RequestMapping(value = "/register", produces="text/plain")
	@ResponseBody
	public String register(
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password
	){
		return "ok";
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

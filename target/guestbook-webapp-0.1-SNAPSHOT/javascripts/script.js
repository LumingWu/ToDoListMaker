/**
 * Editor: Luming Wu
 */
$(document).ready(function(){

    var path = window.location.pathname;

    // HTML change require
    var userstatus = $("#userstatus");
    var public = $("#public");
    var private = $("#private");
    var todolist = $("#todolist");

    // Buttons
    var login = $("#login");
    var register = $("#register");
    var logout = $("#logout");

    // Inputs
    var username = $("#username");
    var password = $("#password");

    login.click(function(){
        $.getJSON(path + "/login" + "?username=" + encodeURI(username.val()) + "&password=" + encodeURI(password.val()),
        function(data){
            alert(JSON.stringify(data));
            if(data.username !== "") {
                userstatus.html("<span class='title2'>Welcome, data.username</span>" +
                    "<button onclick='logout()'><img src='images/Exit.png' alt='Exit'></button>");
                for(var obj in data.publictodolist){
                    public.append("<tr><td>" + obj.name + "</td><td>" + obj.owner + "</td></tr>");
                }
                for(var obj in data.privatetodolist){
                    private .append("<tr><td>" + obj.name + "</td><td>" + obj.owner + "</td></tr>");
                }

            }
        });
    });

    register.click(function(){
        $.post("register");
    });

    logout.click(function(){
        $.load(path + '/logout');
        userstatus.html("<input type='text' name='username' placeholder='username' id='username'>" +
            "<input type='text' name='password' placeholder='password' id='password'>" +
            "<button onclick='login()'>Login</button>" +
            "<button onclick='register()'>Register</button>");
    });

});
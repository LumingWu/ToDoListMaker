<!--
Editor: Luming Wu
-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>To Do List Maker</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="stylesheets/styles.css">
    <link rel="icon" type="image/ico" href="images/favicon.ico">
</head>
<body>
<div class="box" id="userstatus">
<c:choose>
    <c:when test="${username == ''}">
        <input type="text" name="username" placeholder="username" id="username">
        <input type="text" name="password" placeholder="password" id="password">
        <button id="login">Login</button>
        <button id="register">Register</button>
    </c:when>
    <c:otherwise>
        <span class="title2">Welcome, ${username}!</span>
        <button id="logout"><img src="images/Exit.png" alt="Exit"></button>
    </c:otherwise>
</c:choose>
</div>
<div class="box">
    <span class="title">To Do List</span>
    <div class="spacing10"></div>
    <div class="box">
        <span class="title2">Public</span><br>
        <div class="spacing10"></div>
        <button onclick="AddPublicToDoList"><img src="images/Add.png" alt="Add"></button>
        <button onclick="RemovePublicToDoList"><img src="images/Remove.png" alt="Remove"></button>
        <button onclick="MovePublicToDoListUp"><img src="images/MoveUp.png" alt="MoveUp"></button>
        <button onclick="MovePublicToDoListDown"><img src="images/MoveDown.png" alt="MoveDown"></button>
        <div class="spacing10"></div>
        <table id="public">
            <tr>
                <td>Name</td>
                <td>Owner</td>
            </tr>
        </table>
        <span class="title2">Private</span><br>
        <div class="spacing10"></div>
        <button onclick="AddPrivateToDoList"><img src="images/Add.png" alt="Add"></button>
        <button onclick="RemovePrivateToDoList"><img src="images/Remove.png" alt="Remove"></button>
        <button onclick="MovePrivateToDoListUp"><img src="images/MoveUp.png" alt="MoveUp"></button>
        <button onclick="MovePrivateToDoListDown"><img src="images/MoveDown.png" alt="MoveDown"></button>
        <div class="spacing10"></div>
        <table id="private">
            <tr>
                <td>Name</td>
                <td>Owner</td>
            </tr>
        </table>
    </div>
    <div class="spacing10"></div>
    <div class="box">
        <span class="title2">Items</span><br>
        <div class="spacing10"></div>
        <button onclick="AddToDo"><img src="images/Add.png" alt="Add"></button>
        <button onclick="RemoveToDo"><img src="images/Remove.png" alt="Remove"></button>
        <button onclick="MoveToDoUp"><img src="images/MoveUp.png" alt="MoveUp"></button>
        <button onclick="MoveToDoDown"><img src="images/MoveDown.png" alt="MoveDown"></button>
        <div class="spacing10"></div>
        <table id="todolist">
            <tr>
                <td>Category</td>
                <td>Description</td>
                <td>Start Date</td>
                <td>End Date</td>
                <td>Complete</td>
            </tr>
        </table>
    </div>
</div>
<script
        src="https://code.jquery.com/jquery-3.1.1.min.js"
        integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
        crossorigin="anonymous">
</script>
<script
        src="javascripts/script.js">
</script>
</body>
</html>

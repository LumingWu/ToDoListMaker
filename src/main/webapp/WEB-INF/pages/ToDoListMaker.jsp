<!-- Editor: Luming Wu -->
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
        <div class="box">
            <c:choose>
                <c:when test="${loginstatus}">
                    <span class="title2">Welcome! ${user.nickname}</span> 
                    <a href="${authurl}"><img src="images/Exit.png" alt="Sign Out"></a>
                    </c:when>
                    <c:otherwise>
                    <span class="title2">Welcome to To Do List Maker!</span> 
                    <a href="${authurl}">Sign In</a>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="box">
            <span class="title">To Do List</span>
            <div class="spacing10"></div>
            <div class="box">
                <span class="title2">Public</span><br>
                <div class="spacing10"></div>
                <button id="AddPublicToDoList"><img src="images/Add.png" alt="Add"></button>
                <button id="RemovePublicToDoList"><img src="images/Remove.png" alt="Remove"></button>
                <button id="MovePublicToDoListUp"><img src="images/MoveUp.png" alt="MoveUp"></button>
                <button id="MovePublicToDoListDown"><img src="images/MoveDown.png" alt="MoveDown"></button>
                <div class="spacing10"></div>
                <table>
                    <thead>
                        <tr>
                            <td>Name -</td>
                            <td>Owner -</td>
                        </tr>
                    </thead>
                    <tbody id="public">
                        <c:if test="${loginstatus}">
                            <c:forEach var="publictdl" items="${public}" varStatus="loop">
                                <tr>
                                    <td>${publictdl.name}</td>
                                    <td>${publictdl.owner}</td>
                                    <input type="hidden" value="${loop.index}">
                                </tr>
                            </c:forEach>
                        </c:if>
                    </tbody>
                </table>
                <div class="spacing10"></div>
                <span class="title2">Private</span><br>
                <div class="spacing10"></div>
                <button id="AddPrivateToDoList"><img src="images/Add.png" alt="Add"></button>
                <button id="RemovePrivateToDoList"><img src="images/Remove.png" alt="Remove"></button>
                <button id="MovePrivateToDoListUp"><img src="images/MoveUp.png" alt="MoveUp"></button>
                <button id="MovePrivateToDoListDown"><img src="images/MoveDown.png" alt="MoveDown"></button>
                <div class="spacing10"></div>
                <table>
                    <thead>
                        <tr>
                            <td>Name -</td>
                            <td>Owner -</td>
                        </tr>
                    </thead>
                    <tbody id="private">
                        <c:if test="${loginstatus}">
                            <c:forEach var="privatetdl" items="${private}" varStatus="loop">
                                <tr>
                                    <td>${privatetdl.name}</td>
                                    <td>${privatetdl.owner}</td>
                                    <input type="hidden" value="${loop.index}">
                                </tr>
                            </c:forEach>
                        </c:if>
                    </tbody>
                </table>
            </div>
            <div class="spacing10"></div>
            <div class="box">
                <span class="title2">Items</span><br>
                <div class="spacing10"></div>
                <button id="AddToDo"><img src="images/Add.png" alt="Add"></button>
                <button id="RemoveToDo"><img src="images/Remove.png" alt="Remove"></button>
                <button id="MoveToDoUp"><img src="images/MoveUp.png" alt="MoveUp"></button>
                <button id="MoveToDoDown"><img src="images/MoveDown.png" alt="MoveDown"></button>
                <div class="spacing10"></div>
                <table>
                    <thead>
                        <tr>
                            <td>Category -</td>
                            <td>Description -</td>
                            <td>Start Date -</td>
                            <td>End Date -</td>
                            <td>Complete -</td>
                        </tr>
                    </thead>
                    <tbody id="todolist">
                        
                    </tbody>
                </table>
            </div>
        </div>
        <script
            src="https://code.jquery.com/jquery-3.1.1.min.js"
            integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
            crossorigin="anonymous">
        </script>
        <script src="javascripts/jquery.tablesorter.min.js"></script>
        <script src="javascripts/script.js"></script>
    </body>
</html>

/**
 * Editor: Luming Wu
 * 
 * ctrl + shift + r to update script file. Chrome is giving hard time.
 */
$(document).ready(function(){
    
    var getUrl = window.location;
    var baseUrl = getUrl .protocol + "//" + getUrl.host + "/" + getUrl.pathname.split('/')[1];

    // HTML change require
    var userstatus = $("#userstatus");
    var public = $("#public");
    var private = $("#private");
    var todolist = $("#todolist");
    
    // Object subject to change
    var selectedpublictdl = null;
    var selectedprivatetdl = null;
    var selectedtd = null;
    
    // Set table to be sortable
    $("table").tablesorter({
        sortReset   : true,
        sortRestart : true
    });
    
    // Set table column arrows
    $("thead tr td").click(function(){
        var th = $(this).closest("th");
        th.prop("data-sorter", "true");
        console.log(th.html());
        var columnname = $(this).text();
        switch(columnname.charAt(columnname.length - 1)){
            case "-":
                $(this).text(columnname.substring(0, columnname.length - 1) + "^");
                break;
            case "^":
                $(this).text(columnname.substring(0, columnname.length - 1) + "v");
                break;
            case "v":
                $(this).text(columnname.substring(0, columnname.length - 1) + "-");
                break;
        }
    });
    
    // Set viewing todolist
    $("#public tr").click(function(){
        selectedpublictdl = $(this);
        $.ajax({
        type:"GET",
        url: "/viewtodolist?type=public&index=" + $(this).children().eq(2).val(),
        dataType: "html",
        contentType: "text/html;charset=utf-8",
        success: function(data){
            todolist.first().html(data);
            todolist.closest("table").trigger("update");
            todolist.children("tr").click(function(){
                selectedtd = $(this);
            });
        }
        });
    });
    
    $("#private tr").click(function(){
        selectedprivatetdl = $(this);
        $.ajax({
        type:"GET",
        url: "/viewtodolist?type=private&index=" + $(this).children().eq(2).val(),
        dataType: "html",
        contentType: "text/html;charset=utf-8",
        success: function(data){
            todolist.first().html(data);
            todolist.closest("table").trigger("update");
            todolist.children("tr").click(function(){
                selectedtd = $(this);
            });
        }
        });
    });
    
    // Set row to move up
    $("#MovePublicToDoListUp").click(function(){
        if(selectedpublictdl !== null){
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedpublictdl.prev().insertAfter(selectedpublictdl);
        }
    });
    
    $("#MovePrivateToDoListUp").click(function(){
        if(selectedprivatetdl !== null){
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedprivatetdl.prev().insertAfter(selectedprivatetdl);
        }
    });
    
    $("#MovePublicToDoListDown").click(function(){
        if(selectedpublictdl !== null){
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedpublictdl.next().insertBefore(selectedpublictdl);
        }
    });
    
    $("#MovePrivateToDoListDown").click(function(){
        if(selectedprivatetdl !== null){
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedprivatetdl.next().insertBefore(selectedprivatetdl);
        }
    });
    
    $("#MoveToDoUp").click(function(){
        if(selectedtd !== null){
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedtd.next().insertBefore(selectedtd);
        }
    });
    
    $("#MoveToDoDown").click(function(){
        if(selectedtd !== null){
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedtd.next().insertBefore(selectedtd);
        }
    });
    
    $("#RemovePublicToDoList").click(function(){
        $.get("/deletetodolist?type=public&index=" + selectedpublictdl.children().eq(2).val(), function(data){
            if(data !== ""){
                selectedpublictdl.remove();
                selectedpublictdl = null;
            }
        });
    });
    
    $("#RemovePrivateToDoList").click(function(){
        $.get("/deletetodolist?type=private&index=" + selectedprivatetdl.children().eq(2).val(), function(data){
            if(data !== ""){
                selectedprivatetdl.remove();
                selectedprivatetdl = null;
            }
        });
    });
    
    $("#AddPublicToDoList").click(function(){
        $.get("/addtodolist?type=public", function(data){
            if(data !== ""){
                public.append(
                    "<tr>"
                    + "<td>EMPTY</td>"
                    + "<td>EMPTY</td>"
                    + "<input type='hidden' value='" + data + "'>"
                    + "</tr>");
            }
        });
    });
    
    $("#AddPrivateToDoList").click(function(){
        $.get("/addtodolist?type=private", function(data){
            if(data !== ""){
                private.append(
                    "<tr>"
                    + "<td>EMPTY</td>"
                    + "<td>EMPTY</td>"
                    + "<input type='hidden' value='" + data + "'>"
                    + "</tr>");
            }
        });
    });
    
    $("#RemoveToDo").click(function(){
        
    });
    
    $("#AddToDo").click(function(){
        
    });
    
});
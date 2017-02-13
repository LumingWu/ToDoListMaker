/**
 * Editor: Luming Wu
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
    
    // Set row to move up
    $("#public tr").click(function(){
        selectedpublictdl = $(this);
        $.ajax({
        type:"GET",
        url: "/viewtodolist?type=public&index=" + $(this).children().eq(2).val(),
        dataType: "html",
        contentType: "text/html;charset=utf-8",
        success: function(data){
            todolist.first().html(data);
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
        }
        });
    });
    
    $("#MovePublicToDoListUp").click(function(){
        selectedpublictdl.prev().insertAfter(selectedpublictdl);
    });
    
    $("#MovePrivateToDoListUp").click(function(){
        selectedprivatetdl.prev().insertAfter(selectedprivatetdl);
    });
    
    $("#MovePublicToDoListDown").click(function(){
        selectedpublictdl.next().insertBefore(selectedpublictdl);
    });
    
    $("#MovePrivateToDoListDown").click(function(){
        selectedprivatetdl.next().insertBefore(selectedprivatetdl);
    });
    
});
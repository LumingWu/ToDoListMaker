/**
 * Editor: Luming Wu
 * 
 * ctrl + shift + r to update script file. Chrome is giving hard time.
 */
$(document).ready(function () {

    var getUrl = window.location;
    var baseUrl = getUrl.protocol + "//" + getUrl.host + "/" + getUrl.pathname.split('/')[1];

    // HTML change require
    var userstatus = $("#userstatus");
    var public = $("#public");
    var private = $("#private");
    var todolist = $("#todolist");

    // Object subject to change
    var selectedpublictdl = null;
    var selectedprivatetdl = null;
    var selectedtd = null;

    // State of the selection
    var selecttype = null;

    // Set table to be sortable
    $("table").tablesorter({
        sortReset: true,
        sortRestart: true
    });

    // Set table column arrows
    $("thead tr td").click(function () {
        var th = $(this).closest("th");
        th.prop("data-sorter", "true");
        console.log(th.html());
        var columnname = $(this).text();
        switch (columnname.charAt(columnname.length - 1)) {
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
    $("#public tr").click(function () {
        selectedpublictdl = $(this);
        selecttype = "public";
        selectedtd = null;
        $.ajax({
            type: "GET",
            url: "/viewtodolist?type=public&index=" + $(this).children().eq(2).val(),
            dataType: "html",
            contentType: "text/html;charset=utf-8",
            success: function (data) {
                todolist.first().html(data);
                todolist.closest("table").trigger("update");
                todolist.children("tr").click(function () {
                    selectedtd = $(this);
                });
                $("#todolist tr td").dblclick(function () {
                    $(this).html("<input type='text'>");
                    $(this).find("input").focusout(function () {
                        var tr = $(this).parent().parent();
                        var td = $(this).parent();
                        var value = $(this).val();
                        $(this).remove();
                        $.get("/modifytodo?type=" + selecttype + "&index=" + (selecttype === "public" ?
                                selectedpublictdl.children().eq(2).val() : selectedprivatetdl.children().eq(2).val())
                                + "&index2=" + selectedtd.children().eq(5).val()
                                + "&category=" + tr.children().eq(0).val()
                                + "&description=" + tr.children().eq(1).val()
                                + "&startdate=" + tr.children().eq(2).val()
                                + "&enddate=" + tr.children().eq(3).val()
                                + "&complete=" + tr.children().eq(4).val()
                                , function (data) {
                                    if (data === "ok") {
                                        td.text(value);
                                    } else {
                                        td.text("");
                                    }
                                });
                    });
                });
            }
        });
    });

    $("#private tr").click(function () {
        selectedprivatetdl = $(this);
        selecttype = "private";
        selectedtd = null;
        $.ajax({
            type: "GET",
            url: "/viewtodolist?type=private&index=" + $(this).children().eq(2).val(),
            dataType: "html",
            contentType: "text/html;charset=utf-8",
            success: function (data) {
                todolist.first().html(data);
                todolist.closest("table").trigger("update");
                todolist.children("tr").click(function () {
                    selectedtd = $(this);
                });
                $("#todolist tr td").dblclick(function () {
                    $(this).html("<input type='text'>");
                    $(this).find("input").focusout(function () {
                        var tr = $(this).parent().parent();
                        var td = $(this).parent();
                        var value = $(this).val();
                        $(this).remove();
                        $.get("/modifytodo?type=" + selecttype + "&index=" + (selecttype === "public" ?
                                selectedpublictdl.children().eq(2).val() : selectedprivatetdl.children().eq(2).val())
                                + "&index2=" + selectedtd.children().eq(5).val()
                                + "&category=" + tr.children().eq(0).val()
                                + "&description=" + tr.children().eq(1).val()
                                + "&startdate=" + tr.children().eq(2).val()
                                + "&enddate=" + tr.children().eq(3).val()
                                + "&complete=" + tr.children().eq(4).val()
                                , function (data) {
                                    if (data === "ok") {
                                        td.text(value);
                                    } else {
                                        td.text("");
                                    }
                                });
                    });
                });
            }
        });
    });

    // Set row to move up
    $("#MovePublicToDoListUp").click(function () {
        if (selectedpublictdl !== null) {
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedpublictdl.prev().insertAfter(selectedpublictdl);
        }
    });

    $("#MovePrivateToDoListUp").click(function () {
        if (selectedprivatetdl !== null) {
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedprivatetdl.prev().insertAfter(selectedprivatetdl);
        }
    });

    $("#MovePublicToDoListDown").click(function () {
        if (selectedpublictdl !== null) {
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedpublictdl.next().insertBefore(selectedpublictdl);
        }
    });

    $("#MovePrivateToDoListDown").click(function () {
        if (selectedprivatetdl !== null) {
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedprivatetdl.next().insertBefore(selectedprivatetdl);
        }
    });

    $("#MoveToDoUp").click(function () {
        if (selectedtd !== null) {
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedtd.next().insertBefore(selectedtd);
        }
    });

    $("#MoveToDoDown").click(function () {
        if (selectedtd !== null) {
            var th = $(this).closest("th");
            th.prop("data-sorter", "false");
            selectedtd.next().insertBefore(selectedtd);
        }
    });

    $("#RemovePublicToDoList").click(function () {
        $.get("/deletetodolist?type=public&index=" + selectedpublictdl.children().eq(2).val(), function (data) {
            if (data === "ok") {
                selectedpublictdl.remove();
                selectedpublictdl = null;
                selecttype = null;
            }
        });
    });

    $("#RemovePrivateToDoList").click(function () {
        $.get("/deletetodolist?type=private&index=" + selectedprivatetdl.children().eq(2).val(), function (data) {
            if (data === "ok") {
                selectedprivatetdl.remove();
                selectedprivatetdl = null;
                selecttype = null;
            }
        });
    });

    $("#AddPublicToDoList").click(function () {
        $.get("/addtodolist?type=public", function (data) {
            if (data !== "") {
                public.append(
                        "<tr>"
                        + "<td></td>"
                        + "<td></td>"
                        + "<input type='hidden' value='" + data + "'>"
                        + "</tr>");
                $("#public tr").click(function () {
                    selectedpublictdl = $(this);
                    selecttype = "public";
                    selectedtd = null;
                    $.ajax({
                        type: "GET",
                        url: "/viewtodolist?type=public&index=" + $(this).children().eq(2).val(),
                        dataType: "html",
                        contentType: "text/html;charset=utf-8",
                        success: function (data) {
                            todolist.first().html(data);
                            todolist.closest("table").trigger("update");
                            todolist.children("tr").click(function () {
                                selectedtd = $(this);
                            });
                            $("#todolist tr td").dblclick(function () {
                                $(this).html("<input type='text'>");
                                $(this).find("input").focusout(function () {
                                    var tr = $(this).parent().parent();
                                    var td = $(this).parent();
                                    var value = $(this).val();
                                    $(this).remove();
                                    $.get("/modifytodo?type=" + selecttype + "&index=" + (selecttype === "public" ?
                                            selectedpublictdl.children().eq(2).val() : selectedprivatetdl.children().eq(2).val())
                                            + "&index2=" + selectedtd.children().eq(5).val()
                                            + "&category=" + tr.children().eq(0).val()
                                            + "&description=" + tr.children().eq(1).val()
                                            + "&startdate=" + tr.children().eq(2).val()
                                            + "&enddate=" + tr.children().eq(3).val()
                                            + "&complete=" + tr.children().eq(4).val()
                                            , function (data) {
                                                if (data === "ok") {
                                                    td.text(value);
                                                } else {
                                                    td.text("");
                                                }
                                            });
                                });
                            });
                        }
                    });
                });
                $("#public tr td").dblclick(function () {
                    $(this).html("<input type='text'>");
                    $(this).find("input").focusout(function () {
                        var tr = $(this).parent().parent();
                        var td = $(this).parent();
                        var value = $(this).val();
                        $(this).remove();
                        $.get("/modifytodolist?type=public&index=" + selectedpublictdl.children().eq(2).val()
                                + "&name=" + tr.children().eq(0).val() + "&owner=" + tr.children().eq(1).val
                                , function (data) {
                                    if (data === "ok") {
                                        td.text(value);
                                    } else {
                                        td.text("");
                                    }
                                });
                    });
                });
            }
        });
    });

    $("#AddPrivateToDoList").click(function () {
        $.get("/addtodolist?type=private", function (data) {
            if (data !== "") {
                private.append(
                        "<tr>"
                        + "<td></td>"
                        + "<td></td>"
                        + "<input type='hidden' value='" + data + "'>"
                        + "</tr>");
                $("#private tr").click(function () {
                    selectedprivatetdl = $(this);
                    selecttype = "private";
                    selectedtd = null;
                    $.ajax({
                        type: "GET",
                        url: "/viewtodolist?type=private&index=" + $(this).children().eq(2).val(),
                        dataType: "html",
                        contentType: "text/html;charset=utf-8",
                        success: function (data) {
                            todolist.first().html(data);
                            todolist.closest("table").trigger("update");
                            todolist.children("tr").click(function () {
                                selectedtd = $(this);
                            });
                            $("#todolist tr td").dblclick(function () {
                                $(this).html("<input type='text'>");
                                $(this).find("input").focusout(function () {
                                    var tr = $(this).parent().parent();
                                    var td = $(this).parent();
                                    var value = $(this).val();
                                    $(this).remove();
                                    $.get("/modifytodo?type=" + selecttype + "&index=" + (selecttype === "public" ?
                                            selectedpublictdl.children().eq(2).val() : selectedprivatetdl.children().eq(2).val())
                                            + "&index2=" + selectedtd.children().eq(5).val()
                                            + "&category=" + tr.children().eq(0).val()
                                            + "&description=" + tr.children().eq(1).val()
                                            + "&startdate=" + tr.children().eq(2).val()
                                            + "&enddate=" + tr.children().eq(3).val()
                                            + "&complete=" + tr.children().eq(4).val()
                                            , function (data) {
                                                if (data === "ok") {
                                                    td.text(value);
                                                } else {
                                                    td.text("");
                                                }
                                            });
                                });
                            });
                        }
                    });
                });
                $("#private tr td").dblclick(function () {
                    $(this).html("<input type='text'>");
                    $(this).find("input").focusout(function () {
                        var tr = $(this).parent().parent();
                        var td = $(this).parent();
                        var value = $(this).val();
                        $(this).remove();
                        $.get("/modifytodolist?type=private&index=" + selectedprivatetdl.children().eq(2).val()
                                + "&name=" + tr.children().eq(0).val() + "&owner=" + tr.children().eq(1).val()
                                , function (data) {
                                    if (data === "ok") {
                                        td.text(value);
                                    } else {
                                        td.text("");
                                    }
                                });
                    });
                });
            }
        });
    });

    $("#todolist tr").click(function () {
        selectedtd = $(this);
    });

    $("#RemoveToDo").click(function () {
        if (selecttype !== null && selectedtd !== null) {
            $.get("/deletetodo?type=" + selecttype + "&index=" + (selecttype === "public" ?
                    selectedpublictdl.children().eq(2).val() : selectedprivatetdl.children().eq(2).val()) + "&index2="
                    + selectedtd.children().eq(5).val()
                    , function (data) {
                        if (data === "ok") {
                            selectedtd.remove();
                            selectedtd = null;
                        }
                    });
        }
    });

    $("#AddToDo").click(function () {
        if (selecttype !== null) {
            $.get("/addtodo?type=" + selecttype + "&index=" + (selecttype === "public" ?
                    selectedpublictdl.children().eq(2).val() : selectedprivatetdl.children().eq(2).val())
                    , function (data) {
                        if (data !== "") {
                            todolist.append(
                                    "<tr>"
                                    + "<td></td>"
                                    + "<td></td>"
                                    + "<td></td>"
                                    + "<td></td>"
                                    + "<td></td>"
                                    + "<input type='hidden' value='" + data + "'>"
                                    + "</tr>");
                            $("#todolist tr").click(function () {
                                selectedtd = $(this);
                            });
                            $("#todolist tr td").dblclick(function () {
                                $(this).html("<input type='text'>");
                                $(this).find("input").focusout(function () {
                                    var tr = $(this).parent().parent();
                                    var td = $(this).parent();
                                    var value = $(this).val();
                                    $(this).remove();
                                    $.get("/modifytodo?type=" + selecttype + "&index=" + (selecttype === "public" ?
                                            selectedpublictdl.children().eq(2).val() : selectedprivatetdl.children().eq(2).val())
                                            + "&index2=" + selectedtd.children().eq(5).val()
                                            + "&category=" + tr.children().eq(0).val()
                                            + "&description=" + tr.children().eq(1).val()
                                            + "&startdate=" + tr.children().eq(2).val()
                                            + "&enddate=" + tr.children().eq(3).val()
                                            + "&complete=" + tr.children().eq(4).val()
                                            , function (data) {
                                                if (data === "ok") {
                                                    td.text(value);
                                                } else {
                                                    td.text("");
                                                }
                                            });
                                });
                            });
                        }
                    });
        }
    });

    $("#public tr td").dblclick(function () {
        $(this).html("<input type='text'>");
        $(this).find("input").focusout(function () {
            var tr = $(this).parent().parent();
            var td = $(this).parent();
            var value = $(this).val();
            $(this).remove();
            $.get("/modifytodolist?type=public&index=" + selectedpublictdl.children().eq(2).val()
                    + "&name=" + tr.children().eq(0).val() + "&owner=" + tr.children().eq(1).val
                    , function (data) {
                        if (data === "ok") {
                            td.text(value);
                        } else {
                            td.text("");
                        }
                    });
        });
    });

    $("#private tr td").dblclick(function () {
        $(this).html("<input type='text'>");
        $(this).find("input").focusout(function () {
            var tr = $(this).parent().parent();
            var td = $(this).parent();
            var value = $(this).val();
            $(this).remove();
            $.get("/modifytodolist?type=private&index=" + selectedprivatetdl.children().eq(2).val()
                    + "&name=" + tr.children().eq(0).val() + "&owner=" + tr.children().eq(1).val()
                    , function (data) {
                        if (data === "ok") {
                            td.text(value);
                        } else {
                            td.text("");
                        }
                    });
        });
    });

    $("#todolist tr td").dblclick(function () {
        $(this).html("<input type='text'>");
        $(this).find("input").focusout(function () {
            var tr = $(this).parent().parent();
            var td = $(this).parent();
            var value = $(this).val();
            $(this).remove();
            $.get("/modifytodo?type=" + selecttype + "&index=" + (selecttype === "public" ?
                    selectedpublictdl.children().eq(2).val() : selectedprivatetdl.children().eq(2).val())
                    + "&index2=" + selectedtd.children().eq(5).val()
                    + "&category=" + tr.children().eq(0).val()
                    + "&description=" + tr.children().eq(1).val()
                    + "&startdate=" + tr.children().eq(2).val()
                    + "&enddate=" + tr.children().eq(3).val()
                    + "&complete=" + tr.children().eq(4).val()
                    , function (data) {
                        if (data === "ok") {
                            td.text(value);
                        } else {
                            td.text("");
                        }
                    });
        });
    });

});
package com.example.tasklistapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//　テンプレートエンジンを使用してHTMLを返す
@Controller
public class HomeController {

    //　タスク情報を保持するモデルをレコードで作成
    record TaskItem(String id, String task, String deadline, boolean done){}
    //  複数のTaskItemオブジェクトを格納するフィールドtaskItemsを作成
    private List<TaskItem> taskItems= new ArrayList<>();
    //  DBとの連携
    private final TaskListDao dao;
    @Autowired
    HomeController(TaskListDao dao){
        this.dao= dao;
    }

//    @RequestMapping(value="/hello")
//    //  HTMLテンプレート"hello.html"を返す
//    String hello(Model model){
//        model.addAttribute("time", LocalDateTime.now());
//        return "hello";
//    }

    //リスト表示 /list →home.html
    @GetMapping(value = "/list")
    String listItems(Model model) {
        List<TaskItem> taskItems= dao.findAll();
        model.addAttribute("taskList", taskItems);
        return "home";
    }

    //タスク追加　/add →/list
    @GetMapping(value="/add")
    String addItem(@RequestParam("task") String task,
                   @RequestParam("deadline") String deadline) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        TaskItem item = new TaskItem(id, task, deadline, false);
//      taskItems.add(item);
        dao.add(item);
        return "redirect:/list";
    }

    @GetMapping(value="delete")
    String deleteItem(@RequestParam("id") String id){
        dao.delete(id);
        return "redirect:/list";
    }

    @GetMapping(value="update")
    String updateItem(@RequestParam("id") String id,
                      @RequestParam("task") String task,
                      @RequestParam("deadline") String deadline,
                      @RequestParam("done") boolean done){
        TaskItem taskItem= new TaskItem(id, task, deadline, done);
        dao.update(taskItem);
        return "redirect:/list";
    }

}

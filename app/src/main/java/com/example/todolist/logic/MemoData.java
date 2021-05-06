package com.example.todolist.logic;

public class MemoData {
    private String start_time;
    private String deadline;
    private String main_part;

    public MemoData(String start_time,String deadline,String main_part) {
        this.start_time = start_time;
        this.deadline = deadline;
        this.main_part = main_part;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getMain_part() {
        return main_part;
    }

    public void setMain_part(String main_part) {
        this.main_part = main_part;
    }
}


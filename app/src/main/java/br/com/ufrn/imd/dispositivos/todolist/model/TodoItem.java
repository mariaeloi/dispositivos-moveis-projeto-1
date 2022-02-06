package br.com.ufrn.imd.dispositivos.todolist.model;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TodoItem implements Serializable {

    private Integer id;
    private String title;
    private String description;
    private Date deadLine;

    public TodoItem() {
    }

    public TodoItem( String title, String description, Date deadLine) {
      //  this.id = id;
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }
    public void setDeadLine(String deadLine) throws ParseException {
        Date date = new SimpleDateFormat("MM/dd/yyyy").parse(deadLine);

        this.deadLine = date;
    }
}

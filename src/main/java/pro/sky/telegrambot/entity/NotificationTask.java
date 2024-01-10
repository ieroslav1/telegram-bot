package pro.sky.telegrambot.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private long chatId;

    @Column(name = "text_task", nullable = false, length = 1000)
    private String textTask;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    // public NotificationTask(){

    // }

    public NotificationTask(long chatId, String textTask, LocalDateTime scheduledTime){
        this.chatId = chatId;
        this.textTask = textTask;
        this.scheduledTime = scheduledTime;
    }

    public NotificationTask() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getTextTask() {
        return textTask;
    }

    public void setTextTask(String textTask) {
        this.textTask = textTask;
    }

    public LocalDateTime getScheduledTime(){
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime){
        this.scheduledTime = scheduledTime;
    }

}


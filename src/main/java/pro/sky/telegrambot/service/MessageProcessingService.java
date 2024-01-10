package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageProcessingService {

    private final NotificationTaskService notificationTaskService;

    @Autowired
    public MessageProcessingService(NotificationTaskService notificationTaskService) {
        this.notificationTaskService = notificationTaskService;
    }

    public void processMessage(String chatId, String messageText) {
        // Паттерн для выделения даты и текста напоминания
        Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})\\s(.+)");
        Matcher matcher = pattern.matcher(messageText);

        if (matcher.matches()) {
            String dateTimeString = matcher.group(1);
            String taskText = matcher.group(2);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime scheduledTime = LocalDateTime.parse(dateTimeString, formatter);


            NotificationTask notificationTask = new NotificationTask();
            notificationTask.setChatId(Long.parseLong(chatId));
            notificationTask.setTextTask(taskText);
            notificationTask.setScheduledTime(scheduledTime);

            notificationTaskService.saveNotificationTask(notificationTask);
        } else {
            System.err.println("Неправильный формат сообщения");
        }
    }
}


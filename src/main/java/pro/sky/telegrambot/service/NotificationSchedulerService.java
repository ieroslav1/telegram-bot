package pro.sky.telegrambot.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java. util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

@Service
public class NotificationSchedulerService {

    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramBot telegramBot;

    @Autowired
    public NotificationSchedulerService(NotificationTaskRepository notificationTaskRepository,
                                        TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendScheduledNotifications() {
        LocalDateTime currentMinute = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        List<NotificationTask> tasksToNotify = notificationTaskRepository.findByScheduledTime(currentMinute);

        Map<Long, List<NotificationTask>> tasksByChatId = tasksToNotify.stream()
                .collect(Collectors.groupingBy(NotificationTask::getChatId));

        tasksByChatId.forEach((chatId, tasksForChat) -> {
            StringBuilder message = new StringBuilder("У вас запланированы задачи:\n");
            for (NotificationTask task : tasksForChat) {
                message.append(task.getTextTask()).append("\n");
            }
            sendMessageToChat(chatId, message.toString());
        });

    }

    private void sendMessageToChat(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
        try {
            telegramBot.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

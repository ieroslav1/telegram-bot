package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.util.List;

@Service
@Transactional
public class NotificationTaskService {

    private final NotificationTaskRepository notificationTaskRepository;

    @Autowired
    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void saveNotificationTask(NotificationTask task) {
        notificationTaskRepository.save(task);
    }


    public List<NotificationTask> getAllNotificationTasks() {
        return notificationTaskRepository.findAll();
    }
}


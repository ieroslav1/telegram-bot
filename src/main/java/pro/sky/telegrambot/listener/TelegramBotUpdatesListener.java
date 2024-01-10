package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.MessageProcessingService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final MessageProcessingService messageProcessingService;

    @Autowired
    public TelegramBotUpdatesListener(MessageProcessingService messageProcessingService) {
        this.messageProcessingService = messageProcessingService;
    }

    public class Commands {
        public static final String START = "/start";
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    Pattern pattern = Pattern.compile("(^\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}:\\d{2}) (.+$)");

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() != null) {
                String chatId = update.message().chat().id().toString();
                String text = update.message().text();

                if (Commands.START.equals(text)) {
                    // Отправить приветственное сообщение
                    SendMessage welcomeMessage = new SendMessage(chatId,
                            "Для планирования задачи отправьте сообщение в формате: " +
                                    "*01.01.2022 20:00 Сделать домашнюю работу*");
                    welcomeMessage.parseMode(ParseMode.Markdown);
                    try {
                        telegramBot.execute(welcomeMessage);
                    } catch (Exception e) {
                        logger.error("Failed to send welcome message. Chat ID: {}. Error: {}", chatId, e.getMessage());
                    }
                } else {
                    // Проверить формат сообщения
                    Matcher matcher = pattern.matcher(text);
                    if (matcher.matches()) {
                        // Сообщение соответствует формату, можно обработать и занести в базу
                        String formattedMessage = "Напоминание успешно сохранено";
                        SendMessage successMessage = new SendMessage(chatId, formattedMessage);
                        try {
                            telegramBot.execute(successMessage);
                            // Добавить логику для занесения в базу
                            messageProcessingService.processMessage(chatId, text);
                        } catch (Exception e) {
                            logger.error("Failed to send success message. Chat ID: {}. Error: {}", chatId,
                                    e.getMessage());
                        }
                    } else {
                        // Сообщение не соответствует формату, вывести сообщение об ошибке
                        String errorMessage = "Сообщение не соответствует формату. Используйте формат: *01.01.2022 20:00 Сделать домашнюю работу*";
                        SendMessage errorMessageResponse = new SendMessage(chatId, errorMessage);
                        errorMessageResponse.parseMode(ParseMode.Markdown);
                        try {
                            telegramBot.execute(errorMessageResponse);
                        } catch (Exception e) {
                            logger.error("Failed to send error message. Chat ID: {}. Error: {}", chatId,
                                    e.getMessage());
                        }
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
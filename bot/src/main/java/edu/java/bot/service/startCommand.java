package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configurable
@EnableConfigurationProperties(Bot.class)
public class startCommand extends Command{
    public startCommand(Command nextCommand) {
        super(nextCommand);
    }
    @Override
    public SendMessage handleCommand (Update update, Bot telegramBot) {
        if (super.getCommand(update).equals("/start")) {
            telegramBot.addUser(getChatId(update));
            return new SendMessage(super.getChatId(update), "Добро пожаловать в Worker бота, "
                + getUserName(update) + "!");
        }
        return super.handleCommand(update, telegramBot);
    }
}

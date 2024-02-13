package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import java.net.URL;
import java.util.*;

public class Bot {
    private static TelegramBot telegramBot;
    private static ChainCommands chainCommands;
    public Map<Long, List<URL>> references;
    public Bot (String telegramToken){
        chainCommands = new ChainCommands();
        references = new HashMap<>();
        telegramBot = new TelegramBot(telegramToken);
    }

    public void processUpdate(){
        int offset = 0;
        while (true) {
            GetUpdates getUpdates = new GetUpdates().limit(1).offset(offset + 1).timeout(0);
            GetUpdatesResponse updatesResponse = telegramBot.execute(getUpdates);
            List<Update> updates = updatesResponse.updates();
            if (updates.isEmpty()){
                continue;
            } else {
                offset = updates.getFirst().updateId();
                updates.forEach(update -> executeCommand(chainCommands.handleCommand(update, this)));
            }
        }

    }
    public void addUser(Long Id){
        if (!references.containsKey(Id)){
            references.put(Id, new ArrayList<URL>());
        }
    }
    public void addURLs(Long Id, URL url){
        references.get(Id).add(url);
    }

    public void deleteURLs(Long Id, URL url){
        references.get(Id).remove(url);
    }
    public TelegramBot getTelegramBot(){
        return telegramBot;
    }

    public List<URL> getListOfURLS(Long Id){
        return references.get(Id);
    }
    protected void executeCommand(SendMessage sendMessage){
        telegramBot.execute(sendMessage);
    }

}

package pl.kajmaczek.mctraps.helpop.ChatInput;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.kajmaczek.mctraps.helpop.HelpOP;

public class ChatInputStuff {
    private int stage;
    public int getStage() {
        return stage;
    }
    public void setStage(int a) {
        stage = a;
    }

    private String message;
    public String getMessage() {
        return message;
    }

    private String user;
    public String getUser() {
        return user;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(user);
    }

    public void dialog(ChatInput map, String username, String message, AsyncPlayerChatEvent event, HelpOP plugin) {

    }

    public void dialog(ChatInput map, Player p, String message, AsyncPlayerChatEvent event, HelpOP player) {

    }

    public void cleanup() {

    }
}

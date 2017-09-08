package pl.kajmaczek.mctraps.helpop.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import pl.kajmaczek.mctraps.helpop.HelpOP;

public class PlayerChatListener implements Listener {
    private final HelpOP plugin;

    public PlayerChatListener(HelpOP plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(plugin.ci.playerInChat(event.getPlayer().getName())) {
            plugin.ci.dialog(event.getPlayer().getName(), event.getMessage(), event, plugin);
        }
    }
}

package pl.kajmaczek.mctraps.helpop.ChatInput;

import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.kajmaczek.mctraps.helpop.HelpOP;
import pl.kajmaczek.mctraps.helpop.Ticket;

import java.sql.ResultSet;

public class CreateHelpop extends ChatInputStuff {
    private int stage = 0;
    private Ticket t;

    @Override
    public int getStage() {
        return stage;
    }

    @Override
    public void setStage(int stage) {
        this.stage = stage;
    }

    @Override
    public void dialog(ChatInput map, String username, String message, AsyncPlayerChatEvent event, HelpOP plugin) {
        event.setCancelled(true);

        if(!(message.toLowerCase().contains("cancel"))) {
            if(!(message.toLowerCase().contains("send"))) {
                if(stage == 0) {
                    t = new Ticket(plugin);
                    t.setMessage(message);
                    stage = 1;
                    Bukkit.getPlayer(username).sendMessage("§aWiadomosc zostala dodana! §7Aby ja §a§lwyslac §7wpisz: §6send§7, a aby ja §c§lanulowac §7wpisz §6cancel§7!");
                } else if(stage == 1) {
                    t.addMessage(message);
                    Bukkit.getPlayer(username).sendMessage("§aWiadomosc zostala dodana! §7Aby ja §a§lwyslac §7wpisz: §6send§7, a aby ja §c§lanulowac §7wpisz §6cancel§7!");
                }
            } else {
                if(stage == 0) {
                    t = new Ticket(plugin);
                    t.setMessage(message);
                    Bukkit.getPlayer(username).sendMessage("§aWiadomosc zostala dodana! §7Aby ja §a§lwyslac §7wpisz: §6send§7, a aby ja §c§lanulowac §7wpisz §6cancel§7!");
                    stage = 1;
                } else if(stage == 1) {
                    t.addMessage(message);
                    Bukkit.getPlayer(username).sendMessage("§aWiadomosc zostala dodana! §7Aby ja §a§lwyslac §7wpisz: §6send§7, a aby ja §c§lanulowac §7wpisz §6cancel§7!");
                }
                t.setUsername(username);
                t.setOpen(true);
                boolean sent = t.create();
                if(sent) {
                    Bukkit.getPlayer(username).sendMessage("§8[§6§lHelpOP§8] §8» §2Pomyslnie wyslano wiadomosc :)");
                } else {
                    Bukkit.getPlayer(username).sendMessage("§8[§6§lHelpOP§8] §8» §4Blad: §cwystapil blad podczas wysylania wiadomosci");
                }

                int id = t.getId();

                Bukkit.broadcast("§8[§6§lHelpOP§8] §8[§6#" + id + "§8] §4" + Bukkit.getPlayer(username).getName() + " §8» §7" + t.getMessage(), "tools.helpop.read");
                Bukkit.broadcast("§8[§6§lHelpOP§8] §8» §7wpisz §6/helpop assign " + id + " §7aby dac znac, ze zajmujesz sie ta sprawa", "tools.helpop.read");
                stage = 9999;
                map.removePlayer(username);
            }
        } else {
            Bukkit.getPlayer(username).sendMessage("§4Anuluowano");
            stage = 9999;
            map.removePlayer(username);
        }
    }
}

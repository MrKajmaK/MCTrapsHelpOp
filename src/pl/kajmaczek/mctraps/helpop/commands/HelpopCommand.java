package pl.kajmaczek.mctraps.helpop.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kajmaczek.mctraps.helpop.ChatInput.CreateHelpop;
import pl.kajmaczek.mctraps.helpop.HelpOP;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HelpopCommand implements CommandExecutor {
    HelpOP plugin;

    public HelpopCommand(HelpOP plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("helpop").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("tools.helpop.banned")) {
                    if (args.length == 0) {
                        p.sendMessage("§8[§6HelpOP§8] §7Wpisz swoja wiadomosc na chat. §7Nastepnie wpisz §6send §7aby §a§lwyslac§7, lub §6cancel§7, aby §c§lanulowac! ");
                        plugin.ci.addToMap(p.getName(), new CreateHelpop());
                    } else {
                        p.sendMessage("§cPoprawne uzycie: §7/helpop");
                    }
                } else {
                    p.sendMessage("§4Blad: §cNie masz dostepu do tej komendy!");
                }
            } else {
                sender.sendMessage("§4Blad: §cmusisz byc graczem aby to zrobic");
            }
            return true;
        } else if (args.length == 2) {
            if(args[0].equalsIgnoreCase("assign")) {
                if(sender.hasPermission("tools.helpop.read")) {
                    try {
                        ResultSet r = plugin.statement.executeQuery("SELECT COUNT(*) FROM " + plugin.rTable + " WHERE id = '" + args[1] + "'");
                        int count = 0;
                        while(r.next()) {
                            count = r.getInt(1);
                        }

                        if(count == 1) {
                            plugin.statement.executeUpdate("UPDATE " + plugin.rTable + " SET assigned = '" + sender.getName() + "' WHERE id = '" + args[1] + "'");
                            Bukkit.broadcast("§8[§6§lHelpOP§8] §8» §7" + sender.getName() + " zajmuje sie sprawa §6#" + args[1], "tools.helpop.read");
                        } else {
                            sender.sendMessage("§4Blad: §czle id :(");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sender.sendMessage("§cWystapil blad w trakcie laczenia z baza danych");
                    }
                } else {
                    sender.sendMessage("§4Blad: §cmusisz byc graczem aby to zrobic");
                }
            } else {
                if(sender.hasPermission("tools.helpop.read")) {
                    sender.sendMessage("§4Poprawne uzycie: §c/helpop lub /helpop assign <id>");
                } else {
                    sender.sendMessage("§4Poprawne uzycie: §c/helpop");
                }
            }
            return true;
        } else {
            if(sender.hasPermission("tools.helpop.read")) {
                sender.sendMessage("§4Poprawne uzycie: §c/helpop lub /helpop assign <id>");
            } else {
                sender.sendMessage("§4Poprawne uzycie: §c/helpop");
            }

            return true;
        }
    }
}

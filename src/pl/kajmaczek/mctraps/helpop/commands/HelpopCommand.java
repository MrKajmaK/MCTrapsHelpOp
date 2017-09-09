package pl.kajmaczek.mctraps.helpop.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kajmaczek.mctraps.helpop.ChatInput.AddHelpop;
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
                        try {
                            ResultSet r = plugin.statement.executeQuery("SELECT COUNT(*) FROM " + plugin.rTable + " WHERE username = '" + p.getName() + "' AND open = '1'");
                            int count = 0;
                            if (r.next()) {
                                count = r.getInt(1);
                            }

                            if (count == 0) {
                                p.sendMessage("§8[§6HelpOP§8] §7Wpisz swoja wiadomosc na chat. §7Nastepnie wpisz §6send §7aby §a§lwyslac§7, lub §6cancel§7, aby §c§lanulowac! ");
                                plugin.ci.addToMap(p.getName(), new CreateHelpop());
                            } else {
                                ResultSet result = plugin.statement.executeQuery("SELECT * FROM " + plugin.rTable + " WHERE username = '" + p.getName() + "' AND open = '1'");
                                int id = 0;
                                if (result.next()) {
                                    id = result.getInt("id");
                                }
                                p.sendMessage("§8[§6HelpOP§8] §7Wpisz swoja wiadomosc na chat. Zostanie ona wyslana do administratora zajmujacego sie twoja sprawa. §7Nastepnie wpisz §6send §7aby §a§lwyslac§7, lub §6cancel§7, aby §c§lanulowac! ");
                                plugin.ci.addToMap(p.getName(), new AddHelpop(id));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            p.sendMessage("§cWystapil blad w trakcie laczenia sie z baza danych");
                        }
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
            if (args[0].equalsIgnoreCase("assign")) {
                if (sender.hasPermission("tools.helpop.read")) {
                    try {
                        ResultSet r = plugin.statement.executeQuery("SELECT COUNT(*) FROM " + plugin.rTable + " WHERE id = '" + args[1] + "'");
                        int count = 0;
                        while (r.next()) {
                            count = r.getInt(1);
                        }

                        if (count == 1) {
                            plugin.statement.executeUpdate("UPDATE " + plugin.rTable + " SET assigned = '" + sender.getName() + "' WHERE id = '" + args[1] + "'");
                            Bukkit.broadcast("§8[§6§lHelpOP§8] §8» §4" + sender.getName() + " §7zajmuje sie sprawa §6#" + args[1], "tools.helpop.read");

                            ResultSet result = plugin.statement.executeQuery("SELECT * FROM " + plugin.rTable + " WHERE id = '" + args[1] + "'");
                            String user = "";
                            while (result.next()) {
                                user = result.getString("username");
                            }

                            plugin.statement.executeUpdate("UPDATE " + plugin.rTable + " SET assigned = '" + sender.getName() + "' WHERE username = '" + user + "' AND open = '1'");

                            Player player = Bukkit.getPlayer(user);
                            if (player.isOnline()) {
                                player.sendMessage("§8[§6§lHelpOP§8] §8» §4" + sender.getName() + " §7zajmuje sie twoja sprawa");
                            }
                        } else {
                            sender.sendMessage("§4Blad: §czle id :(");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sender.sendMessage("§cWystapil blad w trakcie laczenia z baza danych");
                    }
                } else {
                    sender.sendMessage("§4Blad: §cnie masz uprawnien");
                }
            } else if (args[0].equalsIgnoreCase("close")) {
                if (sender.hasPermission("tools.helpop.read")) {
                    try {
                        ResultSet r = plugin.statement.executeQuery("SELECT COUNT(*) FROM " + plugin.rTable + " WHERE id = '" + args[1] + "'");
                        int count = 0;
                        while (r.next()) {
                            count = r.getInt(1);
                        }

                        if (count != 0) {
                            plugin.statement.executeUpdate("UPDATE " + plugin.rTable + " SET open = '0' WHERE id = '" + args[1] + "'");

                            ResultSet result = plugin.statement.executeQuery("SELECT * FROM " + plugin.rTable + " WHERE id = '" + args[1] + "'");
                            String user = "";
                            while (result.next()) {
                                user = result.getString("username");
                            }

                            Bukkit.broadcast("§8[§6§lHelpOP§8] §8» §7sprawa gracza §4" + user + " §6#" + args[1] + " §7zostala zamknieta", "tools.helpop.read");

                            plugin.statement.executeUpdate("UPDATE " + plugin.rTable + " SET open = '0' WHERE username = '" + user + "' AND open = '1'");

                            Player player = Bukkit.getPlayer(user);
                            if (player.isOnline()) {
                                player.sendMessage("§8[§6§lHelpOP§8] §8» §7twoja sprawa zostala zamknieta");
                            }
                        } else {
                            sender.sendMessage("§4Blad: §czle id :(");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        sender.sendMessage("§cWystapil blad w trakcie laczenia z baza danych");
                    }
                } else {
                    if (sender.hasPermission("tools.helpop.read")) {
                        sender.sendMessage("§4Poprawne uzycie: §c/helpop, /helpop assign <id> lub /helpop close <id>");
                    } else {
                        sender.sendMessage("§4Poprawne uzycie: §c/helpop");
                    }
                }
                return true;
            } else {
                if (sender.hasPermission("tools.helpop.read")) {
                    sender.sendMessage("§4Poprawne uzycie: §c/helpop lub /helpop assign <id>");
                } else {
                    sender.sendMessage("§4Poprawne uzycie: §c/helpop");
                }

                return true;
            }
        }
        return true;
    }
}

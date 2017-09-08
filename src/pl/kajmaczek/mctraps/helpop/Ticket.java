package pl.kajmaczek.mctraps.helpop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ticket {
    private HelpOP plugin;
    private int id;
    private String username;
    private String assigned;
    private String message;
    private int open;

    public Ticket(HelpOP plugin) {
        this.plugin = plugin;
    }

    public Ticket(HelpOP plugin, int id, String username, String assigned, String message, int open) {
        this.plugin = plugin;
        this.username = username;
        this.assigned = assigned;
        this.message = message;
        this.open = open;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOpen(boolean open) {
        if(open) {
            this.open = 1;
        } else {
            this.open = 0;
        }
    }

    public void addMessage(String message) {
        this.message += " " + message;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAssigned() {
        return assigned;
    }

    public String getMessage() {return message;}

    public boolean isOpen() {
        if(open == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int create() {
        int k = 0;
        if("x" + username + assigned + open + message != "x") {
            try {
                message = message.replace("send", "");
                plugin.statement.executeUpdate("INSERT INTO " + plugin.rTable + " (username, assigned, open, message) VALUES ('" + username + "', '" + assigned + "', '" + open + "', '" + message + "')", Statement.RETURN_GENERATED_KEYS);
                ResultSet r = plugin.statement.getGeneratedKeys();
                if(r.next()) {
                    k = r.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            }
            return k;
        }
        return 0;
    }

    public boolean push() {
        if ("x" + username + assigned + open + message != "x") {
            try {
                plugin.statement.executeUpdate("UPDATE " + plugin.rTable + " SET username='" + username + "', assigned='" + assigned + "', open='" + open + "', message='" + message + "' WHERE id='" + id + "'");
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}

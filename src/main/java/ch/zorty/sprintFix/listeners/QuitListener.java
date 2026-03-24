package ch.zorty.sprintFix.listeners;

import ch.zorty.sprintFix.SprintFix;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    SprintFix plugin;

    public QuitListener(SprintFix plugin) { this.plugin = plugin; }

    @EventHandler
    public void quit(PlayerQuitEvent e) { plugin.remove_player(e.getPlayer()); }

}

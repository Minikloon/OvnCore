package net.minikloon.ovn.ovncore.libs.sidebar;

import net.minikloon.ovn.ovncore.OvnCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.function.Supplier;

public class SidebarManager implements Listener {
    private Supplier<SidebarLinesProvider> linesProviderFunc = DefaultLinesProvider::new;

    private final Map<UUID, PlayerSidebar> sidebarsPerPlayer = new HashMap<>();

    public SidebarManager() {
        Bukkit.getScheduler().runTaskTimer(OvnCore.getInstance(), () -> {
            Set<PlayerSidebar> toRemove = new HashSet<>(2);
            sidebarsPerPlayer.forEach((playerId, sidebar) -> {
                Player player = Bukkit.getPlayer(playerId);
                if (player == null || !player.isOnline()) {
                    toRemove.add(sidebar);
                    return;
                }
                sidebar.update(player);
            });

            toRemove.forEach(sidebar -> {
                sidebarsPerPlayer.remove(sidebar.getPlayerId());
            });
        }, 0L, 20L);
    }

    public void setLinesProvider(Supplier<SidebarLinesProvider> linesProviderFunc) {
        this.linesProviderFunc = linesProviderFunc;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        setupSidebar(e.getPlayer());
    }

    public void setupSidebar(Player player) {
        ScoreboardManager sbManager = Bukkit.getScoreboardManager();
        Scoreboard board = sbManager.getNewScoreboard();

        Objective objective = board.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        SidebarLinesProvider linesProvider = linesProviderFunc.get();
        PlayerSidebar sidebar = new PlayerSidebar(player.getUniqueId(), linesProvider, board, objective);
        sidebarsPerPlayer.put(player.getUniqueId(), sidebar);
        player.setScoreboard(board);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        sidebarsPerPlayer.remove(e.getPlayer().getUniqueId());
    }
}

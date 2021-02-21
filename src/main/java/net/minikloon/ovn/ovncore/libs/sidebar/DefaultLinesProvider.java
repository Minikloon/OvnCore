package net.minikloon.ovn.ovncore.libs.sidebar;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DefaultLinesProvider implements SidebarLinesProvider {
    private static final String DISPLAY_NAME = ChatColor.of("#FF0099") + "Overnight " + ChatColor.YELLOW + "Network";

    @Override
    public String getDisplayName(Player player) {
        return DISPLAY_NAME;
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("This is a line for " + player.getName());
        return lines;
    }
}

package net.minikloon.ovn.ovncore.libs.sidebar;

import org.bukkit.entity.Player;

import java.util.List;

public interface SidebarLinesProvider {
    String getDisplayName(Player player);

    List<String> getLines(Player player);
}

package net.minikloon.ovn.ovncore;

import net.minikloon.ovn.ovncore.libs.sidebar.SidebarManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OvnCore extends JavaPlugin {
    private static OvnCore INSTANCE;

    private SidebarManager sidebarManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        sidebarManager = new SidebarManager();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(sidebarManager, this);
        getServer().getOnlinePlayers().forEach(sidebarManager::setupSidebar);
    }

    public SidebarManager getSidebarManager() {
        return sidebarManager;
    }

    public static OvnCore getInstance() {
        return INSTANCE;
    }
}

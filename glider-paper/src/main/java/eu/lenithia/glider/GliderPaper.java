package eu.lenithia.glider;

import org.bukkit.plugin.java.JavaPlugin;

public class GliderPaper extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("GliderPaper enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("GliderPaper disabled");
    }
}

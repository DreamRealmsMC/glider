package eu.lenithia.glider.utils;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GliderConsoleText {

    public static void printConsoleText(ProxyServer proxy, String version) {
        proxy.getConsoleCommandSource().sendMessage(Component.text("       ,--.,--.   ,--.               | ").color(NamedTextColor.LIGHT_PURPLE));
        proxy.getConsoleCommandSource().sendMessage(Component.text(" ,---. |  |`--' ,-|  | ,---. ,--.--. | ").color(NamedTextColor.LIGHT_PURPLE));

        Component line3 = Component.text("| .-. ||  |,--.' .-. || .-. :|  .--' | ").color(NamedTextColor.LIGHT_PURPLE)
                .append(Component.text("glider-velocity v" + version).color(NamedTextColor.AQUA));
        proxy.getConsoleCommandSource().sendMessage(line3);

        Component line4 = Component.text("' '-' '|  ||  |\\ `-' |\\   --.|  |    | ").color(NamedTextColor.LIGHT_PURPLE)
                .append(Component.text("by Len_137").color(NamedTextColor.AQUA));
        proxy.getConsoleCommandSource().sendMessage(line4);

        proxy.getConsoleCommandSource().sendMessage(Component.text(".`-  / `--'`--' `---'  `----'`--'    | ").color(NamedTextColor.LIGHT_PURPLE));
        proxy.getConsoleCommandSource().sendMessage(Component.text("`---'                                | ").color(NamedTextColor.LIGHT_PURPLE));
    }
}


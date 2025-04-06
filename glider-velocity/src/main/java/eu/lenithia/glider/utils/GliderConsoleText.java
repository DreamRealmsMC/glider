package eu.lenithia.glider.utils;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class GliderConsoleText {

    public static TextColor PINK = TextColor.color(234, 147, 237);
    public static TextColor LIGHT_BLUE = TextColor.color(150, 135, 222);
    public static TextColor SEPARATOR_COLOR = TextColor.color(255, 255, 255);

    public static void printConsoleText(ProxyServer proxy, String version) {
        proxy.getConsoleCommandSource().sendMessage(Component.text("       ,--.,--.   ,--.              ").color(PINK)
                .append(separator));

        proxy.getConsoleCommandSource().sendMessage(Component.text(" ,---. |  |`--' ,-|  | ,---. ,--.--.").color(PINK)
                .append(separator));

        Component line3 = Component.text("| .-. ||  |,--.' .-. || .-. :|  .--'").color(PINK)
                .append(separator)
                .append(Component.text("glider-velocity v" + version).color(LIGHT_BLUE));
        proxy.getConsoleCommandSource().sendMessage(line3);

        Component line4 = Component.text("' '-' '|  ||  |\\ `-' |\\   --.|  |   ").color(PINK)
                .append(separator)
                .append(Component.text("by Len_137").color(LIGHT_BLUE));
        proxy.getConsoleCommandSource().sendMessage(line4);

        proxy.getConsoleCommandSource().sendMessage(Component.text(".`-  / `--'`--' `---'  `----'`--'   ").color(PINK)
                .append(separator));
        proxy.getConsoleCommandSource().sendMessage(Component.text("`---'                               ").color(PINK)
                .append(separator));

    }

    private static Component separator = Component.text(" | ").color(SEPARATOR_COLOR);

}


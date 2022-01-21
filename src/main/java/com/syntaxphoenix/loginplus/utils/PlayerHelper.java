package com.syntaxphoenix.loginplus.utils;

import org.bukkit.entity.Player;

import net.sourcewriters.minecraft.vcompat.VersionCompatProvider;
import net.sourcewriters.minecraft.vcompat.provider.entity.NmsPlayer;

public final class PlayerHelper {
    
    private PlayerHelper() {}
    
    public static void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        NmsPlayer nmsPlayer = VersionCompatProvider.get().getControl().getPlayerProvider().getPlayer(player);
        nmsPlayer.setTitleTimes(fadeIn, stay, fadeOut);
        nmsPlayer.sendTitle(subtitle);
        nmsPlayer.sendSubtitle(subtitle);
    }

}

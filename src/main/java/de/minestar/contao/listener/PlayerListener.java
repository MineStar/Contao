/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of Contao.
 * 
 * Contao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * Contao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Contao.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.contao.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import de.minestar.contao.core.ContaoCore;
import de.minestar.contao.core.Settings;
import de.minestar.contao.data.ContaoGroup;
import de.minestar.contao.data.UnregisteredUser;
import de.minestar.contao.data.User;
import de.minestar.core.MinestarCore;
import de.minestar.core.units.MinestarGroup;
import de.minestar.core.units.MinestarPlayer;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class PlayerListener implements Listener {

    // TEMPONARY MAP FOR USER WHO ARE IN THE LOGIN PROCESS
    private Map<String, User> attemptToLoginUser = new HashMap<String, User>();

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {

        User user = ContaoCore.dbHandler.getUser(event.getName());
        // IS DEFAULT OR X USER
        if (user == null) {
            MinestarPlayer mPlayer = MinestarCore.getPlayer(event.getName());
            if (mPlayer != null) {
                user = new UnregisteredUser(event.getName(), ContaoGroup.get(mPlayer.getMinestarGroup()));
            }
            attemptToLoginUser.put(event.getName(), user);
        }
        // PLAYER IS AT LEAST A PROBE MEMBER
        else {
//            // ADMINS ARE NOT CONTROLLED BY SYSTEM TO PREVENT ERRORS
//            if (user.getGroup().equals(ContaoGroup.ADMIN))
//                return;

            // GET GROUP FROM GROUP MANAGER PLUGIN
            MinestarGroup localGroup = MinestarCore.getPlayer(event.getName()).getMinestarGroup();

            // COMPARE LOCAL GROUP WITH CONTAO GROUP IN DATABASE
            if (!localGroup.equals(user.getGroup().getMinestarGroup())) {
                ContaoCore.pManager.setGroup(user, user.getGroup());
                ConsoleUtils.printWarning(ContaoCore.NAME, "User '" + user.getMinecraftNickname() + "' has a different contao and minecraft group! Contao Group=" + user.getGroup() + " ; Minecraft Group=" + localGroup + ". User is now in the Contao Group " + user.getGroup());
            }

            switch (user.getGroup()) {
                case PAY :
                    if (user.isPayExpired(new Date())) {
                        // TODO: Downgrade to free user
                    }
                    break;
                case FREE :
                    // FREE SLOTS ARE FULL
                    if (ContaoCore.pManager.getGroupSize(ContaoGroup.FREE) >= Settings.getFreeSlots()) {
                        // TODO: Outsource text to settings
                        event.disallow(Result.KICK_FULL, "Alle Free Slots sind belegt.");
                        return;
                    }
                    break;

                case PROBE :
                    if (user.isProbeFinished()) {
                        // TODO: Check if player reached the requirements to be
                        // automatically a free user

                        // TODO: Fire statistic

                    }
                    break;

                // DO NOTHING
                default :

                    break;

            }

            attemptToLoginUser.put(user.getMinecraftNickname().toLowerCase(), user);
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        User user = attemptToLoginUser.get(player.getName().toLowerCase());
        if (user == null) {
            // SOMETHING WENT SOMEWHERE TERRIBLE WRONG
            return;
        }
        ContaoCore.pManager.addUser(user);

        PlayerUtils.sendMessage(player, ChatColor.GOLD, Bukkit.getOnlinePlayers().length + " / " + Bukkit.getMaxPlayers());

        // WRITE GROUPS
        String groupText = "";
        for (ContaoGroup group : ContaoGroup.values()) {
            groupText = ContaoCore.pManager.getGroupAsString(group);
            // NULL = NO MEMBER OF THIS GROUP IS ONLINE
            if (groupText != null)
                PlayerUtils.sendBlankMessage(player, groupText);
        }

        // TODO: Send start up information
        // Warnings

        // TODO: Fire statistic
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        ContaoCore.pManager.removeUser(event.getPlayer().getName());

        // TODO: Fire statistic
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        event.setFormat("%2$s");
        Player player = event.getPlayer();
        ContaoGroup group = ContaoCore.pManager.getUser(player).getGroup();

        ChatColor color = Settings.getColor(group);
        if (color == null)
            color = ChatColor.GRAY;

        event.setMessage(color + player.getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
    }
}

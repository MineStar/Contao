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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.minestar.contao.core.ContaoCore;
import de.minestar.contao.core.Settings;
import de.minestar.contao.data.ContaoGroup;
import de.minestar.contao.data.UnregisteredUser;
import de.minestar.contao.data.User;
import de.minestar.core.MinestarCore;
import de.minestar.core.units.MinestarGroup;
import de.minestar.core.units.MinestarPlayer;

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
                // TODO: Update local group
            }

            switch (user.getGroup()) {
                case PAY :
                    if (user.isPayExpired(new Date())) {
                        // TODO: Downgrade to free user
                    }
                    attemptToLoginUser.put(user.getMinecraftNickname().toLowerCase(), user);
                    break;
                case FREE :
                    // TODO: Check if server is full - kick free player when yes
                    break;

                case PROBE :
                    if (user.isProbeFinished()) {
                        // TODO: Check if player reached the requirements to be
                        // automatically a free user

                        // TODO: Fire statistic
                    }
                    attemptToLoginUser.put(user.getMinecraftNickname().toLowerCase(), user);
                    break;

                // DO NOTHING
                default :
                    attemptToLoginUser.put(user.getMinecraftNickname().toLowerCase(), user);

                    break;

            }

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

        // TODO: Send start up information
        // Online list
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

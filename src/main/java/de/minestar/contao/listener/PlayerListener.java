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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {
        // TODO: Check database if player is in database
        // TODO: Check database if contao group is the same as the groupmanager
        // group
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogin(PlayerLoginEvent event) {
        // TODO: Put player in online player list
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // TODO: Remove player from online player list
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // TODO: Implement chat handeling
    }
}

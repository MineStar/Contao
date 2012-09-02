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

package de.minestar.contao.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.contao.core.ContaoCore;
import de.minestar.contao.data.ContaoGroup;
import de.minestar.contao.data.User;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public class cmdProbe extends AbstractCommand {

    public cmdProbe(String syntax, String arguments, String node) {
        super(ContaoCore.NAME, syntax, arguments, node);
    }

    @Override
    public void execute(String[] args, Player player) {
        userProbe(args, player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        userProbe(args, console);
    }

    private void userProbe(String[] args, CommandSender sender) {

        String playerName = PlayerUtils.getCorrectPlayerName(args[0]);
        if (playerName == null) {
            ChatUtils.writeError(sender, pluginName, "Der Spieler '" + args[0] + "' wurde nicht gefunden!");
            return;
        }

        int id = -1;
        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            ChatUtils.writeError(sender, pluginName, "Die ID '" + args[1] + "' ist keine Zahl!");
            return;
        }

        User user = ContaoCore.pManager.getUser(playerName);

        // USER IS OFFLINE
        if (user == null) {
            handleOfflinePlayer(sender, playerName, id);
        } else {
            handleOnlinePlayer(sender, user, id);
        }
    }

    private void handleOnlinePlayer(CommandSender sender, User user, int id) {
        // USER HAS ALREADY AN ACCOUNT
        if (user.isRegistered()) {
            ChatUtils.writeError(sender, pluginName, "Spieler '" + user.getMinecraftNickname() + "' hat bereits einen Account! Folgende Informationen: ");
            ChatUtils.writeInfo(sender, pluginName, user.toString());
            return;
        }

        // ACCOUNT HAS NOT BEING VALIDATED VIA E-MAIL!
        if (ContaoCore.dbHandler.isAccountDisabled(id)) {
            ChatUtils.writeError(sender, pluginName, "Der Account mit der ID '" + id + "' ist nicht aktiviert! Wurde E-Mail bestätigt?");
            return;
        }

        if (ContaoCore.dbHandler.addUser(id, user.getMinecraftNickname(), sender.getName())) {
            // LOAD NEW USER FROM DATABASE
            // TODO: Mabye set new values instead of loading them from database?
            user = ContaoCore.dbHandler.getUser(user.getMinecraftNickname());

            ContaoCore.pManager.changeGroup(user, ContaoGroup.PROBE);

            Player player = Bukkit.getPlayerExact(user.getMinecraftNickname());

            // TODO: Outsource greetings text to settings
            PlayerUtils.sendSuccess(player, pluginName, "Willkommen Probe User. Wir wünschen dir viel Spaß beim Bauen.");
        } else {
            ChatUtils.writeError(sender, pluginName, "Fehler beim Eintragen des Users in die Datenbank! Spieler wurde nicht zum Probe User!");
        }
    }

    private void handleOfflinePlayer(CommandSender sender, String playerName, int id) {

        User user = ContaoCore.dbHandler.getUser(playerName);

        // PLAYER HAS NO ACCOUNT!
        if (user == null) {

            // ACCOUNT HAS NOT BEING VALIDATED VIA E-MAIL!
            if (ContaoCore.dbHandler.isAccountDisabled(id)) {
                ChatUtils.writeError(sender, pluginName, "Der Account mit der ID '" + id + "' ist nicht aktiviert! Wurde E-Mail bestätigt?");
                return;
            }

            if (ContaoCore.dbHandler.addUser(id, playerName, sender.getName())) {
                // LOAD NEW USER FROM DATABASE
                // TODO: Mabye set new values instead of loading them from
                // database?
                user = ContaoCore.dbHandler.getUser(playerName);

                ContaoCore.pManager.setGroup(user, ContaoGroup.PROBE);
            } else {
                ChatUtils.writeError(sender, pluginName, "Fehler beim Eintragen des Users in die Datenbank! Spieler wurde nicht zum Probe User!");
            }
        } else {
            ChatUtils.writeError(sender, pluginName, "Spieler '" + playerName + "' hat bereits einen Account! Weitere Informationen:");
            ChatUtils.writeInfo(sender, pluginName, user.toString());
        }
    }

}

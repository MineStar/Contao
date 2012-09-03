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

package de.minestar.contao.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.contao.core.ContaoCore;
import de.minestar.contao.data.ContaoGroup;
import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.utils.ChatUtils;

public class cmdList extends AbstractCommand {

    public cmdList(String syntax, String arguments, String node) {
        super(ContaoCore.NAME, syntax, arguments, node);
    }

    @Override
    public void execute(String[] args, Player player) {
        list(player);
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        list(console);
    }

    private void list(CommandSender sender) {

        ChatUtils.writeMessage(sender, ChatColor.GOLD.toString() + Bukkit.getOnlinePlayers().length + " / " + Bukkit.getMaxPlayers());

        // WRITE GROUPS
        String groupText = "";
        for (ContaoGroup group : ContaoGroup.values()) {
            groupText = ContaoCore.pManager.getGroupAsString(group);
            // NULL = NO MEMBER OF THIS GROUP IS ONLINE
            if (groupText != null)
                ChatUtils.writeMessage(sender, groupText);
        }

    }

}

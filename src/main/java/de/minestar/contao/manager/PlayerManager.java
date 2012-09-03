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

package de.minestar.contao.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.entity.Player;

import de.minestar.contao.core.Settings;
import de.minestar.contao.data.ContaoGroup;
import de.minestar.contao.data.User;
import de.minestar.core.MinestarCore;
import de.minestar.core.units.MinestarPlayer;

public class PlayerManager {

    private Map<String, User> onlineUserMap = new HashMap<String, User>();

    private Map<ContaoGroup, TreeSet<User>> groupMap = new HashMap<ContaoGroup, TreeSet<User>>();

    public PlayerManager() {
        for (ContaoGroup cGroup : ContaoGroup.values()) {
            groupMap.put(cGroup, new TreeSet<User>());
        }
    }

    public void addUser(User user) {
        onlineUserMap.put(user.getMinecraftNickname().toLowerCase(), user);

        groupMap.get(user.getGroup()).add(user);
    }

    public void removeUser(String userName) {
        User user = onlineUserMap.remove(userName.toLowerCase());

        groupMap.get(user.getGroup()).remove(user);
    }

    public User getUser(Player player) {
        return getUser(player.getName());
    }

    public User getUser(String userName) {
        return onlineUserMap.get(userName.toLowerCase());
    }

    public String getGroupAsString(ContaoGroup contaoGroup) {
        Set<User> groupMember = groupMap.get(contaoGroup);
        if (groupMember.isEmpty())
            return null;

        StringBuilder sBuilder = new StringBuilder();

        // BUILD HEAD
        sBuilder.append(Settings.getColor(contaoGroup));
        sBuilder.append(contaoGroup.getDisplayName());
        sBuilder.append('(');
        sBuilder.append(getGroupSize(contaoGroup));
        sBuilder.append(") : ");

        // ADD USER
        for (User user : groupMember) {
            sBuilder.append(user.getMinecraftNickname());
            sBuilder.append(", ");
        }

        // DELETE THE LAST COMMATA
        sBuilder.delete(0, sBuilder.length() - 2);

        return sBuilder.toString();
    }

    public int getGroupSize(ContaoGroup contaoGroup) {
        return groupMap.get(contaoGroup).size();
    }

    public void changeGroup(User user, ContaoGroup newGroup) {

        groupMap.get(user.getGroup()).remove(user);
        groupMap.get(newGroup).add(user);

        setGroup(user, newGroup);
    }

    public void setGroup(User user, ContaoGroup newGroup) {
        MinestarPlayer mPlayer = MinestarCore.getPlayer(user.getMinecraftNickname());
        if (mPlayer != null) {
            mPlayer.setGroup(newGroup.getMinestarGroup());
        }
    }

    public boolean canBeFree(User probeUser) {
        // TODO: Implement requirements
        return false;
    }
}

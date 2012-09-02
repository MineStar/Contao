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
import java.util.TreeSet;

import de.minestar.contao.data.ContaoGroup;
import de.minestar.contao.data.User;

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

    public User getUser(String userName) {
        return onlineUserMap.get(userName.toLowerCase());
    }

}
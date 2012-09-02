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

package de.minestar.contao.data;

import java.util.HashMap;
import java.util.Map;

import de.minestar.core.units.MinestarGroup;

public enum ContaoGroup {

    //@formatter:off
    ADMIN   (MinestarGroup.ADMIN,   "Admin",    3,      "a:2:{i:0;s:1:\"3\";i:1;s:1:\"2\";}"),
    MOD     (MinestarGroup.MOD,     "Mod",      6,      "a:2:{i:0;s:1:\"6\";i:1;s:1:\"2\";}"),
    PAY     (MinestarGroup.PAY,     "Pay",      2,      "a:1:{i:0;s:1:\"2\";}"),
    FREE    (MinestarGroup.FREE,    "Free",     1,      "a:1:{i:0;s:1:\"1\";}"),
    PROBE   (MinestarGroup.PROBE,   "Probe",    5,      "a:1:{i:0;s:1:\"5\";}"),
    DEFAULT (MinestarGroup.DEFAULT, "Gast",     -1,     "a:1:{i:0;s:1:\"4\";}"),
    X       (MinestarGroup.X,       "X",        -1,     "a:1:{i:0;s:1:\"4\";}");
    //@formatter:on

    private final MinestarGroup group;

    private final int contaoGroupId;
    // The serialized string in contao database
    private final String contaoString;

    private final String displayName;

    private ContaoGroup(MinestarGroup group, String displayName, int contaoGroupId, String contaoString) {
        this.contaoString = contaoString;
        this.displayName = displayName;
        this.contaoGroupId = contaoGroupId;
        this.group = group;
    }

    /** @return The GroupManager group name as defined in the group.yml */
    public String getName() {
        return group.getName();
    }

    /**
     * @return The serialized String in the contao database representing the
     *         group of member
     */
    public String getContaoString() {
        return contaoString;
    }

    public int getContaoGroupId() {
        return contaoGroupId;
    }

    public boolean isHigher(ContaoGroup that) {
        return this.ordinal() < that.ordinal();
    }

    public MinestarGroup getMinestarGroup() {
        return group;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static Map<String, ContaoGroup> mapByName;
    private static Map<Integer, ContaoGroup> mapById;
    private static Map<MinestarGroup, ContaoGroup> mapByMinestarGroup;

    static {
        mapByName = new HashMap<String, ContaoGroup>();
        mapById = new HashMap<Integer, ContaoGroup>();
        mapByMinestarGroup = new HashMap<MinestarGroup, ContaoGroup>();

        for (ContaoGroup group : ContaoGroup.values()) {
            mapByName.put(group.getName().toLowerCase(), group);
            mapById.put(group.getContaoGroupId(), group);
            mapByMinestarGroup.put(group.getMinestarGroup(), group);
        }
    }

    public static ContaoGroup get(String groupName) {
        return mapByName.get(groupName.toLowerCase());
    }

    public static ContaoGroup get(int groupID) {
        return mapById.get(groupID);
    }

    public static ContaoGroup get(MinestarGroup minestarGroup) {
        return mapByMinestarGroup.get(minestarGroup);
    }
}

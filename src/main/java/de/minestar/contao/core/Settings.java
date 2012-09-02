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

package de.minestar.contao.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

import de.minestar.contao.data.ContaoGroup;
import de.minestar.minestarlibrary.config.MinestarConfig;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class Settings {

    /* VALUES */

    private static Map<ContaoGroup, ChatColor> colorMap;

    /* USED FOR SETTING */

    private static MinestarConfig config;
    private static File configFile;

    private Settings() {

    }

    public static boolean init(File dataFolder, String pluginName, String pluginVersion) {
        configFile = new File(dataFolder, "config.yml");
        try {
            // LOAD EXISTING CONFIG FILE
            if (configFile.exists())
                config = new MinestarConfig(configFile, pluginName, pluginVersion);
            // CREATE A DEFAUL ONE
            else
                config = MinestarConfig.copyDefault(Settings.class.getResourceAsStream("/config.yml"), configFile);

            loadValues();
            return true;

        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't load the settings from " + configFile);
            return false;
        }
    }

    private static void loadValues() {

        /* COLORS */
        loadColors();
    }

    private static void loadColors() {
        colorMap = new HashMap<ContaoGroup, ChatColor>();
        colorMap.put(ContaoGroup.ADMIN, ChatColor.getByChar(config.getString("Chat Colors.admin")));
        colorMap.put(ContaoGroup.MOD, ChatColor.getByChar(config.getString("Chat Colors.mod")));
        colorMap.put(ContaoGroup.PAY, ChatColor.getByChar(config.getString("Chat Colors.pay")));
        colorMap.put(ContaoGroup.FREE, ChatColor.getByChar(config.getString("Chat Colors.free")));
        colorMap.put(ContaoGroup.PROBE, ChatColor.getByChar(config.getString("Chat Colors.probe")));
        colorMap.put(ContaoGroup.DEFAULT, ChatColor.getByChar(config.getString("Chat Colors.default")));
        colorMap.put(ContaoGroup.X, ChatColor.getByChar(config.getString("Chat Colors.x")));
    }

    public static ChatColor getColor(ContaoGroup contaoGroup) {
        return colorMap.get(contaoGroup);
    }
}

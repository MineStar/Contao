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

package de.minestar.contao.threads;

import org.bukkit.Bukkit;

import de.minestar.contao.statistics.OnlineStatistic;
import de.minestar.minestarlibrary.stats.StatisticHandler;

public class OnlineStatisticThread implements Runnable {

    @Override
    public void run() {
        // TODO: GET GROUP SIZE
        StatisticHandler.handleStatistic(new OnlineStatistic(Bukkit.getOnlinePlayers().length, -1, -1, -1, -1, -1, -1, -1));
    }

}

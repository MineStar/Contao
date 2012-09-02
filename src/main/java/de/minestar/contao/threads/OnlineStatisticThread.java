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

import de.minestar.contao.core.ContaoCore;
import de.minestar.contao.data.ContaoGroup;
import de.minestar.contao.statistics.OnlineStatistic;
import de.minestar.minestarlibrary.stats.StatisticHandler;

public class OnlineStatisticThread implements Runnable {

    @Override
    public void run() {

        // GET GROUP SIZE
        int totalPlayer = Bukkit.getOnlinePlayers().length;
        int xUser = ContaoCore.pManager.getGroupSize(ContaoGroup.X);
        int defaultUser = ContaoCore.pManager.getGroupSize(ContaoGroup.DEFAULT);
        int probeUser = ContaoCore.pManager.getGroupSize(ContaoGroup.PROBE);
        int freeUser = ContaoCore.pManager.getGroupSize(ContaoGroup.FREE);
        int payUser = ContaoCore.pManager.getGroupSize(ContaoGroup.PAY);
        int modUser = ContaoCore.pManager.getGroupSize(ContaoGroup.MOD);
        int adminUser = ContaoCore.pManager.getGroupSize(ContaoGroup.ADMIN);

        StatisticHandler.handleStatistic(new OnlineStatistic(totalPlayer, xUser, defaultUser, probeUser, freeUser, payUser, modUser, adminUser));
    }

}

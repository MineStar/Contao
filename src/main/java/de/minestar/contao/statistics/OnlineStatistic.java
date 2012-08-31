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

package de.minestar.contao.statistics;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

import de.minestar.contao.core.ContaoCore;
import de.minestar.minestarlibrary.stats.Statistic;
import de.minestar.minestarlibrary.stats.StatisticType;

public class OnlineStatistic implements Statistic {

    private int totalPlayer;
    private int xUser;
    private int defaultUser;
    private int probeUser;
    private int freeUser;
    private int payUser;
    private int modUser;
    private int adminUser;

    private Timestamp date;

    public OnlineStatistic() {
        // EMPTY CONSTRUCTOR FOR REFLECTION ACCESS
    }

    public OnlineStatistic(int totalPlayer, int xUser, int defaultUser, int probeUser, int freeUser, int payUser, int modUser, int adminUser) {
        this.totalPlayer = totalPlayer;
        this.xUser = xUser;
        this.defaultUser = defaultUser;
        this.probeUser = probeUser;
        this.freeUser = freeUser;
        this.payUser = payUser;
        this.modUser = modUser;
        this.adminUser = adminUser;
        this.date = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String getPluginName() {
        return ContaoCore.NAME;
    }

    @Override
    public String getName() {
        return "OnlinePlayer";
    }

    @Override
    public LinkedHashMap<String, StatisticType> getHead() {

        LinkedHashMap<String, StatisticType> head = new LinkedHashMap<String, StatisticType>();

        head.put("totalPlayer", StatisticType.INT);
        head.put("xUser", StatisticType.INT);
        head.put("defaultUser", StatisticType.INT);
        head.put("probeUser", StatisticType.INT);
        head.put("freeUser", StatisticType.INT);
        head.put("payUser", StatisticType.INT);
        head.put("modUser", StatisticType.INT);
        head.put("adminUser", StatisticType.INT);
        head.put("date", StatisticType.DATETIME);

        return head;
    }

    @Override
    public Queue<Object> getData() {

        Queue<Object> data = new LinkedList<Object>();

        data.add(totalPlayer);
        data.add(xUser);
        data.add(defaultUser);
        data.add(probeUser);
        data.add(freeUser);
        data.add(payUser);
        data.add(modUser);
        data.add(adminUser);
        data.add(date);

        return data;
    }
}

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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

    private final int ID;
    private final int contaoID;
    private final String contaoNickname;
    private final String minecraftNickname;
    private final Timestamp expireDate;
    private final Timestamp startDate;
    private final Timestamp probeEndDate;
    private final boolean usedFreePayWeek;

    public User(int ID, int contaoID, String contaoNickname, String minecraftNickname, Timestamp expireDate, Timestamp startDate, Timestamp probeEndDate, boolean usedFreePayWeek) {
        this.ID = ID;
        this.contaoID = contaoID;
        this.contaoNickname = contaoNickname;
        this.minecraftNickname = minecraftNickname;
        this.expireDate = expireDate;
        this.startDate = startDate;
        this.probeEndDate = probeEndDate;
        this.usedFreePayWeek = usedFreePayWeek;
    }

    public int getID() {
        return ID;
    }

    public int getContaoID() {
        return contaoID;
    }

    public String getContaoNickname() {
        return contaoNickname;
    }

    public String getMinecraftNickname() {
        return minecraftNickname;
    }

    public Timestamp getExpireDate() {
        return expireDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getProbeEndDate() {
        return probeEndDate;
    }

    public boolean usedFreePayWeek() {
        return usedFreePayWeek;
    }

    public boolean isPayExpired(Date date) {
        return expireDate.after(date);
    }

    public boolean inProbeEnded(Date date) {
        return probeEndDate.after(date);
    }

    @Override
    public String toString() {
        return "User={ID=" + ID + " ; ContaoID=" + contaoID + " ; ContaoNickname=" + contaoNickname + " ; MinecraftNickname=" + minecraftNickname + " ; ExpireDate=" + expireDate + " ; StartDate=" + startDate + " ; ProbeEndDate=" + probeEndDate + " ; UsedFreePayWeek=" + usedFreePayWeek + " }";
    }
}

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
import java.util.Date;

public class UnregisteredUser extends User {

    public UnregisteredUser(String minecraftNickname, ContaoGroup group) {
        super(-1, -1, "GUEST-NAME", minecraftNickname, null, null, null, false, group);
    }

    @Override
    public int getContaoID() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContaoNickname() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Timestamp getExpireDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Timestamp getStartDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Timestamp getProbeEndDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean usedFreePayWeek() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPayExpired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPayExpired(Date date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isProbeFinished() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isProbeFinished(Date date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistered() {
        return false;
    }
}

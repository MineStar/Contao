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

package de.minestar.contao.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import de.minestar.contao.data.User;
import de.minestar.minestarlibrary.database.AbstractMySQLHandler;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class DatabaseHandler extends AbstractMySQLHandler {

    public DatabaseHandler(String pluginName, File SQLConfigFile) {
        super(pluginName, SQLConfigFile);
    }

    @Override
    protected void createStructure(String pluginName, Connection con) throws Exception {
        // STRUCTURE IS GIVEN
    }

    @Override
    protected void createStatements(String pluginName, Connection con) throws Exception {

        selectUser = con.prepareStatement("SELECT mc_pay.id, contao_user_id, tl_member.username, minecraft_nick, admin_nick, expire_date, startDate, probeEndDate, usedFreePayWeek FROM mc_pay, tl_member WHERE minecraft_nick = ? AND contao_user_id = tl_member.id");

        selectContaoID = con.prepareStatement("SELECT tl_member.id FROM tl_member WHERE tl_member.username = ?");

        addUser = con.prepareStatement("INSERT INTO mc_pay (contao_user_id, minecraft_nick, admin_nick, expire_date, startDate, probeEndDate, totalBreak, totalPlaced, usedFreePayWeek) VALUES (?, ?, ?, '1111-11-11', NOW(), ADDDATE(NOW(), INTERVAL 2 WEEK), 0, 0, 0)");

        updateExpireDate = con.prepareStatement("UPDATE mc_pay SET expire_date = ? WHERE id = ?");

        updateProbeEndDate = con.prepareStatement("UPDATE mc_pay SET probeEndDate = ? WHERE id = ?");

        updateFreePayWeek = con.prepareStatement("UPDATE mc_pay SET usedFreePayWeek = ? WHERE id = ?");
    }

    private PreparedStatement selectUser;
    private PreparedStatement selectContaoID;

    private PreparedStatement addUser;

    private PreparedStatement updateExpireDate;
    private PreparedStatement updateProbeEndDate;
    private PreparedStatement updateFreePayWeek;

    public User getUser(String minecraftNick) {
        try {

            selectUser.setString(1, minecraftNick);
            ResultSet rs = selectUser.executeQuery();

            // NO USER FOUND
            if (!rs.next())
                return null;

            // TEMP VARS
            int ID = rs.getInt(1);
            int contaoID = rs.getInt(2);
            String contaoNickname = rs.getString(3);
            String minecraftNickname = rs.getString(4);
            Timestamp expireDate = rs.getTimestamp(5);
            Timestamp startDate = rs.getTimestamp(6);
            Timestamp probeEndDate = rs.getTimestamp(7);
            boolean usedFreePayWeek = rs.getBoolean(8);

            return new User(ID, contaoID, contaoNickname, minecraftNickname, expireDate, startDate, probeEndDate, usedFreePayWeek);

        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't get user information from database! User = " + minecraftNick);
            return null;
        }
    }

    public int getContaoID(String homepageNick) {
        try {

            selectContaoID.setString(1, homepageNick);
            ResultSet rs = selectContaoID.executeQuery();

            // NO NICK FOUND
            if (!rs.next())
                return Integer.MIN_VALUE;

            return rs.getInt(1);

        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't get contaoID from database! HomepageNick = " + homepageNick);
            return Integer.MIN_VALUE;
        }
    }

    public boolean addUser(int contaoID, String minecraftNick, String commandSender) {

        try {

            addUser.setInt(1, contaoID);
            addUser.setString(2, minecraftNick);
            addUser.setString(3, commandSender);

            return addUser.executeUpdate() == 1;
        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't add user to database! ContaoID = " + contaoID + " ; MinecraftNick = " + minecraftNick + " ; CommandSender  = " + commandSender);
            return false;
        }
    }

    public boolean updateExpireDate(User user, Timestamp newDate) {

        try {

            updateExpireDate.setTimestamp(1, newDate);
            updateExpireDate.setInt(2, user.getID());

            return updateExpireDate.executeUpdate() == 1;
        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't update expire date in database ! User = " + user + " ; NewDate=" + newDate);
            return false;
        }
    }

    public boolean updateProbeEndDate(User user, Timestamp newDate) {
        try {

            // TODO: Maybe this is not necessary!
            if (newDate == null)
                updateProbeEndDate.setNull(1, Types.TIMESTAMP);
            else
                updateProbeEndDate.setTimestamp(1, newDate);

            updateProbeEndDate.setInt(2, user.getID());

            return updateProbeEndDate.executeUpdate() == 1;
        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't update probe end date in database! User = " + user + " ; NewDate=" + newDate);
            return false;
        }
    }

    public boolean updateFreePayWeek(User user, boolean hasUsed) {
        try {

            updateFreePayWeek.setBoolean(1, hasUsed);
            updateFreePayWeek.setInt(2, user.getID());

            return updateFreePayWeek.executeUpdate() == 1;
        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't update free pay week state in database! User = " + user + " ; HasUsed=" + hasUsed);
            return false;
        }
    }
}

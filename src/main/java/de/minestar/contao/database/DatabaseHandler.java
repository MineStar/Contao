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

        selectUser = con.prepareStatement("SELECT mc_pay.id, contao_user_id, tl_member.username, minecraft_nick, admin_nick, expire_date, startDate, probeEndDate FROM mc_pay, tl_member WHERE minecraft_nick = ? AND contao_user_id = tl_member.id");
    }

    private PreparedStatement selectUser;

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

            return new User(ID, contaoID, contaoNickname, minecraftNickname, expireDate, startDate, probeEndDate);

        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't get user information from database! User = " + minecraftNick);
            return null;
        }
    }
}

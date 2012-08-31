package de.minestar.contao.core;

import java.io.File;

import de.minestar.contao.database.DatabaseHandler;
import de.minestar.minestarlibrary.AbstractCore;

public class ContaoCore extends AbstractCore {

    public static final String NAME = "Contao";

    // DATABASE HANDLER
    public static DatabaseHandler dbHandler;

    @Override
    protected boolean createManager() {

        dbHandler = new DatabaseHandler(NAME, new File(getDataFolder(), "sqlconfig.yml"));
        if (dbHandler == null || !dbHandler.hasConnection())
            return false;

        return true;
    }

    @Override
    protected boolean commonDisable() {
        dbHandler.closeConnection();

        return dbHandler != null && !dbHandler.hasConnection();
    }
}

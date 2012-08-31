package de.minestar.contao.core;

import java.io.File;

import de.minestar.contao.commands.contao.cmdContao;
import de.minestar.contao.commands.contao.cmdFreeSlot;
import de.minestar.contao.commands.list.cmdList;
import de.minestar.contao.commands.user.cmdAddProbeTime;
import de.minestar.contao.commands.user.cmdAddWarning;
import de.minestar.contao.commands.user.cmdAdmin;
import de.minestar.contao.commands.user.cmdDefault;
import de.minestar.contao.commands.user.cmdFree;
import de.minestar.contao.commands.user.cmdMod;
import de.minestar.contao.commands.user.cmdPay;
import de.minestar.contao.commands.user.cmdProbe;
import de.minestar.contao.commands.user.cmdRemoveWarning;
import de.minestar.contao.commands.user.cmdSearch;
import de.minestar.contao.commands.user.cmdStatus;
import de.minestar.contao.commands.user.cmdUnMod;
import de.minestar.contao.commands.user.cmdUser;
import de.minestar.contao.commands.user.cmdX;
import de.minestar.contao.database.DatabaseHandler;
import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.minestarlibrary.commands.CommandList;

public class ContaoCore extends AbstractCore {

    public static final String NAME = "Contao";

    // DATABASE HANDLER
    public static DatabaseHandler dbHandler;

    public ContaoCore() {
        super(NAME);
    }

    @Override
    protected boolean createManager() {

        dbHandler = new DatabaseHandler(NAME, new File(getDataFolder(), "sqlconfig.yml"));
        if (dbHandler == null || !dbHandler.hasConnection())
            return false;

        return true;
    }

    @Override
    protected boolean createCommands() {
        // @formatter:off
        
        cmdList = new CommandList(NAME, 

                // LIST COMMAND
                new cmdList         ("/who",        "",     ""),
                new cmdList         ("/list",       "",     ""),
                new cmdList         ("/online",     "",     ""),

                new cmdStatus       ("/stats",      "",     ""),

                // USER-MANGEMENT COMMANDS
                new cmdUser         ("/user",       "",     "",
                        
                        // GROUP CHANGE COMMANDS
                        new cmdX            ("x",           "<IngameName> <Grund>",         "contao.rights.x"),
                        new cmdDefault      ("default",     "<IngameName>",                 "contao.rights.default"),
                        new cmdProbe        ("probe",       "<IngameName> <ContaoID>",      "contao.rights.probe"),
                        new cmdFree         ("free",        "<IngameName>",                 "contao.rights.free"),
                        new cmdPay          ("pay",         "<IngameName> <DD.MM.YYYY>",    "contao.rights.pay"),
                        new cmdMod          ("mod",         "<IngameName> ",                "contao.rights.mod"),
                        new cmdUnMod        ("unmod",       "<IngameName> ",                "contao.rights.unmod"),
                        new cmdAdmin        ("admin",       "<IngameName> <DD.MM.YYYY>",    "contao.rights.admin"),

                        // USEFUL COMMANDS
                        new cmdSearch       ("search",      "<HomePage Name>",              "contao.rights.search"),
                        new cmdStatus       ("status",      "",                             ""),

                        // PUNISHMENT COMMANDS
                        new cmdAddWarning   ("awarn",       "<IngameName> <Text>",          "contao.rights.awarn"),
                        new cmdRemoveWarning("rwarn",       "<IngameName> <Index>",         "contao.rights.rwarn"),

                        new cmdAddProbeTime ("probeadd",    "<IngameName> <Tage>",          "contao.rights.probeadd")

                ),

                // CONTAO MANAGEMENT COMMANDS
                new cmdContao       ("/contao",     "",     "",

                        new cmdFreeSlot     ("freeslots",   "<Zahl>",                       "contao.rights.freeslots")

                )        

        );
        // @formatter:on

        return true;
    }

    @Override
    protected boolean commonDisable() {
        dbHandler.closeConnection();

        return dbHandler != null && !dbHandler.hasConnection();
    }
}

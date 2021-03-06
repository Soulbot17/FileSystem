package ru.donkot.filebros.cellsnicons;

import javax.swing.*;

/**
 * Created by Я on 15.09.2017.
 */
public class MyIconSet {
    //CONSTANTS
    private static final String ICONS_PATH = "src/main/resources/icons/";

    private static final ImageIcon DISK_ICON = new ImageIcon(ICONS_PATH + "disk24.png");
    private static final ImageIcon FOLDER_ICON = new ImageIcon(ICONS_PATH + "folder24.png");
    private static final ImageIcon EXPENDED_ICON = new ImageIcon(ICONS_PATH + "expfolder24.png");
    private static final ImageIcon COMPUTER_ICON = new ImageIcon(ICONS_PATH + "computer24.png");
    private static final ImageIcon FOLDERC_ICON = new ImageIcon(ICONS_PATH + "createfolder16.png");
    private static final ImageIcon FOLDERD_ICON = new ImageIcon(ICONS_PATH + "deletefolder16.png");
    private static final ImageIcon FOLDERB_ICON = new ImageIcon(ICONS_PATH + "browsefolder16.png");
    private static final ImageIcon TITLE_ICON = new ImageIcon(ICONS_PATH + "progicon.png");
    private static final ImageIcon SEARCH_ICON = new ImageIcon(ICONS_PATH + "search-icon.png");
    private static final ImageIcon HISTORY_ICON = new ImageIcon(ICONS_PATH + "history-icon.png");
    private static final ImageIcon TNFOLDER_ICON = new ImageIcon(ICONS_PATH + "nicon16.png");
    private static final ImageIcon TSEARCH_ICON = new ImageIcon(ICONS_PATH + "sicon16.png");

    private MyIconSet() {
    }

    //GETTERS
    public static ImageIcon getDiskIcon() {
        return DISK_ICON;
    }

    public static ImageIcon getFolderIcon() {
        return FOLDER_ICON;
    }

    public static ImageIcon getExpendedIcon() {
        return EXPENDED_ICON;
    }

    public static ImageIcon getComputerIcon() {
        return COMPUTER_ICON;
    }

    public static ImageIcon getFoldercIcon() {
        return FOLDERC_ICON;
    }

    public static ImageIcon getFolderdIcon() {
        return FOLDERD_ICON;
    }

    public static ImageIcon getFolderbIcon() {
        return FOLDERB_ICON;
    }

    public static ImageIcon getTitleIcon() {
        return TITLE_ICON;
    }

    public static ImageIcon getSearchIcon() {
        return SEARCH_ICON;
    }

    public static ImageIcon getHistoryIcon() {
        return HISTORY_ICON;
    }

    public static ImageIcon getTnfolderIcon() {
        return TNFOLDER_ICON;
    }

    public static ImageIcon getTsearchIcon() {
        return TSEARCH_ICON;
    }
}

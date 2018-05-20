package ru.donkot.filebros.panels;

import ru.donkot.filebros.FileBros;
import ru.donkot.filebros.Localizable;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class TopMenu extends JMenuBar implements Localizable{
    //FIELDS
    private JMenu menu;
    private FileBros fileBros;

    //CONSTRUCTOR
    public TopMenu(final FileBros fileBros) {
        this.fileBros = fileBros;
        menu = new JMenu(fileBros.getResourceBundle().getString("menuLanguage"));
        JMenuItem rusItem = new JMenuItem(fileBros.getResourceBundle().getString("russianLang"));
        rusItem.addActionListener(e ->
                fileBros.updateLocale(ResourceBundle.getBundle("lang", new Locale("ru","RU"))));

        JMenuItem engItem = new JMenuItem(fileBros.getResourceBundle().getString("englishLang"));
        engItem.addActionListener(e ->
                fileBros.updateLocale(ResourceBundle.getBundle("lang", new Locale("en","US"))));

        JMenuItem gerItem = new JMenuItem(fileBros.getResourceBundle().getString("germanLang"));
        gerItem.addActionListener(e ->
                fileBros.updateLocale(ResourceBundle.getBundle("lang", new Locale("de","CH"))));

        JMenuItem chiItem = new JMenuItem(fileBros.getResourceBundle().getString("chinaLang"));
        chiItem.addActionListener(e ->
                fileBros.updateLocale(ResourceBundle.getBundle("lang", new Locale("zh", "HK"))));

        menu.add(engItem);
        menu.add(rusItem);
        menu.add(gerItem);
        menu.add(chiItem);
        add(menu);
    }


    //FUNCTIONS
    @Override
    public void updateLocale(ResourceBundle bundle) {
        menu.setText(fileBros.getResourceBundle().getString("menuLanguage"));
    }
}

package ru.donkot.FileBros.panels;

import ru.donkot.FileBros.FileBros;
import ru.donkot.FileBros.Localizable;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

public class TopMenu extends JMenuBar implements Localizable{
    //FIELDS
    private JMenu menu;

    //CONSTRUCTOR
    public TopMenu(final FileBros fileBros) {
        menu = new JMenu(FileBros.resourceBundle.getString("menuLanguage"));
        JMenuItem rusItem = new JMenuItem(FileBros.resourceBundle.getString("russianLang"));
        rusItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileBros.updateLocale(ResourceBundle.getBundle("lang", new Locale("ru","RU")));
            }
        });
        JMenuItem engItem = new JMenuItem(FileBros.resourceBundle.getString("englishLang"));
        engItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileBros.updateLocale(ResourceBundle.getBundle("lang", new Locale("en","US")));
            }
        });
        JMenuItem gerItem = new JMenuItem(FileBros.resourceBundle.getString("germanLang"));
        gerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileBros.updateLocale(ResourceBundle.getBundle("lang", new Locale("de","CH")));
            }
        });

        JMenuItem chiItem = new JMenuItem(FileBros.resourceBundle.getString("chinaLang"));
        chiItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileBros.updateLocale(ResourceBundle.getBundle("lang", new Locale("zh", "HK")));
            }
        });
        menu.add(engItem);
        menu.add(rusItem);
        menu.add(gerItem);
        menu.add(chiItem);
        add(menu);
    }


    //FUNCTIONS
    @Override
    public void updateLocale(ResourceBundle bundle) {
        menu.setText(FileBros.resourceBundle.getString("menuLanguage"));
    }
}

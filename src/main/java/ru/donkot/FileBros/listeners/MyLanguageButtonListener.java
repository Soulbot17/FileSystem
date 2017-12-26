package ru.donkot.FileBros.listeners;

import ru.donkot.FileBros.FileBros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyLanguageButtonListener implements ActionListener{
    private FileBros fileBros;

    public MyLanguageButtonListener(FileBros fileBros) {
        this.fileBros = fileBros;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ResourceBundle bundle;
        JPopupMenu menu = new JPopupMenu("Язык");
        menu.add(new JMenuItem("Русский"));
        menu.add(new JMenuItem("Английский"));
        menu.show(fileBros, Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        if (FileBros.resourceBundle.getLocale().getCountry().equals("RU")) {
            bundle = ResourceBundle.getBundle("lang", new Locale("en","US"));
            fileBros.updateLocale(bundle);
            fileBros.getSouthPanel().updateLocale(bundle);
        } else {
            bundle = ResourceBundle.getBundle("lang", new Locale("ru","RU"));
            fileBros.updateLocale(bundle);
            fileBros.getSouthPanel().updateLocale(bundle);
        }
    }
}

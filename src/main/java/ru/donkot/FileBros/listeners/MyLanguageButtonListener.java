package ru.donkot.FileBros.listeners;

import ru.donkot.FileBros.FileBros;

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
        if (FileBros.resourceBundle.getLocale().getCountry().equals("RU")) {
            bundle = ResourceBundle.getBundle("lang", new Locale("en","US"));
            fileBros.updateLocale(bundle);
            fileBros.getNorthPanel().updateLocale(bundle);
        } else {
            bundle = ResourceBundle.getBundle("lang", new Locale("ru","RU"));
            fileBros.updateLocale(bundle);
            fileBros.getNorthPanel().updateLocale(bundle);
        }
    }
}

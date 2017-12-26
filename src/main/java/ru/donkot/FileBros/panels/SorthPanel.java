package ru.donkot.FileBros.panels;

import ru.donkot.FileBros.FileBros;
import ru.donkot.FileBros.Localizable;
import ru.donkot.FileBros.cellsnicons.MyIconSet;
import ru.donkot.FileBros.listeners.MyCreateFolderListener;
import ru.donkot.FileBros.listeners.MyDeleteFolderListener;
import ru.donkot.FileBros.listeners.MyFindButtonListener;
import ru.donkot.FileBros.listeners.MyLanguageButtonListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SorthPanel extends JPanel implements Localizable{
    //FIELDS
    private FileBros fileBros;
    private Font font = new Font(Font.DIALOG, Font.BOLD, 12);
    private JButton createButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JButton browseButton;

    //CONSTRUCTOR
    public SorthPanel(final FileBros fileBros) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.fileBros = fileBros;

        createButton = makeCreateButton();
        deleteButton = makeDeleteButton();
        browseButton = makeBrowseButton();
        searchButton = makeSearchButton();

        add(createButton);
        add(deleteButton);
        add(searchButton);
        add(browseButton);
    }

    //FUNCTIONS
    private JButton makeSearchButton() {
        searchButton = new JButton(MyIconSet.getSearchIcon());
        searchButton.setText(FileBros.resourceBundle.getString("searchFolder"));
        searchButton.setFont(font);
        searchButton.addActionListener(new MyFindButtonListener(fileBros));
        return searchButton;
    }

    private JButton makeBrowseButton() {
        JButton browseButton = new JButton(MyIconSet.getFolderbIcon());
        browseButton.setText(FileBros.resourceBundle.getString("browseFolder"));
        browseButton.setFont(font);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileBros.getCurrentFolder() == null || fileBros.getCurrentFolder().equals("")) return;
                Desktop desktop = Desktop.getDesktop();
                File file = new File(fileBros.getCurrentFolder());
                try {
                    desktop.open(file);
                } catch (IOException | IllegalArgumentException e1) {
                    fileBros.getMyDisplay().setText("Can't browse this folder");
                }
            }
        });
        return browseButton;
    }

    private JButton makeDeleteButton() {
        JButton deleteButton = new JButton(MyIconSet.getFolderdIcon());
        deleteButton.setText(FileBros.resourceBundle.getString("deleteFolder"));
        deleteButton.setFont(font);
        deleteButton.addActionListener(new MyDeleteFolderListener(fileBros));
        return deleteButton;
    }

    private JButton makeCreateButton() {
        createButton = new JButton(MyIconSet.getFoldercIcon());
        createButton.setText(FileBros.resourceBundle.getString("createFolder"));
        createButton.setFont(font);
        createButton.addActionListener(new MyCreateFolderListener(fileBros));
        return createButton;
    }

    @Override
    public void updateLocale(ResourceBundle bundle) {
        createButton.setText(bundle.getString("createFolder"));
        deleteButton.setText(bundle.getString("deleteFolder"));
        searchButton.setText(bundle.getString("searchFolder"));
        browseButton.setText(bundle.getString("browseFolder"));
    }
}

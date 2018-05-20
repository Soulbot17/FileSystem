package ru.donkot.filebros.panels;

import ru.donkot.filebros.FileBros;
import ru.donkot.filebros.Localizable;
import ru.donkot.filebros.cellsnicons.MyIconSet;
import ru.donkot.filebros.listeners.MyCreateFolderListener;
import ru.donkot.filebros.listeners.MyDeleteFolderListener;
import ru.donkot.filebros.listeners.MyFindButtonListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class SorthPanel extends JPanel implements Localizable{
    //FIELDS
    private FileBros fileBros;
    private Font mainFont = new Font(Font.DIALOG, Font.BOLD, 12);
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
        JButton searchButtonPrototype = new JButton(MyIconSet.getSearchIcon());
        searchButtonPrototype.setText(fileBros.getResourceBundle().getString("searchFolder"));
        searchButtonPrototype.setFont(mainFont);
        searchButtonPrototype.addActionListener(new MyFindButtonListener(fileBros));
        return searchButtonPrototype;
    }

    private JButton makeBrowseButton() {
        JButton browseButtonPrototype = new JButton(MyIconSet.getFolderbIcon());
        browseButtonPrototype.setText(fileBros.getResourceBundle().getString("browseFolder"));
        browseButtonPrototype.setFont(mainFont);
        browseButtonPrototype.addActionListener(e -> {
            if (fileBros.getCurrentFolder() == null || fileBros.getCurrentFolder().equals("")) return;
            Desktop desktop = Desktop.getDesktop();
            File file = new File(fileBros.getCurrentFolder());
            try {
                desktop.open(file);
            } catch (IOException | IllegalArgumentException e1) {
                fileBros.getMyDisplay().setText("Can't browse this folder");
            }
        });
        return browseButtonPrototype;
    }

    private JButton makeDeleteButton() {
        JButton deleteButtonPrototype = new JButton(MyIconSet.getFolderdIcon());
        deleteButtonPrototype.setText(fileBros.getResourceBundle().getString("deleteFolder"));
        deleteButtonPrototype.setFont(mainFont);
        deleteButtonPrototype.addActionListener(new MyDeleteFolderListener(fileBros));
        return deleteButtonPrototype;
    }

    private JButton makeCreateButton() {
        JButton createButtonPrototype = new JButton(MyIconSet.getFoldercIcon());
        createButtonPrototype.setText(fileBros.getResourceBundle().getString("createFolder"));
        createButtonPrototype.setFont(mainFont);
        createButtonPrototype.addActionListener(new MyCreateFolderListener(fileBros));
        return createButtonPrototype;
    }

    @Override
    public void updateLocale(ResourceBundle bundle) {
        createButton.setText(bundle.getString("createFolder"));
        deleteButton.setText(bundle.getString("deleteFolder"));
        searchButton.setText(bundle.getString("searchFolder"));
        browseButton.setText(bundle.getString("browseFolder"));
    }
}

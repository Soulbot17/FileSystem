package ru.donkot.FileBros.panels;

import ru.donkot.FileBros.FileBros;
import ru.donkot.FileBros.cellsnicons.MyIconSet;
import ru.donkot.FileBros.listeners.MyCreateFolderListener;
import ru.donkot.FileBros.listeners.MyDeleteFolderListener;
import ru.donkot.FileBros.listeners.MyFindButtonListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class NorthPanel extends JPanel {
    //FIELDS
    private FileBros fileBros;

    //CONSTRUCTOR
    public NorthPanel(final FileBros fileBros) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.fileBros = fileBros;

        JButton createButton = new JButton(MyIconSet.getFoldercIcon());
        createButton.setText("Create folder");
        Font font = new Font(Font.DIALOG, Font.BOLD, 12);
        createButton.setFont(font);
        createButton.addActionListener(new MyCreateFolderListener(fileBros));
        JButton deleteButton = new JButton(MyIconSet.getFolderdIcon());
        deleteButton.setText("Delete folder");
        deleteButton.setFont(font);
        deleteButton.addActionListener(new MyDeleteFolderListener(fileBros));
        JButton browseButton = new JButton(MyIconSet.getFolderbIcon());
        browseButton.setText("Browse folder");
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
        JButton searchButton = new JButton(MyIconSet.getSearchIcon());
        searchButton.setText("Search");
        searchButton.setFont(font);
        searchButton.addActionListener(new MyFindButtonListener(fileBros));

        add(createButton);
        add(deleteButton);
        add(searchButton);
        add(browseButton);
    }
}

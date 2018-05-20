package ru.donkot.filebros.listeners;

import org.apache.commons.io.FileUtils;
import ru.donkot.filebros.FileBros;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

//          Кнопка удаления

public class MyDeleteFolderListener implements ActionListener {
    //FIELDS
    private FileBros fileBros;

    //CONSTRUCTOR
    public MyDeleteFolderListener(FileBros fileBros) {
        this.fileBros = fileBros;
    }

    //FUNCTIONS
    @Override
    public void actionPerformed(ActionEvent e) {
        if (fileBros.getCurrentFolder() == null || fileBros.getCurrentFolder().equals("")) return;
        File[] roots = File.listRoots();
        for (File r : roots) {
            if (fileBros.getCurrentFolder().equals(r.getAbsolutePath())) return;
        }
        File file = new File(fileBros.getCurrentFolder());
        if (!file.canWrite()) return;
        JOptionPane.setDefaultLocale(fileBros.getResourceBundle().getLocale());
        int reply = JOptionPane.showConfirmDialog(null, fileBros.getResourceBundle().getString("deleteFolderMessage"), "", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e1) {
                fileBros.setMydisplayText(fileBros.getResourceBundle().getString("cantDeleteFolder"));
            }
            DefaultTreeModel model = (DefaultTreeModel) fileBros.getMyFolderTree().getModel();
            model.removeNodeFromParent(fileBros.getCurrentNode());
            model.reload(fileBros.getCurrentNode().getNextNode());
        }
    }
}
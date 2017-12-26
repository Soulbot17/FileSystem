package ru.donkot.FileBros.listeners;

import ru.donkot.FileBros.FileBros;
import ru.donkot.FileBros.FileNode;
import ru.donkot.FileBros.Localizable;
import ru.donkot.FileBros.panels.InfoPanel;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Vector;

//          то, что происходит при выборе node в fileTree

public class DirSelectionListener implements TreeSelectionListener, Localizable{
    //FIELDS
    private FileBros fileBros;
    private InfoPanel infoPanel;
    private FileNode fnode;
    //CONSTRUCTOR
    public DirSelectionListener(FileBros fileBros, InfoPanel infoPanel) {
        this.fileBros = fileBros;
        this.infoPanel = infoPanel;
    }

    //FUNCTIONS
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        infoPanel.updateLocale(FileBros.resourceBundle);
        fileBros.setCurrentFolder(e.getPath().toString());
        DefaultMutableTreeNode node = fileBros.getTreeNode(e.getPath());
        fileBros.setCurrentNode(fileBros.getTreeNode(e.getPath()));
        fnode = fileBros.getFileNode(node);
        Vector<File> vfiles = new Vector<>();
        if (fnode != null) {
            fileBros.setCurrentFolder(fnode.getFile().getAbsolutePath());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
            try {
                fileBros.setMydisplayText(FileBros.resourceBundle.getString("selectionFiles") + String.valueOf(fnode.getFile().listFiles().length));
            } catch (NullPointerException g) {
                fileBros.setMydisplayText("");
            }
            File file = new File(fileBros.getCurrentFolder());
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file1 : files) {
                    if (!file1.isDirectory()) {
                        vfiles.add(file1);
                    }
                }
                fileBros.setMyFileListData(vfiles);
            } else fileBros.setMyFileListData(new Vector());
        } else fileBros.setMydisplayText("");
    }

    @Override
    public void updateLocale(ResourceBundle bundle) {
        if (fnode!=null) {
            fileBros.setMydisplayText(FileBros.resourceBundle.getString("selectionFiles") + String.valueOf(fnode.getFile().listFiles().length));
        }
    }
}

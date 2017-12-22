package ru.donkot.FileBros.listeners;

import ru.donkot.FileBros.FileBros;
import ru.donkot.FileBros.FileNode;
import ru.donkot.FileBros.panels.InfoPanel;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Vector;

//          то, что происходит при выборе node в fileTree

public class DirSelectionListener implements TreeSelectionListener {
    //FIELDS
    private FileBros fileBros;
    private InfoPanel infoPanel;
    private Vector nullvector = new Vector();

    //CONSTRUCTOR
    public DirSelectionListener(FileBros fileBros, InfoPanel infoPanel) {
        this.fileBros = fileBros;
        this.infoPanel = infoPanel;
    }

    //FUNCTIONS
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        infoPanel.clearInfoPanel();
        fileBros.setCurrentFolder(e.getPath().toString());
        DefaultMutableTreeNode node = fileBros.getTreeNode(e.getPath());
        fileBros.setCurrentNode(fileBros.getTreeNode(e.getPath()));
        FileNode fnode = fileBros.getFileNode(node);
        Vector<File> vfiles = new Vector<>();
        if (fnode != null) {
            fileBros.setCurrentFolder(fnode.getFile().getAbsolutePath());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
            try {
                fileBros.setMydisplayText("Files: " + String.valueOf(fnode.getFile().listFiles().length));
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
                //myFileList.setListData(vfiles);
            } else fileBros.setMyFileListData(nullvector);
        } else fileBros.setMydisplayText("");
    }
}

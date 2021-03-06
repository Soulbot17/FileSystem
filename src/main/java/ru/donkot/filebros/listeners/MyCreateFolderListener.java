package ru.donkot.filebros.listeners;

import ru.donkot.filebros.FileBros;
import ru.donkot.filebros.FileNode;
import ru.donkot.filebros.Localizable;
import ru.donkot.filebros.cellsnicons.IconData;
import ru.donkot.filebros.cellsnicons.MyIconSet;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ResourceBundle;

//          кнопка создания папки

public class MyCreateFolderListener implements ActionListener, Localizable{
    //FIELDS
    private JFrame frame;
    private JTextField textFileName;
    private FileBros fileBros;

    //CONSTRUCTOR
    public MyCreateFolderListener(FileBros fileBros) {
        this.fileBros = fileBros;
    }

    //FUNCTIONS
    @Override
    public void actionPerformed(ActionEvent e) {
        if (fileBros.getCurrentFolder() == null || fileBros.getCurrentFolder().equals("")) return;
        File file = null;
        try {
            file = new File(fileBros.getCurrentFolder());
        } catch (NullPointerException c) {
            fileBros.setMydisplayText(fileBros.getResourceBundle().getString("selectFolderFirst"));
        }

        if (file!=null && file.isDirectory() && file.canWrite()) {
            Font font = new Font(Font.DIALOG, Font.PLAIN, 16);
            frame = new JFrame(fileBros.getResourceBundle().getString("folderTitle"));
            frame.setIconImage(MyIconSet.getTnfolderIcon().getImage());
            frame.setSize(300, 100);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new FlowLayout());

            textFileName = new JTextField(14);
            textFileName.setFont(font);
            JButton button = new JButton(MyIconSet.getFoldercIcon());
            button.addActionListener(new CreateFolderListener());
            textFileName.addKeyListener(new CreateFolderListener());
            frame.add(textFileName);
            frame.add(button);
            frame.pack();
            frame.setVisible(true);
        } else fileBros.setMydisplayText(fileBros.getResourceBundle().getString("cantWriteHere"));
    }

    @Override
    public void updateLocale(ResourceBundle bundle) {
        // No need
    }

    class CreateFolderListener extends KeyAdapter implements ActionListener {
        void createFile() {
            if (!textFileName.getText().equals("")) {
                File file = new File(fileBros.getCurrentFolder() + "\\" + textFileName.getText());
                if (!file.mkdir()) {
                    fileBros.setMydisplayText(fileBros.getResourceBundle().getString("cantCreateFolderHere"));
                } else file.mkdir();

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileBros.getMyFolderTree().getLastSelectedPathComponent();
                DefaultTreeModel model = (DefaultTreeModel) fileBros.getMyFolderTree().getModel();
                FileNode fileNode = new FileNode(file);
                DefaultMutableTreeNode child =
                        new DefaultMutableTreeNode(new IconData(MyIconSet.getFolderIcon(), MyIconSet.getExpendedIcon(), fileNode));

                node.add(child);
                model.reload(node);
                frame.dispose();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            createFile();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                createFile();
            }
        }
    }
}

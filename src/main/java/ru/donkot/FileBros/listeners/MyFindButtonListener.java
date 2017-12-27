package ru.donkot.FileBros.listeners;

import ru.donkot.FileBros.FileBros;
import ru.donkot.FileBros.cellsnicons.MyIconSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;

//          Кнопка поиска

public class MyFindButtonListener implements ActionListener {
    //FIELDS
    private FileBros fileBros;
    private JFrame searchFrame;
    private JTextField textFind;
    private Vector<File> myHistory = new Vector<>(); // storing searched files

    private Font searchFont = new Font(Font.DIALOG, Font.PLAIN, 15);
    private Font buttonFont = new Font(Font.DIALOG, Font.BOLD, 12);

    //CONSTRUCTOR
    public MyFindButtonListener(FileBros fileBros) {
        this.fileBros = fileBros;
    }

    //FUNCTIONS
    @Override
    public void actionPerformed(ActionEvent e) {
        searchFrame = new JFrame(FileBros.resourceBundle.getString("searchTitle"));
        searchFrame.setIconImage(MyIconSet.getTsearchIcon().getImage());
        searchFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        searchFrame.setSize(300, 100);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setLayout(new FlowLayout());

        textFind = new JTextField(15);
        textFind.setFont(searchFont);
        textFind.addKeyListener(new MyFindListener());

        JButton searchButton = new JButton(MyIconSet.getSearchIcon());
        searchButton.addActionListener(new MyFindListener());
        searchButton.setFont(buttonFont);

        JButton historyButton = new JButton(MyIconSet.getHistoryIcon());
        historyButton.setFont(buttonFont);
        historyButton.setText(FileBros.resourceBundle.getString("historyButton"));
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (myHistory.size() != 0) {
                    fileBros.setMyFileListData(myHistory);
                }
            }
        });

        searchFrame.add(textFind);
        searchFrame.add(searchButton);
        searchFrame.add(historyButton);
        searchFrame.pack();
        searchFrame.setVisible(true);
    }

    protected class MyFindListener extends KeyAdapter implements ActionListener {
        Vector<File> fdata = new Vector<>(); //

        private void doActions() {
            fdata = new Vector<>();
           fileBros.getMyFileList().removeAll();
            find(fileBros.getCurrentFolder(), textFind.getText());
            textFind.setText("");
            myHistory.addAll(fdata);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doActions();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                doActions();
            }
        }

        //          search method
        void find(String path, String find) {
            try {
                File f = new File(path);
                String[] list = f.list();
                if (list.length < 1) return;
                for (String file : list) {
                    if (find.equals(file)) {
                        fdata.add(new File(path, file));
                    }
                    if (!path.endsWith("\\")) {
                        path += "\\";
                    }
                    File tempfile = new File(path, file);
                    if (!file.equals(".") && !file.equals("..")) {
                        if (tempfile.isDirectory()) {
                            find(path + file, find);
                        }
                    }
                }
                fileBros.setMyFileListData(fdata);
                searchFrame.dispose();
            } catch (NullPointerException c) {
                fileBros.setMydisplayText(FileBros.resourceBundle.getString("cantSearchHere"));
            }

        }
    }
}

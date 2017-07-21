package ru.donkot.FileBros;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 * Created by Soulbot17
 * Trial assignment for SPLAT
 */
/*
@TODO NEW

@TODO BUGS
*/

public class FileBros extends JFrame {

    private final boolean additionaltask = false; // set "true" to enable lazy loading

    private static final ImageIcon DISK_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/disk24.png");
    private static final ImageIcon FOLDER_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/folder24.png");
    private static final ImageIcon EXPENDED_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/expfolder24.png");
    private static final ImageIcon COMPUTER_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/computer24.png");
    private static final ImageIcon FOLDERC_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/createfolder16.png");
    private static final ImageIcon FOLDERD_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/deletefolder16.png");
    private static final ImageIcon FOLDERB_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/browsefolder16.png");
    private static final ImageIcon TITLE_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/progicon.png");
    private static final ImageIcon SEARCH_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/search-icon.png");
    private static final ImageIcon HISTORY_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/history-icon.png");
    private static final ImageIcon TNFOLDER_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/nicon16.png");
    private static final ImageIcon TSEARCH_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/sicon16.png");

    private JTree my_folderTree;
    private JTextField mydisplay;
    private DefaultTreeModel mytreemodel;
    private JList my_fileList = new JList();
    private Vector nullvector = new Vector();
    //          info panel
    private JLabel my_infoNameText = new JLabel("File name: no file selected yet.");
    private JLabel my_infoSizeText = new JLabel("File size: ...");
    private JLabel my_infoEditedText = new JLabel("File last edited: ...");
    private JLabel my_infoPathText = new JLabel("File path: ...");
    private JLabel my_infoIsHidden = new JLabel("File is hidden: ...");

    private JTextField my_textFind;
    Vector<File> my_history = new Vector<>(); // storing searched files

    private String currentFile = null;
    private String currentFolder = null;
    private DefaultMutableTreeNode currentNode = null;


    public static void main(String[] args) {
        new FileBros();
    }

    //          my constructor
    public FileBros() throws HeadlessException {
        super("FileDude");
        setIconImage(TITLE_ICON.getImage());
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2);
        setLocationRelativeTo(null);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new IconData(COMPUTER_ICON,null,"PC"));

        DefaultMutableTreeNode node;
        File[] list = File.listRoots();
        for (File aList : list) {
            node = new DefaultMutableTreeNode(new IconData(DISK_ICON, null, new FileNode(aList)));
            top.add(node);
            node.add(new DefaultMutableTreeNode(Boolean.TRUE));
        }
        mytreemodel = new DefaultTreeModel(top);
        my_folderTree = new JTree(mytreemodel);
        my_folderTree.setIgnoreRepaint(true);
        my_folderTree.putClientProperty("JTree.lineStyle","Aligned");
        TreeCellRenderer renderer = new IconCellRenderer();
        my_folderTree.setCellRenderer(renderer);
        my_folderTree.addTreeSelectionListener(new DirSelectionListener());
        my_folderTree.addTreeExpansionListener(new DirExpansionListener());
        my_folderTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        my_folderTree.setShowsRootHandles(true);
        my_folderTree.setEditable(false);
        mydisplay = new JTextField(32);
        mydisplay.setEditable(false);

        JScrollPane paneTree = new JScrollPane(my_folderTree);
        paneTree.setWheelScrollingEnabled(true);
        paneTree.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        paneTree.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    System.exit(0);
            }
        });

        JPanel myinfopanel = createInfoPanel();
        JPanel panelLeft = new JPanel(new BorderLayout());
        panelLeft.add(paneTree,BorderLayout.CENTER);
        panelLeft.add(mydisplay,BorderLayout.SOUTH);
        getContentPane().add(panelLeft,BorderLayout.WEST);

        JScrollPane panelList = new JScrollPane(my_fileList);
        panelList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel panelNorth = creteNorthPanel();
        getContentPane().add(panelNorth,BorderLayout.NORTH);

        my_fileList.setCellRenderer(new MyListCellRenderer());
        my_fileList.addListSelectionListener(new MyListSelectionListener());
        my_fileList.addMouseListener(new MyListMouseAdapter());
        JPanel panelRight = new JPanel(new BorderLayout());
        panelRight.add(panelList,BorderLayout.CENTER);
        panelRight.add(myinfopanel,BorderLayout.SOUTH);
        getContentPane().add(panelRight,BorderLayout.CENTER);
        setVisible(true);
    }

    //          north panel with create/delete etc. buttons
    private JPanel creteNorthPanel(){
        JPanel panel = new JPanel();
        Font font = new Font(Font.DIALOG,Font.BOLD,12);
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));

        JButton createButton = new JButton(FOLDERC_ICON);
        createButton.setText("Create folder");
        createButton.setFont(font);
        createButton.addActionListener(new MyCreateFolderFilstener());

        JButton deleteButton = new JButton(FOLDERD_ICON);
        deleteButton.setText("Delete folder");
        deleteButton.setFont(font);
        deleteButton.addActionListener(new MyDeleteFolderFilstener());

        JButton browseButton = new JButton(FOLDERB_ICON);
        browseButton.setText("Browse folder");
        browseButton.setFont(font);
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentFolder==null||currentFolder.equals("")) return;
                Desktop desktop = Desktop.getDesktop();
                File file = new File(currentFolder);
                try {
                    desktop.open(file);
                } catch (IOException | IllegalArgumentException e1) {
                    mydisplay.setText("Can't browse this folder");
                }
            }
        });

        JButton searchButton = new JButton(SEARCH_ICON);
        searchButton.setText("Search");
        searchButton.setFont(font);
        searchButton.addActionListener(new MyFindButtonListener());

        panel.add(createButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(browseButton);

        return panel;
    }
    //          find button activity
    protected class MyFindButtonListener implements ActionListener {
        JFrame searchFrame;
        JButton searchButton;
        JButton historyButton;
        Font searchFont = new Font(Font.DIALOG,Font.PLAIN,15);
        Font buttonFont = new Font(Font.DIALOG,Font.BOLD,12);

        @Override
        public void actionPerformed(ActionEvent e) {
            searchFrame = new JFrame("Search");
            searchFrame.setIconImage(TSEARCH_ICON.getImage());
            searchFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            searchFrame.setSize(300,100);
            searchFrame.setLocationRelativeTo(null);
            searchFrame.setLayout(new FlowLayout());

            my_textFind = new JTextField(15);
            my_textFind.setFont(searchFont);
            my_textFind.addKeyListener(new MyFindListener());

            searchButton = new JButton(SEARCH_ICON);
            searchButton.addActionListener(new MyFindListener());
            searchButton.setFont(buttonFont);

            historyButton = new JButton(HISTORY_ICON);
            historyButton.setFont(buttonFont);
            historyButton.setText("History");
            historyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (my_history.size()!=0) {
                        my_fileList.setListData(my_history);
                    }
                }
            });

            searchFrame.add(my_textFind);
            searchFrame.add(searchButton);
            searchFrame.add(historyButton);
            searchFrame.pack();
            searchFrame.setVisible(true);
        }

        protected class MyFindListener extends KeyAdapter implements ActionListener {
            Vector<File> fdata = new Vector<>(); //
            @Override
            public void actionPerformed(ActionEvent e) {
                fdata = new Vector<>();
                my_fileList.removeAll();
                find(currentFolder,my_textFind.getText());
                my_textFind.setText("");
                for (File s : fdata) {
                    my_history.add(s);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    fdata = new Vector<>();
                    my_fileList.removeAll();
                    find(currentFolder,my_textFind.getText());
                    my_textFind.setText("");
                    for (File s : fdata) {
                        my_history.add(s);
                    }
                }
            }

            //          search method
            void find(String path, String find) {
                try {
                    File f = new File(path);
                    String[] list = f.list();
                    if (list.length<1) return;
                    for (String file : list) {
                        if (find.equals(file)) {
                            fdata.add(new File(path,file));;
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
                    my_fileList.setListData(fdata);
                    searchFrame.dispose();
                } catch (NullPointerException c) {
                    mydisplay.setText("Can't search here");
                }

            }
        }
    }

    //          delete button activity
    protected class MyDeleteFolderFilstener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFolder==null||currentFolder.equals("")) return;
            File[] roots = File.listRoots();
            for (File r : roots) {
                if (currentFolder.equals(r.getAbsolutePath())) return;
            }
            File file = new File(currentFolder);
            if (!file.canWrite()) return;
            int reply = JOptionPane.showConfirmDialog(null, "Delete this folder?", "", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                //          deleting without apache FileUtil class. It not works well
                    /*if (file.isDirectory()) {
                        for (File c : file.listFiles())
                            c.delete();
                    }
                    if (!file.delete()) {
                        mydisplay.setText("Can't delete this folder");
                    } else file.delete();*/
                try {
                    //      with apache delete
                    FileUtils.deleteDirectory(file);
                } catch (IOException e1) {
                    mydisplay.setText("Can't delete this directory");
                }
                DefaultTreeModel model = (DefaultTreeModel) my_folderTree.getModel();
                model.removeNodeFromParent(currentNode);
                model.reload(currentNode.getNextNode());
            }
        }
    }

    //          create button activity
    protected class MyCreateFolderFilstener implements ActionListener {
        JFrame frame;
        JTextField textFileName;
        @Override
        public void actionPerformed(ActionEvent e) {
        if (currentFolder==null||currentFolder.equals("")) return;
            File file = null;
            try {
                file = new File(currentFolder);
            } catch (NullPointerException c) {
                mydisplay.setText("Select folder first");
            }

            if (file.isDirectory()&&file.canWrite()&&file!=null) {
                Font font = new Font(Font.DIALOG, Font.PLAIN, 16);
                frame = new JFrame("New Folder");
                frame.setIconImage(TNFOLDER_ICON.getImage());
                frame.setSize(300, 100);
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setLayout(new FlowLayout());

                textFileName = new JTextField(14);
                textFileName.setFont(font);
                JButton button = new JButton(FOLDERC_ICON);
                button.addActionListener(new CreateFolderListener());
                textFileName.addKeyListener(new CreateFolderListener());
                frame.add(textFileName);
                frame.add(button);
                frame.pack();
                frame.setVisible(true);
            } else mydisplay.setText("Can't write here");
        }

        class CreateFolderListener extends KeyAdapter implements ActionListener {
            void createFile() {
                if (!textFileName.getText().equals("")) {
                    File file = new File(currentFolder + "\\" + textFileName.getText());
                    if (!file.mkdir()) {
                        mydisplay.setText("Can't create folder here");
                    } else file.mkdir();
                    System.out.println("created");

                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) my_folderTree.getLastSelectedPathComponent();
                    DefaultTreeModel model = (DefaultTreeModel) my_folderTree.getModel();
                    FileNode fnode = new FileNode(file);
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(new IconData(FOLDER_ICON,EXPENDED_ICON,fnode));

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
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    createFile();
                }
            }
        }
    }


    //          double click to open
    protected class MyListMouseAdapter extends MouseAdapter{
        Desktop desktop = Desktop.getDesktop();
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount()==2) {
                try {
                    desktop.open(new File(currentFile));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //          list selector
    protected class MyListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (my_fileList.getSelectedValue()!=null) {
                currentFile = my_fileList.getSelectedValue().toString();
                File file = new File(my_fileList.getSelectedValue().toString());
                String myselectedfileinfo = getFileName(my_fileList.getSelectedValue().toString());
                String bytes = humanReadableByteCount(file.length(),true);
                my_infoNameText.setText(String.format("File name: %s", myselectedfileinfo));
                my_infoSizeText.setText(String.format("File size: %s",bytes));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
                my_infoEditedText.setText(String.format("File last edited: %s",sdf.format(file.lastModified())));
                my_infoPathText.setText("File path: "+file.getAbsolutePath());
                my_infoIsHidden.setText(String.format("File is hidden: %s", file.isHidden() ? "yes" : "no"));
            }
        }

        //          stolen awesome method to display readable byte
        String humanReadableByteCount(long bytes, boolean si) {
            int unit = si ? 1000 : 1024;
            if (bytes < unit) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(unit));
            String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
            return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
        }
    }

    //          panel with information about files
    private JPanel createInfoPanel(){
        JPanel panel = new JPanel();
        Font font = new Font(Font.SANS_SERIF,Font.PLAIN,12);
        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
        panel.setFocusable(false);
        panel.setBorder(BorderFactory.createEtchedBorder());
        my_infoNameText.setFont(font);
        panel.add(my_infoNameText);
        my_infoSizeText.setFont(font);
        panel.add(my_infoSizeText);
        my_infoEditedText.setFont(font);
        my_infoIsHidden.setFont(font);
        panel.add(my_infoIsHidden);
        panel.add(my_infoEditedText);
        my_infoPathText.setFont(font);
        panel.add(my_infoPathText);
        return panel;
    }

    //          get TreeNode from path
    private DefaultMutableTreeNode getTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) path.getLastPathComponent();
    }

    //          get FileNode from TreeNode
    private FileNode getFileNode(DefaultMutableTreeNode node) {
        if (node==null) return null;
        Object obj = node.getUserObject();
        if (obj instanceof IconData) {
            obj = ((IconData) obj).getData();
        }
        if (obj instanceof FileNode) {
            return (FileNode) obj;
        } else return null;
    }

    //          my expansion listener
    class DirExpansionListener implements TreeExpansionListener{

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            final DefaultMutableTreeNode node = getTreeNode(event.getPath());
            final FileNode fnode = getFileNode(node);
            Thread runner = new Thread(){
              public void run(){
                  if (fnode!=null&&fnode.expand(node)) {
                      if (additionaltask) {
                          try {
                              Thread.sleep(2000);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                      Runnable runnable = new Runnable() {
                          @Override
                          public void run() {
                              mytreemodel.reload(node);
                          }
                      };
                      SwingUtilities.invokeLater(runnable);
                  }
              }
            };
            runner.start();
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
        }
    }

    //          when you select node in file tree...
    class DirSelectionListener implements TreeSelectionListener{
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            clearInfoPanel();
            currentFolder = e.getPath().toString();
            DefaultMutableTreeNode node = getTreeNode(e.getPath());
            currentNode = getTreeNode(e.getPath());
            FileNode fnode = getFileNode(node);
            Vector<File> vfiles = new Vector<>();
            if (fnode!=null) {
                currentFolder = fnode.getFile().getAbsolutePath();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
                try {
//                    mydisplay.setText("Files: "+String.valueOf(fnode.getFile().listFiles().length)+" | Last modified: "+sdf.format(fnode.getFile().lastModified()));
                    mydisplay.setText("Files: "+String.valueOf(fnode.getFile().listFiles().length));
                } catch (NullPointerException g) {
                    mydisplay.setText("");
                }
                File file = new File(currentFolder);
                File[] files = file.listFiles();
                if (files!=null&&files.length>0) {
                    for (File file1 : files) {
                        if (!file1.isDirectory()) {
                            vfiles.add(file1);
                        }
                    }
                    my_fileList.setListData(vfiles);
                } else my_fileList.setListData(nullvector);
            } else mydisplay.setText("");
        }
    }

    private void clearInfoPanel(){
        my_infoNameText.setText("File name: no file selected yet.");
        my_infoSizeText.setText("File size: ...");
        my_infoEditedText.setText("File last edited: ...");
        my_infoPathText.setText("File path: ...");
        my_infoIsHidden.setText("File is hidden: ...");
    };

    //          nice icons for my JTree
    class IconCellRenderer extends JLabel implements TreeCellRenderer{
        Color my_textSelectionColor;
        Color my_textNonSelectedColor;
        Color my_bkSelectedColor;
        Color my_bkNonSelectedColor;
        Color my_borderSelectedColor;

        boolean my_selected;

        public IconCellRenderer() {
            my_textSelectionColor = UIManager.getColor("Tree.selectionForeground");
            my_textNonSelectedColor = UIManager.getColor("Tree.textForeground");
            my_bkSelectedColor = UIManager.getColor("Tree.selectionBackground");
            my_bkNonSelectedColor = UIManager.getColor("Tree.textBackground");
            my_borderSelectedColor = UIManager.getColor("Tree.selectionBorderColor");
            setOpaque(false);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object obj = node.getUserObject();
            setText(obj.toString());

            if (obj instanceof Boolean) setText("Waiting for data...");
            if (obj instanceof IconData) {
                IconData idata = (IconData) obj;
                if (expanded) {
                    setIcon(idata.getExpanded());
                } else setIcon(idata.getIcon());
            }
            else setIcon(null);
            setFont(tree.getFont());
            setForeground(selected ? my_textSelectionColor : my_textNonSelectedColor);
            setBackground(selected ? my_bkSelectedColor : my_bkNonSelectedColor);
            my_selected = selected;
            return this;
        }

        //          selection animation
        @Override
        protected void paintComponent(Graphics g) {
            Color bColor = getBackground();
            Icon icon = getIcon();

            g.setColor(bColor);
            int offset = 0;
            if (icon != null && getText() != null) {
                offset = (icon.getIconWidth() + getIconTextGap());
            }
            g.fillRect(offset,0,getWidth()-1-offset,getHeight()-1);
            if (my_selected) {
                g.setColor(my_borderSelectedColor);
                g.drawRect(offset,0,getWidth()-1-offset,getHeight()-1);
            }
            super.paintComponent(g);
        }
    }

    //          file nodes to tree
    class FileNode {
        File my_file;

        public FileNode(File my_file) {
            this.my_file = my_file;
        }

        File getFile() {
            return my_file;
        }

        @Override
        public String toString() {
            return my_file.getName().length() > 0 ? my_file.getName() : my_file.getPath();
        }

        boolean expand(DefaultMutableTreeNode parent) {
            DefaultMutableTreeNode flag = (DefaultMutableTreeNode) parent.getFirstChild();
            if (flag==null) { //no flag
                return false;
            }
            Object obj = flag.getUserObject();
            if (!(obj instanceof Boolean)) { //already expanded
                return false;
            }
            parent.removeAllChildren(); //remove flag

            File[]files = listFiles();
            if (files==null) return true;

            Vector v = new Vector();
            for (File f : files) {
                if (!(f.isDirectory())) continue;

                FileNode fnode = new FileNode(f);
                boolean isAdded = false;
                for (int i = 0; i < v.size(); i++) {
                    FileNode nd = (FileNode) v.elementAt(i);
                    if (fnode.compareTo(nd) < 0) {
                        v.insertElementAt(fnode, i);
                        isAdded = true;
                        break;
                    }
                }
                if (!isAdded) {
                    v.addElement(fnode);
                }
            }
            for (int i = 0; i<v.size();i++) {
                FileNode nd = (FileNode) v.elementAt(i);
                IconData data = new IconData(FileBros.FOLDER_ICON, FileBros.EXPENDED_ICON,nd);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);
                parent.add(node);

                if (nd.hasSubDirs()) {
                    node.add(new DefaultMutableTreeNode(Boolean.TRUE));
                }
            }
            return true;
        }

        boolean hasSubDirs() {
            File[] files = listFiles();
            if (files==null) {
                return false;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    return true;
                }
            }
            return false;
        }

        int compareTo(FileNode toCompare) {
            return my_file.getName().compareToIgnoreCase(toCompare.my_file.getName());
        }

        File[] listFiles() {
            if (!my_file.isDirectory()) {
                return null;
            }
            return my_file.listFiles();
        }
    }

    //          stores information about object's icons
    class IconData {
        Icon n_icon;
        Icon n_expanded;
        Object n_data;

        public IconData(Icon n_icon, Icon n_expanded, Object n_data) {
            this.n_icon = n_icon;
            this.n_expanded = n_expanded;
            this.n_data = n_data;
        }

        public IconData(Icon n_icon, Object n_data) {
            this.n_icon = n_icon;
            this.n_data = n_data;
            this.n_expanded = null;
        }

        public Icon getIcon() {
            return n_icon;
        }

        public Icon getExpanded() {
            return n_expanded!=null ? n_expanded : n_icon;
        }

        public Object getData() {
            return n_data;
        }

        public String toString() {
            return n_data.toString();
        }
    }


    //          get name from path
    protected String getFileName(String filepath){
        String fileName = filepath.substring(filepath.lastIndexOf("\\")+1,filepath.length());
        return fileName;
    }

    //          system view for my file list
    class MyListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            FileSystemView view = FileSystemView.getFileSystemView();
            JLabel label = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
            label.setText(view.getSystemDisplayName((File) value));
            label.setFont(new Font(Font.DIALOG,Font.PLAIN,14));
            label.setIcon(view.getSystemIcon((File) value));
            label.setHorizontalTextPosition(RIGHT);
            return label;
        }
    }
}

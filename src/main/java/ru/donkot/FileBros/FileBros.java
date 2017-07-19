package main.java.ru.donkot.FileBros;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
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
 */
/*
@TODO NEW
-optimize code, it's dirty as hell
-REFACTOR. I need fucking JFrame to call

@TODO BUGS
-lock jtree size
*/

public class FileBros extends JFrame {

    private static final ImageIcon DISK_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/disk24.png");
    private static final ImageIcon FOLDER_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/folder24.png");
    private static final ImageIcon EXPENDED_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/expfolder24.png");
    private static final ImageIcon COMPUTER_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/computer24.png");
    private static final ImageIcon FOLDERC_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/createfolder16.png");
    private static final ImageIcon FOLDERD_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/deletefolder16.png");
    private static final ImageIcon FOLDERB_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/browsefolder16.png");
    private static final ImageIcon REFRESH_ICON = new ImageIcon("src/main/java/ru/donkot/FileBros/iconset/refresh16.png");

    private JPanel myinfopanel;
    private JTree mytree;
    private JTextField mydisplay;
    private DefaultTreeModel mytreemodel;
    private JPanel panelLeft = new JPanel(new BorderLayout());
    private JPanel panelRight = new JPanel(new BorderLayout());
    private JList jList = new JList();
    private Vector nullvector = new Vector();
    private String myselectedfileinfo;
    private JLabel my_infoNameText = new JLabel("File name: no file selected yet.");
    private JLabel my_infoSizeText = new JLabel("File size: ...");
    private JLabel my_infoEditedText = new JLabel("File last edited: ...");
    private JLabel my_infoPathText = new JLabel("File path: ...");
    private JLabel my_infoIsHidden = new JLabel("File is hidden: ...");

    private JScrollPane paneTree;
    private JPanel panelNorth;
    private String currentFile = null;
    private String currentFolder = null;
    private DefaultMutableTreeNode currentNode = null;


    public static void main(String[] args) {
        new FileBros();
    }

    //          my constructor
    public FileBros() throws HeadlessException {
        super("FileDude");
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width/2,Toolkit.getDefaultToolkit().getScreenSize().height/2);
        setLocationRelativeTo(null);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new IconData(COMPUTER_ICON,null,"PC"));

        DefaultMutableTreeNode node;
        File[] list = File.listRoots();
        for (int i = 0; i<list.length;i++) {
            node = new DefaultMutableTreeNode(new IconData(DISK_ICON,null,new FileNode(list[i])));
            top.add(node);
            node.add(new DefaultMutableTreeNode(new Boolean(true)));
        }
        mytreemodel = new DefaultTreeModel(top);
        mytree = new JTree(mytreemodel);
        mytree.setIgnoreRepaint(true);

        mytree.putClientProperty("JTree.lineStyle","Aligned");

        TreeCellRenderer renderer = new IconCellRenderer();
        mytree.setCellRenderer(renderer);

        mytree.addTreeSelectionListener(new DirSelectionListener());
        mytree.addTreeExpansionListener(new DirExpansionListener());

        mytree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        mytree.setShowsRootHandles(true);
        mytree.setEditable(false);
        mydisplay = new JTextField(32);
        mydisplay.setEditable(false);

        paneTree = new JScrollPane(mytree);
        paneTree.setWheelScrollingEnabled(true);
        paneTree.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        paneTree.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

//        add(paneTree,BorderLayout.WEST);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    System.exit(0);

            }
        });

        myinfopanel = createInfoPanel();
//        setResizable(false);
        panelLeft.add(paneTree,BorderLayout.CENTER);
        panelLeft.add(mydisplay,BorderLayout.SOUTH);
        getContentPane().add(panelLeft,BorderLayout.WEST);

        JScrollPane panelList = new JScrollPane(jList);
        panelList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panelNorth = creteNorthPanel();
        getContentPane().add(panelNorth,BorderLayout.NORTH);

        jList.setCellRenderer(new MyListCellRenderer());
        jList.addListSelectionListener(new MyListSelectionListener());
        jList.addMouseListener(new MyListMouseAdapter());
        panelRight.add(panelList,BorderLayout.CENTER);
        panelRight.add(myinfopanel,BorderLayout.SOUTH);
        getContentPane().add(panelRight,BorderLayout.CENTER);
        setVisible(true);
    }

    //          north panel with create/delete etc. buttons
    protected JPanel creteNorthPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        JButton createButton = new JButton(FOLDERC_ICON);
        createButton.setText("Create folder");
        createButton.addActionListener(new MyCreateFolderFilstener());
        JButton deleteButton = new JButton(FOLDERD_ICON);
        deleteButton.setText("Delete fodler");
        deleteButton.addActionListener(new MyDeleteFolderFilstener());
        JButton browseButton = new JButton(FOLDERB_ICON);
        browseButton.setText("Browse folder");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentFolder==null||currentFolder.equals("")) return;
                Desktop desktop = Desktop.getDesktop();
                File file = new File(currentFolder);
                try {
                    desktop.open(file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JButton refreshButton = new JButton(REFRESH_ICON);
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FileBros();
                dispose();
            }
        });

        panel.add(createButton);
        panel.add(deleteButton);
        panel.add(browseButton);
        panel.add(refreshButton);

        return panel;
    }

    protected class MyDeleteFolderFilstener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentFolder==null||currentFolder.equals("")) return;
            File[] roots = File.listRoots();
            for (File r : roots) {
                if (currentFolder.equals(r.getAbsolutePath())) return;
            }
            File file = new File(currentFolder);
            if (file.canWrite()==false) return;
            int reply = JOptionPane.showConfirmDialog(null, "Delete this folder?", "", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                //          deleting without apache FileUtil class
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
                DefaultTreeModel model = (DefaultTreeModel) mytree.getModel();
                model.removeNodeFromParent(currentNode);
                model.reload(currentNode.getNextNode());
            }
        }
    }
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
                    if (file.mkdir()==false) {
                        mydisplay.setText("Can't create folder here");
                    } else file.mkdir();
                    System.out.println("created");

                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) mytree.getLastSelectedPathComponent();
                    DefaultTreeModel model = (DefaultTreeModel) mytree.getModel();
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
            if (e.getValueIsAdjusting()){
                currentFile = jList.getSelectedValue().toString();
                File file = new File(jList.getSelectedValue().toString());
                myselectedfileinfo = getFileName(jList.getSelectedValue().toString());
                String bytes = humanReadableByteCount(file.length(),true);
                my_infoNameText.setText(String.format("File name: %s",myselectedfileinfo));
                my_infoSizeText.setText(String.format("File size: %s",bytes));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
                my_infoEditedText.setText(String.format("File last edited: %s",sdf.format(file.lastModified())));
                my_infoPathText.setText("File path: "+file.getAbsolutePath());
                my_infoIsHidden.setText(String.format("File is hidden: %s",file.isHidden()==true ? "yes" : "no"));
            }
        }

        //          stolen awesome method to display readable byte
        public String humanReadableByteCount(long bytes, boolean si) {
            int unit = si ? 1000 : 1024;
            if (bytes < unit) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(unit));
            String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
            return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
        }
    }

    //          panel with information about files
    protected JPanel createInfoPanel(){
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
    DefaultMutableTreeNode getTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) path.getLastPathComponent();
    }

    //          get FileNode from TreeNode
    FileNode getFileNode(DefaultMutableTreeNode node) {
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
                      /*
                      ADDITIONAL TASK 1
                      try {
                          Thread.sleep(2000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }*/
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
                if (currentFolder==null) return;
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
                    for (int i = 0; i<files.length;i++) {
                        if (!files[i].isDirectory()) {
                            vfiles.add(files[i]);
                        } else continue;
                    }
                    jList.setListData(vfiles);
                } else jList.setListData(nullvector);
            } else mydisplay.setText("");
        }
    }

    protected void clearInfoPanel(){
        my_infoNameText.setText("File name: no file selected yet.");
        my_infoSizeText.setText("File size: ...");
        my_infoEditedText.setText("File last edited: ...");
        my_infoPathText.setText("File path: ...");
        my_infoIsHidden.setText("File is hidden: ...");
    };

    //          nice icons for my JTree
    class IconCellRenderer extends JLabel implements TreeCellRenderer{
        protected Color my_textSelectionColor;
        protected Color my_textNonSelectedColor;
        protected Color my_bkSelectedColor;
        protected Color my_bkNonSelectedColor;
        protected Color my_borderSelectedColor;

        protected boolean my_selected;

        public IconCellRenderer() {
            super();
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


    class FileNode {
        protected File my_file;

        public FileNode(File my_file) {
            this.my_file = my_file;
        }

        public File getFile() {
            return my_file;
        }

        @Override
        public String toString() {
            return my_file.getName().length() > 0 ? my_file.getName() : my_file.getPath();
        }

        public boolean expand (DefaultMutableTreeNode parent) {
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
            for (int m = 0; m<files.length;m++) {
                File f = files[m];
                if (!(f.isDirectory())) continue;

                FileNode fnode = new FileNode(f);
                boolean isAdded = false;
                for (int i = 0; i<v.size();i++) {
                    FileNode nd = (FileNode) v.elementAt(i);
                    if (fnode.compareTo(nd)<0) {
                        v.insertElementAt(fnode,i);
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
                    node.add(new DefaultMutableTreeNode(new Boolean(true)));
                }
            }
            return true;
        }

        protected boolean hasSubDirs() {
            File[] files = listFiles();
            if (files==null) {
                return false;
            }
            for (int m = 0; m<files.length;m++) {
                if (files[m].isDirectory()) {
                    return true;
                }
            }
            return false;
        }

        protected int compareTo(FileNode toCompare) {
            return my_file.getName().compareToIgnoreCase(toCompare.my_file.getName());
        }

        protected File[] listFiles() {
            if (!my_file.isDirectory()) {
                return null;
            }
            return my_file.listFiles();
        }
    }


    class IconData {
        protected Icon n_icon;
        protected Icon n_expanded;
        protected Object n_data;

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

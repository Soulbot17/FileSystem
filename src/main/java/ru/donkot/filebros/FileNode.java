package ru.donkot.filebros;

import ru.donkot.filebros.cellsnicons.IconData;
import ru.donkot.filebros.cellsnicons.MyIconSet;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

/**
 * file nodes to tree
 */
public class FileNode {
    //FIELDS
    private File myFile;

    //CONSTRUCTOR
    public FileNode(File myFile) {
        this.myFile = myFile;
    }

    //GETTERS AND SETTERS
    public File getFile() {
        return myFile;
    }

    //FUNCTIONS
    @Override
    public String toString() {
        return myFile.getName().length() > 0 ? myFile.getName() : myFile.getPath();
    }

    public boolean expand(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode flag = (DefaultMutableTreeNode) parent.getFirstChild(); // смотрим, можно ли развернуть папку
        if (flag == null) { //no flag
            return false;
        }

        Object obj = flag.getUserObject(); // присваиваем первого потомка к объекту
        if (!(obj instanceof Boolean)) { //already expanded
            return false;
        }
        parent.removeAllChildren(); //remove flag

        File[] files = listFiles();
        if (files == null) return true; // если пустой - вернуть ИСТИНУ

        Vector nodes = new Vector();

        for (File f : files) { // пока в массиве файлов есть файлы
            if (!(f.isDirectory())) continue; // если не дерриктория - продолжаем

            FileNode fnode = new FileNode(f); // делаем файл нод из файла
            boolean isAdded = false; // не добавлено
            for (int i = 0; i < nodes.size(); i++) { // от 0 до конца вектора
                FileNode nd = (FileNode) nodes.elementAt(i); // беру i обект
                if (fnode.compareTo(nd) < 0) {  // если имя меньше
                    nodes.insertElementAt(fnode, i); // добавить fileNode на место i
                    isAdded = true; // флаг добавлено
                    break;
                }
            }
            if (!isAdded) { // если не добавилось - ДОБАВИТЬ
                nodes.addElement(fnode);
            }
        }
        printFileTreeByParent(parent, nodes);
        return true;
    }

    private void printFileTreeByParent(DefaultMutableTreeNode parent, Vector nodeVector) {
        for (int i = 0; i < nodeVector.size(); i++) { // от 0 до конца вектора
            FileNode nd = (FileNode) nodeVector.elementAt(i);
            IconData data = new IconData(MyIconSet.getFolderIcon(), MyIconSet.getExpendedIcon(), nd); // делаем иконочку папки для дерева из файл нод
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(data); // создаём node девера
            parent.add(node); // добавляем эту иконку к корневому каталогу

            if (nd.hasSubDirs()) { // если у нода есть поддерриктории
                node.add(new DefaultMutableTreeNode(Boolean.TRUE)); // может иметь "детей"
            }
        }
    }

    private boolean hasSubDirs() {
        File[] files = listFiles();
        return files != null && Arrays.stream(files).anyMatch(File::isDirectory);
    }

    private int compareTo(FileNode toCompare) { //сравнение длины имени
        return myFile.getName().compareToIgnoreCase(toCompare.myFile.getName());
    }

    private File[] listFiles() { // если это папка - нахуй, null, иначе массив с файлами
        if (!myFile.isDirectory()) {
            return new File[0];
        }
        return myFile.listFiles();
    }
}

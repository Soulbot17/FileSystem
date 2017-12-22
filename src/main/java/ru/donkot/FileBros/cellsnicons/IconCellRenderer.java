package ru.donkot.FileBros.cellsnicons;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
//          nice icons for my JTree
public class IconCellRenderer extends JLabel implements TreeCellRenderer {
    //FIELDS
    private Color my_textSelectionColor;
    private Color my_textNonSelectedColor;
    private Color my_bkSelectedColor;
    private Color my_bkNonSelectedColor;
    private Color my_borderSelectedColor;

    private boolean my_selected;

    //CONSTRUCTOR
    public IconCellRenderer() { //  созданным цветам присваиваем цвета системы
        my_textSelectionColor = UIManager.getColor("Tree.selectionForeground");
        my_textNonSelectedColor = UIManager.getColor("Tree.textForeground");
        my_bkSelectedColor = UIManager.getColor("Tree.selectionBackground");
        my_bkNonSelectedColor = UIManager.getColor("Tree.textBackground");
        my_borderSelectedColor = UIManager.getColor("Tree.selectionBorderColor");
        setOpaque(false);
    }

    //FUNCTIONS
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) { //создание иконок jtree
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value; // пиздим значение
        Object obj = node.getUserObject(); // зачем то копирую в отдельный объект
        setText(obj.toString()); // меняем текст на текст объекта (название файла)

        if (obj instanceof Boolean) setText("Waiting for data..."); // если false - ждём
        if (obj instanceof IconData) { // если икондата
            IconData idata = (IconData) obj;
            if (expanded) {
                setIcon(idata.getExpanded());
            } else setIcon(idata.getIcon());
        } else setIcon(null);
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
        g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
        if (my_selected) {
            g.setColor(my_borderSelectedColor);
            g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
        }
        super.paintComponent(g);
    }
}

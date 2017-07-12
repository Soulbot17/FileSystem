package thrash;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by SuperDenissio on 11.07.2017.
 */
public class Testy {
    private Map<String, ImageIcon> imageMap = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Testy();
            }
        });
    }

    public Testy() {
        String[] animalsList = {"cat","dog","pig","dick"};
        imageMap = createImageMap();
        JList list = new JList(animalsList);
        list.setCellRenderer(new AnimalsRenderer());

        JScrollPane pane = new JScrollPane(list);

        JFrame frame = new JFrame("idk");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(pane);
        frame.setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x,0);
        frame.pack();
        frame.setVisible(true);
    }

    class AnimalsRenderer extends DefaultListCellRenderer{
        Font font = new Font(Font.MONOSPACED,Font.BOLD,32);
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
            label.setIcon(imageMap.get(value));
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(font);
            return label;
        }
    }

    private Map<String,ImageIcon> createImageMap() {
        Map<String,ImageIcon> map = new TreeMap<>();
        try {
            map.put("cat",new ImageIcon(new URL("http://icons.iconarchive.com/icons/iconka/meow-2/256/cat-sing-icon.png ")));
            map.put("dog",new ImageIcon(new URL("http://vignette3.wikia.nocookie.net/thehuntergame/images/0/0b/Labrador_retriever_white_male.png/revision/latest?cb=20160304224243")));
            map.put("pig",new ImageIcon(new URL("http://www.pngmart.com/files/1/Pig-Transparent-Background.png")));
            map.put("dick",new ImageIcon(new URL("http://files.gamebanana.com/img/ico/sprays/mr_banana_257.png")));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return map;
    }
}

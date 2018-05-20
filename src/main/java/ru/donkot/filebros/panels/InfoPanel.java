package ru.donkot.filebros.panels;

import ru.donkot.filebros.FileBros;
import ru.donkot.filebros.Localizable;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class InfoPanel extends JPanel implements Localizable{

    //FIELDS
    private JLabel myInfoNameText;
    private JLabel myInfoSizeText;
    private JLabel myInfoEditedText;
    private JLabel myInfoPathText;
    private JLabel myInfoIsHidden;

    private Font mainFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    //CONSTRUCTOR
    public InfoPanel(FileBros fileBros) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder());

        prepareTextLabels();
        updateLocale(fileBros.getResourceBundle());
    }

    //GETTERS AND SETTERS
    public void setMyInfoNameText(String text) { myInfoNameText.setText(text); }

    public void setMyInfoSizeText(String text) {
        myInfoSizeText.setText(text);
    }

    public void setMyInfoEditedText(String text) {
        myInfoEditedText.setText(text);
    }

    public void setMyInfoPathText(String text) {
        myInfoPathText.setText(text);
    }

    public void setMyInfoIsHidden(String text) {
        myInfoIsHidden.setText(text);
    }

    //FUNCTIONS
    private void prepareTextLabels() {
        myInfoNameText = new JLabel();
        myInfoNameText.setFont(mainFont);
        add(myInfoNameText);

        myInfoSizeText = new JLabel();
        myInfoSizeText.setFont(mainFont);
        add(myInfoSizeText);

        myInfoEditedText = new JLabel();
        myInfoEditedText.setFont(mainFont);
        add(myInfoEditedText);

        myInfoPathText = new JLabel();
        myInfoPathText.setFont(mainFont);
        add(myInfoPathText);

        myInfoIsHidden = new JLabel();
        myInfoIsHidden.setFont(mainFont);
        add(myInfoIsHidden);
    }

    @Override
    public void updateLocale(ResourceBundle bundle) {
        myInfoNameText.setText(bundle.getString("myInfoNameText"));
        myInfoSizeText.setText(bundle.getString("myInfoSizeText"));
        myInfoEditedText.setText(bundle.getString("myInfoEditedText"));
        myInfoPathText.setText(bundle.getString("myInfoPathText"));
        myInfoIsHidden.setText(bundle.getString("myInfoIsHidden"));
    }
}

package main;

import javax.swing.*;
import java.awt.*;

public class WordSnapshot extends JPanel {


    private static final int MAX_BOX = 32;
    private static final int MIN_BOX = 10;
    private static final int GAP = 0;

    private String word = "";
    private int selectedIndex;



    public WordSnapshot(){
        setOpaque(false);
        setFocusable(true);
        setBorder(BorderFactory.createMatteBorder(2, 2, 2,2, Color.BLUE));
    }

    public void setWord(String word, int selected){
        this.word = (word == null) ? " " : word;
        this.selectedIndex = selected;
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize(){
        int n = Math.max(1, word.length());
        // Calculate exactly how wide the panel needs to be to hold all boxes at maximum size
        int preferredWidth = GAP + n * (MAX_BOX + GAP);
        int preferredHeight = MAX_BOX + 2 * GAP;

        return new Dimension(preferredWidth, preferredHeight);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int n = word.length();
        System.out.println("Word length is " + n + "\n");
        System.out.println("n=" + word.length() + " width=" + getWidth() + " height=" + getHeight());
        if(n == 0) return;


        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int fit = (getWidth() - GAP) / n - GAP;
        int size = Math.clamp(fit, MIN_BOX, MAX_BOX);
        int y = (getHeight() - size) / 2;

        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size * 3 / 5));
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < n; i++){
            System.out.println("Setting character " + i + "\n");
            int x = GAP + i * (size + GAP);
            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, size, size);
            if(i == selectedIndex){
                g2.setStroke(new BasicStroke(2.0f));
                g2.setColor(Color.RED);
            }
            else{
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1.0f));
            }
            g2.drawRect(x, y, size, size);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1.0f));
            char ch = word.charAt(i);
            if(Character.isLetterOrDigit(ch)){
                String s = String.valueOf(Character.toUpperCase(ch));
                int tx = x + (size - fm.stringWidth(s)) / 2;
                int ty = y + (size - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(s, tx, ty);
            }
        }
    }
}

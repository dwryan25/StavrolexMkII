package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;


public class KeyHandler extends KeyAdapter {
    private final GamePanel gp;


    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
       JTextField source = (JTextField) e.getSource();
    }

}

package dvsdk.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *
 * @author develop-dvs
 */
public class MulisKeyIntListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c)) {
            e.consume();
        } else {
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) { 

    }
}
package dvsdk.client;

import java.text.ParseException;
import javax.swing.JFormattedTextField;

/**
 *
 * @author develop-dvs
 */
public class MulisIntFormatter extends JFormattedTextField.AbstractFormatter{

    @Override
    public void install(final JFormattedTextField ftf) {
        int prevLen = ftf.getDocument().getLength();
        int savedCaretPos = ftf.getCaretPosition();
        super.install(ftf);
        if (ftf.getDocument().getLength() == prevLen) {
            ftf.setCaretPosition(savedCaretPos);
        }
    }
    
    //TODO: FIX!
    
    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text==null) return "";
        return Integer.parseInt(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value==null) return "";
        return Integer.toString(((Number) value).intValue());
    }   
}
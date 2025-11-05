package by.gomel.freedev.ucframework.ucswing.uicontrols.filters;

import by.gomel.freedev.ucframework.ucswing.uicontrols.interfaces.IUCComponent;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Created by Andy on 23.03.2015.
 */
public class CustomDocumentFilter extends DocumentFilter {
    private final String regex;
    private IUCComponent component;

    public CustomDocumentFilter(IUCComponent component, String regex) {
        this.component = component;
        this.regex = regex;
    }

    @Override
    public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
        super.remove(fb, offset, length);
        hideBaloon();
    }

    @Override
    public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr) throws BadLocationException {
        if (isValid(string)) {
            super.insertString(fb, offset, string, attr);
        }
        hideBaloon();
    }

    @Override
    public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs) throws BadLocationException {
        if (isValid(text)) {
            super.replace(fb, offset, length, text, attrs);
        }
        hideBaloon();
    }

    private boolean isValid(String value) {
        return regex.equals("") || value.matches(regex);
    }

    private void hideBaloon() {
        if (component.getUCController() != null) {
            component.getUCController().getBaloon().hideBaloon();
        }
    }
}

package edu.cmu.jsphdev.picky.exception;

import android.widget.TextView;

/**
 * CustomException to be thrown when text changed in a text field does not follow the grammar business rules of the app.
 */
public class InvalidCredentialGrammarExceptipn extends PickyException {

    TextView errorView;
    String message;

    public InvalidCredentialGrammarExceptipn(TextView textView, String message) {
        this.errorView = textView;
        this.message = message;
    }

    @Override
    public void fix() {
        errorView.setError(message);
    }
}

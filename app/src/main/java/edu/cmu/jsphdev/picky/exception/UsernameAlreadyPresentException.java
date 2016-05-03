package edu.cmu.jsphdev.picky.exception;

import android.content.Context;
import android.widget.Toast;

/**
 * CustomException to be thrown when the user opts for a username that is already present in the system.
 */
public class UsernameAlreadyPresentException extends PickyException {

    Context context;

    public UsernameAlreadyPresentException(Context context) {
        this.context = context;
    }

    @Override
    public void fix() {
        Toast.makeText(context, "Username already taken!", Toast.LENGTH_LONG).show();
    }
}

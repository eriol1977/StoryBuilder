package storybuilder.validation;

import java.util.logging.Level;
import java.util.logging.Logger;
import storybuilder.main.view.MainWindowController;

/**
 *
 * @author Francesco Bertolino
 */
public class ErrorManager
{

    public static void showErrorMessage(final String message)
    {
        MainWindowController.getInstance().updateStatusBarMessage(message);
        Logger.getGlobal().log(Level.SEVERE, message);
    }

}

package storybuilder.validation;

import java.util.logging.Level;
import java.util.logging.Logger;
import storybuilder.main.MainWindowController;

/**
 *
 * @author Francesco Bertolino
 */
public class ErrorManager
{

    public static void showErrorMessage(final Class<?> clazz, final String message)
    {
        MainWindowController.getInstance().updateStatusBarMessage(message);
        Logger.getLogger(clazz.getName()).log(Level.SEVERE, message);
    }
    
    public static void showErrorMessage(final Class<?> clazz, final String message, final Exception ex)
    {
        MainWindowController.getInstance().updateStatusBarMessage(message);
        Logger.getLogger(clazz.getName()).log(Level.SEVERE, message, ex);
    }
}

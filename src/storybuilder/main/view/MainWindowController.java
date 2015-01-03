package storybuilder.main.view;

import storybuilder.main.view.AbstractView;
import javafx.stage.Stage;

/**
 *
 * @author Francesco Bertolino
 */
public class MainWindowController
{

    private final static MainWindowController instance = new MainWindowController();

    private MainWindow mainWindow;

    private MainWindowController()
    {
    }

    public final static MainWindowController getInstance()
    {
        return instance;
    }

    public MainWindow getMainWindow()
    {
        return mainWindow;
    }

    public void setMainWindow(MainWindow mainWindow)
    {
        this.mainWindow = mainWindow;
    }

    public void updateTitle()
    {
        mainWindow.updateTitle();
    }

    public void updateStatusBarMessage(final String message)
    {
        mainWindow.getMainPane().getStatusBar().setMessage(message);
    }

    public void switchView(final AbstractView view)
    {
        mainWindow.getMainPane().setContent(view);
    }

    public void enableMenus(final boolean enable)
    {
        mainWindow.getMainPane().getMenuBar().enableMenus(enable);
    }

    public Stage getStage()
    {
        return mainWindow.getStage();
    }
}

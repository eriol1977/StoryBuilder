package storybuilder.main;

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
}

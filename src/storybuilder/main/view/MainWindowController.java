package storybuilder.main.view;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import storybuilder.graph.view.GraphView;
import storybuilder.main.Cache;
import storybuilder.section.model.Section;
import storybuilder.section.view.SectionsView;

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

    public void switchToView(final String clazz)
    {
        mainWindow.getMainPane().setContent(clazz);
    }

    public void switchToEmptyView()
    {
        mainWindow.getMainPane().setContent("storybuilder.main.view.EmptyView");
    }

    public void enableMenus(final boolean enable)
    {
        mainWindow.getMainPane().getMenuBar().enableMenus(enable);
    }

    public Stage getStage()
    {
        return mainWindow.getStage();
    }

    public double getScreenWidth()
    {
        final Screen screen = Screen.getPrimary();
        final Rectangle2D bounds = screen.getVisualBounds();
        return bounds.getWidth();
    }

    public double getScreenHeight()
    {
        final Screen screen = Screen.getPrimary();
        final Rectangle2D bounds = screen.getVisualBounds();
        return bounds.getHeight();
    }

    public void switchToSection(final String sectionId, final int expandedPane)
    {
        final Section section = Cache.getInstance().getStory().getSection(sectionId);
        if (section != null) {
            final SectionsView sectionsView
                    = (SectionsView) mainWindow.getMainPane().setContent("storybuilder.section.view.SectionsView");
            sectionsView.setExpandedPane(expandedPane);
            sectionsView.selectElement(section);
        }
    }

    public void jumpToGraphSection(final Section section)
    {
        if (section != null) {
            final GraphView graphView
                    = (GraphView) mainWindow.getMainPane().setContent("storybuilder.graph.view.GraphView");
            graphView.jumpToSection(section);
        }
    }
}

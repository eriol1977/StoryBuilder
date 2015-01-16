package storybuilder.main.view;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import storybuilder.event.view.EventsView;
import storybuilder.item.view.ItemsView;
import storybuilder.main.Cache;
import storybuilder.section.model.Section;
import storybuilder.section.view.SectionsView;
import storybuilder.validation.ErrorManager;
import storybuilder.validation.SBException;

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
            final SectionsView sectionsView = new SectionsView();
            mainWindow.getMainPane().setContent(sectionsView);
            sectionsView.setExpandedPane(expandedPane);
            sectionsView.selectElement(section);
        }
    }

    public void switchToNewItem(final String callingSectionId, final int callingAccordionSection)
    {
        final ItemsView itemsView = new ItemsView();
        mainWindow.getMainPane().setContent(itemsView);
        try {
            itemsView.showNewElementView(callingSectionId, callingAccordionSection);
        } catch (SBException ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
        }
    }

    public void switchToNewEvent(final String callingSectionId, final int callingAccordionSection)
    {
        final EventsView eventsView = new EventsView();
        mainWindow.getMainPane().setContent(eventsView);
        try {
            eventsView.showNewElementView(callingSectionId, callingAccordionSection);
        } catch (SBException ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
        }
    }
}

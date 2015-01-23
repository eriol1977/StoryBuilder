package storybuilder.main.view;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.BorderPane;
import storybuilder.validation.ErrorManager;

/**
 *
 * @author Francesco Bertolino
 */
public class MainPane extends BorderPane
{

    private final SBMenuBar menuBar;

    private final StatusBar statusBar;

    private final Map<String, AbstractView> cachedViews = new HashMap<>();

    public MainPane()
    {
        menuBar = new SBMenuBar(this);
        setTop(menuBar);

        statusBar = new StatusBar();
        setBottom(statusBar);

        setCenter(new EmptyView());
    }

    AbstractView getView(final String clazz)
    {
        AbstractView view = cachedViews.get(clazz);
        if (view == null) {
            try {
                view = (AbstractView) Class.forName(clazz).newInstance();
                cachedViews.put(clazz, view);
            } catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException ex) {
                ErrorManager.showErrorMessage("Error while loading view");
            }
        }
        return view;
    }

    AbstractView setContent(final String clazz)
    {
        final AbstractView view = getView(clazz);
        setCenter(view);
        return view;
    }

    SBMenuBar getMenuBar()
    {
        return menuBar;
    }

    StatusBar getStatusBar()
    {
        return statusBar;
    }
}

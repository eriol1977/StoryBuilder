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

    void setContent(final AbstractView view)
    {
        setCenter(view);
    }

    void setContent(final String clazz)
    {
        setCenter(getView(clazz));
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

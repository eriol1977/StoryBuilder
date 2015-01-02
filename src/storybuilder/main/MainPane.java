package storybuilder.main;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Francesco Bertolino
 */
public class MainPane extends BorderPane
{

    private final SBMenuBar menuBar;

    private final StatusBar statusBar;

    public MainPane()
    {
        menuBar = new SBMenuBar(this);
        setTop(menuBar);

        statusBar = new StatusBar();
        setBottom(statusBar);

        setCenter(new EmptyView());
    }

    void setContent(final AbstractView view)
    {
        setCenter(view);
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

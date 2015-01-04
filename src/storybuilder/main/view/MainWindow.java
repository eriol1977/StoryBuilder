package storybuilder.main.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import storybuilder.main.Cache;

/**
 *
 * @author Francesco Bertolino
 */
public class MainWindow extends Application
{

    private Stage stage;

    private MainPane mainPane;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        initStage(stage);
        MainWindowController.getInstance().setMainWindow(this);
    }

    private void initStage(Stage st)
    {
        this.stage = st;
        updateTitle();
        this.mainPane = new MainPane();
        stage.setScene(new Scene(mainPane));
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
    }

    void updateTitle()
    {
        String title = "Story Builder";
        final String storyTitle = Cache.getInstance().getStory().getTitle();
        if (!storyTitle.isEmpty()) {
            title += " - " + storyTitle;
        }
        stage.setTitle(title);
    }

    MainPane getMainPane()
    {
        return mainPane;
    }

    Stage getStage()
    {
        return stage;
    }

}

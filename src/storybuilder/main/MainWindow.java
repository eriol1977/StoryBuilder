package storybuilder.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Francesco Bertolino
 */
public class MainWindow extends Application
{

    private Stage stage;

    private MainPane mainPane;

    private final static double screenWidth = 800;

    private final static double screenHeight = 600;

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
        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
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

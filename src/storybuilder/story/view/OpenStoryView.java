package storybuilder.story.view;

import java.io.File;
import storybuilder.main.AbstractView;
import javafx.stage.FileChooser;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class OpenStoryView extends AbstractView
{

    public OpenStoryView()
    {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a story");
        fileChooser.setInitialDirectory(new File(cache.getPreferences().getDirectoryPath()));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        final File file = fileChooser.showOpenDialog(mwc.getStage());
        if (file != null) {
            final Story story = Story.load(file);
            mwc.updateTitle();
            mwc.updateStatusBarMessage("Story \"" + story.getTitle() + "\" loaded");
            mwc.enableMenus(true);
        }
    }

}

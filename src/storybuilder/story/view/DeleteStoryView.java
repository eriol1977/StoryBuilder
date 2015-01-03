package storybuilder.story.view;

import java.io.File;
import storybuilder.main.view.AbstractView;
import javafx.stage.FileChooser;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class DeleteStoryView extends AbstractView
{

    public DeleteStoryView()
    {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a story");
        fileChooser.setInitialDirectory(new File(cache.getPreferences().getDirectoryPath()));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        final File file = fileChooser.showOpenDialog(mwc.getStage());
        if (file != null) {
            Story.delete(file);
            mwc.updateTitle();
            mwc.updateStatusBarMessage("Story deleted");
            mwc.enableMenus(false);
        }
    }

}

package storybuilder.story.view;

import java.io.File;
import storybuilder.main.view.AbstractView;
import javafx.stage.FileChooser;
import storybuilder.story.model.Story;
import storybuilder.validation.ErrorManager;
import storybuilder.validation.SBException;

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
            try {
                final Story story = Story.load(file);
                mwc.clearViewsCache();
                mwc.updateTitle();
                mwc.updateStatusBarMessage("Story \"" + story.getTitle() + "\" loaded");
                mwc.enableMenus(true);
            } catch (SBException ex) {
                ErrorManager.showErrorMessage(ex.getFailCause());
            }
        }
    }

}

package storybuilder.story.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import storybuilder.main.view.AbstractView;
import javafx.scene.control.TextField;
import storybuilder.main.view.EmptyView;
import storybuilder.story.model.Story;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class NewStoryView extends AbstractView
{

    public NewStoryView()
    {
        addTitle("New Story");
        final TextField titleField = addLabeledTextInput("Title");
        final TextField fileField = addLabeledTextInput("Filename");
        final Button button = addButton("Create");
        button.setOnAction((ActionEvent e) -> {
            final String title = titleField.getText();
            final String filename = fileField.getText();
            final Story story = new Story(title, filename + ".xml");
            try {
                story.save();
                cache.setStory(story);
                mwc.updateTitle();
                mwc.updateStatusBarMessage("Story \"" + title + "\" created");
                mwc.switchView(new EmptyView());
                mwc.enableMenus(true);
            } catch (ValidationFailed ex) {
                mwc.updateStatusBarMessage(ex.getFailCause());
            }
        });
    }

}

package storybuilder.story.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import storybuilder.main.AbstractView;
import javafx.scene.control.TextField;
import storybuilder.main.EmptyView;
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
            final Story story = new Story(title, filename);
            try {
                story.save();
                cache.setStory(story);
                mwc.updateTitle();
                mwc.updateStatusBarMessage("Story \"" + title + "\" created");
                mwc.switchView(new EmptyView());
            } catch (ValidationFailed ex) {
                mwc.updateStatusBarMessage(ex.getFailCause());
            }
        });
    }

}

package storybuilder.story.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import storybuilder.main.AbstractView;
import javafx.scene.control.TextField;
import storybuilder.main.EmptyView;
import storybuilder.story.model.Story;

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
        final Button button = addButton("Create");
        button.setOnAction((ActionEvent e) -> {
            final String title = titleField.getText();
            cache.setStory(new Story(title, titleField.getText().toLowerCase()));
            mwc.updateTitle();
            mwc.updateStatusBarMessage("Story \"" + title + "\" created");
            mwc.switchView(new EmptyView());
        });
    }
    
}

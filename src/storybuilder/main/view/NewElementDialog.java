package storybuilder.main.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import storybuilder.main.model.StoryElement;
import storybuilder.validation.ErrorManager;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class NewElementDialog extends SBDialog
{

    public NewElementDialog(final ObservableList<StoryElement> itemList, final AbstractDetailView view)
    {
        view.getSaveButton().setOnAction((ActionEvent event) -> {
            try {
                view.setElementValues();
                final StoryElement element = view.getElementToSave();
                element.validate();
                saveElement(element);
                itemList.add(element);
                close();
            } catch (ValidationFailed ex) {
                ErrorManager.showErrorMessage(ex.getFailCause());
            } catch (SBException ex) {
                ErrorManager.showErrorMessage(ex.getFailCause());
            }
        });

        add(view);
    }

    protected abstract void saveElement(final StoryElement element) throws SBException;
}

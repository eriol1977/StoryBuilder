package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import storybuilder.event.view.EventDoubleList;
import storybuilder.item.view.ItemDoubleList;
import storybuilder.main.view.DoubleList;
import storybuilder.main.view.MainWindowController;
import storybuilder.section.model.Get;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class GetView extends VBox
{

    private final DoubleList itemsField;

    private final DoubleList eventsField;

    public GetView(final Section section)
    {
        final Get get = section.getGet();

        setSpacing(10);
        setPadding(new Insets(10));

        final Label itemsLabel = new Label("Items to get");
        final Button newItem = new Button("New");
        newItem.setOnAction((ActionEvent event) -> {
            MainWindowController.getInstance().switchToNewItem(section.getNameWithoutPrefix(), SectionDetailView.EXPAND_GETS);
        });
        getChildren().add(new HBox(10, itemsLabel, newItem));

        itemsField = new ItemDoubleList(get != null ? get.getItemIds() : new ArrayList<>());
        getChildren().add(itemsField);

        getChildren().add(new Separator());

        final Label eventsLabel = new Label("Events to add");
        final Button newEvent = new Button("New");
        newEvent.setOnAction((ActionEvent event) -> {
            MainWindowController.getInstance().switchToNewEvent(section.getNameWithoutPrefix(), SectionDetailView.EXPAND_GETS);
        });
        getChildren().add(new HBox(10, eventsLabel, newEvent));

        eventsField = new EventDoubleList(get != null ? get.getEventIds() : new ArrayList<>());
        getChildren().add(eventsField);
    }

    public List<String> getItemIds()
    {
        return itemsField.getRightItems();
    }

    public List<String> getEventsIds()
    {
        return eventsField.getRightItems();
    }

    public List<String> getIds()
    {
        final List<String> ids = new ArrayList<>(getItemIds());
        ids.addAll(getEventsIds());
        return ids;
    }

    public String[] getIdsArray()
    {
        return getIds().toArray(new String[0]);
    }
}

package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import storybuilder.main.view.AbstractView;
import storybuilder.main.view.SBDialog;
import storybuilder.minigame.model.MinigameKind;
import storybuilder.minigame.model.MinigameParameter;
import storybuilder.section.model.MinigameInstance;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameInstanceView extends AbstractView
{

    private final Section section;

    private List<TextField> fields = new ArrayList<>();

    public MinigameInstanceView(final Section section)
    {
        this.section = section;

        if (section.getMinigame() != null) {
            final HBox box = new HBox(10);
            box.getChildren().add(new Text(section.getMinigame().getResume()));

            final Button update = new Button("Update");
            update.setOnAction((ActionEvent event) -> {

            });
            box.getChildren().add(update);

            final Button remove = new Button("Remove");
            remove.setOnAction((ActionEvent event) -> {

            });
            box.getChildren().add(remove);
            add(box);
        } else {
            final Button add = new Button("Add");
            add.setOnAction((ActionEvent event) -> {
                final SBDialog dialog = new SBDialog();
                dialog.setMinHeight(500);

                dialog.add(new Label("Configure the minigame"));

                final HBox box = new HBox(10);
                final ObservableList<MinigameKind> games
                        = FXCollections.observableArrayList(cache.getStory().getMinigames());
                final ComboBox<MinigameKind> combo = new ComboBox<>(games);
                box.getChildren().add(combo);
                final Button save = new Button("Save");
                save.setOnAction((ActionEvent ev) -> {
                    // TODO
                });
                box.getChildren().add(save);
                dialog.add(box);

                dialog.add(new Separator());

                dialog.add(new VBox());
                
                combo.setOnAction((ActionEvent ev) -> {
                    dialog.replaceLastNode(getMinigamePanel(combo.getSelectionModel().getSelectedItem(), null));
                });
                combo.getSelectionModel().selectFirst();

                dialog.show();
            });
            add(add);
        }
    }

    private VBox getMinigamePanel(final MinigameKind minigame, final MinigameInstance instance)
    {
        fields.clear();
        final VBox panel = new VBox(10);
        final List<MinigameParameter> parameters = minigame.getParameters();
        HBox box;
        TextField field;
        int i = 0;
        for (final MinigameParameter parameter : parameters) {
            box = new HBox(10);
            box.getChildren().add(new Label(parameter.getDefinition()));
            field = new TextField();
            if (instance != null) {
                field.setText(instance.getValues().get(i));
            } else {
                field.setPromptText(parameter.getPlaceHolder());
            }
            fields.add(field);
            box.getChildren().add(field);
            panel.getChildren().add(box);
            panel.getChildren().add(new Separator());
            i++;
        }
        return panel;
    }

}

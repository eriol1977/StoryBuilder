package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameInstanceView extends AbstractView
{

    private final Section section;

    private final Text resume;

    private final List<TextField> fields = new ArrayList<>();

    private MinigameInstance minigame;

    private final Button update;
    private final Button remove;
    private final Button add;
    private final HBox buttonBox;

    public MinigameInstanceView(final Section section)
    {
        this.section = section;
        this.minigame = section.getMinigame();
        this.resume = new Text();
        add(resume);

        buttonBox = new HBox(10);
        add(buttonBox);

        add = new Button("Add");
        add.setOnAction((ActionEvent event) -> {
            showDialog();
        });

        update = new Button("Update");
        update.setOnAction((ActionEvent event) -> {
            showDialog();
        });

        remove = new Button("Remove");
        remove.setOnAction((ActionEvent event) -> {
            minigame = null;
            updateView();
        });

        updateView();
    }

    private void updateView()
    {
        buttonBox.getChildren().clear();
        if (minigame == null) {
            resume.setText("No minigame linked to this section");
            buttonBox.getChildren().add(add);
        } else {
            resume.setText(minigame.getResume());
            buttonBox.getChildren().add(update);
            buttonBox.getChildren().add(remove);
        }
    }

    private void showDialog()
    {
        final SBDialog dialog = new SBDialog();
        dialog.setMinHeight(600);
        final Label message = new Label("Configure the minigame");
        dialog.add(message);

        final HBox wBox = new HBox(10);
        wBox.getChildren().add(new Label("Winning section:"));
        final ObservableList<String> wSections
                = FXCollections.observableArrayList(cache.getStory().getSectionIds(true));
        final ComboBox<String> winningSections = new ComboBox<>(wSections);
        wBox.getChildren().add(winningSections);
        dialog.add(wBox);

        final HBox lBox = new HBox(10);
        lBox.getChildren().add(new Label("Losing section:"));
        final ObservableList<String> lSections
                = FXCollections.observableArrayList(cache.getStory().getSectionIds(true));
        final ComboBox<String> losingSections = new ComboBox<>(lSections);
        lBox.getChildren().add(losingSections);
        dialog.add(lBox);

        if (minigame == null) {
            winningSections.getSelectionModel().selectFirst();
            losingSections.getSelectionModel().selectFirst();
        } else {
            winningSections.getSelectionModel().select(minigame.getWinningSectionNumber());
            losingSections.getSelectionModel().select(minigame.getLosingSectionNumber());
        }

        dialog.add(new Separator());

        final HBox box = new HBox(10);
        final ObservableList<MinigameKind> games
                = FXCollections.observableArrayList(cache.getStory().getMinigames());
        final ComboBox<MinigameKind> gameKinds = new ComboBox<>(games);
        box.getChildren().add(gameKinds);
        final Button save = new Button("Save");
        save.setOnAction((ActionEvent ev) -> {
            final List<String> values = fields.stream().map(tf -> tf.getText()).collect(Collectors.toList());
            final MinigameInstance newInstance
                    = new MinigameInstance(section.getName() + "_minigame",
                            gameKinds.getSelectionModel().getSelectedItem(),
                            winningSections.getSelectionModel().getSelectedItem(),
                            losingSections.getSelectionModel().getSelectedItem(),
                            values,
                            false);
            try {
                newInstance.validate();
                MinigameInstanceView.this.minigame = newInstance;
                dialog.close();
                updateView();
            } catch (ValidationFailed ex) {
                message.setText(ex.getFailCause());
            }
        });
        box.getChildren().add(save);
        dialog.add(box);
        dialog.add(new Separator());
        dialog.add(new VBox());
        gameKinds.setOnAction((ActionEvent ev) -> {
            dialog.replaceLastNode(getMinigamePanel(gameKinds.getSelectionModel().getSelectedItem()));
        });

        if (minigame == null) {
            gameKinds.getSelectionModel().selectFirst();
        } else {
            gameKinds.getSelectionModel().select(minigame.getKind());
        }
        dialog.show();
    }

    private VBox getMinigamePanel(final MinigameKind kind)
    {
        fields.clear();
        final VBox panel = new VBox(10);
        final List<MinigameParameter> parameters = kind.getParameters();
        HBox box;
        TextField field;
        int i = 0;
        for (final MinigameParameter parameter : parameters) {
            box = new HBox(10);
            box.getChildren().add(new Label(parameter.getDefinition()));
            field = new TextField();
            if (minigame != null) {
                field.setText(minigame.getValues().get(i));
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

    MinigameInstance getMinigame()
    {
        return minigame;
    }

}

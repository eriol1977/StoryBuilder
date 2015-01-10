package storybuilder.minigame.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import storybuilder.main.view.AbstractView;
import storybuilder.minigame.model.MinigameKind;
import storybuilder.minigame.model.MinigameParameter;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigamesView extends AbstractView
{

    public MinigamesView()
    {
        addTitle("Minigames");

        final ObservableList<MinigameKind> games
                = FXCollections.observableArrayList(cache.getStory().getMinigames());
        final ComboBox<MinigameKind> combo = new ComboBox<>(games);
        add(combo);

        final ObservableList<MinigameParameter> params
                = FXCollections.observableArrayList();
        combo.setOnAction((ActionEvent event) -> {
            params.clear();
            params.addAll(combo.getSelectionModel().getSelectedItem().getParameters());
        });
        combo.getSelectionModel().selectFirst();

        getChildren().add(new Label("")); // empty separator
        addLabel("Parameters");
                
        final TableView paramsTable = new TableView(params);
        
        final TableColumn colDefinition = new TableColumn("Definition");
        colDefinition.setMinWidth(mwc.getScreenWidth() / 3);
        colDefinition.setCellValueFactory(new PropertyValueFactory<>("definition"));
        colDefinition.setCellFactory(TextFieldTableCell.forTableColumn());
        colDefinition.setSortable(false);
        paramsTable.getColumns().add(colDefinition);
        
        final TableColumn colObs = new TableColumn("Obs");
        colObs.setMinWidth(mwc.getScreenWidth() / 3);
        colObs.setCellValueFactory(new PropertyValueFactory<>("obs"));
        colObs.setCellFactory(TextFieldTableCell.forTableColumn());
        colObs.setSortable(false);
        paramsTable.getColumns().add(colObs);
        
        final TableColumn colExample = new TableColumn("Example");
        colExample.setMinWidth(mwc.getScreenWidth() / 3);
        colExample.setCellValueFactory(new PropertyValueFactory<>("placeHolder"));
        colExample.setCellFactory(TextFieldTableCell.forTableColumn());
        colExample.setSortable(false);
        paramsTable.getColumns().add(colExample);
        
        add(paramsTable);
    }

}

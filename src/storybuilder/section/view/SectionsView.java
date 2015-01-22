package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.main.view.SBDialog;
import storybuilder.section.model.Section;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionsView extends AbstractTableView
{

    /**
     * Remembers which pane was open in the previously viewed section detail
     */
    private int expandedPane = -1;

    public SectionsView()
    {
        addTitle("Sections");

        table.setMinWidth(102);
        table.setMaxWidth(102);
        
        table.setRowFactory(new Callback<TableView<Section>, TableRow<Section>>()
        {
            @Override
            public TableRow<Section> call(TableView<Section> tableView)
            {
                final TableRow<Section> row = new TableRow<>();
                final ContextMenu contextMenu = buildContextMenu(row);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:  
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
                );
                return row;
            }

            private ContextMenu buildContextMenu(final TableRow<Section> row)
            {
                final MenuItem linksMenuItem = new MenuItem("Linked by");
                linksMenuItem.setOnAction((ActionEvent event) -> {
                    final List<Section> sections = cache.getStory().getSectionsPointingTo(row.getItem());
                    final SBDialog dialog = new SBDialog();
                    dialog.setWidth(200);
                    if (sections.isEmpty()) {
                        dialog.add(new Label("None"));
                    } else {
                        final int ROW_HEIGHT = 24;
                        ObservableList<String> model
                                = FXCollections.observableArrayList(sections.stream().map(s -> s.getNameWithoutPrefix()).collect(Collectors.toList()));
                        final ListView<String> list = new ListView<>(model);
                        final int h = model.size() * ROW_HEIGHT + 2;
                        list.setPrefHeight(h);
                        dialog.setHeight(h > 120 ? h : 120);
                        list.getSelectionModel().selectedItemProperty().
                                addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                                    mwc.switchToSection(new_val, SectionDetailView.EXPAND_PARAGRAPHS);
                                    dialog.close();
                                });
                        dialog.add(list);
                    }
                    dialog.show();
                });
                final ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().add(linksMenuItem);
                return contextMenu;
            }
        });
    }

    @Override
    protected IStoryElement getNewElement() throws SBException
    {
        final int newSectionId = cache.getStory().getLastSectionId() + 1;
        return new Section(Section.PREFIX + newSectionId, false);
    }

    @Override
    protected AbstractDetailView showDetailView(final boolean isNewElement, final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Section((Section) element);
        final SectionDetailView sectionDetailView = new SectionDetailView(isNewElement, element, this);
        layout.getChildren().add(sectionDetailView);
        return sectionDetailView;
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Code", "nameWithoutPrefix", 60));
        return columns;
    }

    @Override
    public void addElement(IStoryElement element)
    {
        super.addElement(element);
        // new sections can appear because of new links
        data.clear();
        loadData();
    }

    @Override
    protected void addElementToStory(final IStoryElement element) throws SBException
    {
        cache.getStory().addSection((Section) element);
    }

    @Override
    public void updateElement(IStoryElement element)
    {
        super.updateElement(element);
        // new sections can appear because of new links
        data.clear();
        loadData();
    }

    @Override
    protected void updateElementInStory(final IStoryElement element) throws SBException
    {
        cache.getStory().updateSection((Section) stashed, (Section) element);
    }

    @Override
    protected void deleteElementFromStory(final IStoryElement element) throws SBException
    {
        cache.getStory().deleteSection((Section) element);
    }

    @Override
    protected void loadData()
    {
        data.addAll(cache.getStory().getSections());
    }

    int getExpandedPane()
    {
        return expandedPane;
    }

    public void setExpandedPane(final int expandedPane)
    {
        this.expandedPane = expandedPane;
    }

}

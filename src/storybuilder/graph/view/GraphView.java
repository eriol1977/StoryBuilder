package storybuilder.graph.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import storybuilder.graph.controller.GraphController;
import storybuilder.main.Cache;
import storybuilder.main.view.AbstractView;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class GraphView extends AbstractView
{

    private final GraphController controller;

    public GraphView()
    {
        addTitle("Graph");
        final Button refreshButton = addButton("Refresh");
        refreshButton.setOnAction((ActionEvent event) -> {
            refresh();
        });

        this.controller = new GraphController();
        add(this.controller.getGraph());

        refresh();
    }

    private void refresh()
    {
        this.controller.drawGraphForSection(getStartingSection());
    }

    public void jumpToSection(final Section section)
    {
        this.controller.drawGraphForSection(section);
    }

    protected Section getStartingSection()
    {
        return Cache.getInstance().getStory().getSection("1");
    }
}

package storybuilder.graph.view;

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

        this.controller = new GraphController();
        add(this.controller.getGraph());

        this.controller.drawGraphForSection(getStartingSection());
    }

    protected Section getStartingSection()
    {
        return Cache.getInstance().getStory().getSection("1");
    }
}

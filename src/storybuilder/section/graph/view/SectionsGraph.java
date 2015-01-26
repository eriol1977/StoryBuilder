package storybuilder.section.graph.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Pane;
import storybuilder.main.view.AbstractView;
import storybuilder.main.view.MainWindowController;
import storybuilder.section.model.Section;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class SectionsGraph extends AbstractView
{

    private final static double WIDTH = MainWindowController.getInstance().getScreenWidth() - 100;

    private final static double HEIGHT = MainWindowController.getInstance().getScreenHeight() - 100;

    private final static double HOR_START = 100;

    private final static double VER_START = 50;

    private final static double VER_MIDDLE = 300;

    private final static double VER_END = 550;

    private final Story story;

    private final Map<SectionNode, List<SectionNode>> nodesByOrigin = new HashMap<>();

    private final Map<SectionNode, List<SectionNode>> nodesByDestination = new HashMap<>();

    public SectionsGraph()
    {
        addTitle("Graph");

        story = cache.getStory();

        final Section section = story.getSection("1");

        drawForSection(section);

    }

    void drawForSection(final Section section)
    {
        nodesByOrigin.clear();
        nodesByDestination.clear();
        
        final Pane canvas = new Pane();
        canvas.setPrefSize(WIDTH, HEIGHT);

        drawNodes(section, canvas);

        drawLinks(canvas);

        getChildren().clear();
        add(canvas);
    }

    private void drawNodes(final Section section, final Pane canvas)
    {
        final SectionNode node = buildDestinationNode(null, section, WIDTH / 2, VER_MIDDLE);
        
        List<Section> sections = story.getSectionsPointingTo(section);
        double[] xs = getXs(HOR_START, WIDTH - HOR_START, sections.size());
        for (int i = 0; i < sections.size(); i++) {
            canvas.getChildren().add(buildOriginNode(node, sections.get(i), xs[i], VER_START));
        }

        canvas.getChildren().add(node);

        sections = story.getSectionsLinkedBy(section);
        xs = getXs(HOR_START, WIDTH - HOR_START, sections.size());
        for (int i = 0; i < sections.size(); i++) {
            canvas.getChildren().add(buildDestinationNode(node, sections.get(i), xs[i], VER_END));
        }
    }

    private double[] getXs(final double startX, final double endX, final int nodes)
    {
        final double w = endX - startX;
        final double step = (w / nodes) / 2;
        final double[] xs = new double[nodes];
        double x = startX;
        for (int i = 1; i <= nodes; i++) {
            xs[i - 1] = (i * step) + x;
            x += step;
        }
        return xs;
    }

    private SectionNode buildOriginNode(final SectionNode destination, final Section section, final double x, final double y)
    {
        final SectionNode node = new SectionNode(this, section, x, y);
        if (destination != null) {
            List<SectionNode> nodes = this.nodesByDestination.get(destination);
            if (nodes == null) {
                nodes = new ArrayList<>();
                nodesByDestination.put(destination, nodes);
            }
            nodes.add(node);
        }
        return node;
    }

    private SectionNode buildDestinationNode(final SectionNode origin, final Section section, final double x, final double y)
    {
        final SectionNode node = new SectionNode(this, section, x, y);
        if (origin != null) {
            List<SectionNode> nodes = this.nodesByOrigin.get(origin);
            if (nodes == null) {
                nodes = new ArrayList<>();
                nodesByOrigin.put(origin, nodes);
            }
            nodes.add(node);
        }
        return node;
    }

    private void drawLinks(final Pane canvas)
    {
        List<SectionNode> nodes;
        for (SectionNode destination : nodesByDestination.keySet()) {
            nodes = nodesByDestination.get(destination);
            nodes.stream().forEach(n -> canvas.getChildren().add(
                    new NodeLink(n, destination)));
        }
        for (SectionNode origin : nodesByOrigin.keySet()) {
            nodes = nodesByOrigin.get(origin);
            nodes.stream().forEach(n -> canvas.getChildren().add(
                    new NodeLink(origin, n)));
        }
    }
}

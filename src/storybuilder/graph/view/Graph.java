package storybuilder.graph.view;

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
public class Graph extends AbstractView
{

    private final static double WIDTH = MainWindowController.getInstance().getScreenWidth() - 100;

    private final static double HEIGHT = MainWindowController.getInstance().getScreenHeight() - 100;

    private final static double HOR_START = 100;

    private final static double VER_START = 50;

    private final static double VER_MIDDLE = 300;

    private final static double VER_END = 550;

    private final Story story;

    private final Map<NodeView, List<NodeView>> linksByOrigin = new HashMap<>();

    private final Map<NodeView, List<NodeView>> linkSwitchesByOrigin = new HashMap<>();

    private final Map<NodeView, List<NodeView>> linksByDestination = new HashMap<>();

    private final Map<NodeView, List<NodeView>> linkSwitchesByDestination = new HashMap<>();

    public Graph()
    {
        addTitle("Graph");

        story = cache.getStory();

        final Section section = story.getSection("1");

        drawForSection(section);

    }

    void drawForSection(final Section section)
    {
        linksByOrigin.clear();
        linksByDestination.clear();
        linkSwitchesByOrigin.clear();
        linkSwitchesByDestination.clear();

        final Pane canvas = new Pane();
        canvas.setPrefSize(WIDTH, HEIGHT);

        drawNodes(section, canvas);

        drawLinks(canvas);

        getChildren().clear();
        add(canvas);
    }

    private void drawNodes(final Section section, final Pane canvas)
    {
//        final SectionNode node = buildDestinationNode(null, section, GraphDirectLink.LINK, WIDTH / 2, VER_MIDDLE);
//
//        List<Section> sections = story.getSectionsPointingTo(section);
//        double[] xs = getXs(HOR_START, WIDTH - HOR_START, sections.size());
//        for (int i = 0; i < sections.size(); i++) {
//            canvas.getChildren().add(buildOriginNode(node, sections.get(i), xs[i], VER_START));
//        }
//
//        canvas.getChildren().add(node);
//
//        sections = story.getSectionsLinkedBy(section);
//        List<Section> sectionsBySwitch = story.getSectionsLinkSwitchedBy(section);
//        xs = getXs(HOR_START, WIDTH - HOR_START, sections.size() + sectionsBySwitch.size());
//        for (int i = 0; i < sections.size(); i++) {
//            canvas.getChildren().add(buildDestinationNode(node, sections.get(i), GraphDirectLink.LINK, xs[i], VER_END));
//        }
//        for (int i = sections.size(); i < sections.size() + sectionsBySwitch.size(); i++) {
//            canvas.getChildren().add(buildDestinationNode(node, sectionsBySwitch.get(i - sections.size()), GraphDirectLink.LINK_SWITCH, xs[i], VER_END));
//        }
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

//    private NodeView buildOriginNode(final NodeView destination, final Section section, final double x, final double y)
//    {
//        final NodeView node = new NodeView(this, section, x, y);
//        if (destination != null) {
//            List<NodeView> nodes = this.linksByDestination.get(destination);
//            if (nodes == null) {
//                nodes = new ArrayList<>();
//                linksByDestination.put(destination, nodes);
//            }
//            nodes.add(node);
//        }
//        return node;
//    }
//
//    private NodeView buildDestinationNode(final NodeView origin, final Section section, final int kind, final double x, final double y)
//    {
//        final NodeView node = new NodeView(this, section, x, y);
////        if (origin != null) {
////            if (kind == GraphDirectLink.LINK) {
////                List<SectionNode> nodes = this.linksByOrigin.get(origin);
////                if (nodes == null) {
////                    nodes = new ArrayList<>();
////                    linksByOrigin.put(origin, nodes);
////                }
////                nodes.add(node);
////            } else {
////                List<SectionNode> nodes = this.linkSwitchesByOrigin.get(origin);
////                if (nodes == null) {
////                    nodes = new ArrayList<>();
////                    linkSwitchesByOrigin.put(origin, nodes);
////                }
////                nodes.add(node);
////            }
////        }
//        return node;
//    }

    private void drawLinks(final Pane canvas)
    {
//        List<SectionNode> nodes;
//        for (SectionNode destination : linksByDestination.keySet()) {
//            nodes = linksByDestination.get(destination);
//            nodes.stream().forEach(n -> canvas.getChildren().add(
//                    new GraphDirectLink(n, destination, GraphDirectLink.LINK)));
//        }
//        for (SectionNode origin : linksByOrigin.keySet()) {
//            nodes = linksByOrigin.get(origin);
//            nodes.stream().forEach(n -> canvas.getChildren().add(
//                    new GraphDirectLink(origin, n, GraphDirectLink.LINK)));
//        }
//        for (SectionNode origin : linkSwitchesByOrigin.keySet()) {
//            nodes = linkSwitchesByOrigin.get(origin);
//            nodes.stream().forEach(n -> canvas.getChildren().add(
//                    new GraphDirectLink(origin, n, GraphDirectLink.LINK_SWITCH)));
//        }
    }

    
}

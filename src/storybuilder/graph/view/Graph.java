package storybuilder.graph.view;

import storybuilder.graph.view.connection.ConnectionView;
import javafx.scene.layout.Pane;
import storybuilder.main.view.AbstractView;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class Graph extends AbstractView
{

    private final Pane canvas;

    public Graph(final double width, final double height)
    {
        canvas = new Pane();
        canvas.setPrefSize(width, height);
        add(canvas);
    }

    public void clear()
    {
        canvas.getChildren().clear();
    }

    public void drawNode(final NodeView nodeView)
    {
        canvas.getChildren().add(nodeView);
    }

    public void drawConnection(final ConnectionView connectionView)
    {
        canvas.getChildren().add(connectionView);
    }
    
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

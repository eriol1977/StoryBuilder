package storybuilder.graph.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import storybuilder.graph.model.GraphModel;
import storybuilder.graph.model.Node;
import storybuilder.graph.model.connection.Connection;
import storybuilder.graph.model.connection.LinkConnection;
import storybuilder.graph.model.connection.MinigameConnection;
import storybuilder.graph.model.connection.SwitchConnection;
import storybuilder.graph.view.Graph;
import storybuilder.graph.view.NodeView;
import storybuilder.graph.view.connection.ConnectionView;
import storybuilder.graph.view.connection.LinkConnectionView;
import storybuilder.graph.view.connection.MinigameConnectionView;
import storybuilder.graph.view.connection.SwitchConnectionView;
import storybuilder.main.view.MainWindowController;
import storybuilder.section.model.Section;
import storybuilder.section.view.SectionDetailView;

/**
 *
 * @author Francesco Bertolino
 */
public class GraphController
{

    private final static double WIDTH = MainWindowController.getInstance().getScreenWidth() - 100;

    private final static double HEIGHT = MainWindowController.getInstance().getScreenHeight() - 100;

    private final static double FIRST_X = 100;

    private final static double CENTER_X = WIDTH / 2;

    private final static double TOP = 50;

    private final static double MIDDLE = 300;

    private final static double BOTTOM = 550;

    private final Graph graph;

    private final GraphModel model;

    private final Map<Node, NodeView> nodeViews;

    public GraphController()
    {
        this.graph = new Graph(WIDTH, HEIGHT);
        this.model = new GraphModel();
        this.nodeViews = new HashMap<>();
    }

    public void drawGraphForSection(final Section section)
    {
        model.buildForSection(section);
        clear();
        drawNodes(model.getNodesToTarget(), TOP);
        drawNode(model.getTargetNode(), MIDDLE);
        drawNodes(model.getNodesFromTarget(), BOTTOM);
        drawConnections(model.getConnections());
    }

    private void clear()
    {
        this.nodeViews.clear();
        this.graph.clear();
    }

    private void drawNode(final Node node, final double y)
    {
        graph.drawNode(buildNodeView(node, CENTER_X, y));
    }

    private void drawNodes(final List<Node> nodes, final double y)
    {
        final double[] xs = getXs(FIRST_X, WIDTH - FIRST_X, nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            graph.drawNode(buildNodeView(nodes.get(i), xs[i], y));
        }
    }

    private void drawConnections(final List<Connection> connections)
    {
        connections.stream().forEach(c -> graph.drawConnection(buildConnectionView(c)));
    }

    private NodeView buildNodeView(final Node node, final double x, final double y)
    {
        final NodeView nodeView = new NodeView(this, node, x, y);
        this.nodeViews.put(node, nodeView);
        return nodeView;
    }

    private ConnectionView buildConnectionView(final Connection connection)
    {
        final NodeView origin = this.nodeViews.get(connection.getOrigin());
        final NodeView destination = this.nodeViews.get(connection.getDestination());
        final double x1 = origin.getX();
        final double y1 = origin.getY();
        final double x2 = destination.getX();
        final double y2 = destination.getY();
        switch (connection.getKind()) {
            case LINK:
                return new LinkConnectionView((LinkConnection) connection, x1, y1, x2, y2);
            case SWITCH:
                return new SwitchConnectionView((SwitchConnection) connection, x1, y1, x2, y2);
            case MINIGAME:
                return new MinigameConnectionView((MinigameConnection) connection, x1, y1, x2, y2);
            default:
                return null;
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

    public void switchToTableForSection(final Section section)
    {
        MainWindowController.getInstance().
                switchToSection(section.getNameWithoutPrefix(), SectionDetailView.EXPAND_PARAGRAPHS);
    }

    public void showContextMenuForNode(final NodeView nodeView, final double x, final double y)
    {
        final MenuItem addMenuItem = new MenuItem("Go to table entry");
        addMenuItem.setOnAction((ActionEvent ev) -> {
            switchToTableForSection(nodeView.getNode().getSection());
        });
        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(addMenuItem);
        contextMenu.show(nodeView, x, y);
    }

    public Graph getGraph()
    {
        return graph;
    }

    public GraphModel getModel()
    {
        return model;
    }
}

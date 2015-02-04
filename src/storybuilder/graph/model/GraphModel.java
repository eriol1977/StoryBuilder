package storybuilder.graph.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import storybuilder.graph.controller.GraphDatasource;
import storybuilder.section.model.Link;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class GraphModel
{

    private Node target;

    private List<Node> toTarget;

    private List<Node> fromTarget;

    private List<Connection> connections;

    public Node buildNode(final Section section)
    {
        return new Node(section);
    }

    public LinkConnection buildConnection(final Node origin, final Node destination, final Link link)
    {
        return new LinkConnection(origin, destination, link);
    }

    public SwitchConnection buildConnection(final Node origin, final Node destination, final LinkSwitchGraphData data)
    {
        return new SwitchConnection(origin, destination, data);
    }

    public MinigameConnection buildConnection(final Node origin, final Node destination, final MinigameGraphData data)
    {
        return new MinigameConnection(origin, destination, data);
    }

    public void buildForSection(final Section section)
    {
        target = buildNode(section);

        final GraphDatasource dataSource = getDataSource();

        connections = new ArrayList<>();

        toTarget = new ArrayList<>();
        buildToTargetByLink(dataSource, section);
        buildToTargetBySwitch(dataSource, section);
        buildToTargetByMinigame(dataSource, section);
        Collections.sort(toTarget);

        fromTarget = new ArrayList<>();
        buildFromTargetByLink(dataSource, section);
        buildFromTargetBySwitch(dataSource, section);
        buildFromTargetByMinigame(dataSource, section);
        Collections.sort(fromTarget);

        Collections.sort(connections);
    }

    private void buildToTargetByLink(final GraphDatasource dataSource, final Section section)
    {
        Node node;
        List<Link> links;
        final Map<Section, List<Link>> linkConnectionsTo = dataSource.getLinkConnectionsTo(section);
        for (final Section s : linkConnectionsTo.keySet()) {
            links = linkConnectionsTo.get(s);
            for (final Link link : links) {
                node = new Node(s);
                toTarget.add(node);
                connections.add(new LinkConnection(node, target, link));
            }
        }
    }

    private void buildFromTargetByLink(final GraphDatasource dataSource, final Section section)
    {
        Node node;
        List<Link> links;
        final Map<Section, List<Link>> linkConnectionsFrom = dataSource.getLinkConnectionsFrom(section);
        for (final Section s : linkConnectionsFrom.keySet()) {
            links = linkConnectionsFrom.get(s);
            for (final Link link : links) {
                node = new Node(s);
                fromTarget.add(node);
                connections.add(new LinkConnection(target, node, link));
            }
        }
    }

    private void buildToTargetBySwitch(final GraphDatasource dataSource, final Section section)
    {
        Node node;
        List<LinkSwitchGraphData> data;
        final Map<Section, List<LinkSwitchGraphData>> switchConnectionsTo = dataSource.getSwitchConnectionsTo(section);
        for (final Section s : switchConnectionsTo.keySet()) {
            data = switchConnectionsTo.get(s);
            for (final LinkSwitchGraphData d : data) {
                node = new Node(s);
                toTarget.add(node);
                connections.add(new SwitchConnection(node, target, d));
            }
        }
    }

    private void buildFromTargetBySwitch(final GraphDatasource dataSource, final Section section)
    {
        Node node;
        List<LinkSwitchGraphData> data;
        final Map<Section, List<LinkSwitchGraphData>> switchConnectionsFrom = dataSource.getSwitchConnectionsFrom(section);
        for (final Section s : switchConnectionsFrom.keySet()) {
            data = switchConnectionsFrom.get(s);
            for (final LinkSwitchGraphData d : data) {
                node = new Node(s);
                fromTarget.add(node);
                connections.add(new SwitchConnection(target, node, d));
            }
        }
    }

    private void buildToTargetByMinigame(final GraphDatasource dataSource, final Section section)
    {
        Node node;
        MinigameGraphData data;
        final Map<Section, MinigameGraphData> minigameConnectionsTo = dataSource.getMinigameConnectionsTo(section);
        for (final Section s : minigameConnectionsTo.keySet()) {
            data = minigameConnectionsTo.get(s);
            node = new Node(s);
            toTarget.add(node);
            connections.add(new MinigameConnection(node, target, data));
        }
    }

    private void buildFromTargetByMinigame(final GraphDatasource dataSource, final Section section)
    {
        Node node;
        MinigameGraphData data;
        final Map<Section, MinigameGraphData> minigameConnectionsFrom = dataSource.getMinigameConnectionsFrom(section);
        for (final Section s : minigameConnectionsFrom.keySet()) {
            data = minigameConnectionsFrom.get(s);
            node = new Node(s);
            fromTarget.add(node);
            connections.add(new MinigameConnection(target, node, data));
        }
    }

    public Node getTargetNode()
    {
        return target;
    }

    public List<Node> getNodesToTarget()
    {
        return toTarget;
    }

    public List<Node> getNodesFromTarget()
    {
        return fromTarget;
    }

    public List<Connection> getConnections()
    {
        return connections;
    }

    protected GraphDatasource getDataSource()
    {
        return new GraphDatasource();
    }
}

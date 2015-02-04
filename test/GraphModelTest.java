/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import storybuilder.graph.model.GraphDatasource;
import storybuilder.graph.model.connection.Connection;
import storybuilder.graph.model.GraphModel;
import storybuilder.graph.model.connection.LinkConnection;
import storybuilder.graph.model.struct.LinkSwitchGraphData;
import storybuilder.graph.model.connection.MinigameConnection;
import storybuilder.graph.model.struct.MinigameGraphData;
import storybuilder.graph.model.Node;
import storybuilder.graph.model.connection.SwitchConnection;
import storybuilder.minigame.model.MinigameKind;
import storybuilder.section.model.Link;
import storybuilder.section.model.LinkSwitch;
import storybuilder.section.model.MinigameInstance;
import storybuilder.section.model.Paragraph;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco
 */
public class GraphModelTest
{

    public GraphModelTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Testa {@link GraphModel#buildNode(storybuilder.section.model.Section, double, double)
     * }
     */
    @Test
    public void testBuildNode()
    {
        final String sectionNumber = "1";
        final String paragraph1Text = "Testo del primo paragrafo.";
        final String paragraph2Text = "Testo del secondo paragrafo.";
        final String paragraph3Text = "Testo del terzo paragrafo.";

        final Section section = new Section(Section.PREFIX + sectionNumber, false);
        final Paragraph p1 = new Paragraph("par1", paragraph1Text, false);
        final Paragraph p2 = new Paragraph("par2", paragraph2Text, false);
        final Paragraph p3 = new Paragraph("par3", paragraph3Text, false);
        section.setParagraphs(Arrays.asList(p1, p2, p3));

        final GraphModel model = new GraphModel();

        final Node node = model.buildNode(section);

        Assert.assertNotNull(node);
        Assert.assertEquals(section, node.getSection());
        Assert.assertEquals(sectionNumber, node.getTitle());

        final String expectedDescription = paragraph1Text + "\n"
                + paragraph2Text + "\n"
                + paragraph3Text;
        Assert.assertEquals(expectedDescription, node.getDescription());

        Assert.assertTrue(node.isStarting());
        Assert.assertFalse(node.isEnding());
    }

    /**
     * Testa {@link GraphModel#buildConnection(storybuilder.graph.model.Node, storybuilder.graph.model.Node, storybuilder.section.model.Link)
     * }
     */
    @Test
    public void testBuildLinkConnection()
    {
        final Section section1 = new Section(Section.PREFIX + 1, false);
        final Section section2 = new Section(Section.PREFIX + 2, false);
        final Link link = new Link("link", "2",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);

        final GraphModel model = new GraphModel();
        final Node node1 = model.buildNode(section1);
        final Node node2 = model.buildNode(section2);

        final LinkConnection connection = model.buildConnection(node1, node2, link);

        Assert.assertNotNull(connection);
        Assert.assertEquals(node1, connection.getOrigin());
        Assert.assertEquals(node2, connection.getDestination());
        Assert.assertEquals(link, connection.getLink());
        Assert.assertEquals(link.getReadableContent(), connection.getDescription());
    }

    /**
     * Testa {@link GraphModel#buildConnection(storybuilder.graph.model.Node, storybuilder.graph.model.Node, storybuilder.graph.model.LinkSwitchGraphData)
     * }
     */
    @Test
    public void testBuildSwitchConnection()
    {
        final Section section1 = new Section(Section.PREFIX + 1, false);
        final Section section2 = new Section(Section.PREFIX + 2, false);
        final Link link = new Link("link", "3",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);
        final LinkSwitchGraphData data = new LinkSwitchGraphData(section1, link);

        final GraphModel model = new GraphModel();
        final Node node1 = model.buildNode(section1);
        final Node node2 = model.buildNode(section2);

        final SwitchConnection connection = model.buildConnection(node1, node2, data);
        Assert.assertNotNull(connection);
        Assert.assertEquals(node1, connection.getOrigin());
        Assert.assertEquals(node2, connection.getDestination());
        Assert.assertEquals(link, connection.getData().getLink());
        Assert.assertEquals(data.getText(), connection.getDescription());
    }

    /**
     * Testa {@link GraphModel#buildConnection(storybuilder.graph.model.Node, storybuilder.graph.model.Node, storybuilder.graph.model.MinigameGraphData)
     * }
     */
    @Test
    public void testBuildMinigameConnection()
    {
        final Section section1 = new Section(Section.PREFIX + 1, false);
        final Section section2 = new Section(Section.PREFIX + 2, false);
        final MinigameInstance game = new MinigameInstance("game1",
                new MinigameKind("code", "title", "clazz", new ArrayList<>()),
                "2", "3", new ArrayList<>(), false);
        final MinigameGraphData data = new MinigameGraphData(game, true);

        final GraphModel model = new GraphModel();
        final Node node1 = model.buildNode(section1);
        final Node node2 = model.buildNode(section2);

        final MinigameConnection connection = model.buildConnection(node1, node2, data);

        Assert.assertNotNull(connection);
        Assert.assertEquals(node1, connection.getOrigin());
        Assert.assertEquals(node2, connection.getDestination());
        Assert.assertEquals(game, connection.getData().getGame());
        Assert.assertEquals(data.getText(), connection.getDescription());
    }

    /**
     * Testa {@link GraphModel#buildForSection(storybuilder.section.model.Section)
     * }
     */
    @Test
    public void testBuildForSection_NoConnections()
    {
        final Section section1 = new Section(Section.PREFIX + 1, false);
        final Section section2 = new Section(Section.PREFIX + 2, false);
        final Section section3 = new Section(Section.PREFIX + 3, false);

        final GraphModel model = new GraphModelForTests(section1, section2, section3);
        model.buildForSection(section2);

        Node targetNode = model.getTargetNode();
        List<Node> nodesToTarget = model.getNodesToTarget();
        List<Node> nodesFromTarget = model.getNodesFromTarget();
        List<Connection> connections = model.getConnections();

        Assert.assertNotNull(targetNode);
        Assert.assertEquals(section2, targetNode.getSection());
        Assert.assertNotNull(nodesToTarget);
        Assert.assertTrue(nodesToTarget.isEmpty());
        Assert.assertNotNull(nodesFromTarget);
        Assert.assertTrue(nodesFromTarget.isEmpty());
        Assert.assertNotNull(connections);
        Assert.assertTrue(connections.isEmpty());
    }

    /**
     * Testa {@link GraphModel#buildForSection(storybuilder.section.model.Section)
     * }
     */
    @Test
    public void testBuildForSection_OnlyLinkConnections()
    {
        final Section section1 = new Section(Section.PREFIX + 1, false);
        final Section section2 = new Section(Section.PREFIX + 2, false);
        final Section section3 = new Section(Section.PREFIX + 3, false);
        final Link link_1_2 = new Link("link_1_2", section2.getNameWithoutPrefix(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);
        section1.setLinks(Arrays.asList(link_1_2));
        final Link link_2_3 = new Link("link_2_3", section3.getNameWithoutPrefix(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);
        section2.setLinks(Arrays.asList(link_2_3));

        final GraphModel model = new GraphModelForTests(section1, section2, section3);
        model.buildForSection(section1);

        Node targetNode = model.getTargetNode();
        List<Node> nodesToTarget = model.getNodesToTarget();
        List<Node> nodesFromTarget = model.getNodesFromTarget();
        List<Connection> connections = model.getConnections();

        Assert.assertNotNull(targetNode);
        Assert.assertEquals(section1, targetNode.getSection());
        Assert.assertNotNull(nodesToTarget);
        Assert.assertTrue(nodesToTarget.isEmpty());
        Assert.assertNotNull(nodesFromTarget);
        Assert.assertEquals(1, nodesFromTarget.size());
        Assert.assertEquals(section2, nodesFromTarget.get(0).getSection());
        Assert.assertNotNull(connections);
        Assert.assertEquals(1, connections.size());
        Assert.assertEquals(targetNode, connections.get(0).getOrigin());
        Assert.assertEquals(nodesFromTarget.get(0), connections.get(0).getDestination());
        Assert.assertTrue(connections.get(0) instanceof LinkConnection);

        model.buildForSection(section2);

        targetNode = model.getTargetNode();
        nodesToTarget = model.getNodesToTarget();
        nodesFromTarget = model.getNodesFromTarget();
        connections = model.getConnections();

        Assert.assertNotNull(targetNode);
        Assert.assertEquals(section2, targetNode.getSection());
        Assert.assertNotNull(nodesToTarget);
        Assert.assertEquals(1, nodesToTarget.size());
        Assert.assertEquals(section1, nodesToTarget.get(0).getSection());
        Assert.assertNotNull(nodesFromTarget);
        Assert.assertEquals(1, nodesFromTarget.size());
        Assert.assertEquals(section3, nodesFromTarget.get(0).getSection());
        Assert.assertNotNull(connections);
        Assert.assertEquals(2, connections.size());
        Assert.assertEquals(nodesToTarget.get(0), connections.get(0).getOrigin());
        Assert.assertEquals(targetNode, connections.get(0).getDestination());
        Assert.assertTrue(connections.get(0) instanceof LinkConnection);
        Assert.assertEquals(targetNode, connections.get(1).getOrigin());
        Assert.assertEquals(nodesFromTarget.get(0), connections.get(1).getDestination());
        Assert.assertTrue(connections.get(1) instanceof LinkConnection);

        model.buildForSection(section3);

        targetNode = model.getTargetNode();
        nodesToTarget = model.getNodesToTarget();
        nodesFromTarget = model.getNodesFromTarget();
        connections = model.getConnections();

        Assert.assertNotNull(targetNode);
        Assert.assertEquals(section3, targetNode.getSection());
        Assert.assertNotNull(nodesToTarget);
        Assert.assertEquals(1, nodesToTarget.size());
        Assert.assertEquals(section2, nodesToTarget.get(0).getSection());
        Assert.assertNotNull(nodesFromTarget);
        Assert.assertTrue(nodesFromTarget.isEmpty());
        Assert.assertNotNull(connections);
        Assert.assertEquals(1, connections.size());
        Assert.assertEquals(nodesToTarget.get(0), connections.get(0).getOrigin());
        Assert.assertEquals(targetNode, connections.get(0).getDestination());
        Assert.assertTrue(connections.get(0) instanceof LinkConnection);
    }

    /**
     * Testa {@link GraphModel#buildForSection(storybuilder.section.model.Section)
     * }
     */
    @Test
    public void testBuildForSection_OnlySwitchConnections()
    {
        final Section section1 = new Section(Section.PREFIX + 1, false);
        final Section section2 = new Section(Section.PREFIX + 2, false);
        final Section section3 = new Section(Section.PREFIX + 3, false);
        final Section section4 = new Section(Section.PREFIX + 4, false);
        final Section section5 = new Section(Section.PREFIX + 5, false);
        final Link link_2_3 = new Link("link_2_3", section3.getNameWithoutPrefix(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);
        final LinkSwitch linkSwitch_2_3 = new LinkSwitch("switch_2_3", "2", "1", link_2_3, false);
        section1.setLinkSwitches(Arrays.asList(linkSwitch_2_3));
        final Link link_3_4 = new Link("link_3_4", section4.getNameWithoutPrefix(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);
        final LinkSwitch linkSwitch_3_4 = new LinkSwitch("switch_3_4", "3", "1", link_3_4, false);
        section5.setLinkSwitches(Arrays.asList(linkSwitch_3_4));

        final GraphModel model = new GraphModelForTests(section1, section2, section3, section4, section5);

        model.buildForSection(section3);

        Node targetNode = model.getTargetNode();
        List<Node> nodesToTarget = model.getNodesToTarget();
        List<Node> nodesFromTarget = model.getNodesFromTarget();
        List<Connection> connections = model.getConnections();

        Assert.assertNotNull(targetNode);
        Assert.assertEquals(section3, targetNode.getSection());
        Assert.assertNotNull(nodesToTarget);
        Assert.assertEquals(1, nodesToTarget.size());
        Assert.assertEquals(section2, nodesToTarget.get(0).getSection());
        Assert.assertNotNull(nodesFromTarget);
        Assert.assertEquals(1, nodesFromTarget.size());
        Assert.assertEquals(section4, nodesFromTarget.get(0).getSection());
        Assert.assertNotNull(connections);
        Assert.assertEquals(2, connections.size());
        Assert.assertEquals(nodesToTarget.get(0), connections.get(0).getOrigin());
        Assert.assertEquals(targetNode, connections.get(0).getDestination());
        Assert.assertTrue(connections.get(0) instanceof SwitchConnection);
        Assert.assertEquals(section1, ((SwitchConnection) connections.get(0)).getData().getActivatingSection());
        Assert.assertEquals(targetNode, connections.get(1).getOrigin());
        Assert.assertEquals(nodesFromTarget.get(0), connections.get(1).getDestination());
        Assert.assertTrue(connections.get(1) instanceof SwitchConnection);
        Assert.assertEquals(section5, ((SwitchConnection) connections.get(1)).getData().getActivatingSection());
    }

    /**
     * Testa {@link GraphModel#buildForSection(storybuilder.section.model.Section)
     * }
     */
    @Test
    public void testBuildForSection_OnlyMinigameConnections()
    {
        final Section section1 = new Section(Section.PREFIX + 1, false);
        final Section section2 = new Section(Section.PREFIX + 2, false);
        final Section section3 = new Section(Section.PREFIX + 3, false);
        final MinigameInstance game1 = new MinigameInstance("game1",
                new MinigameKind("code1", "title1", "clazz1", new ArrayList<>()),
                "2", "3", new ArrayList<>(), false);
        section1.setMinigame(game1);
        final MinigameInstance game2 = new MinigameInstance("game2",
                new MinigameKind("code2", "title2", "clazz2", new ArrayList<>()),
                "3", "1", new ArrayList<>(), false);
        section2.setMinigame(game2);

        final GraphModel model = new GraphModelForTests(section1, section2, section3);

        model.buildForSection(section2);

        Node targetNode = model.getTargetNode();
        List<Node> nodesToTarget = model.getNodesToTarget();
        List<Node> nodesFromTarget = model.getNodesFromTarget();
        List<Connection> connections = model.getConnections();

        Assert.assertNotNull(targetNode);
        Assert.assertEquals(section2, targetNode.getSection());
        Assert.assertNotNull(nodesToTarget);
        Assert.assertEquals(1, nodesToTarget.size());
        Assert.assertEquals(section1, nodesToTarget.get(0).getSection());
        Assert.assertNotNull(nodesFromTarget);
        Assert.assertEquals(2, nodesFromTarget.size());
        Assert.assertEquals(section1, nodesFromTarget.get(0).getSection());
        Assert.assertEquals(section3, nodesFromTarget.get(1).getSection());
        Assert.assertNotNull(connections);
        Assert.assertEquals(3, connections.size());
        Assert.assertEquals(nodesToTarget.get(0), connections.get(0).getOrigin());
        Assert.assertEquals(targetNode, connections.get(0).getDestination());
        Assert.assertTrue(connections.get(0) instanceof MinigameConnection);
        Assert.assertTrue(((MinigameConnection) connections.get(0)).getData().isWinning());
        Assert.assertEquals(targetNode, connections.get(1).getOrigin());
        Assert.assertEquals(nodesFromTarget.get(0), connections.get(1).getDestination());
        Assert.assertTrue(connections.get(1) instanceof MinigameConnection);
        Assert.assertFalse(((MinigameConnection) connections.get(1)).getData().isWinning());
        Assert.assertEquals(nodesFromTarget.get(1), connections.get(2).getDestination());
        Assert.assertTrue(connections.get(2) instanceof MinigameConnection);
        Assert.assertTrue(((MinigameConnection) connections.get(2)).getData().isWinning());
    }

    /**
     * Testa {@link GraphModel#buildForSection(storybuilder.section.model.Section)
     * }
     */
    @Test
    public void testBuildForSection_MixedConnections()
    {
        final Section section1 = new Section(Section.PREFIX + 1, false);
        final Section section2 = new Section(Section.PREFIX + 2, false);
        final Section section3 = new Section(Section.PREFIX + 3, false);
        final Section section4 = new Section(Section.PREFIX + 4, false);
        final Section section5 = new Section(Section.PREFIX + 5, false);
        final Section section6 = new Section(Section.PREFIX + 6, false);
        final Section section7 = new Section(Section.PREFIX + 7, false);
        final Section section8 = new Section(Section.PREFIX + 8, false);
        final Link link_1_3 = new Link("link_1_3", "3",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);
        section1.setLinks(Arrays.asList(link_1_3));
        final Link link_2_3 = new Link("link_2_3", "3",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);
        final LinkSwitch linkSwitch_2_3 = new LinkSwitch("switch_2_3", "2", "1", link_2_3, false);
        section1.setLinkSwitches(Arrays.asList(linkSwitch_2_3));
        final Link link_3_4 = new Link("link_3_4", "4",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);
        final LinkSwitch linkSwitch_3_4 = new LinkSwitch("switch_3_4", "3", "1", link_3_4, false);
        section5.setLinkSwitches(Arrays.asList(linkSwitch_3_4));
        final Link link_3_5 = new Link("link_3_5", "5",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(), false);
        section3.setLinks(Arrays.asList(link_3_5));
        final MinigameInstance game1 = new MinigameInstance("game1",
                new MinigameKind("code1", "title1", "clazz1", new ArrayList<>()),
                "3", "4", new ArrayList<>(), false);
        section8.setMinigame(game1);
        final MinigameInstance game2 = new MinigameInstance("game2",
                new MinigameKind("code2", "title2", "clazz2", new ArrayList<>()),
                "6", "7", new ArrayList<>(), false);
        section3.setMinigame(game2);

        final GraphModel model = new GraphModelForTests(section1, section2, section3,
                section4, section5, section6, section7, section8);

        model.buildForSection(section3);

        Node targetNode = model.getTargetNode();
        List<Node> nodesToTarget = model.getNodesToTarget();
        List<Node> nodesFromTarget = model.getNodesFromTarget();
        List<Connection> connections = model.getConnections();
        
        Assert.assertNotNull(targetNode);
        Assert.assertEquals(section3, targetNode.getSection());
        
        Assert.assertNotNull(nodesToTarget);
        Assert.assertEquals(3, nodesToTarget.size());
        Assert.assertEquals(section1, nodesToTarget.get(0).getSection());
        Assert.assertEquals(section2, nodesToTarget.get(1).getSection());
        Assert.assertEquals(section8, nodesToTarget.get(2).getSection());
        
        Assert.assertNotNull(nodesFromTarget);
        Assert.assertEquals(4, nodesFromTarget.size());
        Assert.assertEquals(section4, nodesFromTarget.get(0).getSection());
        Assert.assertEquals(section5, nodesFromTarget.get(1).getSection());
        Assert.assertEquals(section6, nodesFromTarget.get(2).getSection());
        Assert.assertEquals(section7, nodesFromTarget.get(3).getSection());
        
        Assert.assertNotNull(connections);
        Assert.assertEquals(7, connections.size());
        
        // link da 1 a 3
        Assert.assertEquals(nodesToTarget.get(0), connections.get(0).getOrigin());
        Assert.assertEquals(targetNode, connections.get(0).getDestination());
        Assert.assertTrue(connections.get(0) instanceof LinkConnection);
        
        // switch da 2 a 3 (attivato da 1)
        Assert.assertEquals(nodesToTarget.get(1), connections.get(1).getOrigin());
        Assert.assertEquals(targetNode, connections.get(1).getDestination());
        Assert.assertTrue(connections.get(1) instanceof SwitchConnection);
        Assert.assertEquals(section1, ((SwitchConnection) connections.get(1)).getData().getActivatingSection());
        
        // switch da 3 a 4 (attivato da 5)
        Assert.assertEquals(targetNode, connections.get(2).getOrigin());
        Assert.assertEquals(nodesFromTarget.get(0), connections.get(2).getDestination());
        Assert.assertTrue(connections.get(2) instanceof SwitchConnection);
        Assert.assertEquals(section5, ((SwitchConnection) connections.get(2)).getData().getActivatingSection());
        
        // link da 3 a 5
        Assert.assertEquals(targetNode, connections.get(3).getOrigin());
        Assert.assertEquals(nodesFromTarget.get(1), connections.get(3).getDestination());
        Assert.assertTrue(connections.get(3) instanceof LinkConnection);
        
        // minigame da 3 a 6 (vittoria)
        Assert.assertEquals(targetNode, connections.get(4).getOrigin());
        Assert.assertEquals(nodesFromTarget.get(2), connections.get(4).getDestination());
        Assert.assertTrue(connections.get(4) instanceof MinigameConnection);
        Assert.assertTrue(((MinigameConnection) connections.get(4)).getData().isWinning());
        
        // minigame da 3 a 7 (sconfitta)
        Assert.assertEquals(targetNode, connections.get(5).getOrigin());
        Assert.assertEquals(nodesFromTarget.get(3), connections.get(5).getDestination());
        Assert.assertTrue(connections.get(5) instanceof MinigameConnection);
        Assert.assertFalse(((MinigameConnection) connections.get(5)).getData().isWinning());
        
        // minigame da 8 a 3 (vittoria)
        Assert.assertEquals(nodesToTarget.get(2), connections.get(6).getOrigin());
        Assert.assertEquals(targetNode, connections.get(6).getDestination());
        Assert.assertTrue(connections.get(6) instanceof MinigameConnection);
        Assert.assertTrue(((MinigameConnection) connections.get(6)).getData().isWinning());
    }

    class GraphModelForTests extends GraphModel
    {

        private final Section[] sections;

        public GraphModelForTests(final Section... sections)
        {
            this.sections = sections;
        }

        @Override
        protected GraphDatasource getDataSource()
        {
            return new GraphDatasource()
            {
                @Override
                protected List<Section> getStorySections()
                {
                    return Arrays.asList(sections);
                }

                @Override
                protected Section getSection(String sectionId)
                {
                    return getStorySections().stream().filter(s -> s.getNameWithoutPrefix().equals(sectionId))
                            .findFirst().orElseGet(null);
                }
            };
        }
    }
}

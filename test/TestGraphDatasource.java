/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import storybuilder.graph.controller.GraphDatasource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import storybuilder.graph.model.LinkSwitchGraphData;
import storybuilder.graph.model.MinigameGraphData;
import storybuilder.section.model.Link;
import storybuilder.section.model.LinkSwitch;
import storybuilder.section.model.MinigameInstance;
import storybuilder.section.model.Section;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco
 */
public class TestGraphDatasource
{

    private GraphDatasource dataSource;

    private Section section1;
    private Section section2;
    private Section section3;
    private Section section4;
    private Section section5;

    public TestGraphDatasource()
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
    public void setUp() throws SBException
    {
        section1 = new Section(Section.PREFIX + 1, false);
        section2 = new Section(Section.PREFIX + 2, false);
        section3 = new Section(Section.PREFIX + 3, false);
        section4 = new Section(Section.PREFIX + 4, false);
        section5 = new Section(Section.PREFIX + 5, false);

        dataSource = new GraphDatasource()
        {

            @Override
            protected List<Section> getStorySections()
            {
                return Arrays.asList(section1, section2, section3, section4, section5);
            }

            @Override
            protected Section getSection(String sectionId)
            {
                switch (sectionId) {
                    case "1":
                        return section1;
                    case "2":
                        return section2;
                    case "3":
                        return section3;
                    case "4":
                        return section4;
                    case "5":
                        return section5;
                    default:
                        return null;
                }
            }

        };
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Testa {@link GraphDatasource#getLinkConnectionsTo(storybuilder.section.model.Section)
     * }
     *
     * @throws SBException
     */
    @Test
    public void testGetLinkConnectionsTo() throws SBException
    {
        final Link link1 = buildEmptyLink(section1, 1, section5);
        final Link link2 = buildEmptyLink(section2, 1, section5);
        buildEmptyLink(section3, 1, section3);
        final Link link4 = buildEmptyLink(section4, 1, section5);

        final Map<Section, List<Link>> sectionsLinkedTo = dataSource.getLinkConnectionsTo(section5);

        Assert.assertNotNull(sectionsLinkedTo);
        Assert.assertEquals(3, sectionsLinkedTo.size());

        final List<Section> orderedOrigins = new ArrayList<>(sectionsLinkedTo.keySet());
        Collections.sort(orderedOrigins);

        Assert.assertEquals(section1, orderedOrigins.get(0));
        List<Link> links = sectionsLinkedTo.get(orderedOrigins.get(0));
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        Assert.assertEquals(link1, links.get(0));

        Assert.assertEquals(section2, orderedOrigins.get(1));
        links = sectionsLinkedTo.get(orderedOrigins.get(1));
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        Assert.assertEquals(link2, links.get(0));

        Assert.assertEquals(section4, orderedOrigins.get(2));
        links = sectionsLinkedTo.get(orderedOrigins.get(2));
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        Assert.assertEquals(link4, links.get(0));
    }

    /**
     * Testa {@link GraphDatasource#getLinkConnectionsFrom(storybuilder.section.model.Section)
     * }
     *
     * @throws SBException
     */
    @Test
    public void testGetLinkConnectionsFrom() throws SBException
    {
        final Link link1 = buildEmptyLink(section1, 1, section2);
        final Link link2 = buildEmptyLink(section1, 2, section3);
        final Link link3 = buildEmptyLink(section1, 3, section5);
        final Link link4 = buildEmptyLink(section1, 4, section5);
        buildEmptyLink(section2, 1, section3);
        buildEmptyLink(section4, 1, section5);

        final Map<Section, List<Link>> sectionsLinkedBy = dataSource.getLinkConnectionsFrom(section1);

        Assert.assertNotNull(sectionsLinkedBy);
        Assert.assertEquals(3, sectionsLinkedBy.size());

        final List<Section> orderedTargets = new ArrayList<>(sectionsLinkedBy.keySet());
        Collections.sort(orderedTargets);

        Assert.assertEquals(section2, orderedTargets.get(0));
        List<Link> links = sectionsLinkedBy.get(orderedTargets.get(0));
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        Assert.assertEquals(link1, links.get(0));

        Assert.assertEquals(section3, orderedTargets.get(1));
        links = sectionsLinkedBy.get(orderedTargets.get(1));
        Assert.assertNotNull(links);
        Assert.assertEquals(1, links.size());
        Assert.assertEquals(link2, links.get(0));

        Assert.assertEquals(section5, orderedTargets.get(2));
        links = sectionsLinkedBy.get(orderedTargets.get(2));
        Assert.assertNotNull(links);
        Assert.assertEquals(2, links.size());
        Assert.assertEquals(link3, links.get(0));
        Assert.assertEquals(link4, links.get(1));
    }

    /**
     * Testa {@link GraphDatasource#getSwitchConnectionsTo(storybuilder.section.model.Section)
     * }
     *
     * @throws SBException
     */
    @Test
    public void testGetSwitchConnectionsTo() throws SBException
    {
        final Link link1 = buildEmptyLink("link1", section5);
        final Link link2 = buildEmptyLink("link2", section5);
        final Link link3 = buildEmptyLink("link3", section5);

        // la sezione 1 possiede uno switch che aggiunge un link (che punta alla sezione 5) alla sezione 3
        buildLinkSwitch(section1, 1, section3, "1", link1);
        buildLinkSwitch(section1, 2, section3, "2", link2);
        buildLinkSwitch(section2, 1, section4, "1", link3);

        final Map<Section, List<LinkSwitchGraphData>> sectionsLinkedBySwitchTo
                = dataSource.getSwitchConnectionsTo(section5);

        Assert.assertNotNull(sectionsLinkedBySwitchTo);
        Assert.assertEquals(2, sectionsLinkedBySwitchTo.size());

        final List<Section> orderedOrigins = new ArrayList<>(sectionsLinkedBySwitchTo.keySet());
        Collections.sort(orderedOrigins);

        Assert.assertEquals(section3, orderedOrigins.get(0));
        List<LinkSwitchGraphData> structs = sectionsLinkedBySwitchTo.get(orderedOrigins.get(0));
        Assert.assertNotNull(structs);
        Assert.assertEquals(2, structs.size());
        Assert.assertEquals(section1, structs.get(0).getActivatingSection());
        Assert.assertEquals(link1, structs.get(0).getLink());
        Assert.assertEquals(section1, structs.get(1).getActivatingSection());
        Assert.assertEquals(link2, structs.get(1).getLink());

        Assert.assertEquals(section4, orderedOrigins.get(1));
        structs = sectionsLinkedBySwitchTo.get(orderedOrigins.get(1));
        Assert.assertNotNull(structs);
        Assert.assertEquals(1, structs.size());
        Assert.assertEquals(section2, structs.get(0).getActivatingSection());
        Assert.assertEquals(link3, structs.get(0).getLink());
    }

    /**
     * Testa {@link GraphDatasource#getSwitchConnectionsFrom(storybuilder.section.model.Section)
     * }
     *
     * @throws SBException
     */
    @Test
    public void testGetSwitchConnectionsFrom() throws SBException
    {
        final Link link1 = buildEmptyLink("link1", section3);
        final Link link2 = buildEmptyLink("link2", section4);
        final Link link3 = buildEmptyLink("link3", section4);
        final Link link4 = buildEmptyLink("link4", section5);

        // la sezione 1 possiede uno switch che aggiunge un link (che punta alla sezione 3) alla sezione 2
        buildLinkSwitch(section1, 1, section2, "1", link1);
        buildLinkSwitch(section1, 2, section2, "2", link2);
        buildLinkSwitch(section3, 2, section2, "3", link3);
        buildLinkSwitch(section2, 1, section3, "1", link4);

        final Map<Section, List<LinkSwitchGraphData>> sectionsLinkedBySwitchBy
                = dataSource.getSwitchConnectionsFrom(section2);

        Assert.assertNotNull(sectionsLinkedBySwitchBy);
        Assert.assertEquals(2, sectionsLinkedBySwitchBy.size());

        final List<Section> orderedTargets = new ArrayList<>(sectionsLinkedBySwitchBy.keySet());
        Collections.sort(orderedTargets);

        Assert.assertEquals(section3, orderedTargets.get(0));
        List<LinkSwitchGraphData> structs = sectionsLinkedBySwitchBy.get(orderedTargets.get(0));
        Assert.assertNotNull(structs);
        Assert.assertEquals(1, structs.size());
        Assert.assertEquals(section1, structs.get(0).getActivatingSection());
        Assert.assertEquals(link1, structs.get(0).getLink());

        Assert.assertEquals(section4, orderedTargets.get(1));
        structs = sectionsLinkedBySwitchBy.get(orderedTargets.get(1));
        Assert.assertNotNull(structs);
        Assert.assertEquals(2, structs.size());
        Assert.assertEquals(section1, structs.get(0).getActivatingSection());
        Assert.assertEquals(link2, structs.get(0).getLink());
        Assert.assertEquals(section3, structs.get(1).getActivatingSection());
        Assert.assertEquals(link3, structs.get(1).getLink());
    }

    /**
     * Testa {@link GraphDatasource#getMinigameConnectionsTo(storybuilder.section.model.Section)
     * }
     *
     * @throws SBException
     */
    @Test
    public void testGetMinigameConnectionsTo() throws SBException
    {
        final MinigameInstance game1 = new MinigameInstance(getMinigameId(section1),
                null, "2", "3", new ArrayList<>(), false);
        section1.setMinigame(game1);
        final MinigameInstance game2 = new MinigameInstance(getMinigameId(section2),
                null, "3", "4", new ArrayList<>(), false);
        section2.setMinigame(game2);
        final MinigameInstance game3 = new MinigameInstance(getMinigameId(section4),
                null, "1", "5", new ArrayList<>(), false);
        section4.setMinigame(game3);

        final Map<Section, MinigameGraphData> sectionsLinkedByMinigameTo
                = dataSource.getMinigameConnectionsTo(section3);

        Assert.assertNotNull(sectionsLinkedByMinigameTo);
        Assert.assertEquals(2, sectionsLinkedByMinigameTo.size());

        final List<Section> orderedOrigins = new ArrayList<>(sectionsLinkedByMinigameTo.keySet());
        Collections.sort(orderedOrigins);

        Assert.assertEquals(section1, orderedOrigins.get(0));
        MinigameGraphData gameData = sectionsLinkedByMinigameTo.get(orderedOrigins.get(0));
        Assert.assertNotNull(gameData);
        Assert.assertEquals(game1, gameData.getGame());
        Assert.assertFalse(gameData.isWinning());

        Assert.assertEquals(section2, orderedOrigins.get(1));
        gameData = sectionsLinkedByMinigameTo.get(orderedOrigins.get(1));
        Assert.assertNotNull(gameData);
        Assert.assertEquals(game2, gameData.getGame());
        Assert.assertTrue(gameData.isWinning());
    }

    /**
     * Testa {@link GraphDatasource#getMinigameConnectionsFrom(storybuilder.section.model.Section)
     * }
     *
     * @throws SBException
     */
    @Test
    public void testGetMinigameConnectionsFrom() throws SBException
    {
        final MinigameInstance game1 = new MinigameInstance(getMinigameId(section1),
                null, "2", "3", new ArrayList<>(), false);
        section1.setMinigame(game1);
        final MinigameInstance game2 = new MinigameInstance(getMinigameId(section2),
                null, "3", "4", new ArrayList<>(), false);
        section2.setMinigame(game2);

        final Map<Section, MinigameGraphData> sectionsLinkedByMinigameBy
                = dataSource.getMinigameConnectionsFrom(section1);

        Assert.assertNotNull(sectionsLinkedByMinigameBy);
        Assert.assertEquals(2, sectionsLinkedByMinigameBy.size());

        final List<Section> orderedTargets = new ArrayList<>(sectionsLinkedByMinigameBy.keySet());
        Collections.sort(orderedTargets);

        Assert.assertEquals(section2, orderedTargets.get(0));
        MinigameGraphData gameData = sectionsLinkedByMinigameBy.get(orderedTargets.get(0));
        Assert.assertNotNull(gameData);
        Assert.assertEquals(game1, gameData.getGame());
        Assert.assertTrue(gameData.isWinning());

        Assert.assertEquals(section3, orderedTargets.get(1));
        gameData = sectionsLinkedByMinigameBy.get(orderedTargets.get(1));
        Assert.assertNotNull(gameData);
        Assert.assertEquals(game1, gameData.getGame());
        Assert.assertFalse(gameData.isWinning());
    }

    private LinkSwitch buildLinkSwitch(final Section origin, final int number,
            final Section target, final String newLinkNumber, final Link newLink)
    {
        final LinkSwitch linkSwitch = new LinkSwitch(getLinkSwitchId(origin, number), target.getNameWithoutPrefix(),
                newLinkNumber, newLink, false);
        final List<LinkSwitch> linkSwitches = origin.getLinkSwitches();
        linkSwitches.add(linkSwitch);
        return linkSwitch;
    }

    private Link buildEmptyLink(final Section origin, final int number, final Section target)
    {
        final Link link = new Link(getLinkId(origin, number), target.getNameWithoutPrefix(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false);
        final List<Link> links = origin.getLinks();
        links.add(link);
        return link;
    }

    private Link buildEmptyLink(final String linkId, final Section target)
    {
        return new Link(linkId, target.getNameWithoutPrefix(),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false);
    }

    private String getLinkId(final Section section, final int number)
    {
        return section.getName() + "_link_" + number;
    }

    private String getLinkSwitchId(final Section section, final int number)
    {
        return section.getName() + "_switch_" + number;
    }

    private String getMinigameId(final Section section)
    {
        return section.getName() + "_minigame";
    }
}

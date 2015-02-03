/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import storybuilder.graph.controller.GraphDatasource;
import java.util.ArrayList;
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
import storybuilder.main.Cache;
import storybuilder.section.model.Link;
import storybuilder.section.model.LinkSwitch;
import storybuilder.section.model.MinigameInstance;
import storybuilder.section.model.Section;
import storybuilder.story.model.Story;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco
 */
public class TestGraphDatasource
{

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

        final Story story = new Story("Test", "")
        {

            @Override
            public void incrementLastSectionId() throws SBException
            {
                // do nothing
            }

        };
        story.addSection(section1);
        story.addSection(section2);
        story.addSection(section3);
        story.addSection(section4);
        story.addSection(section5);

        Cache.getInstance().setStory(story);
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testGetSectionsLinkedToSection() throws SBException
    {
        final Link link1 = buildEmptyLink(section1, 1, section5);
        final Link link2 = buildEmptyLink(section2, 1, section5);
        buildEmptyLink(section3, 1, section3);
        final Link link4 = buildEmptyLink(section4, 1, section5);

        final GraphDatasource ds = new GraphDatasource();
        final Map<Section, List<Link>> sectionsLinkedTo = ds.getSectionsLinkedTo(section5);

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

    @Test
    public void testGetSectionsLinkedBySwitchToSection() throws SBException
    {
        final Link link1 = buildEmptyLink("link1", section5);
        final Link link2 = buildEmptyLink("link2", section5);
        final Link link3 = buildEmptyLink("link3", section5);

        // la sezione 1 possiede uno switch che aggiunge un link (che punta alla sezione 5) alla sezione 3
        buildLinkSwitch(section1, 1, section3, "1", link1);
        buildLinkSwitch(section1, 2, section3, "2", link2);
        buildLinkSwitch(section2, 1, section4, "1", link3);

        final GraphDatasource ds = new GraphDatasource();
        final Map<Section, List<LinkSwitchGraphData>> sectionsLinkedBySwitchTo
                = ds.getSectionsLinkedBySwitchTo(section5);

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

    @Test
    public void testGetSectionsLinkedByMinigameToSection() throws SBException
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

        final GraphDatasource ds = new GraphDatasource();
        final Map<Section, MinigameGraphData> sectionsLinkedByMinigameTo
                = ds.getSectionsLinkedByMinigameTo(section3);

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

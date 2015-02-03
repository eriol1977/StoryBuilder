package storybuilder.graph.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import storybuilder.graph.model.LinkSwitchGraphData;
import storybuilder.graph.model.MinigameGraphData;
import storybuilder.main.Cache;
import storybuilder.section.model.Link;
import storybuilder.section.model.LinkSwitch;
import storybuilder.section.model.MinigameInstance;
import storybuilder.section.model.Section;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class GraphDatasource
{

    public Map<Section, List<Link>> getSectionsLinkedTo(final Section target)
    {
        final Map<Section, List<Link>> result = new HashMap<>();

        final Story story = Cache.getInstance().getStory();
        final List<Section> sections = story.getSections();

        List<Link> links;
        List<Link> chosenLinks;
        for (final Section section : sections) {
            links = section.getLinks();
            chosenLinks = new ArrayList<>();
            for (final Link link : links) {
                if (link.getSectionId().equals(target.getNameWithoutPrefix())) {
                    chosenLinks.add(link);
                }
            }
            if (!chosenLinks.isEmpty()) {
                result.put(section, chosenLinks);
            }
        }

        return result;
    }

    public Map<Section, List<LinkSwitchGraphData>> getSectionsLinkedBySwitchTo(final Section target)
    {
        final Map<Section, List<LinkSwitchGraphData>> result = new HashMap<>();

        final Story story = Cache.getInstance().getStory();
        final List<Section> sections = story.getSections();

        List<LinkSwitch> linkSwitches;
        List<LinkSwitch> chosenSwitches;
        List<LinkSwitchGraphData> data;
        Section s;
        // per ogni sezione della storia...
        for (final Section section : sections) {
            linkSwitches = section.getLinkSwitches();
            chosenSwitches = new ArrayList<>();

            // ...seleziona gli switch che puntano alla sezione target...
            for (final LinkSwitch linkSwitch : linkSwitches) {
                if (linkSwitch.getLink() != null
                        && linkSwitch.getLink().getSectionId().equals(target.getNameWithoutPrefix())) {
                    chosenSwitches.add(linkSwitch);
                }
            }

            // ...e inserisce i dati (sezione attivatrice dello switch + link)
            // nel risultato, usando come chiave la sezione che riceverà il link
            // una volta attivato lo switch
            if (!chosenSwitches.isEmpty()) {
                for (final LinkSwitch linkSwitch : chosenSwitches) {
                    s = story.getSection(linkSwitch.getSectionNumber());
                    data = result.get(s);
                    if (data == null) {
                        data = new ArrayList<>();
                        result.put(s, data);
                    }
                    data.add(new LinkSwitchGraphData(section, linkSwitch.getLink()));
                }
            }
        }

        return result;
    }

    public Map<Section, MinigameGraphData> getSectionsLinkedByMinigameTo(final Section target)
    {
        final Map<Section, MinigameGraphData> result = new HashMap<>();

        final Story story = Cache.getInstance().getStory();
        final List<Section> sections = story.getSections();

        MinigameInstance game;
        for (final Section section : sections) {
            game = section.getMinigame();
            if (game != null) {
                if (game.getWinningSectionNumber().equals(target.getNameWithoutPrefix())) {
                    result.put(section, new MinigameGraphData(game, true));
                } else if (game.getLosingSectionNumber().equals(target.getNameWithoutPrefix())) {
                    result.put(section, new MinigameGraphData(game, false));
                }
            }
        }

        return result;
    }

}

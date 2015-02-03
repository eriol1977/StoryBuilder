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

/**
 *
 * @author Francesco Bertolino
 */
public class GraphDatasource
{

    public Map<Section, List<Link>> getLinkConnectionsTo(final Section target)
    {
        final Map<Section, List<Link>> result = new HashMap<>();

        final List<Section> sections = getStorySections();

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

    public Map<Section, List<Link>> getLinkConnectionsFrom(final Section origin)
    {
        final Map<Section, List<Link>> result = new HashMap<>();

        Section target;
        List<Link> linksToTarget;
        final List<Link> links = origin.getLinks();
        for (final Link link : links) {
            target = getSection(link.getSectionId());
            linksToTarget = result.get(target);
            if (linksToTarget == null) {
                linksToTarget = new ArrayList<>();
                result.put(target, linksToTarget);
            }
            linksToTarget.add(link);
        }

        return result;
    }

    public Map<Section, List<LinkSwitchGraphData>> getSwitchConnectionsTo(final Section target)
    {
        final Map<Section, List<LinkSwitchGraphData>> result = new HashMap<>();

        final List<Section> sections = getStorySections();

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
                    s = getSection(linkSwitch.getSectionNumber());
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

    public Map<Section, List<LinkSwitchGraphData>> getSwitchConnectionsFrom(final Section origin)
    {
        final Map<Section, List<LinkSwitchGraphData>> result = new HashMap<>();

        final List<Section> sections = getStorySections();

        List<LinkSwitch> linkSwitches;
        List<LinkSwitch> chosenSwitches;
        List<LinkSwitchGraphData> data;
        Section s;
        // per ogni sezione della storia...
        for (final Section section : sections) {
            linkSwitches = section.getLinkSwitches();
            chosenSwitches = new ArrayList<>();

            // ...seleziona gli switch che hanno origine dalla sezione origin...
            for (final LinkSwitch linkSwitch : linkSwitches) {
                if (linkSwitch.getLink() != null
                        && linkSwitch.getSectionNumber().equals(origin.getNameWithoutPrefix())) {
                    chosenSwitches.add(linkSwitch);
                }
            }

            // ...e inserisce i dati (sezione attivatrice dello switch + link)
            // nel risultato, usando come chiave la sezione alla quale punta
            // il link
            if (!chosenSwitches.isEmpty()) {
                for (final LinkSwitch linkSwitch : chosenSwitches) {
                    s = getSection(linkSwitch.getLink().getSectionId());
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

    public Map<Section, MinigameGraphData> getMinigameConnectionsTo(final Section target)
    {
        final Map<Section, MinigameGraphData> result = new HashMap<>();

        final List<Section> sections = getStorySections();

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

    public Map<Section, MinigameGraphData> getMinigameConnectionsFrom(final Section origin)
    {
        final Map<Section, MinigameGraphData> result = new HashMap<>();

        final MinigameInstance game = origin.getMinigame();
        result.put(getSection(game.getWinningSectionNumber()), new MinigameGraphData(game, true));
        result.put(getSection(game.getLosingSectionNumber()), new MinigameGraphData(game, false));

        return result;
    }

    protected List<Section> getStorySections()
    {
        return Cache.getInstance().getStory().getSections();
    }

    protected Section getSection(final String sectionId)
    {
        return Cache.getInstance().getStory().getSection(sectionId);
    }

}

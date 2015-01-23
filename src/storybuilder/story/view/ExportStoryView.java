package storybuilder.story.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import storybuilder.main.FileManager;
import storybuilder.main.model.StoryElement;
import storybuilder.main.view.AbstractView;
import storybuilder.section.model.Section;
import storybuilder.story.model.NullStory;
import storybuilder.story.model.Story;
import storybuilder.validation.ErrorManager;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class ExportStoryView extends AbstractView
{

    public ExportStoryView()
    {
        try {
            final Story story = cache.getStory();
            if(story instanceof NullStory)
            {
                ErrorManager.showErrorMessage("A story must be opened to be exported");
                return;
            }

            final Document originalDoc = story.getXmlDoc();
            final String sections = FileManager.findElementNamed("sections", originalDoc).getTextContent();
            final String starting = FileManager.findElementNamed("starting", originalDoc).getTextContent();
            final String ending = FileManager.findElementNamed("ending", originalDoc).getTextContent();

            final DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder icBuilder = icFactory.newDocumentBuilder();
            final Document doc = icBuilder.newDocument();
            final Element mainRootElement = doc.createElement("resources");
            doc.appendChild(mainRootElement);

            FileManager.addElement(doc, "l_title", story.getTitle());
            FileManager.addElement(doc, "sections", sections);
            FileManager.addElement(doc, "starting", starting);
            FileManager.addElement(doc, "ending", ending);

            Comment comment = doc.createComment("commands");
            mainRootElement.appendChild(comment);
            saveStoryElements(story.getCommands(), doc);

            comment = doc.createComment("events");
            mainRootElement.appendChild(comment);
            saveStoryElements(story.getEvents(), doc);

            comment = doc.createComment("joins");
            mainRootElement.appendChild(comment);
            saveStoryElements(story.getJoins(), doc);

            comment = doc.createComment("items");
            mainRootElement.appendChild(comment);
            saveStoryElements(story.getItems(), doc);

            comment = doc.createComment("sections");
            mainRootElement.appendChild(comment);
            for (final Section section : story.getSections()) {
                saveStoryElements(section.getParagraphs(), doc);
                if (section.getGet() != null) {
                    saveStoryElements(Arrays.asList(section.getGet()), doc);
                }
                if (section.getDrop() != null) {
                    saveStoryElements(Arrays.asList(section.getDrop()), doc);
                }
                saveStoryElements(section.getParagraphSwitches(), doc);
                saveStoryElements(section.getLinkSwitches(), doc);
                saveStoryElements(section.getLinks(), doc);
                if (section.getMinigame() != null) {
                    saveStoryElements(Arrays.asList(section.getMinigame()), doc);
                }
            }

            final String fileName = cache.getPreferences().getDirectoryPath() + "\\exported_" + story.getFileName();
            FileManager.saveDocument(doc, fileName);

            mwc.updateStatusBarMessage("Story exported to " + fileName);

        } catch (SBException ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
        } catch (ParserConfigurationException ex) {
            ErrorManager.showErrorMessage("Error while exporting story");
        }
    }

    private void saveStoryElements(final List<? extends StoryElement> elements, final Document doc)
    {
        List<StoryElement> toExport = new ArrayList<>(elements);
        toExport = toExport.stream().filter(e -> !e.isDefault()).collect(Collectors.toList());
        Collections.sort(toExport);
        toExport.stream().forEach(e -> doc.getDocumentElement().appendChild(e.build(doc)));
    }
}

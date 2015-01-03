package storybuilder.preferences.model;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import storybuilder.main.FileManager;
import storybuilder.validation.ErrorManager;

/**
 *
 * @author Francesco Bertolino
 */
public class Preferences
{

    private final static String FILE_PATH = "resources/prefs.xml";

    private String directoryPath;

    public Preferences()
    {
        load();
    }

    public String getDirectoryPath()
    {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath)
    {
        this.directoryPath = directoryPath;
        save();
    }

    private void load()
    {
        try {
            final Document doc = FileManager.openDocument(FILE_PATH);
            final Node directoryNode = doc.getElementsByTagName("directory").item(0);
            directoryPath = !directoryNode.getTextContent().isEmpty() ? directoryNode.getTextContent() : System.getProperty("user.home");
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ErrorManager.showErrorMessage(Preferences.class, "Error while loading preferences", ex);
            loadDefault();
        }
    }

    private void loadDefault()
    {
        directoryPath = System.getProperty("user.home");
    }

    private void save()
    {
        try {
            final Document doc = FileManager.openDocument(FILE_PATH);
            final Node directoryNode = doc.getElementsByTagName("directory").item(0);
            directoryNode.setTextContent(directoryPath);
            FileManager.saveDocument(doc, FILE_PATH);
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            ErrorManager.showErrorMessage(Preferences.class, "Error while saving preferences", ex);
        }
    }
}

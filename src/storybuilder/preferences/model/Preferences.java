package storybuilder.preferences.model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import storybuilder.main.FileManager;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class Preferences
{

    private final static String FILE_PATH = "resources/prefs.xml";

    private String directoryPath;

    public Preferences() throws SBException
    {
        load();
    }

    public String getDirectoryPath()
    {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) throws SBException
    {
        this.directoryPath = directoryPath;
        save();
    }

    private void load() throws SBException
    {
        try {
            final Document doc = FileManager.openDocument(FILE_PATH);
            final Node directoryNode = doc.getElementsByTagName("directory").item(0);
            directoryPath = !directoryNode.getTextContent().isEmpty() ? directoryNode.getTextContent() : System.getProperty("user.home");
        } catch (SBException ex) {
            loadDefault();
            throw new SBException("Error while loading preferences");
        }
    }

    private void loadDefault()
    {
        directoryPath = System.getProperty("user.home");
    }

    private void save() throws SBException
    {
        final Document doc = FileManager.openDocument(FILE_PATH);
        final Node directoryNode = doc.getElementsByTagName("directory").item(0);
        directoryNode.setTextContent(directoryPath);
        FileManager.saveDocument(doc, FILE_PATH);
    }
}

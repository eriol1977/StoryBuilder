package storybuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class AbstractForm extends Application
{

    protected final String title;

    protected final String prefix;

    protected final String filename;

    private final double width;

    private final double height;

    protected final Text message = new Text();

    protected final TableView table = new TableView();

    protected final ObservableList<IStoryElement> data = FXCollections.observableArrayList();

    public AbstractForm(final String title, final String prefix, final String filename, final double width, final double height)
    {
        this.title = title;
        this.prefix = prefix;
        this.filename = filename;
        this.width = width;
        this.height = height;
    }

    @Override
    public void start(Stage stage)
    {
        Scene scene = new Scene(new Group());
        stage.setTitle(title);
        stage.setWidth(width);
        stage.setHeight(height);

        message.setFont(new Font("Arial", 15));
        message.setFill(Color.FIREBRICK);

        final Label label = new Label(title);
        label.setFont(new Font("Arial", 20));
        final Button saveButton = new Button("Save");
        saveButton.setAlignment(Pos.CENTER_RIGHT);
        saveButton.setOnAction((ActionEvent e) -> {
            try {
                writeToFile(filename);
                message.setText("Elements saved to " + filename + ".xml");
            } catch (Exception ex) {
                Logger.getLogger(CommandsForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        final Button loadButton = new Button("Load");
        loadButton.setAlignment(Pos.CENTER_RIGHT);
        loadButton.setOnAction((ActionEvent e) -> {
            try {
                data.clear();
                loadFromFile(filename);
                message.setText("Elements loaded from " + filename + ".xml");
            } catch (Exception ex) {
                Logger.getLogger(CommandsForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        final HBox hbHeader = new HBox();
        hbHeader.setSpacing(10);
        hbHeader.getChildren().addAll(label, saveButton, loadButton);

        table.setEditable(true);

        List<TableColumn> columns = getColumns();

        TableColumn deleteCol = getDeleteColumn();

        try {
            loadFromFile(filename);
            message.setText("Elements loaded from commands.xml");
        } catch (Exception ex) {
            Logger.getLogger(CommandsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        table.setItems(data);
        table.getColumns().addAll(columns);
        table.getColumns().add(deleteCol);

        final HBox hbAdd = getAddNewElementBox();

        final VBox vbox = new VBox();
        vbox.setSpacing(15);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(hbHeader, hbAdd, table, message);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    protected abstract List<TableColumn> getColumns();

    protected TableColumn getColumn(final String fieldName, final double minWidth)
    {
        final TableColumn column = new TableColumn(fieldName.toUpperCase());
        column.setMinWidth(minWidth);
        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        return column;
    }

    protected abstract HBox getAddNewElementBox();

    private TableColumn getDeleteColumn()
    {
        TableColumn deleteCol = new TableColumn<>("Delete");
        deleteCol.setSortable(false);
        deleteCol.setMinWidth(50);
        deleteCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Object, Boolean>, ObservableValue<Boolean>>()
                {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Object, Boolean> p)
                    {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });
        deleteCol.setCellFactory(
                new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>()
                {
                    @Override
                    public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> p)
                    {
                        return new ButtonCell();
                    }
                });
        return deleteCol;
    }

    protected void writeToFile(final String fileName) throws Exception
    {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        icBuilder = icFactory.newDocumentBuilder();
        Document doc = icBuilder.newDocument();
        Element mainRootElement = doc.createElement("resources");
        doc.appendChild(mainRootElement);
        data.stream().forEach(c -> mainRootElement.appendChild(c.build(doc)));

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult file = new StreamResult(new File(fileName + ".xml"));
        transformer.transform(source, file);
    }

    protected void loadFromFile(final String fileName) throws Exception
    {
        File fXmlFile = new File(fileName + ".xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc;
        try {
            doc = dBuilder.parse(fXmlFile);
        } catch (SAXException | IOException ex) {
            return;
        }
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("string");
        List<IStoryElement> elements = new ArrayList<>(nList.getLength());
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elements.add(parseElement(node));
            }
        }
        data.addAll(elements);
    }

    protected abstract IStoryElement parseElement(final Node node);

    protected class ButtonCell extends TableCell<Object, Boolean>
    {

        final Button cellButton = new Button("X");

        ButtonCell()
        {
            cellButton.setOnAction((ActionEvent t) -> {
                data.remove(getTableRow().getIndex());
                message.setText("Element deleted");
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty)
        {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            } else {
                setGraphic(null);
            }
        }
    }
}

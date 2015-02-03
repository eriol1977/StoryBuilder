package storybuilder.main.view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import storybuilder.validation.ErrorManager;

/**
 *
 * @author Francesco Bertolino
 */
public class SBMenuBar extends MenuBar
{

    private final MainPane mainPane;

    private final Menu menuStory;

    private final Menu menuCommands;

    private final Menu menuEvents;

    private final Menu menuItems;

    private final Menu menuJoins;

    private final Menu menuMinigames;

    private final Menu menuSections;
    
    private final Menu menuSectionsGraph;

    SBMenuBar(final MainPane mainPane)
    {
        this.mainPane = mainPane;
        menuStory = buildMenuStory();
        menuSections = buildMenuButton("Sections", KeyCode.S, "storybuilder.section.view.SectionsView", false);
        menuSectionsGraph = buildMenuButton("Graph", KeyCode.G, "storybuilder.graph.view.Graph", true);
        menuCommands = buildMenuButton("Commands", KeyCode.O, "storybuilder.command.view.CommandsView", false);
        menuEvents = buildMenuButton("Events", KeyCode.E, "storybuilder.event.view.EventsView", false);
        menuItems = buildMenuButton("Items", KeyCode.I, "storybuilder.item.view.ItemsView", false);
        menuJoins = buildMenuButton("Joins", KeyCode.J, "storybuilder.join.view.JoinsView", false);
        menuMinigames = buildMenuButton("Minigames", KeyCode.M, "storybuilder.minigame.view.MinigamesView", false);
        getMenus().addAll(menuStory, menuSections,menuSectionsGraph, menuCommands, menuEvents, menuItems, menuJoins, menuMinigames);
        enableMenus(false);
    }

    private Menu buildMenuStory()
    {
        final Menu menu = new Menu("Story");
        final MenuItem newStory = buildMenuItem("New", KeyCode.N, "storybuilder.story.view.NewStoryView", true);
        final MenuItem open = buildMenuItem("Open", KeyCode.O, "storybuilder.story.view.OpenStoryView", true);
        final MenuItem delete = buildMenuItem("Delete", KeyCode.D, "storybuilder.story.view.DeleteStoryView", true);
        final MenuItem export = buildMenuItem("Export", KeyCode.E, "storybuilder.story.view.ExportStoryView", true);
        final MenuItem prefs = buildMenuItem("Preferences", KeyCode.P, "storybuilder.preferences.view.PreferencesView", true);
        menu.getItems().addAll(newStory, open, delete, new SeparatorMenuItem(), export, new SeparatorMenuItem(), prefs);
        return menu;
    }

    private Menu buildMenuButton(final String label, final KeyCode accelerator, final String clazz, final boolean createNewView)
    {
        final Menu menu = new Menu();
        menu.setAccelerator(new KeyCodeCombination(accelerator, KeyCombination.CONTROL_DOWN));
        final Label menuLabel = new Label(label); // to make menu header clickable
        menuLabel.setOnMouseClicked((Event t) -> {
            getMenuButtonAction(createNewView, clazz);
        });
        menu.setGraphic(menuLabel);
        return menu;
    }

    private MenuItem buildMenuItem(final String label, final KeyCode accelerator, final String clazz, final boolean createNewView)
    {
        final MenuItem menuItem = new MenuItem(label);
        menuItem.setAccelerator(new KeyCodeCombination(accelerator, KeyCombination.CONTROL_DOWN));
        menuItem.setOnAction((ActionEvent t) -> {
            getMenuButtonAction(createNewView, clazz);
        });
        return menuItem;
    }

    private void getMenuButtonAction(final boolean createNewView, final String clazz)
    {
        if (createNewView) {
            try {
                mainPane.setContent((AbstractView) Class.forName(clazz).newInstance());
            } catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException ex) {
                ErrorManager.showErrorMessage("Error while loading view");
            }
        } else {
            mainPane.setContent(clazz);
        }
    }

    final void enableMenus(boolean enable)
    {
        menuCommands.setDisable(!enable);
        menuEvents.setDisable(!enable);
        menuItems.setDisable(!enable);
        menuJoins.setDisable(!enable);
        menuMinigames.setDisable(!enable);
        menuSections.setDisable(!enable);
        menuSectionsGraph.setDisable(!enable);
    }

}

package fr.exratio.jme.devkit.terrain;

import com.simsilica.es.base.DefaultEntityData;
import com.simsilica.mathd.Quatd;
import com.simsilica.mathd.Vec3d;
import fr.exratio.devkit.plugin.DevKitPlugin;
import fr.exratio.devkit.service.CoreService;
import fr.exratio.devkit.service.EventService;
import fr.exratio.devkit.service.JmeEngineService;
import fr.exratio.devkit.service.MenuService;
import fr.exratio.devkit.service.ServiceManager;
import fr.exratio.devkit.service.WindowService;
import fr.exratio.devkit.swing.WindowLocationSaver;
import fr.exratio.devkit.swing.WindowSizeSaver;
import fr.exratio.devkit.tree.spatial.NodeTreeNode;
import fr.t4c.editor.ECSService;
import fr.t4c.editor.forms.EntityListView;
import fr.t4c.editor.graph.DisplayComponentListener;
import fr.t4c.editor.gui.component.quatd.QuatdEditor;
import fr.t4c.editor.gui.component.vec3d.Vec3dEditor;
import fr.t4c.editor.serialization.ScenePrefab;
import java.awt.Window;
import java.beans.PropertyEditorManager;
import java.util.logging.Level;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * A simple test plugin. Note that the base package of "plugin.devkit" is **REQUIRED**
 */
public class TerrainPlugin extends DevKitPlugin {

  public static final String LOADING_OK_MESSAGE = "Scene loaded properly.";
  public static final String LOADING_TITLE = "Scene Loading Status";
  public static final String ENTITY_DATA_SERVICE_NAME = "ENTITY_DATA";
  public static final int DEFAULT_WINDOW_WIDTH = 150;
  public static final int DEFAULT_WINDOW_HEIGHT = 300;
  static Logger LOGGER = LoggerFactory.getLogger(TerrainPlugin.class);

  public TerrainPlugin() {

    // Required: set a unique name identifier for our plugin.
    // This will be used by other plugins to identify and depend-on or require you.
    pluginConfiguration.setId("ECSPlugin");

    // Optional: the text that will be displayed in the logs between square brackets.
    pluginConfiguration.setPrefix("ECSPlugin");

    // Required: the version of this plugin
    // Some plugins may depend-on a specific version of your plugin.
    pluginConfiguration.setVersion("1.0.0");

    // Optional: plugins we use for additional functionality if available.
    // The plugin will still load if these plugins are not available.
    String[] softDependencies = {"AnotherPlugin", "AnotherAnotherPlugin"};
    pluginConfiguration.setSoftDependencies(softDependencies);

    // Optional: plugins we *require* in order to function at all.
    // If these plugins are not present, the plugin will *not* be loaded.
    String[] dependencies = {"NonExistantPlugin"};
    // we're not going to set this. Our plugin won't run
    // because the required dependencies are not real.
    // pluginConfiguration.setDependencies(dependencies);

    ServiceManager.registerService(ecsService);
  }

  @Override
  public void onInitialize() throws Exception {
    getLogger().log(Level.INFO, "I am initialized.");
  }

  @Override
  public void onEnabled() {
    getLogger().log(Level.INFO, "I am enabled.");

    // create a custom main menu.
    configureMainMenu();

    // Get an instance of the running jMonkeyEngine application.
    JmeEngineService jmeApp = ServiceManager.getService(JmeEngineService.class);
    getLogger().info("JME Application instance is: " + (jmeApp == null ? "null" : "active"));

    // create an event listener to listen for changes in the scene tree selection.
    ServiceManager.getService(EventService.class);
    ServiceManager.getService(EventService.class);

    /*
          ServiceManager.getService(RegistrationService.class).registerComponentBuilder(
              SpatialComponent.class , SpatialEntityComponentPropertySectionBuilder.class);
          ServiceManager.getService(RegistrationService.class).registerComponentBuilder(
              Position.class , PositionComponentPropertySectionBuilder.class);
    */
    // add a menu item to the Node context menu in the scene tree.
    JMenuItem customNodeMenuItem = new JMenuItem("Custom Node Item");
    ServiceManager.getService(MenuService.class)
        .addItemToContextMenu(NodeTreeNode.class, customNodeMenuItem);

    // create the component window
    ServiceManager.getService(CoreService.class).getMainPage().addTabToSouthPane(EntityListView.WINDOW_TITLE, entityListView.$$$getRootComponent$$$());

  }

  public void showWindow(String title, JPanel rootPanel, int windowWidth, int windowHeight) {
    Window windowToShow = ServiceManager.getService(WindowService.class).getWindow(title);
    if (windowToShow == null) {
      JDialog newWindow = buildWindow(title, rootPanel, windowWidth, windowHeight);
      // position the window from the saved position
      ServiceManager.getService(WindowService.class)
          .positionWindowFromSavedPosition(newWindow, title);
      // resize the window from the saved size.
      ServiceManager.getService(WindowService.class).sizeWindowFromSavedSize(newWindow, title);
      newWindow.setVisible(true);
    } else {
      // finally, show the window.
      windowToShow.setVisible(true);
    }
  }

  public void showWindow(String title, JPanel rootPanel) {
    showWindow(title, rootPanel, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
  }



}

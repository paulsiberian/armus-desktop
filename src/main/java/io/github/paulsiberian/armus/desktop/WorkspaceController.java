/*
 * Copyright (c) Храпунов П. Н., 2019.
 */

package io.github.paulsiberian.armus.desktop;

import io.github.paulsiberian.armus.api.GUIManager;
import io.github.paulsiberian.armus.api.SettingsManager;
import io.github.paulsiberian.armus.api.WorkspaceManager;
import io.github.paulsiberian.armus.api.filesystem.DirTreeItem;
import io.github.paulsiberian.armus.api.filesystem.FileInfo;
import io.github.paulsiberian.armus.api.utils.FileUtil;
import io.github.paulsiberian.armus.api.utils.WorkspaceUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WorkspaceController implements Initializable {
    private final SettingsManager settings = SettingsManager.getInstance();
    private final WorkspaceManager workspace = WorkspaceManager.getInstance();
    private final GUIManager gui = GUIManager.getInstance();
    private DirectoryChooser newWorkspaceChooser;

    @FXML private Label dirViewPlaceHolder;
    @FXML private TextField searchField;
    @FXML private TableColumn<FileInfo, FontIcon> iconColumn;
    @FXML private TableColumn<FileInfo, String> nameColumn;
    @FXML private TableColumn<FileInfo, String> sizeColumn;
    @FXML private TableColumn<FileInfo, String> creationDateColumn;
    @FXML private TableColumn<FileInfo, String> lastModifiedDateColumn;

    @FXML private ToolBar pathBar;
    @FXML private TableView<FileInfo> dirView;
    @FXML private TreeView<String> dirTree;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        newWorkspaceChooser = new DirectoryChooser();
        newWorkspaceChooser.setTitle(resourceBundle.getString("menubar.file.new.workspace_chooser"));
        newWorkspaceChooser.setInitialDirectory(workspace.getRootDir().getParentFile());

        var fileNewWorkspaceText = resourceBundle.getString("menubar.file.new.workspace");
        var fileNewWorkspace = new MenuItem(fileNewWorkspaceText);
        fileNewWorkspace.setOnAction(this::file_new_workspace);
        var fileNewMenu = (Menu) gui.getMenuBar().getMenus().get(0).getItems().get(0);
        var fileNewMenuItems = fileNewMenu.getItems();
        for (var item : fileNewMenuItems) {
            if (item.getText().equals(resourceBundle.getString("menubar.file.new.workspace"))) {
                fileNewMenuItems.remove(item);
                break;
            }
        }
        fileNewMenu.getItems().add(fileNewWorkspace);

        iconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        lastModifiedDateColumn.setCellValueFactory(new PropertyValueFactory<>("lastModifiedDate"));

        dirView.setItems(FileUtil.getFiles(workspace.getCurrentDir()));
        dirView.setRowFactory(tv -> {
            var row = new TableRow<FileInfo>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                    var file = row.getItem().getFile();
                    if (file.isDirectory()) {
                        workspace.setCurrentDir(file);
                    } else if (file.isFile()) {
                        FileUtil.exec(file);
                    }
                }
            });
            return row;
        });

        dirTree.setShowRoot(false);
        dirTree.setRoot(new DirTreeItem(workspace.getRootDir()));
        dirTree.getSelectionModel().selectedItemProperty().addListener((changed, oldItem, newItem) -> {
            workspace.setCurrentDir(((DirTreeItem) newItem).getFile());
        });
        pathBar.getItems().add(new Label(workspace.getRootDir().getName()));
        workspace.currentDirProperty().addListener(((changed, oldDir, newDir) -> {
            var iconCode = "mdi-chevron-right";
            pathBar.getItems().clear();
            var rootDir = workspace.getRootDir();
            var parent = newDir.getParentFile();
            pathBar.getItems().add(new Label(newDir.getName()));
            if (!newDir.equals(rootDir)) {
                while (!parent.equals(rootDir)) {
                    var icon = new FontIcon(iconCode);
                    icon.setIconSize(24);
                    var p = parent;
                    pathBar.getItems().add(icon);
                    var link = new Hyperlink(parent.getName());
                    link.setOnAction(event -> workspace.setCurrentDir(p));
                    pathBar.getItems().add(link);
                    parent = p.getParentFile();
                }
                var icon = new FontIcon(iconCode);
                icon.setIconSize(24);
                pathBar.getItems().add(icon);
                var rootLink = new Hyperlink(rootDir.getName());
                rootLink.setOnAction(event -> workspace.setCurrentDir(rootDir));
                pathBar.getItems().add(rootLink);
            }
            dirView.setItems(FileUtil.getFiles(newDir));
        }));

        searchField.textProperty().addListener(((changed, oldText, newText) -> {
            if (!newText.isEmpty()) {
                dirView.setPlaceholder(new Label(resourceBundle.getString("dirview.label.placeholder_search")));
                if (!newText.equals(oldText)) {
                    var searchList = FXCollections.observableArrayList(new ArrayList<FileInfo>());
                    try {
                        var searchStream = WorkspaceUtil.findFileStream(newText, workspace.getRootDir());
                        searchStream.forEach(file -> searchList.add(new FileInfo(file)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dirView.setItems(searchList);
                }
            } else {
                dirView.setPlaceholder(dirViewPlaceHolder);
                dirView.setItems(FileUtil.getFiles(workspace.getCurrentDir()));
            }
        }));
    }

    @FXML
    private void reloadDirTree(ActionEvent event) {
        dirTree.setRoot(new DirTreeItem(workspace.getRootDir()));
    }

    @FXML
    private void collapseAllDirTree(ActionEvent event) {
    }

    @FXML
    private void file_new_workspace(ActionEvent event) {
        var dir = newWorkspaceChooser.showDialog(gui.getWindow());
        if (dir != null) {
            settings.setWorkspaceProperty(SettingsManager.WORKSPACE_PATH, dir.getPath());
            workspace.init(dir);
        }
        try {
            gui.setContentCenter(gui.loadFXML("workspace"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
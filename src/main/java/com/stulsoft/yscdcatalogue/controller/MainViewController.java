/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stulsoft.yscdcatalogue.Utils;
import com.stulsoft.yscdcatalogue.data.Configuration;
import com.stulsoft.yscdcatalogue.data.DiskItem;
import com.stulsoft.yscdcatalogue.data.DiskItemNode;
import com.stulsoft.yscdcatalogue.data.DiskItemTree;
import com.stulsoft.yscdcatalogue.data.DiskItemType;
import com.stulsoft.yscdcatalogue.data.SoftItem;
import com.stulsoft.yscdcatalogue.data.SoftItemNode;
import com.stulsoft.yscdcatalogue.data.SoftItemTree;
import com.stulsoft.yscdcatalogue.data.SoftItemType;
import com.stulsoft.yscdcatalogue.persistence.ConfigurationPersistence;
import com.stulsoft.yscdcatalogue.persistence.DBManager;
import com.stulsoft.yscdcatalogue.persistence.SoftItemTreePersistence;
import com.stulsoft.yscdcatalogue.service.FileTreeWalk;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Yuriy Stul
 *
 */
public class MainViewController {

	private Logger logger = LogManager.getLogger(MainViewController.class);

	private TreeItem<SoftItem> sourceTreeItemFoCutPaste = null;

	private final Image dirIcon = new Image(getClass().getResourceAsStream("/images/Folder-icon.png"));
	private final Image fileIcon = new Image(getClass().getResourceAsStream("/images/Document-Blank-icon.png"));
	private final Image categoryIcon = new Image(getClass().getResourceAsStream("/images/Category.png"));
	private final Image diskIcon = new Image(getClass().getResourceAsStream("/images/Compact_Disk.png"));

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="softTree"
	private TreeView<SoftItem> softTree;

	@FXML // fx:id="menuItemAddCategory"
	private MenuItem menuItemAddCategory; // Value injected by FXMLLoader

	@FXML // fx:id="menuItemEditCategory"
	private MenuItem menuItemEditCategory; // Value injected by FXMLLoader

	@FXML // fx:id="menuItemAddDisk"
	private MenuItem menuItemAddDisk; // Value injected by FXMLLoader

	@FXML // fx:id="menuItemDelete"
	private MenuItem menuItemDelete; // Value injected by FXMLLoader

	@FXML // fx:id="menuItemFind"
	private MenuItem menuItemFind; // Value injected by FXMLLoader

	@FXML // fx:id="menuCItemAddCategory"
	private MenuItem menuCItemAddCategory; // Value injected by FXMLLoader

	@FXML // fx:id="menuCItemEditCategory"
	private MenuItem menuCItemEditCategory; // Value injected by FXMLLoader

	@FXML // fx:id="menuCItemAddDisk"
	private MenuItem menuCItemAddDisk; // Value injected by FXMLLoader

	@FXML // fx:id="menuCItemDelete"
	private MenuItem menuCItemDelete; // Value injected by FXMLLoader

	@FXML // fx:id="menuItemCut"
	private MenuItem menuItemCut;

	@FXML // fx:id="menuItemPaste"
	private MenuItem menuItemPaste;

	@FXML // fx:id="menuCItemCut"
	private MenuItem menuCItemCut;

	@FXML // fx:id="menuCItemPaste"
	private MenuItem menuCItemPaste;

	@FXML // fx:id="diskTree"
	private TreeTableView<DiskItem> diskTree;

	@FXML
	private TreeTableColumn<DiskItem, String> diskTreeNameColumn;

	@FXML
	private TreeTableColumn<DiskItem, String> diskTreeDateColumn;

	@FXML
	private TreeTableColumn<DiskItem, String> diskTreeSizeColumn;

	@FXML
	private TreeTableColumn<DiskItem, String> diskTreeCommentColumn;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	private void initialize() {
		assert softTree != null : "fx:id=\"softTree\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuItemAddCategory != null : "fx:id=\"menuItemAddCategory\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuItemEditCategory != null : "fx:id=\"menuItemEditCategory\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuItemAddDisk != null : "fx:id=\"menuItemAddDisk\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuItemDelete != null : "fx:id=\"menuItemDelete\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuItemFind != null : "fx:id=\"menuItemFind\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuItemCut != null : "fx:id=\"menuItemCut\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuItemPaste != null : "fx:id=\"menuItemPaste\" was not injected: check your FXML file 'mainView.fxml'.";

		assert menuCItemAddCategory != null : "fx:id=\"menuCItemAddCategory\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuCItemEditCategory != null : "fx:id=\"menuCItemEditCategory\" was not injected: check your FXML file 'mainView.fxml'.";

		assert menuCItemAddDisk != null : "fx:id=\"menuCItemAddDisk\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuCItemDelete != null : "fx:id=\"menuCItemDelete\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuCItemCut != null : "fx:id=\"menuCItemCut\" was not injected: check your FXML file 'mainView.fxml'.";
		assert menuCItemPaste != null : "fx:id=\"menuCItemPaste\" was not injected: check your FXML file 'mainView.fxml'.";

		assert diskTree != null : "fx:id=\"diskTree\" was not injected: check your FXML file 'mainView.fxml'.";

		diskTree.setEditable(true);

		diskTreeNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<DiskItem, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue().getFileName()));
		diskTreeNameColumn.setEditable(false);

		diskTreeDateColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<DiskItem, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue().getFileDate()));
		diskTreeDateColumn.setEditable(false);

		diskTreeSizeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<DiskItem, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue().getFileSize()));
		diskTreeSizeColumn.setEditable(false);

		diskTreeCommentColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<DiskItem, String> p) -> new ReadOnlyStringWrapper(p.getValue().getValue().getComment()));
		diskTreeCommentColumn.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
		diskTreeCommentColumn.setEditable(true);
		diskTreeCommentColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<DiskItem, String>>() {

			/**
			 * @param event
			 */
			@Override
			public void handle(final CellEditEvent<DiskItem, String> event) {
				final DiskItem diskItem = event.getRowValue().getValue();
				diskItem.setComment(event.getNewValue());
				TreeItem<DiskItem> diskItemViewTree = event.getRowValue();

				DiskItemTree diskItemTree = Utils.buildDiskTree(diskItemViewTree, softTree.getSelectionModel().getSelectedItem().getValue().getDiskId());

				try {
					DBManager.getInstance().saveDiskItemTree(diskItemTree);
				}
				catch (Exception e) {
					String msg = String.format("Failed saving changes. Error: %s", e.getMessage());
					logger.error(msg, e);
					(new Alert(AlertType.ERROR, msg)).showAndWait();
				}
			}
		});

		softTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<SoftItem>>() {

			/**
			 * @param observable
			 * @param oldValue
			 * @param newValue
			 */
			@Override
			public void changed(ObservableValue<? extends TreeItem<SoftItem>> observable, TreeItem<SoftItem> oldValue, TreeItem<SoftItem> newValue) {
				if (newValue != null && newValue.getValue().getDiskId() != null) {
					DiskItemTree tree;
					try {
						tree = DBManager.getInstance().getDiskItemTree(newValue.getValue().getDiskId());
					}
					catch (Exception e) {
						(new Alert(AlertType.ERROR, e.getMessage())).showAndWait();
						return;
					}
					diskTree.setRoot(getRootDiskTreeView(tree));
				} else {
					diskTree.setRoot(null);
				}
			}
		});
		initializeModel(null);
	}

	private void initializeModel(final Object model) {
		try {
			Configuration configuration = ConfigurationPersistence.load();
			final String fileName = configuration.getDirectoryName();
			if (StringUtils.isEmpty(fileName)) {
				softTree.setRoot(new TreeItem<SoftItem>(new SoftItem("Library", SoftItemType.CATEGORY)));
			} else {
				logger.info("Loading {}", fileName);
				SoftItemTree softItemTree = SoftItemTreePersistence.load();
				softTree.setRoot(buildSoftTreeItem(softItemTree));
			}
		}
		catch (Exception e) {
			String message = String.format("Failed loading. Error: %s.", e.getMessage());
			logger.error(message, e);
		}
	}

	/**
	 * Builds a TreeView from the tree.
	 * 
	 * @return the TreeView from the tree.
	 */
	private TreeItem<DiskItem> getRootDiskTreeView(final DiskItemTree tree) {
		TreeItem<DiskItem> root = new TreeItem<DiskItem>(tree.getRoot().getData());
		addDiskTreeItemChildren(root, tree.getRoot().getChildren());

		return root;
	}

	/**
	 * Recursively adds children to tree item.
	 * 
	 * @param treeItem
	 *            current tree item.
	 * @param children
	 *            children.
	 */
	private void addDiskTreeItemChildren(final TreeItem<DiskItem> treeItem, final List<DiskItemNode> children) {
		for (DiskItemNode chield : children) {

			TreeItem<DiskItem> chieldTreeItem = new TreeItem<DiskItem>(chield.getData(), new ImageView((chield.getData().getType() == DiskItemType.DIRECTORY ? dirIcon : fileIcon)));
			chieldTreeItem.setExpanded(true);
			treeItem.getChildren().add(chieldTreeItem);
			addDiskTreeItemChildren(chieldTreeItem, chield.getChildren());
		}
	}

	@FXML
	private void onAddCategory(ActionEvent event) {
		TreeItem<SoftItem> node = softTree.getSelectionModel().getSelectedItem();
		if (node != null) {
			SoftItem softItem = node.getValue();
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/categoryView.fxml"));
				VBox root = loader.load();
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Add new category");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(getPrimaryStage());
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

				dialogStage.setScene(scene);

				CategoryViewController controller = loader.getController();
				controller.setDialogStage(dialogStage);
				controller.setParentCategoryName(softItem.getName());

				dialogStage.showAndWait();

				if (controller.isOkClicked()) {
					TreeItem<SoftItem> leaf = new TreeItem<SoftItem>(new SoftItem(controller.getCategoryName(), SoftItemType.CATEGORY), new ImageView(categoryIcon));
					node.getChildren().add(leaf);
					sortSoftTreeItem(node);

					DBManager.getInstance().saveSoftItemTree(Utils.buildSoftTree(softTree.getRoot()));
				}
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	@FXML
	private void onEditCategory(ActionEvent event) {
		TreeItem<SoftItem> node = softTree.getSelectionModel().getSelectedItem();
		if (node != null) {
			TreeItem<SoftItem> parent = node.getParent();
			SoftItem softItem = node.getValue();
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/categoryView.fxml"));
				VBox root = loader.load();
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Edit category");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(getPrimaryStage());
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

				dialogStage.setScene(scene);

				CategoryViewController controller = loader.getController();
				controller.setDialogStage(dialogStage);
				if (parent != null) {
					controller.setParentCategoryName(parent.getValue().getName());
				}
				controller.setCategoryName(softItem.getName());

				dialogStage.showAndWait();

				if (controller.isOkClicked()) {
					softItem.setName(controller.getCategoryName());
					DBManager.getInstance().saveSoftItemTree(Utils.buildSoftTree(softTree.getRoot()));
					softTree.refresh();
				}
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	@FXML
	private void onDelete(ActionEvent event) {
		TreeItem<SoftItem> node = softTree.getSelectionModel().getSelectedItem();
		if (node != null && node.getParent() != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION, String.format("Are you sure to delete \"%s\" category/disk?", node.getValue().getName()));
			Optional<ButtonType> answer = alert.showAndWait();
			if (answer.isPresent() && answer.get() == ButtonType.OK) {
				DBManager.getInstance().deleteAllDisks(node);
				node.getParent().getChildren().remove(node);
			}
		}
	}

	@FXML
	private void onAddDisk(ActionEvent event) {
		TreeItem<SoftItem> node = softTree.getSelectionModel().getSelectedItem();
		if (node != null && node.getValue().getType() != SoftItemType.DISK) {
			DirectoryChooser dirChooser = new DirectoryChooser();
			dirChooser.setTitle("Choose disk.");
			File directory = dirChooser.showDialog(getPrimaryStage());
			if (directory != null) {

				Service<DiskItemTree> service = new Service<DiskItemTree>() {

					/**
					 * @return Task<DiskItemTree>
					 */
					@Override
					protected Task<DiskItemTree> createTask() {
						return new Task<DiskItemTree>() {
							/**
							 * @return DiskItemTree
							 * @throws Exception
							 *             I/O exception
							 */
							@Override
							protected DiskItemTree call() throws Exception {
								getPrimaryStage().getScene().setCursor(Cursor.WAIT);
								try {
									DiskItemTree tree = FileTreeWalk.buildFileTree(directory.getAbsolutePath());
									return tree;
								}
								catch (IOException e) {
									logger.error("Failed adding a disk " + directory.getAbsolutePath() + ", error: " + e.getMessage(), e);
								}
								finally {
									getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
								}
								return null;
							}
						};
					}
				};

				service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

					/**
					 * @param event
					 *            the worker state event
					 */
					@Override
					public void handle(WorkerStateEvent event) {
						DiskItemTree tree = (DiskItemTree) event.getSource().getValue();
						diskTree.setRoot(getRootDiskTreeView(tree));

						String storageName;
						try {
							storageName = Utils.getStorageName(directory);
							TreeItem<SoftItem> child = new TreeItem<SoftItem>(new SoftItem(storageName, SoftItemType.DISK), new ImageView(diskIcon));
							child.getValue().setDiskId(tree.getId());
							node.getChildren().add(child);
							sortSoftTreeItem(node);
							try {
								DBManager.getInstance().saveDiskItemTree(tree);
								DBManager.getInstance().saveSoftItemTree(Utils.buildSoftTree(softTree.getRoot()));
							}
							catch (Exception e) {
								String msg = String.format("Failed storing Disk Item Tree into database. Error: %s", e.getMessage());
								logger.error(msg, e);
								throw new IOException(msg, e);
							}
							softTree.getSelectionModel().select(child);
						}
						catch (IOException e) {
							String msg = String.format("Failed adding disk. Error: %s", e.getMessage());
							logger.error(msg, e);
							(new Alert(AlertType.ERROR, msg)).showAndWait();
						}
					}
				});
				service.start();
			}
		}
	}

	@FXML
	private void onMenuValidation(Event event) {
		validateMenu();
	}

	@FXML
	private void onContextMenuShowing(Event event) {
		validateMenu();
	}

	private void validateMenu() {
		Cursor cursor = getPrimaryStage().getScene().getCursor();
		if (cursor != null && cursor.equals(Cursor.WAIT)) {
			menuCItemAddCategory.setDisable(true);
			menuCItemEditCategory.setDisable(true);

			menuCItemAddDisk.setDisable(true);
			menuCItemDelete.setDisable(true);

			menuItemAddCategory.setDisable(true);
			menuItemEditCategory.setDisable(true);

			menuItemAddDisk.setDisable(true);
			menuItemDelete.setDisable(true);

			menuItemCut.setDisable(true);
			menuItemPaste.setDisable(true);

			menuItemFind.setDisable(true);

			menuCItemCut.setDisable(true);
			menuCItemPaste.setDisable(true);

			return;
		}

		TreeItem<SoftItem> selectedNode = softTree.getSelectionModel().getSelectedItem();

		menuCItemAddCategory.setDisable(false);
		menuCItemEditCategory.setDisable(false);

		menuCItemAddDisk.setDisable(false);
		menuCItemDelete.setDisable(false);

		menuItemAddCategory.setDisable(false);
		menuItemEditCategory.setDisable(false);

		menuItemAddDisk.setDisable(false);
		menuItemDelete.setDisable(false);

		menuItemCut.setDisable(false);
		menuItemPaste.setDisable(false);

		menuItemFind.setDisable(false);

		menuCItemCut.setDisable(false);
		menuCItemPaste.setDisable(false);

		if (selectedNode == null) {
			menuCItemAddCategory.setDisable(true);
			menuCItemEditCategory.setDisable(true);

			menuCItemAddDisk.setDisable(true);
			menuCItemDelete.setDisable(true);

			menuItemAddCategory.setDisable(true);
			menuItemEditCategory.setDisable(true);

			menuItemAddDisk.setDisable(true);
			menuItemDelete.setDisable(true);

			menuItemCut.setDisable(true);
			menuItemPaste.setDisable(true);

			menuCItemCut.setDisable(true);
			menuCItemPaste.setDisable(true);
		} else {
			if (selectedNode.getParent() == null) {
				menuItemDelete.setDisable(true);
				menuCItemDelete.setDisable(true);
			}

			if (selectedNode.getValue().getType() == SoftItemType.DISK) {
				menuCItemAddDisk.setDisable(true);
				menuItemAddDisk.setDisable(true);

				menuItemPaste.setDisable(true);
				menuCItemPaste.setDisable(true);
			}
		}

		if (sourceTreeItemFoCutPaste == null) {
			menuItemPaste.setDisable(true);
			menuCItemPaste.setDisable(true);
		}
	}

	private Stage getPrimaryStage() {
		return (Stage) softTree.getScene().getWindow();
	}

	@FXML
	private void onNew(ActionEvent event) {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Choose directory with data base.");

		Configuration configuration;
		try {
			configuration = ConfigurationPersistence.load();
		}
		catch (Exception e) {
			(new Alert(AlertType.ERROR, e.getMessage())).showAndWait();
			configuration = new Configuration(null);
		}

		if (!StringUtils.isEmpty(configuration.getDirectoryName())) {
			dirChooser.setInitialDirectory(new File(configuration.getDirectoryName()));
		}

		File directory = dirChooser.showDialog(getPrimaryStage());
		if (directory != null) {
			try {
				configuration.setDirectoryName(directory.getAbsolutePath());
				ConfigurationPersistence.save(configuration);
				DBManager.getInstance().setDirectory(directory.getAbsolutePath());
				TreeItem<SoftItem> root = new TreeItem<SoftItem>(new SoftItem("CHANGE THIS LIBRARAY NAME", SoftItemType.CATEGORY));
				softTree.setRoot(root);
				getPrimaryStage().setTitle(Utils.getTitle(directory));
			}
			catch (Exception e) {
				String message = String.format("Failed loading from %s. Error: %s", directory.getAbsolutePath(), e.getMessage());
				logger.error(message, e);
			}
		}

		try {
			DBManager.getInstance().saveSoftItemTree(Utils.buildSoftTree(softTree.getRoot()));
		}
		catch (Exception e) {
			String msg = String.format("Failed saving in database. Error: %s", e.getMessage());
			logger.error(msg, e);
			(new Alert(AlertType.ERROR, msg)).showAndWait();
		}
	}

	@FXML
	private void onOpen(ActionEvent event) {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Choose directory with data base.");
		Configuration configuration;
		try {
			configuration = ConfigurationPersistence.load();
		}
		catch (Exception e) {
			(new Alert(AlertType.ERROR, e.getMessage())).showAndWait();
			configuration = new Configuration(null);
		}
		if (!StringUtils.isEmpty(configuration.getDirectoryName())) {
			dirChooser.setInitialDirectory(new File(configuration.getDirectoryName()));
		}
		File directory = dirChooser.showDialog(getPrimaryStage());
		if (directory != null) {
			try {
				configuration.setDirectoryName(directory.getAbsolutePath());
				ConfigurationPersistence.save(configuration);
				DBManager.getInstance().setDirectory(directory.getAbsolutePath());
				SoftItemTree softItemTree = SoftItemTreePersistence.load();
				TreeItem<SoftItem> root;
				if (softItemTree != null) {
					root = buildSoftTreeItem(softItemTree);
				} else {
					root = new TreeItem<SoftItem>();
				}
				softTree.setRoot(root);
				getPrimaryStage().setTitle(Utils.getTitle(directory));
			}
			catch (Exception e) {
				String message = String.format("Failed loading from %s. Error: %s", directory.getAbsolutePath(), e.getMessage());
				logger.error(message, e);
			}
		}
	}

	@FXML
	private void onExit(ActionEvent event) {
		Platform.exit();
	}

	private TreeItem<SoftItem> buildSoftTreeItem(final SoftItemTree softTree) {
		TreeItem<SoftItem> treeItem = new TreeItem<SoftItem>(softTree.getRoot().getData(), new ImageView(categoryIcon));

		for (SoftItemNode child : softTree.getRoot().getChildren()) {
			addChild(treeItem, child);
		}

		softTree.getRoot().getChildren().sort(new Comparator<SoftItemNode>() {

			@Override
			public int compare(SoftItemNode o1, SoftItemNode o2) {
				return o1.getData().getName().compareTo(o2.getData().getName());
			}
		});

		return treeItem;
	}

	private void addChild(final TreeItem<SoftItem> treeItem, final SoftItemNode child) {
		TreeItem<SoftItem> subTreeItem = new TreeItem<SoftItem>(child.getData(), (child.getData().getType() == SoftItemType.CATEGORY ? new ImageView(categoryIcon) : new ImageView(diskIcon)));
		treeItem.getChildren().add(subTreeItem);
		sortSoftTreeItem(treeItem);
		for (SoftItemNode softItemNode : child.getChildren()) {
			addChild(subTreeItem, softItemNode);
		}
	}

	// Event Listener on MenuItem[#menuItemFind].onAction
	@FXML
	public void onFind(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/searchView.fxml"));
			VBox root = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Search folder or file");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(getPrimaryStage());
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

			dialogStage.setScene(scene);

			SearchViewController controller = loader.getController();
			controller.setSoftTree(softTree);

			dialogStage.showAndWait();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@FXML
	private void onCut(final ActionEvent event) {
		sourceTreeItemFoCutPaste = softTree.getSelectionModel().getSelectedItem();
	}

	@FXML
	private void onPaste(final ActionEvent event) {
		if (sourceTreeItemFoCutPaste == null) {
			return;
		}

		TreeItem<SoftItem> destTreeItemFoCutPaste = softTree.getSelectionModel().getSelectedItem();
		if (destTreeItemFoCutPaste == null) {
			return;
		}

		if (sourceTreeItemFoCutPaste.getParent().equals(destTreeItemFoCutPaste)) {
			return;
		}

		sourceTreeItemFoCutPaste.getParent().getChildren().remove(sourceTreeItemFoCutPaste);
		destTreeItemFoCutPaste.getChildren().add(sourceTreeItemFoCutPaste);
		sortSoftTreeItem(destTreeItemFoCutPaste);

		sourceTreeItemFoCutPaste = null;
	}

	private void sortSoftTreeItem(final TreeItem<SoftItem> node) {
		node.getChildren().sort(new Comparator<TreeItem<SoftItem>>() {

			@Override
			public int compare(TreeItem<SoftItem> o1, TreeItem<SoftItem> o2) {
				return o1.getValue().getName().compareTo(o2.getValue().getName());
			}
		});

	}
}

/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.stulsoft.yscdcatalogue.Utils;
import com.stulsoft.yscdcatalogue.data.SearchResult;
import com.stulsoft.yscdcatalogue.data.SoftItem;
import com.stulsoft.yscdcatalogue.data.SoftItemTree;
import com.stulsoft.yscdcatalogue.service.Search;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author Yuriy Stul
 *
 */
public class SearchViewController {
	// private Stage dialogStage;
	private TreeView<SoftItem> softTree;

	/**
	 * @param softTree
	 *            the softTree to set
	 */
	public void setSoftTree(TreeView<SoftItem> softTree) {
		this.softTree = softTree;
	}

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="searchText"
	private TextField searchText; // Value injected by FXMLLoader

	@FXML // fx:id="findButton"
	private Button findButton; // Value injected by FXMLLoader

	@FXML // fx:id="resultTable"
	private TableView<SearchResult> resultTable; // Value injected by FXMLLoader

	@FXML // fx:id="categoryColumn"
	private TableColumn<SearchResult, String> categoryColumn;

	@FXML // fx:id="diskColumn"
	private TableColumn<SearchResult, String> diskColumn;

	@FXML // fx:id="fileColumn"
	private TableColumn<SearchResult, String> fileColumn;

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert searchText != null : "fx:id=\"searchText\" was not injected: check your FXML file 'searchView.fxml'.";
		assert findButton != null : "fx:id=\"findButton\" was not injected: check your FXML file 'searchView.fxml'.";
		assert resultTable != null : "fx:id=\"resultTable\" was not injected: check your FXML file 'searchView.fxml'.";
		assert categoryColumn != null : "fx:id=\"categoryColumn\" was not injected: check your FXML file 'searchView.fxml'.";
		assert diskColumn != null : "fx:id=\"diskColumn\" was not injected: check your FXML file 'searchView.fxml'.";
		assert fileColumn != null : "fx:id=\"fileColumn\" was not injected: check your FXML file 'searchView.fxml'.";

		categoryColumn.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("categoryName"));
		diskColumn.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("diskName"));
		fileColumn.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("fileName"));

		resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SearchResult>() {

			/**
			 * @param observable
			 * @param oldValue
			 * @param newValue
			 */
			@Override
			public void changed(ObservableValue<? extends SearchResult> observable, SearchResult oldValue, SearchResult newValue) {
				if (newValue != null) {
					softTree.getSelectionModel().select(newValue.getTreeItem());
				}
			}
		});
	}

	@FXML
	void onFind(ActionEvent event) {
		if (softTree == null) {
			throw new RuntimeException("softTree is undefinded");
		}

		SoftItemTree softItemTree = Utils.buildSoftTree(softTree.getRoot());
		Search search = new Search(softItemTree, searchText.getText());
		ObservableList<SearchResult> results = search.find();
		resultTable.setItems(results);
	}
}

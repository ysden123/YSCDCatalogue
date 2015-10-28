/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @author Yuriy Stul
 *
 */
public class CategoryViewController {
	private Stage dialogStage;
	private boolean isOkClicked = false;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="parentCategoryName"
	private TextField parentCategoryName; // Value injected by FXMLLoader

	@FXML // fx:id="categoryName"
	private TextField categoryName; // Value injected by FXMLLoader

	@FXML // fx:id="buttonOK"
	private Button buttonOK; // Value injected by FXMLLoader

	@FXML // fx:id="buttonCancel"
	private Button buttonCancel; // Value injected by FXMLLoader

	/**
	 * @return the dialogStage
	 */
	public Stage getDialogStage() {
		return dialogStage;
	}

	/**
	 * @param dialogStage
	 *            the dialogStage to set
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * @return the category name
	 */
	public String getCategoryName() {
		return categoryName.getText();
	}

	/**
	 * @param name the category name
	 */
	public void setCategoryName(final String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("name is null or empty.");
		}
		categoryName.setText(name);
	}
	
	/**
	 * @param name set parent category name
	 */
	public void setParentCategoryName(final String name) {
		if (StringUtils.isEmpty(name)) {
			throw new IllegalArgumentException("name is null or empty.");
		}
		parentCategoryName.setText(name);
	}

	/**
	 * @return the isOkClicked
	 */
	public boolean isOkClicked() {
		return isOkClicked;
	}

	@FXML
	void onCancelButton(ActionEvent event) {
		isOkClicked = false;
		dialogStage.close();
	}

	@FXML
	void onOkButton(ActionEvent event) {
		String errorMessage = "";

		if (categoryName.getText().isEmpty()) {
			errorMessage += " Category name is empty.";
		}

		if (errorMessage.isEmpty()) {
			isOkClicked = true;
			dialogStage.close();
		} else {
			Alert alert = new Alert(AlertType.ERROR, errorMessage);
			alert.showAndWait();
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert parentCategoryName != null : "fx:id=\"parentCategoryName\" was not injected: check your FXML file 'categoryView.fxml'.";
		assert categoryName != null : "fx:id=\"categoryName\" was not injected: check your FXML file 'categoryView.fxml'.";
		assert buttonOK != null : "fx:id=\"buttonOK\" was not injected: check your FXML file 'categoryView.fxml'.";
		assert buttonCancel != null : "fx:id=\"buttonCancel\" was not injected: check your FXML file 'categoryView.fxml'.";
	}
}

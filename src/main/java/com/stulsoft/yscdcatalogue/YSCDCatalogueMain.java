/**
 * Copyright (c) 2015, Yuriy Stul. All rights reserved
 */
package com.stulsoft.yscdcatalogue;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.stulsoft.yscdcatalogue.controller.MainViewController;
import com.stulsoft.yscdcatalogue.data.Configuration;
import com.stulsoft.yscdcatalogue.persistence.ConfigurationPersistence;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Yuriy Stul
 *
 */
public class YSCDCatalogueMain extends Application {
	private static Logger logger = LogManager.getLogger(YSCDCatalogueMain.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(final Stage primaryStage) throws Exception {
		logger.debug("Start application.");

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainView.fxml"));
			VBox root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			setInitialTitle(primaryStage);
			
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/application.png")));

			final MainViewController mainViewController = loader.getController();
			primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					if (!mainViewController.onCloseWindow()) {
						event.consume();
					}
				}
			});

			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	/**
	 * @param args
	 *            the application arguments.
	 */
	public static void main(String[] args) {
		launch(args);
		logger.debug("Finish application.");

	}

	private void setInitialTitle(final Stage primaryStage) {
		try {
			Configuration configuration = ConfigurationPersistence.load();
			final String fileName = configuration.getFileName();
			if (StringUtils.isEmpty(fileName)) {
				primaryStage.setTitle(Utils.getTitle(null));
			} else {
				logger.info("Loading {}", fileName);
				primaryStage.setTitle(Utils.getTitle(new File(fileName)));
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}

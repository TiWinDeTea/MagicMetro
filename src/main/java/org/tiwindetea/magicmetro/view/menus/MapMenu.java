/*
 * MIT License
 *
 * Copyright (c) 2016 TiWinDeTea - contact@tiwindetea.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.tiwindetea.magicmetro.view.menus;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import org.tiwindetea.magicmetro.global.scripts.MapScript;

import java.io.IOException;
import java.util.Collection;

/**
 * Map menu of the game.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class MapMenu implements Menu {

	private final MenuController menuController;
	private StackPane mainStackPane;
	private MapMenuController mapMenuController;

	/**
	 * Instantiates a new MapMenu.
	 *
	 * @param menuController the menu controller
	 */
	public MapMenu(MenuController menuController, Collection<MapScript> mapScripts) {

		this.menuController = menuController;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MapMenuController.class.getResource("MapMenu.fxml"));

		try {
			this.mainStackPane = (StackPane) loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}

		this.mapMenuController = loader.getController();

		for(MapScript mapScript : mapScripts) {
			Button mapButton = new Button(mapScript.mapName);
			this.mapMenuController.mapsVBox.getChildren().add(mapButton);
			mapButton.setOnAction(event -> menuController.launchGame(new MapScript(mapScript)));
		}

		Button exitButton = new Button("Exit");
		this.mapMenuController.mapsVBox.getChildren().add(exitButton);
		exitButton.setOnAction(event -> onExit());

		this.mainStackPane.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.ESCAPE) {
				onExit();
			}
		});
	}

	private void onExit() {
		this.menuController.exitMenu();
	}

	@Override
	public Parent getRoot() {
		return this.mainStackPane;
	}

}

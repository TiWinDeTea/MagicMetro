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

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Stack;

/**
 * Class that manage the different menus .
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class MenuManager implements MenuController {

	private final Stage stage;
	private final Pane voidPane = new Pane();
	private final Scene scene = new Scene(this.voidPane);
	private final Stack<Menu> menuQueue = new Stack<>();
	private Menu currentMenu;

	/**
	 * Instantiates a new MenuManager.
	 *
	 * @param stage the stage
	 */
	public MenuManager(Stage stage) {
		this.stage = stage;
		stage.setScene(this.scene);
	}

	/**
	 * Display menus.
	 */
	public void DisplayMenus() {
		this.currentMenu = new MainMenu(this);
		this.scene.setRoot(this.currentMenu.getRoot());
	}

	@Override
	public void exitMenu() {
		if(!this.menuQueue.isEmpty()) {
			this.currentMenu = this.menuQueue.pop();
			this.scene.setRoot(this.currentMenu.getRoot());
		}
		else {
			this.stage.close();
		}
	}

	@Override
	public void goToOptionMenu() {
		this.menuQueue.push(this.currentMenu);
		this.currentMenu = new OptionMenu(this);
		this.scene.setRoot(this.currentMenu.getRoot());
	}

	@Override
	public void goToMapMenu() {
		this.menuQueue.push(this.currentMenu);
		this.currentMenu = new MapMenu(this);
		this.scene.setRoot(this.currentMenu.getRoot());
	}

	@Override
	public boolean isFullScreen() {
		return this.stage.isFullScreen();
	}

}

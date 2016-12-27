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

package org.tiwindetea.magicmetro;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.arakhne.afc.math.geometry.d2.d.MultiShape2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.tiwindetea.magicmetro.global.eventdispatcher.EventDispatcher;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.FullScreenToggleEvent;
import org.tiwindetea.magicmetro.global.scripts.MapScript;
import org.tiwindetea.magicmetro.view.menus.MenuManager;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Main class, launch the MainMenu.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class Main extends Application {

	private final Collection<MapScript> mapScripts = new LinkedList<>();
	private Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		initMapScripts();
		MenuManager menuManager = new MenuManager(primaryStage, this.mapScripts);
		menuManager.DisplayMenus();
		primaryStage.show();

		//TODO: shortcut to go fullscreen?

		// shortcut to exit fullscreen
		primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.F, KeyCombination.SHIFT_DOWN));

		EventDispatcher.getInstance().addListener(FullScreenToggleEvent.class, this::onFullScreenToggleEvent);
	}

	private void onFullScreenToggleEvent(FullScreenToggleEvent event) {
		this.stage.setFullScreen(event.fullScreen);
	}

	private void initMapScripts() {
		//FIXME: tests
		MultiShape2d<Rectangle2d> multiShape2d = new MultiShape2d<>();
		multiShape2d.add(new Rectangle2d(50, 50, 400, 100));
		multiShape2d.add(new Rectangle2d(50, 150, 100, 400));
		multiShape2d.add(new Rectangle2d(500, 350, 200, 200));
		MapScript testMapScript = new MapScript("test map", multiShape2d);
		this.mapScripts.add(testMapScript);
	}

}

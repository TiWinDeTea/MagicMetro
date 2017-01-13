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
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.arakhne.afc.math.geometry.d2.d.MultiShape2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.tiwindetea.magicmetro.global.TimeManager;
import org.tiwindetea.magicmetro.global.eventdispatcher.EventDispatcher;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.GameExitEvent;
import org.tiwindetea.magicmetro.global.scripts.ElementChoiceScript;
import org.tiwindetea.magicmetro.global.scripts.ElementScript;
import org.tiwindetea.magicmetro.global.scripts.LineScript;
import org.tiwindetea.magicmetro.global.scripts.MapScript;
import org.tiwindetea.magicmetro.global.scripts.PassengerCarScript;
import org.tiwindetea.magicmetro.global.scripts.StationScript;
import org.tiwindetea.magicmetro.global.scripts.StationUpgradeScript;
import org.tiwindetea.magicmetro.global.scripts.TrainScript;
import org.tiwindetea.magicmetro.global.scripts.TunnelScript;
import org.tiwindetea.magicmetro.global.util.Pair;
import org.tiwindetea.magicmetro.model.StationType;
import org.tiwindetea.magicmetro.model.TrainType;
import org.tiwindetea.magicmetro.view.menus.MenuManager;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

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
		MenuManager menuManager = new MenuManager(this.stage, this.mapScripts);
		menuManager.DisplayMenus();
		this.stage.show();

		//TODO: shortcut to go fullscreen?

		// shortcut to exit fullscreen
		this.stage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.F, KeyCombination.SHIFT_DOWN));

		this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				TimeManager.getInstance().end();
				EventDispatcher.getInstance().fire(new GameExitEvent());
			}
		});
	}

	private void initMapScripts() {
		//TODO: save maps description in files and load them
		//FIXME: test maps
		{
			MultiShape2d<Rectangle2d> multiShape2d = new MultiShape2d<>();
			multiShape2d.add(new Rectangle2d(0, 1010, 380, 50));
			multiShape2d.add(new Rectangle2d(330, 780, 50, 230));
			multiShape2d.add(new Rectangle2d(380, 780, 390, 50));
			multiShape2d.add(new Rectangle2d(720, 450, 50, 330));
			multiShape2d.add(new Rectangle2d(770, 450, 720, 50));
			multiShape2d.add(new Rectangle2d(1490, 450, 50, 270));
			multiShape2d.add(new Rectangle2d(1540, 670, 150, 50));
			multiShape2d.add(new Rectangle2d(1690, 450, 50, 270));
			multiShape2d.add(new Rectangle2d(1740, 450, 180, 50));
			MapScript mapScript = new MapScript("Magic map", 1920, 1080, multiShape2d);
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(0),
				new Point2d(830, 750),
				StationType.CIRCLE));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(0),
				new Point2d(560, 700),
				StationType.TRIANGLE));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(0),
				new Point2d(610, 480),
				StationType.SQUARE));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(30),
				new Point2d(1300, 690),
				StationType.CIRCLE));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(60),
				new Point2d(150, 530),
				StationType.CROSS));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(90),
				new Point2d(1620, 170),
				StationType.CIRCLE));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(120),
				new Point2d(1250, 380),
				StationType.TRIANGLE));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(150),
				new Point2d(1610, 640),
				StationType.CROSS));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(180),
				new Point2d(980, 330),
				StationType.DIAMOND));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(210),
				new Point2d(1030, 850),
				StationType.STAR));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(240),
				new Point2d(350, 280),
				StationType.SQUARE));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(270),
				new Point2d(930, 590),
				StationType.TRIANGLE));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(300),
				new Point2d(670, 350),
				StationType.DIAMOND));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(60),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new TrainScript(TrainType.NORMAL), 1));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(120),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new LineScript(), 1));
				  add(new Pair<>(new StationUpgradeScript(5), 1));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(180),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new PassengerCarScript(), 2));
				  add(new Pair<>(new LineScript(), 1));
				  add(new Pair<>(new TunnelScript(), 2));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(240),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new TrainScript(TrainType.NORMAL), 1));
				  add(new Pair<>(new StationUpgradeScript(5), 1));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(300),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new LineScript(), 1));
				  add(new Pair<>(new PassengerCarScript(), 2));
				  add(new Pair<>(new TunnelScript(), 2));

			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(360),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new StationUpgradeScript(5), 1));
				  add(new Pair<>(new TrainScript(TrainType.NORMAL), 1));
			  }}
			));

			mapScript.initialStationUpgrades = 1;
			mapScript.initialLines = 3;
			mapScript.initialTrains = 3;
			mapScript.initialTunnels = 3;
			this.mapScripts.add(mapScript);
		}
		{
			MultiShape2d<Rectangle2d> multiShape2d = new MultiShape2d<>();
			multiShape2d.add(new Rectangle2d(0, 1010, 380, 50));
			multiShape2d.add(new Rectangle2d(330, 780, 50, 230));
			multiShape2d.add(new Rectangle2d(380, 780, 390, 50));
			multiShape2d.add(new Rectangle2d(720, 450, 50, 330));
			multiShape2d.add(new Rectangle2d(770, 450, 720, 50));
			multiShape2d.add(new Rectangle2d(1490, 450, 50, 270));
			multiShape2d.add(new Rectangle2d(1540, 670, 150, 50));
			multiShape2d.add(new Rectangle2d(1690, 450, 50, 270));
			multiShape2d.add(new Rectangle2d(1740, 450, 180, 50));
			MapScript mapScript = new MapScript("Random map", 1920, 1080, multiShape2d);
			Random random = new Random();
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(0),
				new Point2d(random.nextInt(1720) + 100, random.nextInt(880) + 100),
				StationType.random()));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(0),
				new Point2d(random.nextInt(1720) + 100, random.nextInt(880) + 100),
				StationType.random()));
			mapScript.stationScripts.add(
			  new StationScript(Duration.ofSeconds(0),
				new Point2d(random.nextInt(1720) + 100, random.nextInt(880) + 100),
				StationType.random()));
			for(int i = 1; i < 25; ++i) {
				mapScript.stationScripts.add(
				  new StationScript(Duration.ofSeconds(30 * i),
					new Point2d(random.nextInt(1720) + 100, random.nextInt(880) + 100),
					StationType.random()));
			}
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(60),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new TrainScript(TrainType.NORMAL), 1));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(120),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new PassengerCarScript(), 2));
				  add(new Pair<>(new LineScript(), 1));
				  add(new Pair<>(new TunnelScript(), 2));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(180),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new LineScript(), 1));
				  add(new Pair<>(new StationUpgradeScript(5), 1));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(240),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new LineScript(), 1));
				  add(new Pair<>(new PassengerCarScript(), 2));
				  add(new Pair<>(new TunnelScript(), 2));

			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(300),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new TrainScript(TrainType.NORMAL), 1));
				  add(new Pair<>(new StationUpgradeScript(5), 1));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(360),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new StationUpgradeScript(5), 1));
				  add(new Pair<>(new TrainScript(TrainType.NORMAL), 1));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(420),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new PassengerCarScript(), 2));
				  add(new Pair<>(new LineScript(), 1));
				  add(new Pair<>(new TunnelScript(), 2));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(480),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new LineScript(), 1));
				  add(new Pair<>(new StationUpgradeScript(5), 1));
			  }}
			));
			mapScript.elementChoiceScripts.add(new ElementChoiceScript(
			  Duration.ofSeconds(540),
			  new LinkedList<Pair<ElementScript, Integer>>() {{
				  add(new Pair<>(new StationUpgradeScript(5), 1));
				  add(new Pair<>(new TrainScript(TrainType.NORMAL), 1));
			  }}
			));

			mapScript.initialStationUpgrades = 3;
			mapScript.initialLines = 3;
			mapScript.initialTrains = 4;
			mapScript.initialTunnels = 3;
			this.mapScripts.add(mapScript);
		}

	}

}

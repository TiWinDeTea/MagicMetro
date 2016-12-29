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

package org.tiwindetea.magicmetro.view;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.arakhne.afc.math.geometry.d2.d.MultiShape2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.dfx.MultiShape2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Rectangle2dfx;
import org.tiwindetea.magicmetro.global.TimeManager;
import org.tiwindetea.magicmetro.global.util.Utils;
import org.tiwindetea.magicmetro.model.StationType;
import org.tiwindetea.magicmetro.model.TrainType;

/**
 * ViewManager, manage the view, create graphical elements and display them.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class ViewManager {

	private static final Color BACKGROUND_COLOR = Color.LIGHTGRAY;
	private static final Color MAP_BACKGROUND_COLOR = Color.WHITE;

	private AnchorPane mainAnchorPane = new AnchorPane();
	private final Pane cPane = new Pane();
	private final MapView mapView = new MapView();
	private final ConcreteInventoryView concreteInventoryView;

	private final Skin skin = new Skin();
	private final MultiShape2dfx<Rectangle2dfx> water = new MultiShape2dfx<>();

	private int currentLineNumber = 0;

	public ViewManager() {
		this.mainAnchorPane.setBackground(new Background(new BackgroundFill(
		  BACKGROUND_COLOR,
		  CornerRadii.EMPTY,
		  Insets.EMPTY)));
		this.mainAnchorPane.getChildren().add(this.cPane);
		AnchorPane.setLeftAnchor(this.cPane, 0d);

		// ----- time -----
		Label label = new Label();
		label.setFont(new Font(25));
		AnimationTimer animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				label.setText("Time: " + TimeManager.getInstance().getTimeAsSeconds());
			}
		};
		animationTimer.start();

		Button playButton = new Button("Play");
		playButton.setOnAction(event -> TimeManager.getInstance().start());

		Button pauseButton = new Button("Pause");
		pauseButton.setOnAction(event -> TimeManager.getInstance().pause());

		Label timeSpeedLabel = new Label("Speed: 1.0");

		Slider slider = new Slider();
		slider.setMajorTickUnit(25);
		slider.setMinorTickCount(5);
		slider.setBlockIncrement(1);
		slider.setShowTickMarks(true);
		slider.setMin(0);
		slider.setMax(100);
		slider.setValue(50);
		slider.valueProperty()
		  .addListener((observable, oldValue, newValue) -> {
			  if(newValue.doubleValue() < 50) {
				  double timeSpeed = Utils.map(newValue.doubleValue(), 0, 50, 0.1, 1);
				  TimeManager.getInstance().setSpeed(timeSpeed);
				  timeSpeedLabel.setText("Speed: " + Math.floor(timeSpeed * 100) / 100);
			  }
			  else {
				  double timeSpeed = Utils.map(newValue.doubleValue(), 50, 100, 1, 10);
				  TimeManager.getInstance().setSpeed(timeSpeed);
				  timeSpeedLabel.setText("Speed: " + Math.floor(timeSpeed * 100) / 100);
			  }
		  });

		VBox timeVBox = new VBox();
		AnchorPane.setRightAnchor(timeVBox, 0d);
		timeVBox.getChildren().add(label);
		timeVBox.getChildren().add(playButton);
		timeVBox.getChildren().add(pauseButton);
		timeVBox.getChildren().add(slider);
		timeVBox.getChildren().add(timeSpeedLabel);
		timeVBox.setAlignment(Pos.CENTER_RIGHT);
		timeVBox.setSpacing(5);
		timeVBox.setPadding(new Insets(10));
		this.mainAnchorPane.getChildren().add(timeVBox);

		// ----- inventory and map -----
		this.mapView.setBackgroundColor(MAP_BACKGROUND_COLOR);
		this.cPane.getChildren().add(this.mapView);

		this.concreteInventoryView = new ConcreteInventoryView(this.mapView); //FIXME: cyclic reference, care at deletion
		StackPane inventoryStackPane = new StackPane();
		inventoryStackPane.getChildren().add(this.concreteInventoryView);
		AnchorPane.setRightAnchor(inventoryStackPane, 0d);
		AnchorPane.setLeftAnchor(inventoryStackPane, 0d);
		AnchorPane.setBottomAnchor(inventoryStackPane, 0d);
		this.mainAnchorPane.getChildren().add(inventoryStackPane);

		this.mapView.setInventoryView(this.concreteInventoryView); //FIXME: cyclic reference, care at deletion
	}

	public void setMapSize(double width, double height) {
		Platform.runLater(() -> this.mapView.setWidth(width));
		Platform.runLater(() -> this.mapView.setHeight(height));
	}

	public void setWater(MultiShape2d<Rectangle2d> water) {
		Platform.runLater(() -> this.mapView.setWater(water));
	}

	public TrainView createTrainView(TrainType type) {
		ConcreteTrainView concreteTrainView = new ConcreteTrainView(
		  this.skin.newTrainView(type),
		  Skin.TRAIN_VIEW_WIDTH,
		  Skin.TRAIN_VIEW_HEIGHT,
		  this.skin.getTrainPassengerPositions(),
		  this.skin);
		Platform.runLater(() -> this.mapView.addTrain(concreteTrainView));
		return concreteTrainView;
	}

	public StationView createStationView(StationType type) {
		ConcreteStationView concreteStationView = new ConcreteStationView(
		  this.skin.newStationView(type),
		  Skin.STATION_VIEW_WIDTH,
		  Skin.STATION_VIEW_HEIGHT,
		  this.skin);
		Platform.runLater(() -> this.mapView.addStation(concreteStationView));
		return concreteStationView;
	}

	public LineView createLineView() {
		ConcreteLineView concreteLineView = new ConcreteLineView(this.skin.getLineColor(this.currentLineNumber++));
		this.mapView.addLine(concreteLineView);
		return concreteLineView;
	}

	public Parent getRoot() {
		return this.mainAnchorPane;
	}

	public InventoryView getInventoryView() {
		return this.concreteInventoryView;
	}

}

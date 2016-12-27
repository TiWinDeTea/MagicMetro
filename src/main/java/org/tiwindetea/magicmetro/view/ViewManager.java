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
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.arakhne.afc.math.geometry.d2.d.MultiShape2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.dfx.MultiShape2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Rectangle2dfx;
import org.tiwindetea.magicmetro.global.TimeManager;
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

	private final Skin skin = new Skin();
	private final MultiShape2dfx<Rectangle2dfx> water = new MultiShape2dfx<>();

	public ViewManager() {
		this.mainAnchorPane.setBackground(new Background(new BackgroundFill(
		  BACKGROUND_COLOR,
		  CornerRadii.EMPTY,
		  Insets.EMPTY)));
		this.mainAnchorPane.getChildren().add(this.cPane);
		AnchorPane.setLeftAnchor(this.cPane, 0d);

		this.mapView.setBackgroundColor(MAP_BACKGROUND_COLOR);
		this.cPane.getChildren().add(this.mapView);
	}

	public void setMapSize(double width, double height) {
		this.mapView.setWidth(width);
		this.mapView.setHeight(height);
	}

	public void setWater(MultiShape2d<Rectangle2d> water) {
		this.mapView.setWater(water);
	}

	public TrainView createTrainView(TrainType type) {
		ConcreteTrainView concreteTrainView = new ConcreteTrainView(
		  this.skin.newTrainView(type),
		  Skin.TRAIN_VIEW_WIDTH,
		  Skin.TRAIN_VIEW_HEIGHT,
		  this.skin.getTrainPassengerPositions(),
		  this.skin);
		this.mapView.add(concreteTrainView);
		return concreteTrainView;
	}

	public StationView createStationView(StationType type) {
		ConcreteStationView concreteStationView = new ConcreteStationView(
		  this.skin.newStationView(type),
		  Skin.STATION_VIEW_WIDTH,
		  Skin.STATION_VIEW_HEIGHT,
		  this.skin);
		this.mapView.add(concreteStationView);
		return concreteStationView;
	}

	public void createLineView() {
		//TODO
	}

	public Parent getRoot() {
		return this.mainAnchorPane;
	}

}

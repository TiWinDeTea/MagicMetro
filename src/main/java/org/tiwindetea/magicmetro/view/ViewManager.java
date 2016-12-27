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

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.arakhne.afc.math.geometry.d2.d.MultiShape2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.dfx.MultiShape2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Rectangle2dfx;
import org.tiwindetea.magicmetro.model.StationType;
import org.tiwindetea.magicmetro.model.TrainType;

/**
 * ViewManager, manage the view, create graphical elements and display them.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class ViewManager {

	private static final Color WATER_COLOR = Color.AQUA;

	private AnchorPane mainAnchorPane = new AnchorPane();
	private final Pane cPane = new Pane();
	private final Group waterGroup = new Group();

	private final Skin skin = new Skin();
	private final MultiShape2dfx<Rectangle2dfx> water = new MultiShape2dfx<>();

	public ViewManager() {
		this.mainAnchorPane.getChildren().add(this.cPane);
		AnchorPane.setLeftAnchor(this.cPane, 0d);

		this.cPane.getChildren().add(this.waterGroup);
	}

	public void setWater(MultiShape2d<Rectangle2d> water) {
		this.water.clear();
		this.waterGroup.getChildren().clear();
		for(Rectangle2d rectangle2d : water.getBackendDataList()) {
			Rectangle2dfx rectangle2dfx = new Rectangle2dfx();
			Rectangle rectangle = new Rectangle();
			rectangle.setFill(WATER_COLOR);
			rectangle.widthProperty().bind(rectangle2dfx.widthProperty());
			rectangle.heightProperty().bind(rectangle2dfx.heightProperty());
			rectangle.translateXProperty().bind(rectangle2dfx.minXProperty());
			rectangle.translateYProperty().bind(rectangle2dfx.minYProperty());
			rectangle2dfx.setMinX(rectangle2d.getMinX());
			rectangle2dfx.setMaxX(rectangle2d.getMaxX());
			rectangle2dfx.setMinY(rectangle2d.getMinY());
			rectangle2dfx.setMaxY(rectangle2d.getMaxY());
			this.water.add(rectangle2dfx);
			this.waterGroup.getChildren().add(rectangle);
		}
	}

	public TrainView createTrainView(TrainType type) {
		ConcreteTrainView concreteTrainView = new ConcreteTrainView(
		  this.skin.newTrainView(type),
		  Skin.TRAIN_VIEW_WIDTH,
		  Skin.TRAIN_VIEW_HEIGHT,
		  this.skin.getTrainPassengerPositions(),
		  this.skin);
		this.cPane.getChildren().add(concreteTrainView);
		return concreteTrainView;
	}

	public StationView createStationView(StationType type) {
		ConcreteStationView concreteStationView = new ConcreteStationView(
		  this.skin.newStationView(type),
		  Skin.STATION_VIEW_WIDTH,
		  Skin.STATION_VIEW_HEIGHT,
		  this.skin);
		this.cPane.getChildren().add(concreteStationView);
		return concreteStationView;
	}

	public void createLineView() {
		//TODO
	}

	public Parent getRoot() {
		return this.mainAnchorPane;
	}

}

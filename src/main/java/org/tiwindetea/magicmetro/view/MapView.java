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

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.arakhne.afc.math.geometry.d2.d.MultiShape2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.dfx.MultiShape2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Rectangle2dfx;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Representation of a passenger used by ConcreteTrainView and ConcreteStationView.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class MapView extends DraggableZoomableParent {

	private static final Color WATER_COLOR = Color.AQUA;
	private static final double DEFAULT_WIDTH = 800;
	private static final double DEFAULT_HEIGHT = 600;

	private final Pane mainPane = new Pane();
	private final MultiShape2dfx<Rectangle2dfx> water = new MultiShape2dfx<>(); // for collisions
	private final Collection<Rectangle> waterRectangles = new LinkedList<>();

	public MapView() {
		super();
		this.setWidth(DEFAULT_WIDTH);
		this.setHeight(DEFAULT_HEIGHT);
		this.getChildren().add(this.mainPane);
	}

	public void setWidth(double width) {
		this.mainPane.setMinWidth(width);
		this.mainPane.setMaxWidth(width);
		this.mainPane.setPrefWidth(width);
	}

	public void setHeight(double height) {
		this.mainPane.setMinHeight(height);
		this.mainPane.setMaxHeight(height);
		this.mainPane.setPrefHeight(height);
	}

	public void setWater(MultiShape2d<Rectangle2d> water) {
		this.mainPane.getChildren().removeAll(this.waterRectangles);
		this.waterRectangles.clear();
		this.water.clear();
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
			this.waterRectangles.add(rectangle);
			this.mainPane.getChildren().add(rectangle);
		}
	}

	public void add(Node node) {
		this.mainPane.getChildren().add(node);
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.mainPane.setBackground(new Background(new BackgroundFill(
		  backgroundColor,
		  CornerRadii.EMPTY,
		  Insets.EMPTY)));
	}

}

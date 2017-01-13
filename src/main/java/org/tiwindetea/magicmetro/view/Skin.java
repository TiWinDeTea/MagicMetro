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

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.model.StationType;
import org.tiwindetea.magicmetro.model.TrainType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Skin, give graphical element to the viewManager.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class Skin implements PassengerViewFactory {

	//FIXME: temporary test implementation

	public static final int STATION_VIEW_WIDTH = 40;
	public static final int STATION_VIEW_HEIGHT = 40;
	public static final int TRAIN_VIEW_WIDTH = 75;
	public static final int TRAIN_VIEW_HEIGHT = 40;
	public static final int PASSENGERCAR_VIEW_WIDTH = 60;
	public static final int PASSENGERCAR_VIEW_HEIGHT = 40;
	public static final int STATION_UPGRADE_VIEW_WIDTH = 40;
	public static final int STATION_UPGRADE_VIEW_HEIGHT = 40;

	private static final Color PASSENGERS_COLOR = Color.GRAY;
	private static final Color STATIONS_COLOR1 = Color.DARKGRAY;
	private static final Color STATIONS_COLOR2 = Color.BLACK;
	private static final Color ERROR_COLOR = Color.BLACK;
	private static final List<Color> LINES_COLORS = new ArrayList<Color>() {{
		add(Color.RED);
		add(Color.BLUE);
		add(Color.YELLOW);
		add(Color.GREEN);
		add(Color.PINK);
		add(Color.CYAN);
		add(Color.MAROON);
		add(Color.PURPLE);
		add(Color.LIME);
	}};
	private static final List<Point2d> TRAIN_PASSENGERS_POSITIONS = new ArrayList<Point2d>() {{
		add(new Point2d(0, 0));
		add(new Point2d(20, 0));
		add(new Point2d(40, 0));
		add(new Point2d(0, 20));
		add(new Point2d(20, 20));
		add(new Point2d(40, 20));
	}};

	@Override
	@Nonnull
	public Shape newPassengerView(@Nonnull StationType wantedStation) {
		switch(wantedStation) {
		case SQUARE: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  0d, 0d,
			  20d, 0d,
			  20d, 20d,
			  0d, 20d
			);
			polygon.setFill(PASSENGERS_COLOR);
			return polygon;
		}
		case TRIANGLE: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  10d, 0d,
			  20d, 20d,
			  0d, 20d
			);
			polygon.setFill(PASSENGERS_COLOR);
			return polygon;
		}
		case CIRCLE: {
			Circle circle = new Circle(10);
			circle.setLayoutX(10);
			circle.setLayoutY(10);
			circle.setFill(PASSENGERS_COLOR);
			return circle;
		}
		case STAR: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  10d, 0d,
			  12.245, 6.91,
			  19.51, 6.91,
			  13.635, 11.18,
			  15.88, 18.09,
			  10d, 13.82,
			  4.12, 18.09,
			  6.865, 11.16,
			  0.49, 6.91,
			  7.795, 6.795
			);
			polygon.setFill(PASSENGERS_COLOR);
			return polygon;
		}
		case DIAMOND: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  10d, 0d,
			  20d, 10d,
			  10d, 20d,
			  0d, 10d
			);
			polygon.setFill(PASSENGERS_COLOR);
			return polygon;
		}
		case CROSS: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  6.6665, 0d,
			  13.333, 0d,
			  13.333, 6.6665,
			  20d, 6.6665,
			  20d, 13.333,
			  13.333, 13.333d,
			  13.333, 20d,
			  6.6665, 20d,
			  6.6665, 13.333,
			  0d, 13.333,
			  0d, 6.6665,
			  6.6665, 6.6665
			);
			polygon.setFill(PASSENGERS_COLOR);
			return polygon;
		}
		default: // return undefined shape
			break;
		}
		//TODO
		Circle circle = new Circle(10);
		circle.setFill(ERROR_COLOR);
		return circle;
	}

	@Nonnull
	public Shape newStationView(@Nonnull StationType type) {
		switch(type) {
		case SQUARE: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  0d, 0d,
			  40d, 0d,
			  40d, 40d,
			  0d, 40d
			);
			polygon.setFill(STATIONS_COLOR1);
			polygon.setStroke(STATIONS_COLOR2);
			polygon.setStrokeWidth(7);
			return polygon;
		}
		case TRIANGLE: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  20d, 0d,
			  40d, 40d,
			  0d, 40d
			);
			polygon.setFill(STATIONS_COLOR1);
			polygon.setStroke(STATIONS_COLOR2);
			polygon.setStrokeWidth(7);
			return polygon;
		}
		case CIRCLE: {
			Circle circle = new Circle(20);
			circle.setLayoutX(20);
			circle.setLayoutY(20);
			circle.setFill(STATIONS_COLOR1);
			circle.setStroke(STATIONS_COLOR2);
			circle.setStrokeWidth(7);
			return circle;
		}
		case STAR: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  20d, 0d,
			  24.49, 13.82,
			  39.02, 13.82,
			  27.27, 22.36,
			  31.76, 36.18,
			  20d, 27.64,
			  8.24, 36.18,
			  12.73, 22.36,
			  0.98, 13.82,
			  15.59, 13.59
			);
			polygon.setFill(STATIONS_COLOR1);
			polygon.setStroke(STATIONS_COLOR2);
			polygon.setStrokeWidth(4);
			return polygon;
		}
		case DIAMOND: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  20d, 0d,
			  40d, 20d,
			  20d, 40d,
			  0d, 20d
			);
			polygon.setFill(STATIONS_COLOR1);
			polygon.setStroke(STATIONS_COLOR2);
			polygon.setStrokeWidth(7);
			return polygon;
		}
		case CROSS: {
			Polygon polygon = new Polygon();
			polygon.getPoints().addAll(
			  13.333, 0d,
			  26.666, 0d,
			  26.666, 13.333,
			  40d, 13.333,
			  40d, 26.666,
			  26.666, 26.666d,
			  26.666, 40d,
			  13.333, 40d,
			  13.333, 26.666,
			  0d, 26.666,
			  0d, 13.333,
			  13.333, 13.333
			);
			polygon.setFill(STATIONS_COLOR1);
			polygon.setStroke(STATIONS_COLOR2);
			polygon.setStrokeWidth(7);
			return polygon;
		}
		default: // return undefined shape
			break;
		}
		//TODO
		Circle circle = new Circle(20);
		circle.setFill(ERROR_COLOR);
		return circle;
	}

	@Nonnull
	public Shape newTrainView(@Nonnull TrainType type) {
		//TODO: switch type
		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(
		  0d, 0d,
		  60d, 0d,
		  75d, 20d,
		  60d, 40d,
		  0d, 40d
		);
		polygon.setFill(ERROR_COLOR);
		return polygon;
	}

	@Nonnull
	public Shape newPassengerCarView() {
		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(
		  0d, 0d,
		  60d, 0d,
		  60d, 40d,
		  0d, 40d
		);
		polygon.setFill(ERROR_COLOR);
		return polygon;
	}

	@Nonnull
	public Color getLineColor(int lineNumber) {
		if(lineNumber < LINES_COLORS.size()) {
			return LINES_COLORS.get(lineNumber);
		}
		return ERROR_COLOR;
	}

	@Nonnull
	public List<Point2d> getTrainPassengerPositions() {
		return TRAIN_PASSENGERS_POSITIONS;
	}

	@Nonnull
	public List<Point2d> getPassengerCarPassengerPositions() {
		return TRAIN_PASSENGERS_POSITIONS;
	}

	public Shape newStationUpgradeView() {
		Circle circle = new Circle(20);
		circle.setFill(ERROR_COLOR);
		return circle;
	}

}

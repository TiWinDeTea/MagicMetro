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

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import org.arakhne.afc.math.geometry.d2.dfx.MultiShape2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Point2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Rectangle2dfx;
import org.arakhne.afc.math.geometry.d2.dfx.Segment2dfx;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * The view of a line section.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class SectionView extends Parent {

	private static final double STROKE_WIDTH = 20d;
	private static final double HOOK_LENGTH = 30d;
	private static final double HOOK_WIDTH = 50d;
	private static final ArrayList<Double> LINE_DOTS = new ArrayList<Double>() {{
		add(15d);
		add(30d);
	}};

	private final ConcreteLineView line;

	private ConcreteStationView fromStation = null;
	private ConcreteStationView toStation = null;
	private SectionView prevSection = null;
	private SectionView nextSection = null;

	private MultiShape2dfx<Rectangle2dfx> water = new MultiShape2dfx<>(); // for collisions
	private boolean intersectWater = false;
	private Polyline polyline;
	private Point2dfx a;
	private Point2dfx b;
	private Point2dfx c;
	private Segment2dfx ab;
	private Segment2dfx bc;

	private final Group fromHook = new Group();
	private final Group toHook = new Group();

	private SectionMouseListener sectionMouseListener = null;

	private final EventHandler<MouseEvent> onFromHookMousePressed = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			SectionView.this.sectionMouseListener.mousePressedOnSectionHook(SectionView.this,
			  SectionView.this.fromStation);
		}
	};

	private final EventHandler<MouseEvent> onToHookMousePressed = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			SectionView.this.sectionMouseListener.mousePressedOnSectionHook(SectionView.this,
			  SectionView.this.toStation);
		}
	};

	private void updatePoints() {
		if(Math.abs(this.c.getX() - this.a.getX()) > Math.abs(this.c.getY() - this.a.getY())) {
			this.b.setX(this.a.getX()
			  + (this.c.getX() - this.a.getX())
			  - (this.c.getX() > this.a.getX() ? 1 : -1) * Math.abs(this.c.getY() - this.a.getY()));
			this.b.setY(this.a.getY());
		}
		else {
			this.b.setX(this.a.getX());
			this.b.setY(this.a.getY()
			  + (this.c.getY() - this.a.getY())
			  - (this.c.getY() > this.a.getY() ? 1 : -1) * Math.abs(this.c.getX() - this.a.getX()));
		}
	}

	private void updateWaterIntersection() {
		this.ab.set(this.a, this.b);
		this.bc.set(this.b, this.c);
		if(this.ab.intersects(this.water) || this.bc.intersects(this.water)) {
			this.intersectWater = true;
			this.polyline.getStrokeDashArray().addAll(LINE_DOTS);
		}
		else {
			this.intersectWater = false;
			this.polyline.getStrokeDashArray().clear();
		}
	}

	private void updateHooks() {
		if(this.fromHook.isVisible()) {
			double angle = Math.toDegrees(Math.atan2(
			  this.a.getY() - this.b.getY(),
			  this.a.getX() - this.b.getX()));
			this.fromHook.getTransforms().clear();
			this.fromHook.getTransforms().add(new Rotate(angle, 0, HOOK_WIDTH / 2));
			this.fromHook.setTranslateX(this.a.getX());
			this.fromHook.setTranslateY(this.a.getY());
		}
		if(this.toHook.isVisible()) {
			double angle = Math.toDegrees(Math.atan2(
			  this.c.getY() - this.b.getY(),
			  this.c.getX() - this.b.getX()));
			this.toHook.getTransforms().clear();
			this.toHook.getTransforms().add(new Rotate(angle, 0, HOOK_WIDTH / 2));
			this.toHook.setTranslateX(this.c.getX());
			this.toHook.setTranslateY(this.c.getY());
		}
	}

	/**
	 * Instantiates a new SectionView.
	 *
	 * @param line  the line
	 * @param fromX the x coordinate of section start point
	 * @param fromY the y coordinate of section start point
	 * @param toX   the x coordinate of section end point
	 * @param toY   the y coordinate of section end point
	 */
	public SectionView(ConcreteLineView line, double fromX, double fromY, double toX, double toY) {
		this.line = line;
		this.polyline = new Polyline();
		this.polyline.getPoints().add(fromX);
		this.polyline.getPoints().add(fromY);
		this.polyline.getPoints().add(fromX);
		this.polyline.getPoints().add(fromY);
		this.polyline.getPoints().add(toX);
		this.polyline.getPoints().add(toY);
		this.a = new Point2dfx();
		this.a.xProperty().addListener(((observable, oldValue, newValue) ->
		  this.polyline.getPoints().set(0, newValue.doubleValue())));
		this.a.yProperty().addListener(((observable, oldValue, newValue) ->
		  this.polyline.getPoints().set(1, newValue.doubleValue())));
		this.b = new Point2dfx();
		this.b.xProperty().addListener(((observable, oldValue, newValue) ->
		  this.polyline.getPoints().set(2, newValue.doubleValue())));
		this.b.yProperty().addListener(((observable, oldValue, newValue) ->
		  this.polyline.getPoints().set(3, newValue.doubleValue())));
		this.c = new Point2dfx();
		this.c.xProperty().addListener(((observable, oldValue, newValue) ->
		  this.polyline.getPoints().set(4, newValue.doubleValue())));
		this.c.yProperty().addListener(((observable, oldValue, newValue) ->
		  this.polyline.getPoints().set(5, newValue.doubleValue())));
		this.a.set(fromX, fromY);
		this.b.set(fromX, fromY);
		this.c.set(toX, toY);
		this.ab = new Segment2dfx(this.a, this.b);
		this.bc = new Segment2dfx(this.b, this.c);

		this.polyline.setStroke(this.line.color);
		this.polyline.setStrokeWidth(STROKE_WIDTH);
		this.getChildren().add(this.polyline);

		this.updatePoints();
		this.updateWaterIntersection();

		this.polyline.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(SectionView.this.sectionMouseListener != null) {
					SectionView.this.sectionMouseListener.mousePressedOnSection(SectionView.this, event.getX(), event.getY());
				}
			}
		});

		Rectangle fromRectangle1 = new Rectangle(HOOK_LENGTH, STROKE_WIDTH);
		fromRectangle1.setFill(this.line.color);
		fromRectangle1.setTranslateY((HOOK_WIDTH - STROKE_WIDTH) / 2);
		fromRectangle1.setOnMousePressed(this.onFromHookMousePressed);

		Rectangle fromRectangle2 = new Rectangle(STROKE_WIDTH, HOOK_WIDTH);
		fromRectangle2.setFill(this.line.color);
		fromRectangle2.setTranslateX(HOOK_LENGTH);
		fromRectangle2.setOnMousePressed(this.onFromHookMousePressed);

		this.fromHook.setLayoutY(-fromRectangle2.getHeight() / 2);
		this.fromHook.getChildren().add(fromRectangle1);
		this.fromHook.getChildren().add(fromRectangle2);
		this.fromHook.setVisible(false);
		this.getChildren().add(this.fromHook);

		Rectangle toRectangle1 = new Rectangle(HOOK_LENGTH, STROKE_WIDTH);
		toRectangle1.setFill(this.line.color);
		toRectangle1.setTranslateY((HOOK_WIDTH - STROKE_WIDTH) / 2);
		toRectangle1.setOnMousePressed(this.onToHookMousePressed);

		Rectangle toRectangle2 = new Rectangle(STROKE_WIDTH, HOOK_WIDTH);
		toRectangle2.setFill(this.line.color);
		toRectangle2.setTranslateX(HOOK_LENGTH);
		toRectangle2.setOnMousePressed(this.onToHookMousePressed);

		this.toHook.setLayoutY(-toRectangle2.getHeight() / 2);
		this.toHook.getChildren().add(toRectangle1);
		this.toHook.getChildren().add(toRectangle2);
		this.toHook.setVisible(false);
		this.getChildren().add(this.toHook);

		this.updateHooks();
	}

	/**
	 * Sets the hook at the section start point visibility.
	 *
	 * @param visible the visibility
	 */
	public void setFromHookVisible(boolean visible) {
		this.fromHook.setVisible(visible);
		updateHooks();
	}

	/**
	 * Sets the hook at the section end point visibility.
	 *
	 * @param visible the visibility
	 */
	public void setToHookVisible(boolean visible) {
		this.toHook.setVisible(visible);
		updateHooks();
	}

	/**
	 * Instantiates a new SectionView.
	 *
	 * @param line the line
	 * @param from the section start point
	 * @param to   the section end point
	 */
	public SectionView(ConcreteLineView line, Point2dfx from, Point2dfx to) {
		this(line, from.getX(), from.getY(), to.getX(), to.getY());
	}

	/**
	 * Instantiates a new SectionView.
	 *
	 * @param line  the line
	 * @param initX the x coordinate of section start and end point
	 * @param initY the y coordinate of section start and end point
	 */
	public SectionView(ConcreteLineView line, double initX, double initY) {
		this(line, initX, initY, initX, initY);
	}

	/**
	 * Sets the section start point.
	 *
	 * @param x the x coordinate of section start point
	 * @param y the y coordinate of section start point
	 */
	public void setFrom(double x, double y) {
		this.a.xProperty().setValue(x);
		this.a.yProperty().setValue(y);
		this.updatePoints();
		this.updateWaterIntersection();
		this.updateHooks();
	}

	/**
	 * Sets the section start point.
	 *
	 * @param from the section start point
	 */
	public void setFrom(Point2dfx from) {
		this.setFrom(from.getX(), from.getY());
	}

	/**
	 * Gets the section start point.
	 *
	 * @return the section start point
	 */
	public Point2dfx getFrom() {
		return this.a.clone();
	}

	/**
	 * Gets the section middle point.
	 *
	 * @return the section middle point
	 */
	public Point2dfx getMiddle() {
		return this.c.clone();
	}

	/**
	 * Sets the section end point.
	 *
	 * @param x the x coordinate of section end point
	 * @param y the y coordinate of section end point
	 */
	public void setTo(double x, double y) {
		this.c.xProperty().setValue(x);
		this.c.yProperty().setValue(y);
		this.updatePoints();
		this.updateWaterIntersection();
		this.updateHooks();
	}

	/**
	 * Sets the section end point.
	 *
	 * @param to the the section end point
	 */
	public void setTo(Point2dfx to) {
		this.setTo(to.getX(), to.getY());
	}

	/**
	 * Gets the section end point.
	 *
	 * @return the the section end point
	 */
	public Point2dfx getTo() {
		return this.c.clone();
	}

	/**
	 * Sets water for collisions.
	 *
	 * @param water the water
	 */
	public void setWater(@Nonnull MultiShape2dfx<Rectangle2dfx> water) {
		this.water = water;
		this.updateWaterIntersection();
	}

	/**
	 * Determine if the section intersect the water.
	 *
	 * @return the boolean
	 */
	public boolean intersectWater() {
		return this.intersectWater;
	}

	/**
	 * Sets from station.
	 *
	 * @param fromStation the from station
	 */
	public void setFromStation(@Nullable ConcreteStationView fromStation) {
		this.fromStation = fromStation;
	}

	/**
	 * Gets from station.
	 *
	 * @return the from station
	 */
	@Nullable
	public ConcreteStationView getFromStation() {
		return this.fromStation;
	}

	/**
	 * Sets to station.
	 *
	 * @param toStation the to station
	 */
	public void setToStation(@Nullable ConcreteStationView toStation) {
		this.toStation = toStation;
	}

	/**
	 * Gets to station.
	 *
	 * @return the to station
	 */
	@Nullable
	public ConcreteStationView getToStation() {
		return this.toStation;
	}

	/**
	 * Sets previous section.
	 *
	 * @param prevSection the previous section
	 */
	public void setPrevSection(@Nullable SectionView prevSection) {
		this.prevSection = prevSection;
	}

	/**
	 * Gets previous section.
	 *
	 * @return the previous section
	 */
	@Nullable
	public SectionView getPrevSection() {
		return this.prevSection;
	}

	/**
	 * Sets next section.
	 *
	 * @param nextSection the next section
	 */
	public void setNextSection(@Nullable SectionView nextSection) {
		this.nextSection = nextSection;
	}

	/**
	 * Gets next section.
	 *
	 * @return the next section
	 */
	@Nullable
	public SectionView getNextSection() {
		return this.nextSection;
	}

	/**
	 * Gets line.
	 *
	 * @return the line
	 */
	public ConcreteLineView getLine() {
		return this.line;
	}

	/**
	 * Sets section mouse listener.
	 *
	 * @param sectionMouseListener the section mouse listener
	 */
	public void setSectionMouseListener(SectionMouseListener sectionMouseListener) {
		this.sectionMouseListener = sectionMouseListener;
	}

}

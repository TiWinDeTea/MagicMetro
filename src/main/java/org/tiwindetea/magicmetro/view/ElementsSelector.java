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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.tiwindetea.magicmetro.global.eventdispatcher.EventDispatcher;
import org.tiwindetea.magicmetro.global.eventdispatcher.events.InventoryElementAdditionEvent;
import org.tiwindetea.magicmetro.global.scripts.ElementScript;
import org.tiwindetea.magicmetro.global.util.Pair;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * ElementsSelector, allow the user to choose a element to add to his inventory.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class ElementsSelector extends StackPane {

	private static final Font NUMBER_FONT = new Font(30);
	private static final Font NAME_FONT = new Font(25);
	private static final double HORIZONTAL_SPACING = 100;
	private static final double VERTICAL_SPACING = 15;
	private static final double PICTURE_WIDTH = 150;
	private static final double PICTURE_HEIGHT = 150;
	private static final double PICTURE_ZOOM_FACTOR = 1.25;

	public ElementsSelector(@Nonnull List<Pair<ElementScript, Integer>> elementScripts,
	                        @Nonnull MenuListener elementsSelectorListener) {

		Rectangle rectangle = new Rectangle();
		rectangle.widthProperty().bind(this.widthProperty());
		rectangle.heightProperty().bind(this.heightProperty());
		rectangle.setFill(Color.GRAY);
		rectangle.setOpacity(0.5);
		this.getChildren().add(rectangle);

		HBox hBox = new HBox();
		for(Pair<ElementScript, Integer> elementScript : elementScripts) {
			Image image = null;
			switch(elementScript.getLeft().type) {
			case TRAIN:
				image = ViewSettings.TRAIN_IMAGE;
				break;
			case STATION_UPGRADE:
				image = ViewSettings.STATION_UPGRADE_IMAGE;
				break;
			case TUNNEL:
				image = ViewSettings.TUNNEL_IMAGE;
				break;
			case LINE:
				image = ViewSettings.LINE_IMAGE;
				break;
			case PASSENGER_CAR:
				image = ViewSettings.PASSENGER_CAR_IMAGE;
				break;
			}
			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(PICTURE_WIDTH);
			imageView.setFitHeight(PICTURE_HEIGHT);
			Label numberLabel = new Label(elementScript.getRight().toString());
			numberLabel.setFont(NUMBER_FONT);
			Label nameLabel = new Label(elementScript.getLeft().type.toString());
			nameLabel.setFont(NAME_FONT);
			VBox vBox = new VBox();
			vBox.getChildren().addAll(imageView, nameLabel, numberLabel);
			vBox.setAlignment(Pos.CENTER);
			vBox.setSpacing(VERTICAL_SPACING);
			vBox.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					EventDispatcher.getInstance()
					  .fire(new InventoryElementAdditionEvent(elementScript.getLeft(), elementScript.getRight()));
					elementsSelectorListener.closeMenu(ElementsSelector.this);
				}
			});
			vBox.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					imageView.setScaleX(PICTURE_ZOOM_FACTOR);
					imageView.setScaleY(PICTURE_ZOOM_FACTOR);
				}
			});
			vBox.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					imageView.setScaleX(1);
					imageView.setScaleY(1);
				}
			});
			hBox.getChildren().addAll(vBox);
		}
		hBox.setAlignment(Pos.CENTER);
		hBox.setSpacing(HORIZONTAL_SPACING);
		this.getChildren().add(hBox);
	}

}

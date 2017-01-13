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

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.annotation.Nonnull;

/**
 * ExitMenu, allow the user to exit the game.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class ExitMenu extends StackPane {

	public ExitMenu(@Nonnull MenuListener menuListener) {
		Rectangle rectangle = new Rectangle();
		rectangle.widthProperty().bind(this.widthProperty());
		rectangle.heightProperty().bind(this.heightProperty());
		rectangle.setFill(Color.GRAY);
		rectangle.setOpacity(0.5);
		this.getChildren().add(rectangle);

		Button continueButton = new Button("continue");
		continueButton.setOnAction((event) -> {
			menuListener.closeMenu(this);
		});
		Button exitButton = new Button("exit game");
		exitButton.setOnAction((event) -> {
			menuListener.exitGame();
		});
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(40);
		vBox.getChildren().addAll(continueButton, exitButton);
		this.getChildren().add(vBox);
		requestLayout();
		setAlignment(Pos.CENTER);
	}

}

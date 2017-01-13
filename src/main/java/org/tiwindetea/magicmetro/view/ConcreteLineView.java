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

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.tiwindetea.magicmetro.global.IdGenerator;

import java.util.LinkedList;
import java.util.List;

/**
 * A JavaFx implementation of LineView.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class ConcreteLineView extends Parent implements LineView {

	public final int gameId = IdGenerator.newId();
	public final Color color;

	private List<SectionView> sections = new LinkedList<>();

	/**
	 * Instantiates a new Concrete line view.
	 *
	 * @param color the color
	 */
	public ConcreteLineView(Color color) {
		this.color = color;
	}

	/**
	 * Add section.
	 *
	 * @param section the section
	 */
	public void addSection(SectionView section) {
		this.sections.add(section);
		this.getChildren().add(section);
	}

	/**
	 * Remove section.
	 *
	 * @param section the section
	 */
	public void removeSection(SectionView section) {
		this.sections.remove(section);
		this.getChildren().remove(section);
	}

	@Override
	public int getGameId() {
		return this.gameId;
	}

	@Override
	public void deleteSection(int sectionId) {
		for(SectionView section : this.sections) {
			if(section.gameId == sectionId) {
				FadeTransition fadeTransition = new FadeTransition(Duration.seconds(7), section);
				fadeTransition.setFromValue(0.6);
				fadeTransition.setToValue(0);
				fadeTransition.play();
				fadeTransition.setOnFinished(event -> {
					ConcreteLineView.this.getChildren().remove(section);
					fadeTransition.setNode(null);
				});
				this.sections.remove(section);
				break;
			}
		}
	}

}

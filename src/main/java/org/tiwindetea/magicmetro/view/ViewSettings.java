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

import javafx.scene.image.Image;

/**
 * Settings of the view.
 *
 * @author Maxime PINARD
 * @since 0.1
 */
public class ViewSettings {
	//TODO: externalise in resource bundle
	public static final Image LINE_IMAGE = new Image(ConcreteInventoryView.class.getResource(
	  "InventoryLine.png").toString());
	public static final Image STATION_UPGRADE_IMAGE = new Image(ConcreteInventoryView.class.getResource(
	  "InventoryStationUpgrade.png").toString());
	public static final Image PASSENGER_CAR_IMAGE = new Image(ConcreteInventoryView.class.getResource(
	  "InventoryPassengerCar.png").toString());
	public static final Image TRAIN_IMAGE = new Image(ConcreteInventoryView.class.getResource(
	  "InventoryTrain.png").toString());
	public static final Image TUNNEL_IMAGE = new Image(ConcreteInventoryView.class.getResource(
	  "InventoryTunnel.png").toString());
}

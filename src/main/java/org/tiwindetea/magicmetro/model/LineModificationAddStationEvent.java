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

package org.tiwindetea.magicmetro.model;

import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.global.eventdispatcher.Event;

/**
 * Created by Julien Barbier on 02/01/2017.
 */

/**
 * Event for modification of Line
 * @author Julien Barbier
 * @since 0.1
 */
public class LineModificationAddStationEvent implements Event{

    public final int idLine;
    public final int idStationA;
    public final int idStationB;
    public final int idStationC;
    public final Point2d aToC;
    public final Point2d bToC;

    public LineModificationAddStationEvent(int idLine, int idStationA, int idStationB, int idStationC, Point2d aToC, Point2d bToC){
        this.idLine = idLine;
        this.idStationA = idStationA;
        this.idStationB = idStationB;
        this.idStationC = idStationC;
        this.aToC = aToC;
        this.bToC = bToC;
    }
}

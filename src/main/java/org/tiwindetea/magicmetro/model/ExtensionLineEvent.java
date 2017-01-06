package org.tiwindetea.magicmetro.model;

import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.tiwindetea.magicmetro.global.eventdispatcher.Event;

/**
 * Created by Julien Barbier on 05/01/2017.
 */
public class ExtensionLineEvent implements Event{
    public int idStationA;
    public int idStationExtension;
    public Point2d connectionSectionMiddle;
    public int idLine;

    public ExtensionLineEvent(int idStationA, int idStationExtension, Point2d connectionSectionMiddle, int idLine) {
        this.idStationA = idStationA;
        this.idStationExtension = idStationExtension;
        this.connectionSectionMiddle = connectionSectionMiddle;
        this.idLine = idLine;
    }
}

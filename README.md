# MagicMetro

## What is this
A UTBM school project for LO43.

Unfinished clone of [MiniMetro](http://dinopoloclub.com/minimetro/).

## Build
The project use Apache Maven to build *jar* file, with and without dependencies:

To compile use the following command:

``$ mvn package``

The following dependencies will be downloaded:
- jsr305 3.0.1 (com.google.code.findbugs)
- junit 4.4 (junit)
- math 13.0 (org.arakhne.afc.core)
- mathfx 13.0 (org.arakhne.afc.advanced)

2 *jar* will be created in ``./target``:
- MagicMetro.jar
- MagicMetro-jar-with-dependencies.jar

## Copyright

This work is under the MIT License

[Read the license file](LICENSE)

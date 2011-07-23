---
layout: doc
version: 1.0.x
title: Input
---
# Input

Bei der Erstellung eines neuen Projekts k&ouml;nnen optional Daten importiert werden, die normalerweise bereits in Form von Tabellen vorliegen und ansonsten m&uuml;hsam von Hand eingetippt werden m&uuml;ssten. Diese werden als Input bezeichnet.

Alle Input-Dateien m&uuml;ssen in dem Ordner liegen, der bei der Erstellung des Projekts angegeben wird. Sie werden genau einmal importiert und k&ouml;nnen danach wieder entfernt werden. Es ist nicht m&ouml;glich, im Nachhinein Daten zu importieren. Wenn Sie keine Daten importieren wollen, k&ouml;nnen Sie den Input-Pfad bei der Erstellung des Projekts leer lassen.

## buerger.csv

Die wichtigere von beiden Input-Dateien ist die `buerger.csv`. Sie werden normalerweise vom Sekretariat der Schule eine Excel-Tabelle mit Vorname, Nachname und Klasse jedes Sch&uuml;lers bzw. Vorname und Nachname jedes Lehrers erhalten. F&uuml;gen Sie diese in einer Tabelle zusammen und erstellen Sie zus&auml;tzlich die Spalte "Betriebsnummer". Das sieht dann wie folgt aus:

![Bild der Tabelle](http://img199.imageshack.us/img199/8954/buergercsv.png)

F&uuml;r die fertige Tabelle gelten folgende Regeln:

1. Jede Zelle au&szlig;er dem "Betriebsnummer"-Feld muss einen Wert enthalten.
2. Die Spalten "Betriebsnummer" und "Nummer" m&uuml;ssen positive ganze Zahlen enthalten.
3. Die erste Zeile der Tabelle wird beim Einlesen nicht ber&uuml;cksichtigt, sie sollte deshalb
keine Daten, sondern Spalten&uuml;berschriften enthalten. Die Spalten&uuml;berschriften k&ouml;nnen
beliebig gew&auml;hlt werden. Alle anderen Zeilen m&uuml;ssen Daten enthalten.
4. Die Spalte "Nummer" wird beim Einlesen nicht ber&uuml;cksichtigt; die B&uuml;rger werden von
Nummer 1 ab durchnummeriert. Sie darf aber trotzdem nicht fehlen!
5. Keine Zelle darf Hochkommata (`"`) enthalten.
6. Sie Spalten m&uuml;ssen in der Reihenfolge angeordnet sein, wie auf dem Bild zu sehen.

Speichern Sie nun diese Tabelle unter dem Namen `buerger.csv` als CSV-Datei wie im [CSV-Tutorial](Tut_CSV.html) beschrieben und legen Sie sie in den Input-Ordner.

## waren.csv

Mit dieser Tabelle k&ouml;nnen sie Daten &uuml;ber die Waren, die Sie w&auml;hrend des Projekts im Warenlager anbieten, importieren lassen. Ihre Warentabelle muss dabei wie folgt aufgebaut sein:

![Bild](http://img146.imageshack.us/img146/6618/warencsv.png)

Die Bedeutung der Spalten "Paketbeschreibung", "Paketgr&ouml;&szlig;e" und "Paketeinheit" werden im [entsprechenden Tutorial](WL_Units.html) erl&auml;utert.

F&uuml;r die Tabelle gelten folgende Regeln:

1. Jedes Feld muss einen Wert enthalten.
2. Die Spalte "Nummer" muss positive ganze Zahlen enthalten.
3. Die Spalten "Preis in Euro", "Paketgr&ouml;&szlig;e" und "Preis im Staat" m&uuml;ssen positive Dezimalzahlen enthalten.
4. Die erste Zeile der Tabelle wird beim Einlesen nicht ber&uuml;cksichtigt, sie sollte deshalb
keine Daten, sondern Spalten&uuml;berschriften enthalten. Die Spalten&uuml;berschriften k&ouml;nnen
beliebig gew&auml;hlt werden. Alle anderen Zeilen m&uuml;ssen Daten enthalten.
5. Die Spalte "Nummer" wird beim Einlesen nicht ber&uuml;cksichtigt; die B&uuml;rger werden von
Nummer 1 ab durchnummeriert. Sie darf aber trotzdem nicht fehlen!
6. Keine Zelle darf Hochkommata (`"`) enthalten.
7. Sie Spalten m&uuml;ssen in der Reihenfolge angeordnet sein, wie auf dem Bild zu sehen.

Speichern Sie nun diese Tabelle unter dem Namen `waren.csv` als CSV-Datei wie im [CSV-Tutorial](Tut_CSV.html) beschrieben und legen Sie sie in den Input-Ordner. Wenn keine Warendaten importiert werden sollen, erstellen Sie einfach eine leere Tabelle, die nur Spalten&uuml;berschriften enth&auml;lt.

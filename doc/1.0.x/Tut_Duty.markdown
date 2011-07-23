---
layout: doc
version: 1.0.x
title: Zoll
---
# Zoll

![Bild der Benutzeroberfl&auml;che](http://img718.imageshack.us/img718/1675/adminclientduty.png)

Beim Zoll wird registriert, wann jeder B&uuml;rger den Staat betritt und verl&auml;sst. Diese Informationen k&ouml;nnen Sie der Tabelle entnehmen:

* **B&uuml;rger-ID:** Die Nummer des B&uuml;rgers, der den Staat betreten/verlassen hat.  
* **Datum:** Das Datum, an dem er ihn betreten/verlassen hat.  
* **Uhrzeit:** Die Uhrzeit, zu der er ihn betreten/verlassen hat.  
* **Typ:** Ob er ihn betreten oder verlassen hat. "login" steht f&uuml;r Betreten, "logout" f&uuml;r Verlassen.

F&uuml;r diese Benutzeroberfl&auml;che sind die zwei Filter "ID" und "Datum" besonders wichtig. Siehe das [Filter-Tutorial](Tut_Filtering.html).  
Im Zollbereich k&ouml;nnen Sie keine Daten ver&auml;ndern.

## Nicht ausgeloggte B&uuml;rger exportieren

&Uuml;ber diese Schaltfl&auml;che k&ouml;nnen Sie eine Datei erstellen, die alle B&uuml;rger auflistet, die sich an einem Tag des Projekts nicht ausgeloggt haben. Das weist darauf hin, dass diese B&uuml;rger versucht haben, das Zoll-System zu umgehen.

Geben Sie hierzu wenn Sie gefragt werden zun&auml;chst eine Datei mit der Endung `.csv` an, in der die Daten gespeichert werden sollen. Importieren Sie danach die erstellte Liste beispielsweise in OpenOffice/LibreOffice, wie im [CSV-Tutorial](Tut_CSV.html) beschrieben.

## B&uuml;rger exportieren, die nicht gen&uuml;gend Zeit im Staat verbracht haben
&Uuml;ber diese Schaltfl&auml;che k&ouml;nnen Sie eine Datei erstellen, die alle B&uuml;rger auflistet, die nicht eine bestimmte Mindestanwesenheitszeit im Staat verbracht haben.

Geben Sie hierzu wenn Sie gefragt werden zun&auml;chst die Zeit an, die jeder B&uuml;rger mindestens im Staat verbringen muss (in Minuten). Danach m&uuml;ssen Sie noch eine Datei mit der Endung `.csv` angeben, in der die Liste gespeichert werden soll. Importieren Sie anschlie&szlig;end die erstellte Liste beispielsweise in OpenOffice/LibreOffice, wie im [CSV-Tutorial](Tut_CSV.html) beschrieben.

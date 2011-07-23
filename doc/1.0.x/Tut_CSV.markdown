---
layout: doc
version: 1.0.x
title: Umgang mit CSV-Dateien
---
# Umgang mit CSV-Dateien

Dateien mit der Dateiendung `.csv` sind Textdateien, die einem bestimmten Format folgen. Man kann sie sich als einfache Darstellung von Tabellen vorstellen. Die csv-Datei

    "ID","Nachname","Vorname","Klasse","Betriebs-Nr"
    1,"Otto","Karl","6c",-1

entspricht beispielsweise einer Tabelle mit 5 Spalten und 2 Zeilen.

## Import in OpenOffice/LibreOffice

Als Textdateien k&ouml;nnen csv-Dateien zwar mit jedem beliebigen Editor angesehen und ver&auml;ndert werden, aber es ist praktischer, sie als Tabellen zu importieren.

Dazu wird die Datei mit [OpenOffice](http://www.openoffice.org/) bzw. [LibreOffice](http://www.libreoffice.org/) ge&ouml;ffnet, entweder per Doppelklick oder &uuml;ber "&Ouml;ffnen mit...". Es erscheint der folgende Dialog mit Einstellungen zum Importieren:

![Bild des Dialogs](http://img856.imageshack.us/img856/573/csvimportlibreoffice.png)

Die oberen Optionen belassen Sie einfach bei den Standardeinstellungen. Unter "Trennoptionen" w&auml;hlen Sie "Getrennt" und "Komma". Unter "Weitere Optionen" w&auml;hlen Sie "Werte in Hochkomma als Text".

Wenn Sie nun auf "OK" klicken, wird aus den Daten eine Tabelle erzeugt, die Sie wie gewohnt speichern und bearbeiten k&ouml;nnen.

## Export in OpenOffice/LibreOffice

Um [Input](Tut_Input.html)-Dateien zu erzeugen, m&uuml;ssen Sie manchmal auch aus vorhandenen Tabellen csv-Dateien erstellen. Dazu gehen Sie wie folgt vor:

1. Im Men&uuml; "Datei" w&auml;hlen Sie "Speichern unter".  
![Bild](http://img806.imageshack.us/img806/8895/csvexportlibreofficeste.png)
2. Im Dateiauswahl-Dialog w&auml;hlen Sie als Format "Text CSV (.csv)".  
![Bild](http://img580.imageshack.us/img580/8895/csvexportlibreofficeste.png)
3. Im nun erscheinenden Export-Dialog lassen Sie alle Einstellungen so, wie sie sind, und klicken auf "OK".  
![Bild](http://img32.imageshack.us/img32/8895/csvexportlibreofficeste.png)

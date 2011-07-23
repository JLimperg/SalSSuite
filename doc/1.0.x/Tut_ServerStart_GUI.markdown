---
layout: doc
version: 1.0.x
title: Server mit graphischer Oberflaeche
---
# Einen Server mit graphischer Oberfl&auml;che starten

Nachdem Sie die SalSSuite [installiert haben](Tut_Installation.html), k&ouml;nnen Sie durch einen einfachen Doppelklick auf `Server.jar` im Installationsverzeichnis den Server starten (wenn Ihr Computer [richtig konfiguriert](AdminBasics.html) ist). Es erscheint dann folgender Auswahldialog:

![Bild des Dialogs](http://img812.imageshack.us/img812/2792/serverpreprojectdialog.png)

Da Sie noch kein Projekt erstellt haben, w&auml;hlen Sie "Neues Projekt".

## Ein Projekt erstellen

Ein "Projekt" repr&auml;sentiert ein Schule als Staat-Projekt. Wenn Sie ein neues erstellen, m&uuml;ssen Sie einige grundlegende Informationen angeben (die fast alle [nachtr&auml;glich ge&auml;ndert werden k&ouml;nnen](Tut_ServerPanel.html)), damit der Server arbeiten kann. Das tun Sie im folgenden Fenster:

![Bild des Fensters](http://img19.imageshack.us/img19/4697/serverprojectsetupdialo.png)

* **Projektname:** Ein beliebiger Name f&uuml;r Ihr Projekt. Er sollte so aussagekr&auml;ftig sein, dass Sie auch in zwei Tagen noch etwas mit ihm anfangen k&ouml;nnen.  
* **Starttag:** Der Tag, an dem der Staat seine Tore &ouml;ffnet.  
* **Endtag:** Der Tag, an dem der Staat f&uuml;r immer das Zeitliche segnet.  
* **Ausgabepfad:** Mit einem Klick auf "Durchsuchen" w&auml;hlen Sie einen Ordner, in den der Server Informationen ausgeben soll. Dieser Ordner ist derzeit ohne Funktion. 
* **Eingabepfad:** Mit einem Klick auf "Durchsuchen" w&auml;hlen Sie einen Ordner, aus dem der Server Daten importieren soll. N&auml;heres siehe [Input-Tutorial](Tut_Input.html). 
* **Server-Port:** Der untere der beiden aufeinanderfolgenden Ports, auf denen der Server Verbindungen entgegen nimmt. Wird hier beispielsweise 45877 eingetragen (die Voreinstellung), so 'belegt' der Server Ports 45877 und 45878. Bei den Clients gen&uuml;gt die Angabe des Ports, der hier eingetragen wird.

Mit einem Click auf "OK" erstellen Sie schlie&szlig;lich das Projekt und k&ouml;nnen nun Ihre Clients benutzen oder [mit der Server-Oberfl&auml;che arbeiten](Tut_AdminClient.html).

## Ein Projekt weiterf&uuml;hren

Wenn Sie den Server herunterfahren und neu starten, erscheint wieder der folgende Dialog:

![Bild des Dialogs](http://img812.imageshack.us/img812/2792/serverpreprojectdialog.png)

Nun k&ouml;nnen Sie einfach auf "Letztes Projekt" klicken. Der Server wird das zuletzt ge&ouml;ffnete Projekt laden und danach wie gewohnt die graphische Oberfl&auml;che anzeigen.

Wenn Sie ein anderes Projekt laden m&ouml;chten, klicken Sie auf "Projekt &ouml;ffnen". Anschlie&szlig;end k&ouml;nnen Sie die Projektdefinitionsdatei (`proj.xml`) des zu &ouml;ffnenden Projekts angeben. Normalerweise finden Sie alle Projekte im Verzeichnis `[Installationsverzeichnis]\Projekte\`; die Projektdefinitionsdatei liegt im Projektordner, der den Namen ihres Projekts tr&auml;gt.

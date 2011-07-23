---
layout: doc
version: 1.0.x
title: Grundlagen
---
# Grundlagen
Auf dieser Seite werden Ihnen die Grundlagen der SalSSuite erl&auml;utert. Es wird vorausgesetzt, dass die Grundlagen der Kommunikation &uuml;ber Computernetzwerke (IP-Adressen, Ports, Domains etc.) bekannt sind.

## Java und Java-Programme
Die SalSSuite ist in der Programmiersprache Java programmiert, deswegen funktioniert sie etwas anders als gew&ouml;hnliche Programme (aber doch ganz &auml;hnlich).

Java-Programme erkennt man an ihrer Dateiendung `.jar`. Um sie auszuf&uuml;hren, braucht man eine sogenannte Java-Laufzeitumgebung, englisch Java Runtime Environment (JRE). Normalerweise sollte sie auf aktuellen Desktop-PCs vorinstalliert sein, ansonsten erhalten Sie auf [java.com](http://java.com/de/) eine Version f&uuml;r Ihr Betriebssystem. Diese JRE muss sowohl auf dem Server als auch auf jedem Computer, mit dem Sie einen Client nutzen wollen (dazu sp&auml;ter mehr) installiert sein.

Ist die JRE erst einmal installiert, sollten sich mit `.jar` endende Dateien per Doppelklick wie normale Programme ausf&uuml;hren lassen. Wenn das nicht funktioniert, probieren Sie, ob Sie im "&Ouml;ffnen mit..."-Dialog einen Eintrag "Sun JRE", "Java JRE" o.&Auml;. finden.

## Die Server-Client-Architektur
Kernst&uuml;ck der SalSSuite ist der Server (`Server.jar`). Er bietet eine Oberfl&auml;che zur Kontrolle einer eingebetteten [Derby](http://db.apache.org/derby/)-Datenbank. Beim Start initialisiert er diese und stellt sie an einem vom Nutzer eingestellten Port bereit. Au&szlig;erdem startet er den Authentifizierungs-Mechanismus, vermittels dessen sich Clients zum Zugriff auf die Datenbank autorisieren k&ouml;nnen.  
Bitte beachten Sie, dass die SalSSuite *zwei* Ports belegt: den, den Sie beim Starten des Servers angeben, und den darauf folgenden. Standardm&auml;&szlig;ig sind dies Port 45877 und 45878. Bei den Clients muss jedoch immer nur der erste (hier 45877) angegeben werden.

L&auml;uft der Server, so k&ouml;nnen sich Clients bei ihm anmelden. Dazu ben&ouml;tigen Sie die IP-Adresse des Servers, den Port, auf dem der Server auf Verbindungen wartet, einen Benutzernamen und ein Passwort. Die Eingabemaske dazu sieht wie folgt aus:

![Bild der Eingabemaske](http://img860.imageshack.us/img860/8369/clientsconnecttoserver.png)

Bevor das funktioniert brauchen wir aber erst einmal einen installierten Server. Wie das funktioniert, wird im [n&auml;chsten Tutorial](Tut_Installation.html) beschreiben.

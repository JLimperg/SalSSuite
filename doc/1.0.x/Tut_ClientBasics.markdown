---
layout: doc
version: 1.0.x
title Grundlagen: Clients (1.0.x)
---
# Grundlagen: Clients

Ein Client ist ein Programm, das Daten vom Server bezieht, manipuliert und wieder an den Server zur&uuml;ckschickt. Die Clients der SalSSuite sehen aus wie gew&ouml;hnliche Desktop-Anwendungen, schicken aber kontinuierlich Daten hin und her. (Deswegen reagieren sie auch manchmal ein bisschen tr&auml;ge.)

## Eine Verbindung aufbauen

Bevor ein Client verwendet werden kann, muss zun&auml;chst eine Verbindung zum Server aufgebaut werden. Die Oberfl&auml;che dazu sieht wie folgt aus:

![Bild der Benutzeroberfl&auml;che](http://img706.imageshack.us/img706/8369/clientsconnecttoserver.png)

Server-Adresse und Port sollten richtig eingestellt sein. Ihren Benutzernamen und Ihr Passwort erhalten Sie vom System-Administrator. Der Reiter "Proxy" sollte ebenfalls standardm&auml;&szlig;ig richtig eingestellt sein.

## Gemeinsame Elemente der Clients

Die meisten Clients arbeiten mit Daten in Tabellen- oder Listenform. Ein typisches Beispiel ist die B&uuml;rgerverwaltung (ein Teil des AdminClients):

![Bild der B&uuml;rgerverwaltung](http://img508.imageshack.us/img508/6321/adminclientcitizenmanag.png)

### Daten aktualisieren

Der "Aktualisieren"-Button dient dazu, die Daten des Clients mit denen des Servers abzugleichen. Wenn etwas nicht funktioniert wie geplant, dr&uuml;cken Sie probeweise diesen Button und schauen Sie, ob in der Zwischenzeit jemand die Daten ver&auml;ndert hat, auf die Sie zugreifen m&ouml;chten.

### Datens&auml;tze hinzuf&uuml;gen, l&ouml;schen und bearbeiten

Normalerweise findet sich irgendwo im Interface ein Button mit der Aufschrift "hinzuf&uuml;gen". Dieser dient dazu, der Tabelle eine neue Zeile hinzuzuf&uuml;gen. Was beim Bet&auml;tigen des Buttons passiert, ist von Client zu Client unterschiedlich.

Gleiches gilt f&uuml;r die Button mit der Aufschrift "l&ouml;schen" bzw. "bearbeiten".

### Filter

Filter dienen dazu, die in der Tabelle angezeigten Datens&auml;tze zu reduzieren, damit Sie nur die f&uuml;r Ihre Arbeit Wesentlichen sehen. Wie das gemacht wird, beschreibt das [Filter-Tutorial](Tut_Filtering.html).

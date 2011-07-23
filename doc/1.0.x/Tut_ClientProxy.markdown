---
layout: doc
version: 1.0.x
title: Verwendung eines Proxy
---
# Verwendung eines Proxy

Jeder Client kann optional seinen gesamten Netzwerkverkehr &uuml;ber einen [SOCKS-Proxy](http://de.wikipedia.org/wiki/SOCKS) (Protokoll-Version 4 oder 5) leiten. Dazu werden beim Aufbau der Verbindung zum Server im Reiter "Proxy" die Daten des Servers angegeben:

![Bild der zugeh&ouml;rigen Benutzeroberfl&auml;che](http://img860.imageshack.us/img860/9411/clientsconnecttoserverp.png)

* **Proxy-Host:** IP-Adresse oder Dom&auml;nenname des Servers, auf dem der Proxy l&auml;uft. Kann auch der eigene Rechner (localhost) sein.
* **Proxy-Port:** Port, an dem der Proxy-Server Verbindungen entgegennimmt.
* **Benutzername:** Authentifizierung am Proxy wenn n&ouml;tig. Java beherrscht die meisten Authentifizierungsmethoden, bei Problemen sollte jedoch ein Proxy-Server verwendet werden, der keine Authentifizierung verlangt. Diese Angabe ist nicht zu verwechseln mit dem Benutzernamen f&uuml;r die SalSSuite.
* **Passwort:** Passwort des gew&auml;hlten Proxy-Benutzers wenn n&ouml;tig.

Soll kein Proxy verwendet werden, lassen Sie einfach alle Felder dieses Reiters frei (Standardeinstellung). Soll ein Proxy verwendet werden, der keine Authentifizierung verlangt, so lassen Sie die Felder "Benutzername" und "Passwort" frei.

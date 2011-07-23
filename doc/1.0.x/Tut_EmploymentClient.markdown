---
layout: doc
version: 1.0.x
title: Arbeitsamt-Client
---
# Arbeitsamt-Client

Der Arbeitsamt-Client dient dazu, schnell und unkompliziert freie Stellen zu finden und zu verteilen.

![Bild des Clients](http://img820.imageshack.us/img820/5122/clientsemploymentclient.png)

Die Benutzeroberfl&auml;che l&auml;sst sich grob in drei Teile gliedern:

* **Firmen-Liste:**  
Eine Liste aller Betriebe im Staat. Durch Eingabe einer ID im Feld "Nr." und anschlie&szlig;endes Best&auml;tigen mit &lt;Enter&gt; kann die Firma mit dieser ID angew&auml;hlt werden.

* **Firmen-Details:**  
Zeigt Details zur in der Firmen-Liste markierten Firma an. Im Feld "Jobs gesamt" kann die Anzahl der Mitarbeiter, die diese Firma besch&auml;ftigen kann (*inklusive Gr&uuml;nder*) eingegeben werden. Best&auml;tigen mit &lt;Enter&gt;.  
Die **Angestellten-Liste** zeigt alle B&uuml;rger, die au&szlig;er dem Betriebsleiter derzeit beim Betrieb arbeiten.

* **Arbeitslosen-Liste:**  
Eine Liste aller Arbeitslosen im Staat. Durch Eingabe einer ID im Feld "Nr." und anschlie&szlig;endes Best&auml;tigen mit &lt;Enter&gt; kann der B&uuml;rger mit dieser ID ausgew&auml;hlt werden.  
Unten sehen Sie auf einen Blick, wie viele Angestellte derzeit von Betrieben ben&ouml;tigt werden und wie viele B&uuml;rger eine Stelle suchen.

Bitte beachten Sie, dass die Benutzeroberfl&auml;che des Clients oft eher tr&auml;ge reagiert, weil er vergleichsweise gro&szlig;e Datenmengen mit dem Server austauschen muss.

## Arbeitslose einem Betrieb zuweisen

Per Drag&Drop k&ouml;nnen B&uuml;rger von der **Arbeitslosen-** in die **Angestellten-Liste** verschoben werden. Dazu gehen Sie wie folgt vor:

1. Einen Betrieb in der **Firmen-Liste** markieren, sodass er unten bei den **Firmen-Details** auftaucht.
2. Einen Arbeitslosen in der **Arbeitslosen-Liste** anw&auml;hlen und bei gedr&uuml;ckter linker Maustaste in die **Angestellten-Liste** ziehen.

## Angestellte aus einem Betrieb entfernen

Analog zum Vermitteln von Arbeitslosen gehen Sie wie folgt vor:

1. Einen Betrieb in der **Firmen-Liste** markieren, sodass er unten bei den **Firmen-Details** auftaucht.
2. Einen Angestellten in der **Angestellten-Liste** anw&auml;hlen und bei gedr&uuml;ckter linker Maustaste in die **Arbeitslosen-Liste** ziehen.

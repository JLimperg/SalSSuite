---
layout: doc
version: 1.0.x
title: Einkaufsliste
---
# Einkaufsliste

[zur&uuml;ck zum Warenlager-Client](Tut_MagazineClient.html)

![Bild der Benutzeroberfl&auml;che](http://img685.imageshack.us/img685/1707/clientsmagazineclientsh.png)

Jede Zeile dieser Liste folgt dem folgenden Format:

     [ID der Ware] [Bezeichnung] [Anzahl der ben&ouml;tigten Einheiten]

Durch einen Klick auf "eingekauft" k&ouml;nnen Sie angeben, dass Sie eine bestimmte Anzahl [Einheiten](WL_Units.html) dieser Ware eingekauft haben. Ein Klick auf den Button "Alle eingekauft" unter der Liste bewirkt, dass bei jeder Ware exakt so viele Einheiten als vorhanden eingetragen werden, wie angesichts der aktuellen Bestellungen ben&ouml;tigt werden.

## Einkaufsliste exportieren und importieren

Durch einen Klick auf "Liste exportieren" k&ouml;nnen Sie eine Einkaufsliste als Textdatei (Dateiendung `.txt`) abspeichern, die Sie ausdrucken und f&uuml;r den Einkauf verwenden k&ouml;nnen. Sie ist nach H&auml;ndlern geordnet und zeigt alle relevanten Informationen.

Diese Liste k&ouml;nnen Sie wieder 'importieren', indem Sie auf "Liste abgearbeitet" klicken und die vorher erstellte Datei angeben. Die SalSSuite nimmt dann an, dass Sie alle Waren, die auf der exportierten Liste angegeben waren, in genau der angegebenen St&uuml;ckzahl gekauft haben. Sie berechnet dann automatisch f&uuml;r jede Ware, wie viele Einheiten im Warenlager vorhanden sind.

## Mehr einkaufen als bestellt

Wenn Sie auf Vorrat einkaufen wollen, also mehr, als die Betriebe bestellt haben, legen Sie am besten einen fiktiven Betrieb namens "Warenlager" o.&Auml;. an und geben eine Bestellung mit den Waren auf, die Sie zus&auml;tzlich einkaufen wollen. Diese tauchen somit automatisch in der Einkaufsliste auf.

L&ouml;schen Sie diese Bestellung wieder, nachdem Sie die Einkaufsliste abgearbeitet haben.

## Abrechnung erstellen

Mit einem Klick auf "Abrechnung erstellen" k&ouml;nnen Sie eine druckbare Textdatei (Dateiendung `.txt`) erstellen, die detailliert auflistet, wann Sie welche Waren bei welchem H&auml;ndler gekauft haben.

[zur&uuml;ck zum Warenlager-Client](Tut_MagazineClient.html)

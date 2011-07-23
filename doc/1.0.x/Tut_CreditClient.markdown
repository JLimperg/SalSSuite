---
layout: doc
version: 1.0.x
title: Kredit-Client
---
# Kredit-Client

Der Kredit-Client erlaubt es Angestellten der Staatsbank, Kredite an B&uuml;rger oder Betriebe zu vergeben.

![Bild des Clients](http://img88.imageshack.us/img88/8751/clientscreditclient.png)

## Kredit hinzuf&uuml;gen

Wenn Sie auf "Neuer Kredit" klicken, erscheint folgender Dialog:

![Bild des Dialogs](http://img27.imageshack.us/img27/2577/clientscreditclientaddc.png)

* **Kreditnehmer:** W&auml;hlen Sie zun&auml;chst, ob ein B&uuml;rger oder ein Betrieb den Kredit erhalten soll, und tippen Sie dann dessen ID ein.  
* **Betrag:** Der Betrag, den Sie dem Kreditnehmer leihen.  
* **Zinsen pro Tag:** Der Anteil am Betrag, den der Kreditnehmer pro Tag zus&auml;tzlich zur&uuml;ckzahlen muss. Dabei wird der Tag der Ausgabe mitgerechnet, d.h. auch wenn ein Kredit sofort nach Ausgabe zur&uuml;ckgezahlt wird, muss der Kreditnehmer Zinsen bezahlen (au&szlig;er es wird eine Zinsrate von 0% angegeben).  
* **f&auml;llig am:** Das Datum, an dem der Kredit f&auml;llig wird, im Format TT.MM.  
* **ausgegeben am:** Das Datum, an dem der Kredit ausgegeben wurde, im Format TT.MM. Dieses wird automatisch ausgef&uuml;llt und Sie sollten es normalerweise nicht &auml;ndern.

Mit einem Klick auf "Absenden" wird der neue Kredit eingetragen.

## Kredit l&ouml;schen

1. Markieren Sie in der Tabelle eine Zelle des zu l&ouml;schenden Kredits.
2. Klicken Sie auf "Kredit l&ouml;schen".

## Kredit bearbeiten

Aus der Tabelle k&ouml;nnen die folgenden Spalten bearbeitet werden:

* Kreditnehmer  
* zur&uuml;ckgezahlt  
* Startbetrag  
* Zinssatz/Tag  
* ausgezahlt am  
* f&auml;llig am  

Dazu doppelklicken Sie auf eine Zelle, geben den gew&uuml;nschten Wert ein und best&auml;tigen mit &lt;Enter&gt;. Bei der Spalte "zur&uuml;ckgezahlt" gen&uuml;gt es, das H&auml;kchen zu setzen oder zu entfernen.

## Spezielle Filter

Au&szlig;er den Filtern unter der Tabelle (siehe [Filter-Tutorial](Tut_Filtering.html)) gibt es mit Hilfe der zwei Checkboxen &uuml;ber der Tabelle die M&ouml;glichkeit, nur Kredite anzuzeigen, die noch nicht bezahlt wurden, und/oder nur solche, die an diesem Tag f&auml;llig sind oder bereits f&auml;llig waren.

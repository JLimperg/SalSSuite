---
layout: doc
version: 1.0.x
title: Accountverwaltung
---
# Accountverwaltung

## Grundlagen

Administratoren der SalSSuite k&ouml;nnen einzelnen Benutzern Berechtigungen f&uuml;r einzelne Module geben. Dazu k&ouml;nnen Benutzer (sogenannte Accounts) angelegt werden, die &uuml;ber einen Benutzernamen eindeutig identifizierbar sind. Sie k&ouml;nnen mit einem Passwort gesch&uuml;tzt werden. Danach werden jedem Account eine oder mehrere Berechtigungen zugeteilt.

Beispiel: Der Besitzer des Accounts "hans" arbeitet im Warenlager. Sein Account erh&auml;lt somit die Berechtigung "Warenlager", damit er auf den Warenlager-Client zugreifen kann.

Der Benutzer "admin" wird beim Erstellen des Projekts angelegt. Er hat das Passwort, das Sie bei der [Installation](Tut_Installation.html) eingegeben haben, und kann auf alle Module zugreifen. Er kann nicht gel&ouml;scht werden, aber Sie k&ouml;nnen sein Passwort ver&auml;ndern.

Bitte beachten Sie, dass Benutzer, die den AdminClient verwenden wollen, *zus&auml;tzlich* zur jeweiligen Modul-Berechtigung (beispielsweise "B&uuml;rgerverwaltung") noch die Berechtigung "Administration" ben&ouml;tigen.

## Benutzeroberfl&auml;che

![Bild der Benutzeroberfl&auml;che](http://img810.imageshack.us/img810/3354/adminclientusermanageme.png)

In der Tabelle sehen Sie die Benutzernamen der Accounts und ihre s&auml;mtlichen Berechtigungen.

### Benutzer hinzuf&uuml;gen

1. Klicken Sie auf "Benutzer hinzuf&uuml;gen".
2. In einem Dialogfenster werden Sie nach einem Namen f&uuml;r den neuen Benutzer gefragt:  
![Bild des Dialogs](http://img714.imageshack.us/img714/2064/adminclientadduser1.png)
3. Danach m&uuml;ssen Sie das Passwort des neuen Benutzers zwei Mal eingeben:  
![Bild des Dialogs](http://img148.imageshack.us/img148/4889/adminclientadduser2.png)
4. Nun &ouml;ffnet sich der Dialog zum Setzen der Berechtigungen. Verfahren Sie wie unten unter "Berechtigungen bearbeiten" beschrieben.

### Berechtigungen bearbeiten

1. Doppelklicken Sie auf eine Tabellenzeile.
2. Es &ouml;ffnet sich der Dialog zum Bearbeiten der Berechtigungen:  
![Bild des Dialogs](http://img804.imageshack.us/img804/2193/adminclienteditpermissi.png)
3. Markieren Sie die Berechtigungen, die dieser Account haben soll. Ein Haken steht f&uuml;r eine erteilte Berechtigung.
4. Schlie&szlig;en Sie das Fenster durch einen Klick auf das "X", durch die Tastenkombination &lt;Alt&gt;-&lt;F4&gt; o.&Auml;.

### Benutzer l&ouml;schen

1. Markieren Sie mit einem einfachen Linksklick eine Zeile der Tabelle.
2. Klicken Sie auf "Benutzer l&ouml;schen".

### Passwort &auml;ndern

1. Markieren Sie mit einem einfachen Linksklick eine Zeile der Tabelle.
2. Klicken Sie auf "Passwort &auml;ndern".
3. Geben Sie das alte Passwort ein.
4. Geben Sie zweimal das neue Passwort ein.

Beachten Sie, dass Benutzer ihre Passw&ouml;rter nicht selbst &auml;ndern k&ouml;nnen, es sei denn, sie haben Zugriff auf das Modul "Benutzerverwaltung".

---
layout: doc
version: 1.0.x
title: Server ohne graphische Oberflaeche
---
# Einen Server ohne graphische Oberfl&auml;che starten

Wenn der Rechner, auf dem Ihr Server laufen soll, keine graphische Oberfl&auml;che hat, k&ouml;nnen Sie trotzdem alle Funktionen der SalSSuite uneingeschr&auml;nkt nutzen. Zum Start des Servers gehen Sie dazu wie folgt vor:

    java -jar Server.jar -n 
startet einen Server ohne graphische Oberfl&auml;che (Kommandozeilenoption "-n").

Danach sehen Sie ein Auswahlmen&uuml; analog dem der [graphischen Oberfl&auml;che](Tut_ServerStart_GUI.html):

    Was moechten Sie tun?  
    (1) Das letzte Projekt fortsetzen. 
    (2) Ein neues Projekt beginnen.  
    (3) Ein altes Projekt oeffnen.
    
    Ihr Befehl (1, 2, 3):

Beim ersten Start m&ouml;chten Sie vermutlich ein neues Projekt beginnen, also geben Sie "2" ein und dr&uuml;cken "Enter".

## Ein Projekt erstellen

Nun wird der Server Sie nach den gleichen Informationen fragen, die Sie auch in der graphischen Version eingeben. Die Eingabeaufforderungen sollten selbsterkl&auml;rend sein, wenn Sie die [Hinweise zur graphischen Version](Tut_ServerStart_GUI.html) gelesen haben.

Haben Sie alle Informationen korrekt bereitgestellt, zeigt der Server nun das Pendant des ["Server"-Tabs](Tut_ServerPanel.html) seiner graphischen Oberfl&auml;che:

    Was moechten Sie tun?
    (1) Die Daten des aktuellen Projekts bearbeiten.
    (2) Ein Backup anlegen.
    (3) Output generieren.
    (4) Die Daten des Servers speichern.
    (5) Den Server herunterfahren.
    Ihr Befehl (1, 2, 3, 4, 5):

Auch dieses Men&uuml; sollte selbsterkl&auml;rend sein. Siehe die Hinweise zur graphischen Version.

## Den Server als Daemon starten

Nachdem Sie den Server derart vorbereitet haben, k&ouml;nnen Sie ihn nun als Daemon starten. Dazu fahren Sie den laufenden Server herunter (Eingabe "5 &lt;Enter&gt;" im Men&uuml;) und starten ihn mit dem Befehl

    java -jar Server.jar -n -d

neu. Die Option "-d" bewirkt, dass der Server alle Ausgaben unterdr&uuml;ckt und das letzte ge&ouml;ffnete Projekt l&auml;dt. Unter Unix-Betriebssystemen k&ouml;nnen Sie nun mittels

    java -jar Server.jar -n -d &

den Prozess in den Hintergrund schicken und ihn so als Daemon kontinuierlich laufen lassen. Zum Bearbeiten der Daten m&uuml;ssen Sie nun nat&uuml;rlich die Clients benutzen respektive den Server neustarten.

Wenn der Server als Daemon l&auml;uft, k&ouml;nnen Sie zum Herunterfahren einfach den Prozess beenden, beispielsweise indem Sie unter Unix das SIGTERM-Signal senden. Der Server wird dennoch sauber herunterfahren.

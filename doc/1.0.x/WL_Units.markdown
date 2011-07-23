---
layout: doc
version: 1.0.x
title: Einheiten im Warenlager
---
# Einheiten im Warenlager

Im Warenlager werden Waren generell in verschieden gro&szlig;en Portionen eingekauft und abgegeben. In der SalSSuite hei&szlig;t eine Portion ein *Paket*. Um zu definieren, welche Menge einer Ware ein Paket enth&auml;lt, gibt es drei Gr&ouml;&szlig;en:

1. Paketbeschreibung
2. Paketgr&ouml;&szlig;e
3. Paketeinheit

Am besten wird die Bedeutung dieser Begriffe vielleicht durch Beispiele erkl&auml;rt.

Nehmen wir an, wir verkaufen einzelne Einliterflaschen Mineralwasser. Dann ergibt sich daraus das Paket:

    Paketbeschreibung: 1 Flasche
    Paketgr&ouml;&szlig;e: 1
    Paketeinheit: Liter

Verkaufen wir nun das Mineralwasser gleich in K&auml;sten zu je sechs Flaschen, so gilt:

    Paketbeschreibung: 6 Flaschen
    Paketgr&ouml;&szlig;e: 1
    Paketeinheit: Liter

Das gleiche Muster funktioniert auch bei Snacks-T&uuml;ten:

    Paketbeschreibung: 10 Snacks-T&uuml;ten
    Paketgr&ouml;&szlig;e: 50
    Paketeinheit: mg

Wenn man das Ganze auf eine Formel bringen m&ouml;chte, k&ouml;nnte man sagen, ein Paket besteht aus

    `Paketbeschreibung` à `Paketgr&ouml;&szlig;e` `Paketeinheit`

also z.B.

    10 Snacks-T&uuml;ten à 50mg.

## Einkauf und Abgabe

Prinzipiell nimmt die SalSSuite an, dass die Waren in den gleichen Paketen eingekauft und ausgegeben werden. Nun kann es nat&uuml;rlich sein, dass beispielsweise Mineralwasser in 6er-K&auml;sten eingekauft, aber in einzelnen Flaschen ausgegeben wird. In dem Fall sollte einfach die kleinere Paketgr&ouml;&szlig;e (hier: einzelne Flaschen) in der SalSSuite angegeben werden. Beim Einkauf kann die Anzahl zu kaufender Pakete dann durch 6 geteilt werden.

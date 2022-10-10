package utils

import Fighter

/*  Funktion um einen Eingabewert zwischen Minimum und Maximum zu erzwingen.
    value = Eingabewert, min = Minimalstwert, max = Maximalstwert
    Gibt den Wert zwischen min und max zurück. */
/**
 * Returns a clamped [value] between [min] (inclusive) and [max] (inclusive).
 */
fun clamp(value: Int, min: Int, max: Int): Int {
    var result = value
    if (result < min) {
        result = min
    } else if (result > max) {
        result = max
    }
    return result
}

/*  Diese clamp() Funktion überlädt die vorherige (overload). Das heißt es ist die selbe Funktion,
*   nur nimmt sie andere Parameter an. Im Funktionskörper (body) rufen wir dann einfach die vorherige
*   clamp() Funktion auf und setzen die entsprechenden Werte ein.  */
/**
 *  Returns a clamped [value] inside of [range] (inclusive).
 */
fun clamp(value: Int, range: IntRange): Int {
    return clamp(value, range.min(), range.max())
}

/* Diese Funktion nimmt eine Liste mit Elementen vom Typ Fighter und ordnet sie um. Das Element das als
*  Parameter first übergeben wurde soll dabei immer an erster Stelle stehen. Danach sollen alle nachfolgenden,
*  und schließlich alle davorstehenden Elemente folgen.
*  Beispiel: [A,B,C,D,E,F] > wir wollen nun das D am Anfang steht > [D,E,F,A,B,C] */
/**
 * Returns a reordered [list] with elements of type [Fighter], using [first] as its starting point, then loops around.
 */
fun reorderPartyByFighterAsFirst(list: List<Fighter>, first: Fighter): List<Fighter> {
    val reordered: MutableList<Fighter> = mutableListOf()

    // Schritt 1: Füge das erste Element und alle Nachfolgenden hinzu.
    for (i in list.indexOf(first) until list.size) {
        reordered.add(list[i])
    }

    // Schritt 2: Füge nun den Rest von Index 0 bis Index des neuen ersten Elements hinzu.
    for (j in 0 until list.indexOf(first)) {
        reordered.add(list[j])
    }

    return reordered
}
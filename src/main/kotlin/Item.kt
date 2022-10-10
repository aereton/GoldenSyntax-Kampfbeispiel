/* =============================================
            Basisklasse für alle Gegenstände
 ===============================================
 Diese Klasse enthält Daten für unsere Gegenstände sowie eine leere, offene Funktion onUse die bei Gegenstandsbenutzung
 aufgerufen wird. Wir können diese leer lassen, denn Item soll ja nur eine Basisklasse sein, von der dann die richtigen
 Gegenstände erben. In den Unterklassen können wir dann onUse überschreiben und den gewünschten Effekt unseres Gegenstands
 erzielen.
 Parameter im Konstruktor:
    name    = der Name des Gegenstands
    count   = die Stapelmenge (gleiche Gegenstände werden auf einen Stapel gelegt
    desc    = Gegenstandsbeschreibung */
/**
 * Base class for inventory items ([ItemHeal], [ItemBuff] or [ItemRemoveEffect]) with a [name], [count] for stack count and [desc]
 * as inventory description.
 */
open class Item(var name: String, var count: Int = 1, var desc: String) {

    /* Wir öffnen diese Funktion mithilfe des open Schlüsselworts, damit wir diese Funktion in Unterklassen überschreiben können. */
    /** Use this [Item] on the [target]. **/
    open fun onUse(target: Fighter) { }
}
import utils.Prints

/* =============================================
            Inventory class
 ===============================================
 Diese Klasse bildet unser Inventar ab. Im Grunde genommen besteht unser (simples) Inventar nur aus einer Liste mit Elementen
 vom Typ Item sowie zwei Funktionen um ein Gegenstand aus dem Inventar zu konsumieren und um ein Gegenstand aus dem Inventar zu
 entfernen. Da die Unterklassen der Gegenstände (ItemBuff, ItemHeal, ItemRemoveEffect) von der Klasse Item erben, können sie
 alle in unsere Liste mit Typ <Item> abgelegt werden. Und da die Basisklasse eine Funktion onUse() hat, die in jeder Unterklasse
 überschrieben wird, um ihr gewünschtes Verhalten bei Nutzung des Gegenstands zu triggern, können wir ganz und einfach genau
 diese onUse() Funktion ausführen, ohne uns Gedanken machen zu müssen, was genau passieren soll (denn das regeln die Unterklassen
 ja selbst).

 !!Gutes Beispiel für Vorteile von Klassen-Vererbung!!

 Parameter im Konstruktor:
    items   = Liste der Gegenstände im Inventar */
/**
 * Inventory implementation. Contains a list of [items] to keep track of its contents.
 */
class Inventory(var items: MutableList<Item>) {

    /* Mit dieser Funktion können wir einen Gegenstand aus unserem Inventar benutzen. Wir geben den index
    * des Gegenstands aus unserer Liste an sowie ein Ziel vom Typ Fighter.
    * Wir nutzen deshalb den index, weil wir unseren Inventarinhalt aufgelistet nach deren Reihenfolge
    * ausdrucken und mit einer Zahleneingabe eines auswählen. Daher können wir den Input des Users ganz einfach
    * als Parameter übergeben wenn wir diese Funktion während dem Kampf aufrufen. */
    /** Uses an item with [itemIndex] in inventory list of [items] on [target]. **/
    fun useItem(itemIndex: Int, target: Fighter) {

        // Wir prüfen ersteinmal, ob sich der angegebene index überhaupt innerhalb unserer indices der Liste befindet
        if (itemIndex in 0 until items.size) {

            // Gib eine Nachricht aus, dass wir den gewählten Gegenstand benutzen
            Prints.printItemUse(items[itemIndex].name, target.name)

            /* Führe die onUse() Funktion unseres Gegenstands aus. Was auch immer der gewünschte Gegenstand ist
            (ItemBuff, ItemHeal oder ItemRemoveEffect) geht dem Inventar ja nichts an. Wir arbeiten nur mit der
             Basisklasse Item! */
            items[itemIndex].onUse(target)

            // Wir haben einen Gegenstand benutzt/konsumiert, daher sollten wir eins aus unserer Liste abziehen!
            removeItem(items[itemIndex])
        }
    }

    /* Da wir mit einer einfachen Integer Zahl als Mengenangabe innerhalb des Gegenstandes arbeiten, haben wir hier
    * eine Funktion geschrieben, die je nach Anzahl des Gegenstands entweder die Anzahl einfach verringert, oder wenn
    * nicht genug vorhanden sind, den Gegenstand ganz aus der Liste löscht. */
    /** Removes [count] amount of [item] from the inventory **/
    private fun removeItem(item: Item, count: Int = 1) {
        // Ist die Anzahl dieses Gegenstands größer als die Anzahl, die wir entfernen wollen?
        if (item.count > count) {
            // Ja, also ziehe die zu entfernende Anzahl vom Stapel ab
            item.count -= count
        } else {
            // Nein, also lösche den Gegenstand einfach ganz aus der Liste raus
            items.remove(item)
        }
    }
}
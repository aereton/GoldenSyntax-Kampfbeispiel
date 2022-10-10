/* =============================================
            Klasse für Heilgegenstände
 ===============================================
 Diese Klasse erbt von der Basisklasse Item, denn wir möchten für diese Art von Gegenständen ein spezifischen Verhalten
 erwirken. Dafür können wir unsere (vorher leere) Funktion onUse() aus der Basisklasse überschreiben.
 Parameter im Konstruktor:
    name        = der Name des Gegenstands
    count       = die Stapelmenge (gleiche Gegenstände werden auf einen Stapel gelegt
    healValue   = die Anzahl an HP die bei Nutzung beim Nutzer geheilt werden soll
    desc        = Gegenstandsbeschreibung */
/**
 * [ItemHeal] class derived from [Item] base class, with a [name], [count] for stack count, [healValue] for heal amount and [desc] as inventory description.
 */
class ItemHeal(name: String, count: Int, private val healValue: Int = 10, desc: String) : Item(name, count, desc) {

    // Überschreibe onUse() FUnktion aus unserer Basisklasse mithilfe des Schlüsselworts override
    override fun onUse(target: Fighter) {
        /* Falls wir noch wichtigen Code in dieser Funktion in unserer Basisklasse hätten, den wir unbedingt auch in
        * dieser erbenden Klasse ausführen möchten, könnten wir
        *               super.onUse(target)
        * benutzen. Damit würden wir die Funktion aus unserer Basisklasse aufrufen, und danach noch eigenes Verhalten
        * programmieren können. Wir haben die Funktion in der Basisklasse aber leer gelassen, weil wir nichts weiter tun
        * müssen als das jeweilige Verhalten unserer Unterklasse zu programmieren. */

        // Heile das Ziel um gewünschte Anzahl HP
        target.takeHeal(healValue)
    }
}
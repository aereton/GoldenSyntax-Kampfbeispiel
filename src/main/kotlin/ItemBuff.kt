/* =============================================
            Klasse für Buff Gegenstände
 ===============================================
 Diese Klasse erbt von der Basisklasse Item, denn wir möchten für diese Art von Gegenständen ein spezifischen Verhalten
 erwirken. Dafür können wir unsere (vorher leere) Funktion onUse() aus der Basisklasse überschreiben.
 Parameter im Konstruktor:
    name        = der Name des Gegenstands
    count       = die Stapelmenge (gleiche Gegenstände werden auf einen Stapel gelegt
    effect      = Der Statuseffekt der bei Nutzung des Gegenstands auftreten soll
    desc        = Gegenstandsbeschreibung */
/**
 * [ItemBuff] class derived from [Item] base class, with a [name], [count] for stack count, [effect] for [StatusEffect] to apply on use and [desc] as inventory description.
 */
class ItemBuff(name: String, count: Int, private val effect: StatusEffect, desc: String) : Item(name, count, desc) {

    // Überschreibe onUse() FUnktion aus unserer Basisklasse mithilfe des Schlüsselworts override
    override fun onUse(target: Fighter) {
        /* Falls wir noch wichtigen Code in dieser Funktion in unserer Basisklasse hätten, den wir unbedingt auch in
        * dieser erbenden Klasse ausführen möchten, könnten wir
        *               super.onUse(target)
        * benutzen. Damit würden wir die Funktion aus unserer Basisklasse aufrufen, und danach noch eigenes Verhalten
        * programmieren können. Wir haben die Funktion in der Basisklasse aber leer gelassen, weil wir nichts weiter tun
        * müssen als das jeweilige Verhalten unserer Unterklasse zu programmieren. */

        // Füge den Statuseffekt für diesen Gegenstand auf das Ziel hinzu
        target.tryAddStatusEffect(effect, target)
    }
}
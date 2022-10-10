import utils.Prints

/* =============================================
            Klasse für Gegenstände die Statuseffekte entfernen
 ===============================================
 Diese Klasse erbt von der Basisklasse Item, denn wir möchten für diese Art von Gegenständen ein spezifischen Verhalten
 erwirken. Dafür können wir unsere (vorher leere) Funktion onUse() aus der Basisklasse überschreiben.
 Parameter im Konstruktor:
    name            = der Name des Gegenstands
    count           = die Stapelmenge (gleiche Gegenstände werden auf einen Stapel gelegt
    removeAmount    = die Anzahl zufälliger Statuseffekte die bei Nutzung beim Anwender entfernt werden sollen
    desc            = Gegenstandsbeschreibung */
/**
 * [ItemHeal] class derived from [Item] base class, with a [name], [count] for stack count, [removeAmount] for amount of random [StatusEffect] to remove from user and [desc] as inventory description.
 */
class ItemRemoveEffect(name: String, count: Int, private val removeAmount: Int = 1, desc: String) : Item(name, count, desc) {

    // Überschreibe onUse() FUnktion aus unserer Basisklasse mithilfe des Schlüsselworts override
    override fun onUse(target: Fighter) {
        /* Falls wir noch wichtigen Code in dieser Funktion in unserer Basisklasse hätten, den wir unbedingt auch in
        * dieser erbenden Klasse ausführen möchten, könnten wir
        *               super.onUse(target)
        * benutzen. Damit würden wir die Funktion aus unserer Basisklasse aufrufen, und danach noch eigenes Verhalten
        * programmieren können. Wir haben die Funktion in der Basisklasse aber leer gelassen, weil wir nichts weiter tun
        * müssen als das jeweilige Verhalten unserer Unterklasse zu programmieren. */

        // Wir führen diese Schleife so oft aus wie wir Effekte entfernen wollen
        for (i in 0 until removeAmount) {
            // Wir prüfen ob das Ziel überhaupt Statuseffekte auf sich hat
            if (target.effects.size > 1) {
                /* Wir wollen nun einen zufälligen Statuseffekt entfernen. Allerdings wollen wir nicht, dass wir
                * nützliche Effekte wie z.B. eine Erhöhung unserer Angriffskraft entfernen, sondern nur SCHÄDLICHE.
                * Das können wir erreichen, indem wir unsere Effektliste vorher nach gewissen Typen Filtern. In diesem
                * Fall wollen wir nur Statuseffekte in eine neue Liste referenzieren, die vom Typ DOT (Schaden über Zeit),
                * STUN (Lähmung), AGGRO (gezwungen jmd. anzugreifen) oder DEBUFF_SPD (Geschwindigkeit verringert) sind.
                * Dann können wir von dieser gefilterten Liste mit .random() ein zufälliges herauspicken. */
                target.tryRemoveStatusEffect(target.effects.filter { it.type == StatusEffectType.DOT || it.type == StatusEffectType.STUN || it.type == StatusEffectType.AGGRO || it.type == StatusEffectType.DEBUFF_SPD }.random())
            } else {
                // Keine Statuseffekte auf dem Ziel (mehr), also gib eine passende Nachricht aus
                Prints.printNoRemovedStatusEffect(target.name)
            }
        }
    }
}
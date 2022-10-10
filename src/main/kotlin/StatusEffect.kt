/* Ein ENUM ist ein jeweils eigener Datentyp den wir hier erstellen. Wenn wir eine Variable haben, die ein Objekt mit
* diesem Typ enthält, kann diese Variable nur EINEN dieser unten deklarierten Zustände haben. Stell es dir vor
* wie eine Ampel: Sie kann nur GRÜN, GELB oder ROT sein. Das nutzen wir um später mit einer einfachen when() oder if()
* Abfrage zu prüfen, was für ein Statuseffekt-Typ eine Instanz ist. */
enum class StatusEffectType {
    DOT, STUN, AGGRO, BUFF_BLOCK, DEBUFF_SPD, HOT, BUFF_ATK, BUFF_RES
}


/* =============================================
            Datenklasse für Statuseffekte
 ===============================================
 Datenklasse für unsere Statuseffekte. Wir nutzen hier keine normale Klasse sondern eine sog. "data class", die einige Limitationen
 gegenüber normalen Klassen hat -> sie kann z.B. nicht Vererben. Allerdings bietet sie auch einige Vorteile: So können
 wir bspw. eine Instanz, die wir bereits instanziiert haben, mit der Funktion instanz.copy() klonen, um eine
 exakte Kopie davon zu erstellen. Das tun wir deshalb, weil wir die Statuseffekte praktisch als Bibliothek vor uns haben möchten
 und wenn wir einen Statuseffekt auf einen Kämpfer anwenden, kopieren wir einen Klon des gewünschten Effekts in seine Liste
 von aktiven Statuseffekten. So hat jeder Kämpfer seine eigene Version von einem Effekt (z.B. Blutung), dessen Dauer
 nicht voneinander abhängt und nicht gemeinsam verändert wird.
 Parameter im Konstruktor:
              name = der Name des Statuseffekts
              icon = Icon des Statuseffekts
              type = Der Typ des Statuseffekts; unterschiedliche Typen können sich unterschiedlich verhalten (z.b. Blutung vs Lähmung)
            chance = Die Prozentchance, ob ein Effekt erfolgreich auf ein Ziel angewendet werden kann
     durationRange = Die Spanne der Dauer des Effekts (min/max)
       durationMax = Maximale Dauer, die für diesen Effekt erlaubt ist
        powerRange = Effektstärke; kann z.B. Schaden pro Runde sein, oder Blockprozentwert; je nach Typ
              desc = Beschreibung des Statuseffekts */
/**
 * Data class for status effects with a [name], [icon], [type] as [StatusEffectType], [chance], [durationRange], [durationMax] for
 * allowed maximum duration, [powerRange] as effect power and [desc].
 */
data class StatusEffect(val name: String, val icon: String, val type: StatusEffectType, val chance: Int, val durationRange: IntRange, val durationMax: Int, val powerRange: IntRange, val desc: String) {

    /* Die aktuelle Dauer für diesen Statuseffekt ist erst einmal 0 denn wir kopieren bei Anwendung auf einen Kämpfer
    * eine Instanz vom gewünschten Effekt und setzen dann zufällige Dauer auf Basis unserer Dauerspanne auf der Kopie. */
    var duration: Int = 0
    /* Manche Effekte benötigen eine Referenz zum Urheber dieses Statuseffekts, z.B. bei einem AGGRO Effekt der
    * den betroffenen Kämpfer zwingt, den Urheber anzugreifen. */
    var src: Fighter? = null

    /* Mit dieser Funktion können wir eine zufällige Dauer festlegen. Das machen wir dann, wenn wir eine Instanz
    * kopiert haben um sie auf einen Kämpfer anzuwenden. Gibt außerdem diese Dauer zurück, damit wir weiter damit
    * arbeiten können (z.B. eine Nachricht ausgeben, für wie viele Züge dieser Effekt angewendet wurde. */
    /** Sets a random [duration] based on the [durationRange]. Returns the result as well. **/
    fun randomDuration(): Int {

        duration = durationRange.random()

        return duration
    }

    /* Einfache Hilfsfunktion die einen String zurückgibt der uns Namen und Icon anzeigt
     und der dann z.b. so aussehen würde:   [EFFEKTNAME] ☃     */
    /** Builds a simple string to display the effects' name and icon **/
    fun getNameIcon(): String {
        return "[$name] $icon"
    }

    /* Einfache Hilfsfunktion die uns einen String zurückgibt der uns Icon und Dauer anzeigt
    * und der dann z.B. so aussehen würde.      ☃(2)     */
    /** Builds a simple string to display the effects' icon and its duration **/
    fun getIconDuration(): String {
        var text = icon
        if (duration > 0) {         // Wir wollen die Dauer nur anzeigen, wenn der Statuseffekt länger als bis zum nächsten Zug gilt!
            text += "(${duration})"
        }
        return text
    }
}

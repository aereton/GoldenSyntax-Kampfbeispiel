/* Ein ENUM ist ein jeweils eigener Datentyp den wir hier erstellen. Wenn wir eine Variable haben, die ein Objekt mit
* diesem Typ enthält, kann diese Variable nur EINEN dieser unten deklarierten Zustände haben. Stell es dir vor
* wie eine Ampel: Sie kann nur GRÜN, GELB oder ROT sein. Das nutzen wir um später mit einer einfachen when() oder if()
* Abfrage zu prüfen, was für ein Fähigkeitstyp eine Instanz (SkillType) ist oder welche Ziele verfügbar sein sollen (SkillTarget). */
enum class SkillType {
    ATK, HEAL, EFFECT_ONLY, SPAWN
}

enum class SkillTarget {
    OPPONENT_SINGLE, OPPONENT_ALL, PARTY_ALL, PARTY_SINGLE, PARTY_SELF, SELF
}


/* =============================================
            Klasse für Fähigkeiten
 ===============================================
 Diese Klasse dient für das Verhalten und die nötigen Daten für die Anwendung von Fähigkeiten. Sie enthält nur eine
 einzige Funktion, onCast() die eine Liste an Zielen, eine Referenz zum Anwender sowie einen Schadensmodifizierwert
 übergeben bekommt. Der Schadensmodifizierwert kommt auch direkt vom Anwender der Fähigkeit. Außerdem hat eine Fähigkeit
 noch eine Variable usesLeft, die bei Initialisierung auf den Wert usesPerBattle gesetzt wird, also wie oft wir die
 Fähigkeit anwenden können. Diese Variable wird dann pro Nutzung um 1 verringert (wenn sie nicht unendlich oft nutzbar ist, was
 wir bei usesPerBattle mit -1 festgelegen können).
 Parameter im Konstruktor:
              name = der Name der Fähigkeit
              type = Typ dieser Fähigkeit; dies regelt das Verhalten z.B. ob wir Schaden machen sollen, heilen sollen, etc.
             power = die Spanne des Schadens oder der Heilung diese Fähigkeit verursachen kann; abhängig von Typ der Fähigkeit
     usesPerBattle = wie oft diese Fähigkeit insgesamt eingesetzt werden können soll
 useSuccessiveByAi = ob diese Fähigkeit mehrmals hintereinander von der Gegner KI eingesetzt werden kann
       skillEffect = Statuseffekt den wir bei Fähigkeitsnutzung hervorrufen können, kann null sein wenn kein Effekt gewünscht ist
       description = Beschreibung der Fähigkeit
                     */
/**
 * Class for Skills with [name], [type] for [SkillType], [power] for skill power (dmg, heal, etc), [usesPerBattle], [useSuccessiveByAi],
 * [availableTargets] for [SkillTarget], [skillEffect] for [StatusEffect] and [description].
 */
class Skill(
    val name: String,
    val type: SkillType,
    private val power: IntRange,
    val usesPerBattle: Int,
    val useSuccessiveByAi: Boolean,
    val availableTargets: SkillTarget,
    val skillEffect: StatusEffect?,
    val description: String
) {
    // Wir wollen unsere Fähigkeit am Anfang des Kampfes so oft wie das Maximum einsetzen können
    var usesLeft: Int = usesPerBattle

    /* Mit dieser Funktion können wir eine Fähigkeit auf ein oder mehrere Ziele anwenden. Wir gehen hier immer von einer Liste von
    * Zielen aus, falls es nur ein Ziel gibt ist das kein Problem denn für die Anwendung iterieren wir durch die Liste mit
    * einer for Schleife. Ist nur ein Ziel in der Liste, wird die Schleife auch nur einmal ausgeführt!
    * Wir übergeben in dieser Funktion eine Liste an Zielen, den Urheber der diese Fähigkeit eingesetzt hat sowie den
    * Schadensmodifizierwert des Urhebers um den Schaden zu berechnen. */
    /** Casts this skill on all [targets], sets [src] on [StatusEffect] if applied and calculates dmg based on [dmgModifier]. **/
    fun onCast(targets: List<Fighter>, src: Fighter, dmgModifier: Double) {

        /* Ersteinmal wollen wir sehen was für ein Fähigkeitstyp wir sind und je nachdem Schaden austeilen,
        * Heilen oder Helfer herbeizubeschwören. Falls wir keine dieser Bedingungen erfüllen, z.b. wenn der Typ
        * dieser Fähigkeit EFFECT_ONLY ist, dann wollen wir gar nichts tun, denn wir wenden Statuseffekte weiter
        * unten an. */
        when (type) {
            // Teile Schaden auf unsere Ziele aus
            SkillType.ATK -> {
                for (target in targets) {   // Hier iterieren wir durch die Liste unserer Ziele.
                    /* Wir teilen Direktschaden aus, also übergeben wir auch canBlock = true, denn dieser Schaden
                    * soll abblockbar sein. */
                    target.takeDamage((power.random() * dmgModifier).toInt(), true)
                }
            }
            // Heile unsere Ziele
            SkillType.HEAL -> {
                for (target in targets) {
                    target.takeHeal(power.random())
                }
            }
            // Beschwöre unsere Helfer
            SkillType.SPAWN -> {
                /* Wir überprüfen ob der Urheber dieser Fähigkeit vom Typ Enemy ist, denn nur Gegnerinstanzen haben
                * die nötige Funktionalität um Helfer heraufzubeschwören. */
                if (src is Enemy) {
                    /* Der Parameter src, also die Referenz für den Urheber, ist vom Typ Fighter, wir müssen allerdings
                    * auf Funktionen der Unterklasse Enemy zugreifen. Daher speichern wir eine neue Variable ab
                    * mit dem Typ Enemy und legen src darin ab. Hier nutzen wir ein Kotlin Feature namens "Smart cast",
                    * das bedeutet dass der Compiler automatisch erkennt, dass Enemy eine Unterklasse von Fighter ist
                    * und er automatisch die Referenz in den Typ Enemy umwandeln kann. */
                    val boss: Enemy = src   // Grün hinterlegt; fahre mit der Maus drüber dann siehst du die Meldung "Smart cast..."!

                    // Jetzt wollen wir die Funktion auf unserer Gegnerinstanz aufrufen, um Gegner herbeizubeschwören.
                    boss.spawnMinion(power.random())
                }
            }
            else -> { }     // Falls nichts zutrifft, dann wollen wir nichts spezielles tun, daher lassen wir den { } leer!
        }

        /* Wir prüfen ob wir einen Statuseffekt anwenden sollen; ist unsere Variable skillEffect null, dann wollen wir das nicht! */
        if (skillEffect != null) {
            // Wir wollen für jedes Ziel versuchen einen Effekt anzuwenden
            for (target in targets) {
                // Falls das aktuelle Ziel gerade gestorben ist, wollen wir keinen Statuseffekt anwenden
                if (!target.isDead) {
                    target.tryAddStatusEffect(skillEffect, src)
                }
            }
        }

        /* Falls wir eine Fähigkeit nicht permanent benutzen können (also mit -1 gekennzeichnet ist), dann wollen wir
        * die Nutzungsanzahl um 1 verringern. */
        if (usesLeft > 0) {
            usesLeft--
        }
    }
}
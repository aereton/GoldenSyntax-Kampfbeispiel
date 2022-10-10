import utils.*
import kotlin.random.*
import kotlin.math.*

/* =============================================
            Basisklasse für alle Kämpfer
 ===============================================
 Diese Klasse enthält Funktionen für Angriffe mit Skills, Schaden nehmen, Heilung erhalten, Statuseffekte
 hinzufügen, etnfernen und anwenden und das printen von Lebensbalken (in unterschiedlichen Ausführungen),
 sowie Werte für Gesundheit, Geschwindigkeit, Statuseffekt-Resistenz, Zustände (isDead, etc), Skills und
 aktive Statuseffekte.
 Parameter im Konstruktor:
    name    = der Name des Kämpfers
    maxHp   = die maximale Gesundheit
    speed   = die Geschwindigkeit die bestimmt, wann dieser Kämpfer einen Zug machen kann;
              je höher desto eher ist er an der Reihe */
/**
 * Base class for Fighters ([Enemy] or [Hero]) with a [name], [maxHp] for maximum health and [speed]
 * for turn order decided each round during a [Battle].
 */
open class Fighter(val name: String, protected var maxHp: Int, private var speed: Int) {

    // Die aktuelle Gesundheit, die durch Schaden verringert oder durch Heilung erhöht wird
    protected var currentHp: Int = maxHp      // Setzen wir direkt zu Anfang auf die maximale Gesundheit
    // Reale Geschwindigkeit, die durch Effekte verringert oder erhöht wird
    var speedReal: Int = speed              // Setzen wir direkt zu Anfang auf die Basisgeschwindigkeit
    // Basis-Schaden, der als Multiplikator für unseren zufügenden Schaden durch Fähigkeiten dient; 1.0 == 100%
    private val dmgBase: Double = 1.0       // Setzen wir direkt zu Anfang auf 1.0 (dmg * 1.0 == dmg)
    // Realer Schaden, der als Multiplikator für unseren zufügenden Schaden durch Fähigkeiten dient
    var dmgReal: Double                     // Setzen wir in init{} auf Basisschaden
    // Basisresistenz gegen Statuseffekte; 0.0 == 0% - 1.0 == 100%
    private val resistBase: Double = 0.0    // Setzen wir direkt zu Anfang auf 0.0 (= keine Resistenz)
    // Reale Resistenz gegen Statuseffekte; je höher desto wahrscheinlicher ist es keine Statuseffekt zu erhalten
    var resistReal: Double                  // Setzen wir in init{} auf Basisresistenz


    // Wird in takeDamage() gesetzt, falls unsere HP auf 0 oder darunter fallen
    var isDead: Boolean
    // Kann keinen Zug ausführen falls betäubt
    var isStunned: Boolean
    // Ist gezwungen die Quelle des entsprechenden Statuseffekts anzugreifen, kann also keine Aktionswahl treffen.
    var isAggro: Boolean
    // Gibt an ob dieser Kämpfer am Zug ist; wird in einer while() Schleife in Battle.kt genutzt um einen Bug zu fixen
    var isTurn: Boolean

    // Unsere Liste an verfügbaren Skills
    val skills: MutableList<Skill>
    // Unsere Liste an aktiven Statuseffekten
    val effects: MutableList<StatusEffect>


    /* Mit dem Schlüsselwort 'init' können wir Code bereits bei Initialisierung der Instanz ausführen
    * Wir nutzen die Gelegenheit um einigen Variablen einen initialen Wert zuzuweisen. */
    init {
        this.dmgReal = this.dmgBase
        this.resistReal = this.resistBase
        this.isDead = false
        this.isStunned = false
        this.isAggro = false
        this.isTurn = false
        this.skills = mutableListOf()
        this.effects = mutableListOf()
    }

    /* Einfache Hilfsfunktion, um eine Liste an Fähigkeiten diesem Kämpfer hinzuzufügen, dabei prüfen wir ob wir bereits
    * eine Fähigkeit mit gleichem Namen haben, ist das der Fall dann fügen wir diese eine Fähigkeit nicht nochmal hinzu */
    /** Adds a list of [Skill]s to this instance. **/
    fun addSkillSet(skillSet: List<Skill>) {
        for (skill in skillSet) {
            if (!skills.contains(skill)) {
                skills.add(skill)
            }
        }
    }

    /* Einfache Hilfsfunktion, um zu überprüfen ob wir volle Gesundheit haben. */
    /** Returns wether or not this instance has full health **/
    fun hasFullHealth(): Boolean {
        /* Da wir den Vergleichsoperator ist-größer-gleich verwenden, ist das Resultat ein Boolean Wert, daher können wir das Ergebnis der Bedingung direkt zurückgeben. */
        return currentHp >= maxHp
    }

    /* Diese Funktion wendet eine Fähigkeit mithilfe des 'index' aus unserer Liste 'skills' auf alle Kämpfer in unserer Liste
    'targets' an. Wir überprüfen hier im Grunde nur, ob wir diese Fähigkeit überhaupt in unserer Liste 'skills' haben, ob wir
    sie anwenden können oder ob sie verbraucht ist.
    Die eigentliche Logik der Fähigkeit (was passiert wenn diese Fähigkeit angewendet wird?) steht dabei allerdings
    in der .onCast() Funktion, die wir auf der Instanz unserer ausgewählten Fähigkeit bei Erfolg aufrufen. */
    /** Casts a skill of [index] from our [skills] list on all [targets]. **/
    fun castSkill(index: Int, targets: List<Fighter>) {
        // Falls unsere Liste an Fähigkeiten leer ist, werfen wir einen Laufzeitfehler aus.
        if (skills.isEmpty()) {
            throw Exception("Fighter $name has no skills to use!")
        }

        /* Wir suchen hier die gewollte Fähigkeit aus unserer Liste raus und speichern zur späteren Nutzung ab.
        Wir nutzen hier auch die clamp() Funktion aus dem selbstgeschriebenen packages 'utils' (main>kotlin>utils>Utils.kt)
        um sicherzustellen, dass unser index nicht außerhalb der Liste liegt. Falls wir z.B. also einen index von 4 angeben,
        obwohl wir nur eine Liste von 0-3 haben, zwingt die clamp() Funktion unseren Wert auf 3 */
        val skill = skills[clamp(index, 0, skills.size - 1)]

        /* Wir möchten für die Textausgabe weiter unten einen string aller Namen unserer Ziele haben, daher iterieren
        * wir durch die gesamte Liste an Zielen und fügen jedesmal ein & hinten an, falls wir mehr als ein Ziel haben */
        var targetsText = ""
        for (i in targets.indices) {
            targetsText += if (i < targets.size - 1) {  // Eine if Anweisung kann auch Werte zurückgeben!
                "${targets[i].name} & "
            } else {
                targets[i].name
            }
        }

        /* Wir prüfen ob unsere ausgewählte Fähigkeit nutzbar ist. 'usesLeft' gibt eine Zahl an die aussagt,
        * wie oft diese Fähigkeit noch angewendet werden kann. 'usesLeft' kann auch -1 sein, was uns später sagt dass
        * diese Fähigkeit unendlich oft nutzbar ist, daher prüfen wir hier nicht ob diese Zahl kleiner-gleich 0 ist,
        * sondern ob diese Zahl NICHT 0 ist.
        * Falls die ausgewählte Fähigkeit nicht nutzbar ist, werfen wir einen Laufzeitfehler aus, das sollte eigentlich
        * nicht passieren, da wir im Kampfmenü gar keine Fähigkeiten auswählen können, deren 'usesLeft' == 0 ist, daher
        * ist dies eigentlich nur eine Rücksicherung damit wir mitbekommen falls unser Code nicht wie beabsichtigt läuft */
        if (skill.usesLeft != 0) {
            // Gib eine Meldung aus, dass dieser Kämpfer die ausgewählte Fähigkeit auf das oder die Ziel(e) anwendet.
            Prints.printCastSkill(name, skill.name, targetsText)
            /* Wende nun die Fähigkeit tatsächlich an. Wir übergeben unsere Liste an Zielen, die Quelle des Anwenders
            * der Fähigkeit (Schlüsselwort 'this' bezieht sich dabei immer auf die aktuelle Instanz), sowie unseren
            * aktuellen, realen Schadensmultiplikator der für die Schadensberechnung dient. */
            skill.onCast(targets, this, dmgReal)
        } else {
            // Werfe einen Laufzeitfehler aus mit eigener Nachricht.
            throw Exception("Used skill that cannot be cast anymore. This shouldn't have happened!")
        }
    }

    /* Diese Funktion wendet Schaden auf die Instanz in Höhe von 'dmg' an. Sie wird generell von Skills und Statuseffekten
    * aufgerufen. Wir gehen auch sicher, dass sich unsere Gesundheit in einem zulässigen Rahmen befindet, nämlich
    * zwischen 0 und maxHp. Außerdem überprüfen wir hier ob diese Instanz sterben soll, falls die Gesundheit
    * auf 0 fällt. Des Weiteren ziehen wir - falls nötig - einen gewissen Prozentsatz an Schaden ab. */
    /** Apply damage of value [dmg] on this instance **/
    fun takeDamage(dmg: Int, canBlock: Boolean) {
        /* Wir speichern den Schadenswert zur weiteren Berechnung ab, damit wir später genau wissen,
        * wie hoch jeweils der gewollte und der reale Schaden ist. */
        var realDmg = abs(dmg)

        // Wir wollen nur blocken können wenn wir direkten Schaden durch Angriffe bekommen, NICHT durch Statuseffekte
        if (canBlock) {
            /* Wir filtern unsere Liste an aktiven Statuseffekten nach Element die nur vom Statuseffekt-Typ 'BUFF_BLOCK' sind
        * und speichern das Ergebnis als neue Liste ab. */
            val blockFxList: List<StatusEffect> = effects.filter { it.type == StatusEffectType.BUFF_BLOCK }
            /* Nun iterieren wir durch diese Liste und ziehen jeweils einen gewissen Prozentsatz des gewollten Schadens vom
        * realen Schaden ab */
            for (blockFx in blockFxList) {
                /* Wir berechnen unseren Prozentsatz und speichern diesen in einer Variable ab. Als gewollten Prozentsatz,
            * der abgezogen soll nutzen wir einen zufälligen Wert der IntRange 'powerRange' des jeweiligen Statuseffekts,
            * damit wir auch Zufallswerte nutzen können. Falls dieser Prozentsatz ein fester Wert z.B. genau 60% sein soll
            * können wir bei der Instanziierung des Statuseffekts auch eine IntRange von z.b. 60..60 angeben, dann kommt
            * immer der gleiche Wert heraus.
            * Wir teilen diesen Prozentsatz nun durch 100.0 um einen einstelligen Wert zu bekommen
            * 0% wäre dann 0.0, 50% wäre dann 0.5, 100% wäre dann 1.0; damit lässt sich unsere Schadensdifferenz leicht berechnen
            * Beispiele: [80 * 0.0 = 0] [80 * 0.25 = 20] [80 * 0.5 = 40] [80 * 0.75 = 60] [80 * 1.0 = 80]     */
                val blocked: Double = blockFx.powerRange.random() / 100.0
                /* Wir berechnen nun wie viel Schaden vom Realschaden abgezogen werden soll
            * Dabei multiplizieren wir unseren Blockprozentwert mit dem gewollten Schaden, NICHT mit dem realen Schaden,
            * denn unserer Prozentsatz soll sich ja immer auf den gewollten Schaden beziehen.
            * Beispiel:
            * Unser Held hat zwei Statuseffekte die Schaden abblocken, einmal 10% und einmal 50% und erhält nun Schaden von 100.
            * !!Das wollen wir nicht:
            * 100 - (100 * 0.1) = 90            // Erster Statuseffekt (100 * 0.1) = 10
            *  90 -  (90 * 0.5) = 45            // Zweiter Statuseffekt (90 * 0.5) = 45
            * Realer Schaden = 45               // Abzug insgesamt = 55
            * !!Das wollen wir:
            * 100 - (100 * 0.1) = 90            // Erster Statuseffekt (100 * 0.1) = 10
            *  90 - (100 * 0.5) = 40            // Zweiter Statuseffekt (100 * 0.5) = 50
            * Realer Schaden = 40 */            // Abzug insgesamt = 60
                realDmg -= (abs(dmg) * blocked).toInt()     // Wir wandeln den Wert auch gleich in einen Integer um
            }
        }

        /* Berechne die neue Gesundheit und speicher diese ab; wir nutzen hier auch gleich die clamp() Funktion
        * aus unserem 'utils' package, um sicherzugehen dass dass unsere HP immer im zulässigen Rahmen bleibt */
        currentHp = clamp(currentHp - realDmg, 0, maxHp)
        // Gebe eine Nachricht aus die sagt wer wie viel Schaden genommen hat
        Prints.printTakeDamage(name, realDmg)

        // Prüfe ob unsere Gesundheit auf 0 gefallen ist, ist das der Fall dann rufe die die() Funktion auf die uns sterben lässt
        if (currentHp <= 0) {
            die()
        }
    }

    /* Unsere Heilfunktion ist im Prinzip das genaue Gegenteil der Schadensfunktion, nur wesentlich kürzer und einfacher
    * gehalten da wir ja nichts von unserem Heilwert abziehen müssen. */
    /** Apply healing of value [heal] on this instance. **/
    fun takeHeal(heal: Int) {
        // Speichere unsere alte Gesundheit ab, damit wir in der Ausgabe die Differenz ausrechnen können
        val oldHp = currentHp
        /* Berechne die neue Gesundheit und nutze die clamp() Funktion aus unserem 'utils' package, damit wir nicht
        * über unsere maximale Gesundheit hinaus heilen. */
        currentHp = clamp(currentHp + heal, 0, maxHp)
        /* Gebe eine Nachricht aus die sagt wer wie viel Heilung erhalten hat. Wir rechnen die Differenz zwischen neuer und alter
        Gesundheit direkt im String aus denn wir brauchen sie sonst nicht. Wir nutzen hier absichtlich nicht den
        Parameterwert 'heal' von unserer Funktion, denn es kann ja sein dass unsere Gesundheit fast voll war und wir
        möchten den realen Heilungswert ausgeben. */
        Prints.printTakeHeal(name, currentHp - oldHp)
    }

    /* Unsere Sterbefunktion ist ganz einfach gehalten. Wir setzen nur unseren Zustand 'isDead' auf true
    * und geben eine entsprechende Nachricht aus die angibt, wer gestorben ist. */
    /** Lets this instance die in the battle. **/
    private fun die() {
        isDead = true
        Prints.printDie(name)
    }

    /* Eine einfache Hilfsfunktion um schnell zu überprüfen, ob wir einen Statuseffekt mit angegebenen Namen
    * in unserer Liste aktiver Statuseffekte haben. Gibt das Ergebnis dann als Boolean zurück. */
    /** Returns true, if this instance has a [StatusEffect] with [name] applied. Otherwise returns false. **/
    private fun hasStatusEffect(name: String): Boolean {
        for (effect in effects) {
            if (effect.name == name) {
                /* Wenn wir innerhalb einer Schleife mit 'return' einen Wert zurückgeben, wird der Rest der Schleife
                * nicht weiter ausgeführt, wir "brechen" sozusagen aus der Funktion aus. Somit können wir weiter unten
                * ohne weitere Bedingungsabfrage 'return false' zurückgeben, denn diese Zeile wird ja nie erreicht,
                * wenn wir innerhalb der Schleife einen Statuseffekt mit korrektem Namen finden. */
                return true
            }
        }
        // Wie oben erklärt, diese Zeile wird gar nicht erreicht wenn innerhalb der Schleife 'true' zurückgegeben wird.
        return false
    }

    /* Eine einfache Hilfsfunktion um eine Referenz zu einem Statuseffekt mit angegebenen Namen zurückzugeben.
    * Wir haben hier auch zu beachten, was passiert wenn solch ein Statuseffekt sich nicht in unserer Liste befindet,
    * daher geben wir in diesem Fall 'null' zurück. Dann können wir später bei Verwendung dieser Funktion mit einer
    * einfache if-Abfrage prüfen ob wir überhaupt einen Statuseffekt gefunden haben. */
    /** Returns the [StatusEffect] with [name] if it's applied on this instance. Otherwise returns null. **/
    private fun getStatusEffect(name: String): StatusEffect? {  // 'StatusEffekt?' => nullbaren Typ zurückgeben

        for (effect in effects) {
            if (effect.name == name) {
                /* Wir brechen wieder aus der Funktion aus, wenn wir etwas gefunden haben. Gib die Referenz zum
                * Statuseffekt aus der Liste zurück. */
                return effect
            }
        }
        // Wir geben null zurück, falls wir nichts gefunden haben.
        return null
    }

    /* Diese Funktion fügt einen Statuseffekt auf das Ziel hinzu, wenn gewisse Bedingungen erfüllt sind. Sie wird von einer Fähigkeit
     aufgerufen, wenn diese einen Statuseffekt gesetzt hat. Diese Funktion kann man prinzipiell in zwei Teilen sehen, zuerst wollen
     wir prüfen ob wir unsere Resistenz gegen Statuseffekte mit in unsere Chance berechnen sollen (dies ist generell nur bei
     schädlichen Effekten der Fall) und dann wollen wir die Chance berechnen und je nach Ergebnis einen Statuseffekt hinzufügen
     oder nicht. */
    /** Tries to add a StatusEffect [effect] to this instance. [src] references [Fighter] source who casts this. **/
    fun tryAddStatusEffect(effect: StatusEffect, src: Fighter) {

        /* Wir setzen unsere effektive Resistenz gegen Statuseffekte mit einer if Abfrage; falls wir einen nützlichen Effekt
        * erhalten sollen, ist unsere Resistenz immer 0. */
        val effectiveResistance = if (effect.type == StatusEffectType.STUN || effect.type == StatusEffectType.DOT || effect.type == StatusEffectType.DEBUFF_SPD || effect.type == StatusEffectType.AGGRO) {
            (effect.chance * resistReal).toInt()    // Unsere effektive Resistenz wird der Prozentanteil von der Chance des Effekts sein
        } else {
            0
        }

        /* Nun wollen wir unsere Chance berechnen. Das tun wir direkt in einer if Abfrage als Bedingung. Auf der linken Seite generieren wir einen zufälligen Wert zwischen 0 und 100, dann überprüfen wir ob dieser zufällige Wert geringer-gleich als
         unsere Effekchance MINUS unserer effektiven Resistenz ist.
         Bsp: Zufälliger Wert: 36 <= (60% - 10%) ALSO! 36 <= 50 ALSO! true */
        if (Random.nextInt(0..100) <= (effect.chance - effectiveResistance)) {

            /* Statuseffekte sind eine data class, diese sind ähnlich wie normale Klassen, sind aber eher geeignet für Dinge die
            hauptsächlich Daten beinhalten sollen und können mit copy() als neue Instanz kopiert werden. Das tun wir deshalb weil
            unserere Statuseffekte bereits in einer Art "Datenbank" instanziiert wurden und wir sie einfach raussuchen wollen. Da
            die Statuseffekte aber Variablen haben wie z.B. die Dauer in der sie aktiv sind, die jede Runde runterzählen, müssen wir
            für jeden aktiven Statuseffekt eine eigene Instanz erstellen. */
            val copy: StatusEffect = effect.copy()

            // Wir generieren eine zufällige Dauer des Effekts.
            var dur = copy.randomDuration() // randomDuration() gibt uns auch den gesetzten Wert zurück, den brauchen wir noch
            // Wir setzen den Verursacher des Statuseffekts
            copy.src = src

            /* Falls wir schon einen Statuseffekt mit dem selben Namen haben, wollen wir aber nicht einfach einen weiteren
            * unserer Liste hinzufügen. Deshalb prüfen wir erst einmal ob ein Effekt mit diesem Namen in unserer Liste existiert.
            * Ist dies der Fall, addieren wir einfach die Dauer anstatt zwei gleiche Effekte in der Liste zu haben. */
            if (hasStatusEffect(copy.name)) {
                /* Wir wollen niemals unsere maximale Dauer überschreiten, für die Berechnung benötigen wir also die Dauer,
                * !bevor! sie erhöht wurde. */
                val oldDur = getStatusEffect(copy.name)!!.duration
                // Hier berechnen wir die Differenz, also wie viel wir noch hinzufügen KÖNNEN
                dur = copy.durationMax - oldDur
                // Nun setzen wir die Dauer mithilfe unserer clamp Funktion um sicherzugehen dass das Maximum nie überschritten wird.
                getStatusEffect(copy.name)!!.duration = clamp(getStatusEffect(copy.name)!!.duration + dur, -1, copy.durationMax)
            } else {
                // Falls wir noch keinen Effekt mit dem Namen haben, fügen wir ihn einfach unserer Liste hinzu.
                effects.add(copy)
            }

            // Wir printen welchen Effekt wir auf wen und mit welcher Dauer hinzugefügt haben
            Prints.printAddStatusEffect(name, copy.getNameIcon(), dur)
        } else {
            /* Wir konnten keinen Statuseffekt hinzufügen, also printen wir eine Meldung aus die sagt dass wir keinen Erfolg hatten. */
            Prints.printActionFailed(name)
        }
    }

    /* Mit dieser Funktion können wir einen Statuseffekt von unserem Kämpfer wieder entfernen. Wir prüfen zuerst, ob wir solch einen
    Effekt überhaupt in unserer Liste haben und ob die Liste leer oder nicht ist. Wir geben entsprechend eine Nachricht aus,
    falls wir den Effekt nicht entfernen konnten (weil er nicht existiert!) */
    /** Tries to remove a StatusEffect [effect]. **/
    fun tryRemoveStatusEffect(effect: StatusEffect) {
        if (effects.isEmpty() || !effects.contains(effect)) {
            Prints.printNoRemovedStatusEffect(name)
            return  // Mit return wird der untere Code gar nicht ausgeführt, also brauchen wir hier keinen else{} Block!
        }

        // Printe aus was passiert und entferne dann den Effekt aus unserer Liste
        Prints.printRemovedStatusEffect(name, effect.getNameIcon())
        effects.remove(effect)
    }

    /* Diese Funktion dient dazu, alle Statuseffekte in unserer Liste die eine Dauer von 0 haben zu entfernen. Wir rufen diese
    * jedes mal auf wenn der Kämpfer zum Zug kommt. WICHTIG: Da wir permanente Effekte intern mit -1 kennzeichnen,
    * prüfen wir NICHT ob die Dauer kleiner-gleich 0 ist, SONDERN ob die Dauer NICHT 0 ist. */
    /** Removes all expired StatusEffects from this instance **/
    fun expireStatusEffects() {

        // filter{} gibt uns eine Liste zurück mit Elementen deren Bedingung innerhalb der {} Klammern erfüllt ist.
        val expired = effects.filter { it.duration == 0 }

        // Nun wollen wir für jeden Effekt den wir entfernen wollen eine Meldung ausgeben und anschließend entfernen.
        for (exp in expired) {
            Prints.printExpireStatusEffect(name, exp.getNameIcon())
            // removeIf{} geht automatisch durch eine Liste durch und löscht alle Elemente deren Bedingung in {} Klammern erfüllt ist.
            effects.removeIf {it.name == exp.name }
        }
    }

    /* tickStatusEffectsPre() wird in unserer Kampflogik immer aufgerufen, kurz bevor der Kämpfer am Zug ist. Dies kann z.B. sein, wenn er stunned ist, also gar nicht angreifen können soll, etc.. Zuerst resetten wir alle Zustände die dafür relevant sind,
     um sie anschließend, einer nach dem anderen zu aktivieren falls wir einen entsprechenden Statuseffekt in unserer Liste haben. */
    /** Applies StatusEffects on this instance before he can act. **/
    fun tickStatusEffectsPre() {
        // Wir resetten diese Werte weil wir sie jede Runde neu setzen, basierend auf unseren Statuseffekten
        isStunned = false
        isAggro = false
        speedReal = speed
        dmgReal = dmgBase
        resistReal = resistBase

        /* Nun gehen wir unsere Statuseffekte durch. Wir nutzen allerdings eine gefilterte Liste, denn wir wollen nur
        * Effekte mit bestimmten Typen durchsuchen (STUN und AGGRO in diesem Fall) */
        for (fx in effects.filter { it.type == StatusEffectType.STUN || it.type == StatusEffectType.AGGRO }) {

            // Gib eine Meldung aus, dass wir unter diesem Statuseffekt leiden
            Prints.printAffectStatusEffect(name, fx.getNameIcon())

            /* Wir ziehen von unserer Dauer 1 ab, denn wir haben diesen Statuseffekt für diesen Zug beachtet,
            * allerdings NUR, wenn unsere Dauer größer als 0 ist, denn wir möchten ja die Dauer für permanente
            * Statuseffekte die mit -1 gekennzeichnet werden, nicht beinflussen! */
            if (fx.duration > 0) {
                fx.duration--
            }

            /* Nun prüfen wir den Typ des jeweiligen Effekts */
            when (fx.type) {
                StatusEffectType.STUN -> {
                    Prints.printEffectStun(name)
                    // Wir setzen unseren Zustand isStunned auf true wenn wir einen STUN Effekt haben
                    isStunned = true
                }
                StatusEffectType.AGGRO -> {
                    Prints.printEffectAggo(name, fx.src!!.name)
                    // Wir setzen unseren Zustand isAggro auf true wenn wir einen AGGRO Effekt haben
                    isAggro = true
                }
                /* Aufgrund des Datentyps den wir nutzen, müssen wir ALLE Fälle beachten, also sagen wir einfach
                * WENN else DANN passiert nichts */
                else -> {}
            }
        }
    }

    /* tickStatusEffectsPost() wird in unserer Kampflogik immer aufgerufen, NACHDEM dieser Kämpfer seinen Zug gemacht hat.
    * Dies ist z.B. der Fall für Vergiftungen (DOTs) usw. */
    /** Applies StatusEffects on this instance after he acted. **/
    fun tickStatusEffectsPost() {

        /* Wir gehen nun jeden Statuseffekt durch, den wir noch nicht in unserer tickStatusEffectsPre() beachetet haben.
        * Hierfür filtern wir unsere Liste aktiver Effekte mit filterNot{}, also wir wollen eine Liste haben, die genau
        * die genannten Typen NICHT mit drin hat. */
        for (fx in effects.filterNot { it.type == StatusEffectType.STUN || it.type == StatusEffectType.AGGRO }) {

            // Gib eine Meldung aus, dass wir unter diesem Statuseffekt leiden
            Prints.printAffectStatusEffect(name, fx.getNameIcon())

            /* Wir ziehen von unserer Dauer 1 ab, denn wir haben diesen Statuseffekt für diesen Zug beachtet,
            * allerdings NUR, wenn unsere Dauer größer als 0 ist, denn wir möchten ja die Dauer für permanente
            * Statuseffekte die mit -1 gekennzeichnet werden, nicht beinflussen! */
            if (fx.duration > 0) {
                fx.duration--
            }

            /* Nun prüfen wir den Typ des jeweiligen Effekts */
            when(fx.type) {
                StatusEffectType.DOT -> {
                    /* Wir haben einen "damage over time" Effekt, also ziehe Schaden an uns ab. Wir setzen als Parameter
                    * canBlock auf false, denn wir möchten nur direkte Attacken abblocken können. Wenn du bereits blutest,
                    * nützt deine Rüstung ja auch nichts! */
                    takeDamage(fx.powerRange.random(), false)
                }
                StatusEffectType.BUFF_BLOCK -> {
                    /* Wir haben einen Effekt, der unseren Blockwert erhöht. Da wir diesen Wert jeweils bei der Schadensfunktion
                    * takeDamage() individuell berechnen, müssen wir hier nicht viel weiter machen außer eine Meldung auszugeben. */
                    Prints.printEffectBlock(name)
                }
                StatusEffectType.DEBUFF_SPD -> {
                    /* Wir haben einen Effekt, der unsere Geschwindigkeit verringert. Wir ziehen also den Prozentwert unserer
                    * Basisgeschwindigkeit an unserer realen Geschiwndigkeit ab. */
                    speedReal -= (speed * (fx.powerRange.random().toDouble() / 100.0)).toInt()

                    Prints.printEffectDebuffSpd(name)
                }
                StatusEffectType.HOT -> {
                    /* Wir haben einen "heal over time" Effekt, also heile uns. */
                    takeHeal(fx.powerRange.random())
                }
                StatusEffectType.BUFF_ATK -> {
                    /* Wir haben einen Effekt, der unseren Angriffswert erhöht. Wir addieren also den Prozentwert unseres
                    * Basisangriffswert zu unserem realen Angriffswert. */
                    dmgReal += (dmgBase * (fx.powerRange.random().toDouble() / 100.0))

                    Prints.printEffectBuffAtk(name)
                }
                StatusEffectType.BUFF_RES -> {
                    /* Wir haben einen Effekt, der unsere Resistenz gegen feindliche Statuseffekte erhöht. Wir addieren also den
                    Prozentwert des Effekts zu unserer realen Resistenz. */
                    resistReal += fx.powerRange.random().toDouble() / 100.0

                    Prints.printEffectBuffRes(name)
                }
                /* Aufgrund des Datentyps den wir nutzen, müssen wir ALLE Fälle beachten, also sagen wir einfach
                * WENN else DANN passiert nichts */
                else -> {}
            }
        }
    }



    /* Hilfsfunktion, die uns einen String baut, um ein sogenannten "nameplate" darzustellen. Dieses "nameplate"
    * besteht aus einem Lebensbalken (den wir mit einer anderen Hilfsfunktion bauen), unserem Namen und kleine Icons
    * activer Statuseffekte und ihre Dauer (die wir ebenfalls mit einer Hilfsfunktion bauen).
    * Wir nutzen hier take() und padEnd() um ein bestimmtes Format einzuhalten.
    * Mit take(n) limitieren wir den String des Lebensbalken auf eine Länge von 50 Zeichen.
    * Mit padEnd(length, padChar) erweitern wir den String des Lebensbalkens mit Leerzeichen, falls dieser kürzer sein sollte.
    *
    * Ergebnis ist, dass wir zwischen Lebensbalken und Namen einen bestimmten, immer gleichen Abstand haben.
    * ├██████████┤ ♥ HP: ???/???      	NAME	☃(2)
    * */
    /** Displays a nameplate, consisting of a healthbar, name and icons + duration of active StatusEffects **/
    fun fancyDisplayNamePlate(): String {

        return "${fancyDisplayHealthFull().take(50).padEnd(50, ' ')}\t${name}\t${Colors.getColorText(fancyDisplayStatusEffects(), Colors.YELLOW)}"
    }

    /* Hilfsfunktion, die uns einen String baut, um unsere aktiven Statuseffekte schön als kleine Icons mit ihrer Dauer
    * aneinanderzureihen. Das Ergebnis sähe dann z.B. so aus (je nach Statuseffekten):
    * ☃(2) ☹(3) ☠(5) */
    /** Displays active StatusEffects and their duration in a horizontal series. **/
    private fun fancyDisplayStatusEffects(): String {

        var cols = ""

        for (fx in effects) {
            cols += "${fx.getIconDuration()} "
        }

        return cols
    }

    /* Hilfsfunktion, die uns einen String baut, um unseren Lebensbalken gefolgt von unserem Gesundheitsstatus
    * (bestehend aus einem kleinen Gesundheitsicon und unsere aktuelle sowie maximale Gesundheit) anzuzeigen.
    * Wir nutzen hierfür die anderen Hilfsfunktionen, um uns diesen String zusammenzubasteln.
    * Das Ergebnis sähe z.B. dann so aus:
    * ├████░░░░░░┤ ♥ HP: 12/28 */
    /** Displays a health bar and health info (current HP/maxHP) **/
    fun fancyDisplayHealthFull(): String {

        return "${fancyDislayHealthBar()} ${fancyDisplayHealthInfo()}"
    }

    /* Hilfsfunktion, die uns einen String baut, um unseren Lebensbalken darzustellen. Im Grunde berechnen wir
    * nur die aktuelle Prozentzahl unserer HP und bauen unseren Lebensbalken aus 10 Teilen: Wir gehen jede 10% unserer
    * HP mithilfe einer Schleife durch und überprüfen, ob dieses 10% Segment oberhalb oder unterhalb unserer aktuellen
    * Gesundheit als Prozentwert liegt. Je nach Ergebnis fügen wir dann ein ausgefülltes oder ein transparentes Balkenteil
    * ein. Zeichen in einem String können viel mehr sein als einfache Zahlen oder Buchstaben, je nach Zeichenkodierung! Da
    * wir in der Kodierung UTF-8 (siehe rechts unten bei IntelliJ) können wir einfach bei Google nach UTF-8 Zeichen suchen.
    * Eine Übersicht über mögliche Zeichen in UTF-8 findest du z.B. hier: https://www.w3schools.com/charsets/ref_html_utf8.asp
    * Das Ergebnis sähe z.B. dann so aus (bei bspw. 30%):
    * ├███░░░░░░░┤
       1234567890  > Von 1-10 also 10%, 20%, 30%, usw. */
    /** Displays a health bar, segmented into each 10% of the Fighters health. **/
    private fun fancyDislayHealthBar(): String {

        /* Wir berechnen die Prozentzahl unserer aktuellen Gesundheit. Einfacher Dreisatz:
        *     maxHp = 100%
        *         1 = ?%
        * currentHp = AKTUELLE GESUNDHEITSPROZENT
        * */
        val percentage: Double = (100.0 / maxHp) * currentHp

        // Wir fügen den linken Teil des Rahmens an einen String, sowie die Farbe Rot für unseren tatsächlichen Balken
        var bar = "├${Colors.RED}"

        /* Nun gehen wir jede 10% unserer Gesundheit, da wir wollen dass der Balken aus 10 Teilen besteht, durch
        * und prüfen ob diese 10% kleiner-gleich als unsere aktuelle Gesundheitsprozentzahl ist.
        * Ist das der Fall, fügen wir einen ausgefülltes Balkenteil ein, falls nicht ein transparentes Balkenteil. */
        for (i in 1..10) {
            bar += if (i*10 <= percentage.toInt()) {
                "█"
            } else {
                "░"
            }
        }

        // Nun setzen wir die rote Farbe zurück und setzen den rechten Teil des Rahmens ein.
        bar += "${Colors.RESET}┤"

        return bar
    }

    /* Hilfsfunktion, die uns unsere Gesundheitsinfo als String baut. Wir setzen hier eigentlich nur eine Aufschrift
    * sowie unsere aktuelle und maximale Gesundheit ein.
    * Das Ergebnis sähe dann z.B. so aus: ♥ HP: 12/28  */
    /** Displays health info, consisting of a heart icon, the label HP and [currentHp]/[maxHp] **/
    private fun fancyDisplayHealthInfo(): String {
        return "${Colors.RED}♥${Colors.RESET} HP: ${currentHp}/${maxHp}"
    }
}
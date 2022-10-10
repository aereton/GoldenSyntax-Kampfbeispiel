import data.Enemies

/* Ein ENUM ist ein jeweils eigener Datentyp den wir hier erstellen. Wenn wir eine Variable haben, die ein Objekt mit
* diesem Typ enthält, kann diese Variable nur EINEN dieser unten deklarierten Zustände haben. Stell es dir vor
* wie eine Ampel: Sie kann nur GRÜN, GELB oder ROT sein. Das nutzen wir um später mit einer einfachen when() oder if()
* Abfrage zu prüfen, was für ein Gegnertyp eine Instanz ist. */
enum class EnemyType {
    MINION, DRAGON_UNDEAD, DRAGON_FIRE, DRAGON_ICE
}


/* =============================================
            Klasse für Gegner
 ===============================================
 Diese Klasse erbt von der Basisklasse der Kämpfer, denn wir möchten den Kämpfer mit einigen Gegnerspezifischen Daten
 und Funktionen erweitern, z.B. das selbstständige Auswählen von Fähigkeiten oder Zielen wenn dieser Gegner am Zug ist.
 Parameter im Konstruktor:
              name = der Name des Kämpfers
             maxHp = die maximale Gesundheit
             speed = die Geschwindigkeit die bestimmt, wann dieser Kämpfer einen Zug machen kann;
                     je höher desto eher ist er an der Reihe
 summonHpThreshold = eine Prozentzahl von HP die angibt, wann dieser Gegner eine SUMMON Fähigkeit einsetzen kann, um
                     Helfer im Kampf zu beschwören
      spawnMessage = String den wir nutzen, um für jeden Gegner eine individuelle Nachricht auszugeben, wenn dieser im Kampf
                     erscheint.
              type = Unser Gegnertyp. Wir unterscheiden zwischen den unterschiedlichen Drachentypen sowie beschworenen Helfern.
                     Dies benutzen wir um festzustellen, welche Art von Helfern wir beschwören sollen */
/**
 * [Enemy] class derived from [Fighter] with a [name], [maxHp] for maximum health, [speed]
 * for turn order decided each round during a [Battle], [summonHpThreshold] for spawn skill limitation, [spawnMessage] for
 * individual spawn messaging and [type] of [EnemyType] to determine which kind this enemy is.
 */
class Enemy(name: String, maxHp: Int, speed: Int, private val summonHpThreshold: Int, val spawnMessage: String, val type: EnemyType) : Fighter(name, maxHp, speed) {

    // Eine Warteschlange an Gegnern die wir beschwören wollen
    var minionQueue: MutableList<Enemy> = mutableListOf()
    // Wir wollen festhalten, welche Fähigkeit wir als Letztes eingesetzt haben, damit wir manche nicht dauernd wiederholen
    var prevSkillUsed: Skill? = null


    /* Mit dieser Funktion lassen wir den Gegner entscheiden, welche Fähigkeit er in seinem Zug einsetzen soll. Wir müssen hier
    * verschiedene Dinge beachten, z.B. ob er diese Fähigkeit überhaupt einsetzen kann, usw... Haben wir alle Optionen bedacht,
    * können wir uns eine per Zufall herauspicken. Als Parameter müssen wir immer eine Liste an Verbündeten übergeben, weil
    * wir auch abhängig vom Zustand der Anderen Entscheidungen treffen möchten. */
    /** Returns a suitable [Skill] to use. [party] is a List with elements of type [Fighter] that are on the same team. **/
    fun aiSelectSkill(party: List<Fighter>): Skill? {

        /* Zuerst wollen wir uns eine Liste an möglichen Fähigkeiten abspeichern, um diese weiter unten weiter einzuschränken,
        * je nachdem was für Bedingungen für diesen Gegner herrschen. Wir holen uns also eine gefilterte Kopie unserer
        * Fähigkeitenliste, die uns nur Fähigkeiten übergibt, die noch eingesetzt werden können. Eine Fähigkeit, deren
        * usesLeft auf 0 steht, ist erschöpft. Wir prüfen aber nicht ob usesLeft kleiner-gleich 0 ist, denn eine Fähigkeit
        * darf auch manchmal unendlich oft einsetzbar sein, was wir dann in der jeweiligen Fähigkeit mit -1 kennzeichnen. */
        val skillOptions: MutableList<Skill> = skills.filter { it.usesLeft != 0}.toMutableList()

        /* Zuerst wollen wir prüfen, ob wir unsere Beschwörungsfähigkeit(en) aus unseren Optionen entfernen sollen. Das ist
        * immer dann der Fall, wenn unsere aktuelle Gesundheit über unserem festgelegten Prozentwert summonHpThreshold liegt. */
        val hpLimit: Int = (maxHp * (summonHpThreshold / 100.0)).toInt()        // Prozentwert von aktueller Gesundheit ausrechnen

        if (currentHp > hpLimit) {
            /* Mit der Listenfunktion .removeIf{} wird durch die ganze Liste iteriert, und falls die Bedingung innerhalb
            * der {} Klammern zutrifft, wird das jeweilige Element rausgelöscht. */
            skillOptions.removeIf { it.type == SkillType.SPAWN }
        }

        /* Wir wollen außerdem keine Heilungsfähigkeiten einsetzen, falls die Gruppe unserer Verbündeten bereits volle
        * Gesundheit haben. Mit der Listenfunktion .all{} können wir überprüfen, ob auf ALLE Elemente in unserer Liste
        * die Bedingung innerhalb der {} Klammern zutrifft. Trifft es bei einem Element nicht zu, wird von .all{} false
        * zurückgegeben. */
        if (party.all { it.hasFullHealth() }) {
            /* Alle Verbündete haben volle Gesundheit, deshalb wollen wir jetzt alle Fähigkeiten aus unseren Optionen
            * rauslöschen, die entweder vom Fähigkeitstyp HEAL sind, oder die einen StatusEffekt mit dem Typ
            * HOT (= heal over time) verursachen können. */
            skillOptions.removeIf { (it.type == SkillType.HEAL) || ((it.skillEffect != null) && (it.skillEffect.type == StatusEffectType.HOT)) }
        }

        /* Außerdem wollen wir die Fähigkeit aus unseren Optionen, die wir in der letzten Runde genutzt haben. Das
        * speichern wir bei jeder Aktion neu ab. Wir wollen außerdem nur die zuletzt genutzten Fähigkeiten entfernen,
        * die nicht nacheinander in Serie benutzt werden dürfen, manche dürfen das nämlich! */
        skillOptions.removeIf { !it.useSuccessiveByAi && it === prevSkillUsed }

        // Nun können wir eine zufällige Fähigkeit aus unseren verbleibenden Optionen zurückgeben, falls die Liste nicht leer ist.
        return if (skillOptions.isNotEmpty()) {
            skillOptions.random()
        } else {
            null
        }
    }

    /* Diese Funktion dient dazu, auf Basis der Fähigkeit die wir benutzen wollen, ein oder mehrere geeignete Ziele zu finden.
    * Wir müssen also eine Fähigkeit skill übergeben, sowie jeweils eine Liste von Verbündeten und Gegenkämpfern. */
    /** Returns a suitable list of [Fighter] targets. Takes in the [Skill] we're about to use, a list of allies and a
     * list of opponents. **/
    fun aiSelectTargets(skill: Skill, party: List<Fighter>, opponents: List<Fighter>): List<Fighter> {

        // Wir erstellen eine neue Liste, in der wir ein oder mehrere Ziele abspeichern wollen.
        val targets = mutableListOf<Fighter>()

        // Mit einer when Abfrage prüfen wir, auf Basis des Typs der genutzten Fähigkeit, welche Ziele wir zu unserer Liste hinzufügen
        when (skill.availableTargets) {
            // Füge einen zufälligen Gegenkämpfer hinzu
            SkillTarget.OPPONENT_SINGLE -> targets.add(opponents.random())
            // Füge ALLE Gegenkämpfer hinzu
            SkillTarget.OPPONENT_ALL -> targets.addAll(opponents)
            // Füge ALLE Verbündeten hinzu, die nicht ich SELBST sind
            SkillTarget.PARTY_ALL -> targets.addAll(party.filter { it != this }) // Wir filtern nach allen, die NICHT ich sind
            // Füge ALLE Verbündeten hinzu, inklusive mich selbst
            SkillTarget.PARTY_SELF -> targets.addAll(party)
            // Füge ein verbündetes Ziel hinzu
            SkillTarget.PARTY_SINGLE -> {
                // Falls wir einen Heilskill verwenden, wollen wir nur ein zufälliges Ziel haben, dessen HP nicht voll sind.
                if (skill.type == SkillType.HEAL) {
                    targets.addAll(party.filter { !it.hasFullHealth() })  // Wir filtern nach allen, die KEINE vollen HP haben
                    val rnd = targets.random()                           // Wir suchen uns ein zufälliges Ziel aus
                    targets.removeIf { it != rnd }                      // Dann löschen wir alle die nicht unser rausgesuchtes sind
                } else {
                    // Füge einfach ein zufälliges, verbündetes Ziel hinzu
                    targets.add(party.random())
                }
            }
            // Füge mich selbst hinzu
            SkillTarget.SELF -> targets.add(this)
        }
        // Gib unsere Liste zurück
        return targets
    }

    /* Mit dieser Fähigkeit werden neue Helfer für unseren Boss erschaffen, welche das sind hängt von unserem Gegnertyp ab,
    * den wir bei Instanziierung festgelegt haben. Wir übergeben die Anzahl an Helfern die wir erschaffen wollen. Sie können
    * aber noch nicht am Kampf teilnehmen, weil sie noch nicht der Gegnergruppe in Battle.kt zugewiesen wurden.
    * Wir lösen das so, dass wir alle zukünftigen Kampfteilnehmer in eine Liste als Warteschlange hinzufügen, von der aus
    * wir dann in unserer Kampflogik zugreifen und diese jeweils unserer Gegnergruppe zuteilen können. */
    /** Creates [amount] of minions based on this instance's [EnemyType] and adding them to the [minionQueue] list of Fighters. **/
    fun spawnMinion(amount: Int) {

        // Wir durchlaufen diese Schleife so oft wie wir Gegner erschaffen wollen.
        for (i in 1..amount) {
            // Nun prüfen wir welche Gegner wir erschaffen wollen basierend auf unserem Gegnertyp
            when (type) {
                EnemyType.DRAGON_UNDEAD -> {
                    /* Der Untotendrache ist eine Besonderheit, weil er zwei unterschiedliche Diener herbeirufen kann.
                    * Wir generieren also eine Zufallszahl zwischen 0 und 1 und je nach Ergebnis erschaffen wir den einen
                    * oder den anderen Diener. */
                    val rnd = (0..1).random()
                    val minion: Enemy = if (rnd == 0) {
                        Enemies.createMinionCorpse()
                    } else {
                        Enemies.createMinionFly()
                    }
                    minionQueue.add(minion)     // Hier wird der zukünftige Diener der Warteschlange hinzugefügt.
                }
                EnemyType.DRAGON_FIRE -> {
                    val minion: Enemy = Enemies.createMinionFireElemental()
                    minionQueue.add(minion)
                }
                EnemyType.DRAGON_ICE -> {
                    val minion: Enemy = Enemies.createMinionIceGolem()
                    minionQueue.add(minion)
                }
                else -> {
                    return          // Falls wir kein Gegnertyp des Untotendrachen, Feuerdrachen oder Eisdrachen sind, beende die Funktion
                }
            }
        }
    }
}
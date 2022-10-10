import utils.Prints
import java.lang.Exception


/* =============================================
            Klasse für Kampflogik
 ===============================================
 Diese Klasse enthält alle Daten und Funktionen die für das Ausführen eines Kampfes notwendig sind. Wir haben
 zwei Teams, einmal eine Liste an Helden und eine Liste an Gegner. Wir haben außerdem eine Referenz zu unserem Inventar.
 Wollen wir einen Kampf starten, rufen wir doBattle() auf. Dies startet eine while Schleife die solange geht, bis der
 Kampf vorbei (isOver = true) ist. In dieser Schleife rufen wir einfach immer wieder die Funkion battleUpdate() auf,
 die also eine ganze Runde (jeder Kämpfer macht jeweils einen Zug) darstellt. Innerhalb battleUpdate() wiederrum, haben
 wir eine erneute Schleife, die die einzelnen Spielzüge darstellt. Wichtig ist hier auch eine weitere Liste von Kämpfern,
 die turnSet Liste. Sie bildet die Reihenfolge der Spielzüge ab, mit deren Hilfe battleUpdate() dann die einzelnen Züge
 durchiterieren kann. Die turnSet Liste wird jede Runde geleert und dann wird neu entschieden, wer wann drankommt.
 Parameter im Konstruktor:
    partyHeroes         = eine Liste der spielbaren Kämpfer
    partyEnemies        = eine Liste der computergesteuerten Kämpfer (=Gegner)
    inventory           = Referenz zu unserem Inventar
    */
/**
 * Battle class controlling the data and behaviour necessary for turn based combat with [partyHeroes] for a list of player fighters, [partyEnemies] for a list of computer enemies and [inventory] to reference an inventory.
 */
class Battle(private val partyHeroes: MutableList<Hero>, private val partyEnemies: MutableList<Enemy>, private val inventory: Inventory) {

    // Zeigt an, ob der Kampf vorbei ist oder noch läuft
    private var isOver: Boolean
    // Unsere Liste der Reihenfolge der Kämpfer, wann sie am Zug sind.
    private var turnSet: MutableList<Fighter>


    /* Mit dem Schlüsselwort 'init' können wir Code bereits bei Initialisierung der Instanz ausführen
    * Wir nutzen die Gelegenheit um einigen Variablen einen initialen Wert zuzuweisen. */
    init {
        this.isOver = false         // Wenn wir eine neue Instanz erstellen, ist der Kampf ja noch nicht gelaufen
        turnSet = getNextTurnSet()  // Wir wollen für die erste Runde bereits eine Zug-Reihenfolge generieren
    }

    /* Mit dieser Funktion können wir einen Kampf starten, der dann solange läuft bis wir dieser Instanz sagen
    * dass er vorbei sein soll (isOver=true) */
    /** Starts and processes an entire battle until it [isOver]. **/
    fun doBattle() {
        // Warte 3000 Millisekunden, also 3 Sekunden bis zur nächsten Anweisung
        Thread.sleep(3000)
        // Wiederhole unsere Kampfrunden solange der Kampf läuft, also so lange isOver auf false steht
        while (!isOver) {
            battleUpdate()      // Hauptlogik passiert in dieser Funktion
        }
    }

    /* Die Hauptlogik unseres Kampfes. Das Aufrufen dieser Funktion stellt jeweils eine Runde dar, also eine Runde
    * in der jeder Kampfteilnehmer jeweils einen Spielzug durchführen kann.
    * Diese Funktion ist ziemlich lang, das ist Spaghetti-Code. Man kann diese Logik durchaus auch klüger und
    * ordentlicher implementieren, allerdings wollte ich keine erweiterten Konzepte (z.B. Finite State Machines) verwenden,
    * sondern hauptsächlich Konzepte die wir bereits im Grundlagenmodul kennengelernt haben (ausgenommen vielleicht
    * Dinge wie das Sortieren von Listen, data classes, etc - diese sind aber recht einfach nachzuvollziehen mit
    * dem Wissen das wir bereits erlangt haben). */
    /** Main battle round logic. Gets called each round by [doBattle]. **/
    private fun battleUpdate() {

        // Wir benötigen einen Zähler, der sich an der Anzahl der Kampfteilnehmer misst. Für jeden Spielzug wird dieser um 1 erhöht.
        var turnCounter = 0

        /* Unsere gesamte Rundenschleife. Wir wiederholen diese Schleife solange unser Spielzugzähler (turnCounter)
        * kleiner als die Größe der Spielzugliste ist (also die Anzahl an Teilnehmer) */
        while (turnCounter < turnSet.size) {

            /* Wir wollen einen Spielzug machen, wir rufen da unsere Funktion procTurn() auf. Da die Funktion ein Boolean
            * zurückgibt, können wir den Aufruf der Funktion auch gleich in eine if Abfrage setzen und den Spielzugzähler
            * bei Erfolg um 1 erhöhen. Das tun wir deshalb, weil der Spieler bspw. aus Untermenüs zurück ins Hauptmenü
            * wechseln können soll, in dem Fall geben wir in der procTurn() Funktion FALSE zurück, damit wir wissen
            * dass sein Spielzug noch nicht beendet wurde. Dann wiederholt sich die while Schleife einfach nochmal,
            * ohne zum nächsten Spielzug überzugehen. Die gesamte Spielzuglogik wird durch unseren procTurn() Aufruf
            * durchgeführt. Dieser Funktion übergeben wir außerdem als Parameter den aktuellen Kämpfer, der am Zug ist
            * (befindet sich in der Liste turnSet mit einem index gleich = Zugzähler!) */
            if (procTurn(turnSet[turnCounter])) {
                Thread.sleep(2400)
                turnCounter++
            }

            /* Ein Spielzug wurde durchgeführt. Falls jemand in der Zwischenzeit gestorben ist, wollen wir denjenigen
            * aus unseren Listen entfernen. Das können wir mit der Listenfunktion .removeIf{} tun, die durch jedes
            * Element in ihrer Liste iteriert und das jeweilige Element löscht, wenn dessen Boolean isDead = false ist. */
            turnSet.removeIf { it.isDead }
            partyHeroes.removeIf { it.isDead }
            partyEnemies.removeIf { it.isDead }

            /* Wir wollen nach jedem durchgeführten Spielzug überprüfen, ob eines der beiden Teams gewonnen hat. Da
            * gestorbene Kämpfer aus ihrer zugehörigen Liste (Spieler oder Computergegner) entfernt werden, können
            * wir einfach überprüfen, ob eine dieser beiden Listen leer ist. */
            if (partyEnemies.isEmpty()) {

                // Ist die Liste von Computergegnern leer, geben wir eine Meldung aus, dass der Spieler gewonnen hat.
                Prints.printBattleWin()
                /* Wir wollen außerdem unseren Kampf beenden. Daher setzen wir auf isOver auf false und geben die
                * Funktion mit return zurück, also wir beenden sie. Damit kann dann keiner mehr einen Spielzug machen
                * und der Kampf wird sofort beendet. */
                isOver = true
                return
            } else if (partyHeroes.isEmpty()) {
                // Ist die Liste von Spielerhelden leer, geben wir eine Meldung aus, dass der Spieler verloren hat.
                Prints.printBattleLose()

                isOver = true
                return
            }
        }

        // Wir haben eine ganze Runde vollzogen, nun wollen wir für die nächste Runde eine neue Reihenfolge generieren
        turnSet = getNextTurnSet()
    }

    /* Diese Funktion steuert nun, was passiert oder passieren kann, wenn ein Kampfteilnehmer am Zug ist.
    * Wir wollen aktive Statuseffekte dieses Kämpfers kalkulieren (manche VOR und manche NACH dem Spielzug)
    * und das Verhalten festlegen, je nachdem ob der Kämpfer ein spielbarer Held oder ein computergesteuerter Gegner
    * ist. Ist er ein Gegner, dann wollen wir seine KI den Spielzug bestimmen lassen, ist er ein Held wollen
    * wir ein Aktionsmenü einblenden und dann je nach Eingabe eine Entscheidung treffen lassen. Diese Funktion ist
    * ein bisschen ineinander verschachtelt, um die ganzen Untermenüs (wie z.B. Fähigkeit auswählen, Inventargegenstände auswählen,
    * etc) zum Laufen zu bringen, ich habe aber versucht alles so verständlich wie möglich zu unterteilen damit auch
    * nachvollziehbar ist, was gerade wo passiert. */
    /** Processes one turn of the given [currentTurn] as [Fighter] and its available actions. **/
    private fun procTurn(currentTurn: Fighter): Boolean {

        /* Sollte der Spieler aus einem Untermenü zurücknavigieren, wird diese Funktion mit return false zurückgegeben und
        * beendet. D.h. dass diese Funktion erneut durch die while Schleife in battleUpdate() ausgeführt wird. Wir wollen aber
        * z.B. nicht, dass gewisse Statuseffekte den Spieler erneut betreffen, daher überprüfen wir ob der aktuelle
        * Kämpfer noch nicht am Zug war. Dieser Wert isTurn wird dann direkt nach dieser if Abfrage auf true gestellt
        * und erst wieder auf false gestellt, wenn er eine valide Aktion getroffen hat (Fähigkeit eingesetzt, Inventargegenstand
        * benutzt oder Warten ausgewählt). */
        if (!currentTurn.isTurn) {

            /* Wir wollen Statuseffekt von uns entfernen, die nicht mehr gültig sind, also deren Dauer 0 erreicht hat.
            * Wir wollen außerdem manche Statuseffekte triggern, falls wir sie haben (z.B. Lähmung). */
            currentTurn.expireStatusEffects()
            currentTurn.tickStatusEffectsPre()

            /* Sollten wir gelähmt sein, müssen wir diesen Zug aussetzen. Also triggern wir mögliche Statuseffekte die
            * nach einem Zug getriggert werden sollen und geben diese Funktion als true zurück, also
            * dass wir mit unserem Zug fertig sind. */
            if (currentTurn.isStunned) {

                currentTurn.tickStatusEffectsPost()

                return true
            }

            /* Sind wir gezwungen ein bestimmtes Ziel anzugreifen (durch einen Statuseffekt), dann wollen wir den Kämpfer
            * eine zufällige Angriffsfähigkeit auswählen und durchführen lassen und unseren Zug beenden. */
            if (currentTurn.isAggro) {

                // Wir müssen erstmal den Urheber unseres AGGRO Statuseffekts finden
                val fxTarget: Fighter? = currentTurn.effects.find { it.type == StatusEffectType.AGGRO }!!.src

                // Falls wir den Urheber gefunden haben, führe eine zufällige Fähigkeit aus
                if (fxTarget != null) {

                    /* Wir müssen uns eine zufällige Fähigkeit heraussuchen. Wir filtern also unsere Liste an Fähigkeiten
                    * nach folgenden Kriterien (beide Punkte müssen erfüllt sein):
                    *               - Darf NICHT verbraucht sein (usesLeft muss also über null ODER -1 (unendlich) sein
                    *               - Die zulässigen Ziele für die Fähigkeit ENTWEDER ein einzelnes oder mehrere
                    *                   feindliche Ziele sein
                    * Aus dieser gefilterten Liste suchen wir uns dann einfach ein zufälliges Element mit .random() heraus */
                    val rndSkill = currentTurn.skills.filter {it.usesLeft != 0 && (it.availableTargets == SkillTarget.OPPONENT_SINGLE || it.availableTargets == SkillTarget.OPPONENT_ALL)}.random()

                    // Wir haben unseren Zug getan, also triggern wir mögliche Statuseffekte die nach einem Zug getriggert werden sollen
                    currentTurn.tickStatusEffectsPost()

                    // Führe die zufällige Fähigkeit auf den Urheber aus
                    currentTurn.castSkill(currentTurn.skills.indexOf(rndSkill), listOf(fxTarget))

                    // Wir haben unseren Zug beendet
                    return true
                }
            }
        }

        /* Nun sind wir wirklich an der Reihe, können also unsere Auswahl treffen. */
        currentTurn.isTurn = true

        /* Wir wollen wissen, ob der aktuelle Kämpfer ein Computergegner oder ein Spielerheld ist. */
        if (currentTurn is Enemy) {
            // ===============
            // Wir sind ein Computergegner
            // ===============

            /* Wir machen ein bisschen Platz in der Konsole, fügen einen Trennbalken ein und drucken dann den aktuellen
            * Status des Kämpferteams aus, des Kämpfers, der an der Reihe ist (Name, Gesundheit, etc).*/
            Prints.printEmptyLine()
            Prints.printDivider2()
            /* Status des Kämpferteams; diese Funktion benötigt eine Liste an Verbündeten des Kämpfers, der gerade an der
            * Reihe ist. Daher filtern wir unsere Reihenfolge-Liste nach Kämpfern, die vom Typ
            * der Klasse Enemy sind (da wir ja gerade in der Computergegnerlogik sind). Wir müssen außerdem
            * den Kämpfer angeben, der gerade an der Reihe ist, damit wir unsere Ausgabe immer nach der Reihenfolge
            * ordnen können: AKTUELLER KÄMPFER | NÄCHSTES TEAMMITGLIED | NÄCHSTES TEAMMITGLIED usw */
            Prints.printPartyMembersStatus(turnSet.filterIsInstance<Enemy>(), currentTurn)

            /* Wir lassen die Gegner KI eine Fähigkeit auswählen. Das Ergebnis das wir aus der Funktion aiSelectSkill()
            * erhalten, wollen wir ersteinmal abspeichern. */
            val useSkill = currentTurn.aiSelectSkill(partyEnemies)

            /* Falls die Gegner KI eine Fähigkeit auswählen konnte, wollen wir sie natürlich einsetzen. Falls sie keine
            * Fähigkeit finden konnte, wollen wir eine Art Rückversicherung haben und den nächsten Zug dranlassen, also
            * diesen Spielzug korrekt beenden. */
            if (useSkill != null) {

                /* Um eine Fähigkeit einsetzen zu können, brauchen wir natürlich auch ein Ziel. Das entscheidet die
                * Funktion aiSelectTargets() unserer Gegner KI anhand des gewählten Ziels. Wir speichern
                * das zurückgegebene Ziel erst einmal in einer Variable ab. */
                val targets = currentTurn.aiSelectTargets(useSkill, partyEnemies, partyHeroes)

                /* Jetzt setzen wir die Fähigkeit gegen unser Ziel(e) ein. */
                currentTurn.castSkill(currentTurn.skills.indexOf(useSkill), targets)

                // Setze die verwendete Fähigkeit als unsere zuletzt genutzte Fähigkeit
                currentTurn.prevSkillUsed = useSkill

                /* Wir haben unseren Spielzug im Prinzip beendet. Wir setzen also isTurn auf false. Wir geben allerdings
                * noch nicht true zurück, denn wir müssen erst noch einige wenige Gegnerspezifische Sachen erledigen. */
                currentTurn.isTurn = false

                /* Sollten wir in unserer Helfer-Warteschlange jemanden haben, wollen wir diese dem Kampf hinzufügen.
                Haben wir keine Elemente in unserer Warteschlange, wird die for Schleife sowieso nicht ausgeführt. */
                for (queued in currentTurn.minionQueue) {

                    /* Bevor wir den Helfer dem Kampf hinzufügen, prüfen wir außerden, ob nicht der Helfer bereits am
                    * Kampf teilnimmt. Das mag zwar überflüssig erscheinen, ist aber einfach eine Art fail-safe, also
                    * eine Absicherung gegen ungewolltes Verhalten. Somit lassen sich unerwünschte Bugs vermeiden. */
                    if (!partyEnemies.contains(queued)) {

                        // Jetzt wollen wir den Helfer dem Kampf hinzufügen
                        partyEnemies.add(queued)

                        Prints.printSpawnMinion(queued.name, queued.spawnMessage)
                    }
                }

                // Mit .clear() können wir eine Liste komplett leeren. Wir wollen niemanden mehr in unserer Warteschlange haben.
                currentTurn.minionQueue.clear()

                currentTurn.tickStatusEffectsPost()

                // Jetzt können wir true zurückgeben, also dass unser Spielzug tatsächlich beendet ist
                return true

            } else {

                /* Gegner KI konnte keine Fähigkeit zum Einsetzen finden, daher wollen wir unseren Spielzug einfach beenden. */
                Prints.printNoSkillsIdle(currentTurn.name)

                currentTurn.prevSkillUsed = null    // Da wir keine Fähigkeit eingesetzt haben, wollen wir das auch so festhalten

                currentTurn.tickStatusEffectsPost()

                currentTurn.isTurn = false
                return true

            }
        } else {

            // ===============
            // Wir sind ein Spieler
            // ===============

            /* Wir machen ein bisschen Platz in der Konsole, fügen einen Trennbalken ein und drucken dann den aktuellen
            * Status des Kämpferteams aus, des Kämpfers, der an der Reihe ist (Name, Gesundheit, etc).*/
            Prints.printEmptyLine()
            Prints.printDivider2()
            Prints.printPartyMembersStatus(turnSet.filterIsInstance<Hero>(), currentTurn)

            /* Wir wollen außerdem unser Hauptmenü anzeigen lassen */
            Prints.printMainMenu()

            /* Wir wollen jeden Input abfangen, der keine Zahl ist. Das können wir mit try-catch weil wir jeden Input
            * versuchen in einen Integer zu verwandeln. */
            try {

                /* Das ist die Eingabe für unser Hauptmenü
                *   [1] Use Skill
                *   [2] Inventory
                *   [3] Status
                *   [4] Wait
                * Wir können die readln() Anweisung auch direkt als Bedingung für unsere when Abfrage eintragen. */
                when (readln().toInt()) {

                    // SKILL: Wir möchten eine Fähigkeit einsetzen
                    1 -> {

                        // Liste zuerst die Fähigkeiten des aktuellen Kämpfers auf
                        Prints.printSkillsMenu(currentTurn.skills)

                        /* Wir lesen einen erneuten Input. Dieser soll automatisch 1 abgezogen bekommen, da wir diesen
                        * Wert als index für unsere Listen nehmen werden. In der Anzeige wird allerdings 1,2,3,etc
                        * angezeigt als Zahl die wir eingeben sollen, also wollen wir automatisch aus 1 -> 0, 2 - > 1, etc machen */
                        val inputSkill = readln().toInt() - 1

                        /* Wir überprüfen, ob unsere Eingabe größer-gleich 0 und kleiner als die Anzahl an Fähigkeiten ist */
                        if (inputSkill >= 0 && inputSkill < currentTurn.skills.size) {

                            /* Jetzt wollen wir sicherstellen, dass wir ein korrektes Ziel haben. Wir erstellen erst einmal
                            * eine leere Liste Mit allen Zielen die wir haben wollen. Wir speichern uns außerdem ab, welche Art
                            * von Zielen wir für diese Fähigkeit nutzen dürfen. */
                            val targets: MutableList<Fighter> = mutableListOf()
                            val availableTargets = currentTurn.skills[inputSkill].availableTargets

                            /* Falls die Zielart der Fähigkeit eines dieser Werte entspricht, wollen wir ein Ziel selbst auswählen:
                            *       OPPONENT_SINGLE         = Ein einzelnes gegnerisches Ziel
                            *       PARTY_SINGLE            = Ein einzelnes verbündetes Ziel OHNE den Anwender
                            *       PARTY_SELF              = Ein einzelnes verbündetes Ziel INKL dem Anwender */
                            if (availableTargets == SkillTarget.OPPONENT_SINGLE || availableTargets == SkillTarget.PARTY_SINGLE || availableTargets == SkillTarget.PARTY_SELF) {

                                /* Wir können ein Ziel selbst auswählen, also legen wir erstmal eine Liste an, in der
                                * wir dann unsere Ziele ablegen können um sie zur Auswahl darzustellen. */
                                val targetOptions: MutableList<Fighter> = mutableListOf()

                                /* Basierend auf dem Typ der erlaubten Ziele, füge die jeweiligen Kämpfer zu unseren Optionen
                                * hinzu. Bei PARTY_SINGLE nehmen wir einfach die Gruppe der Helden und filtern diese Liste
                                * nach Elementen, die nicht dieselben sind wir der Kämpfer, der aktuell am Zug ist. */
                                when (availableTargets) {

                                    SkillTarget.OPPONENT_SINGLE -> targetOptions.addAll(partyEnemies)
                                    SkillTarget.PARTY_SINGLE -> targetOptions.addAll(partyHeroes.filterNot { it == currentTurn })
                                    SkillTarget.PARTY_SELF -> targetOptions.addAll(partyHeroes)
                                    else -> return false    // FAIL SAFE, das sollte nicht passieren also gehe ins Hauptmenü zurück
                                }

                                /* Wir haben unsere Optionen beisammen, also wollen wir sie ausgeben, damit der User eine
                                * Entscheidung treffen kann. */
                                Prints.printTargetsMenu(targetOptions)

                                /* Wir lesen einen erneuten Input. Dieser soll automatisch 1 abgezogen bekommen, da wir diesen
                                * Wert als index für unsere Listen nehmen werden. In der Anzeige wird allerdings 1,2,3,etc
                                * angezeigt als Zahl die wir eingeben sollen, also wollen wir automatisch aus
                                * 1 -> 0, 2 - > 1, etc machen */
                                val inputTarget = readln().toInt()-1

                                /* Wir überprüfen, ob unsere Eingabe größer-gleich 0 und kleiner als die Anzahl an Zieloptionen ist */
                                if (inputTarget >= 0 && inputTarget < targetOptions.size) {

                                    /* Wir haben unsere Wahl getroffen, also füge das Ziel mit dem index der Eingabe aus der Liste
                                    der Zieloptionen zu unseren tatsächlichen Zielen hinzu. Danach werden die Schleifen alle
                                    verlassen und die Fähigkeit einige Zeilen weiter unten eingesetzt. */
                                    targets.add(targetOptions[inputTarget])

                                } else {

                                    // Ungültige Eingabe, wir gehen wieder ins Hauptmenü zurück
                                    return false
                                }

                            } else {
                                /* Wir sollen automatisch ein Ziel auswählen (z.B. bei Fähigkeiten die eine ganze Gruppe
                                * betreffen, oder die nur den Anwender betreffen. Also füge entsprechend die jeweiligen Kämpfer
                                * zu unserer Zielliste.
                                *       OPPONENT_ALL            = Alle gegnerischen Ziele
                                *       PARTY_ALL               = Alle verbündeten Ziele
                                *       SELF                    = Der Anwender selbst*/
                                when (availableTargets) {
                                    SkillTarget.OPPONENT_ALL -> targets.addAll(partyEnemies)
                                    SkillTarget.PARTY_ALL -> targets.addAll(partyHeroes)
                                    SkillTarget.SELF -> targets.add(currentTurn)
                                    else -> return false    // FAIL SAFE, irgendwas ist schief gelaufen also gehe ins Hauptmenü zurück
                                }
                            }

                            /* Wir haben alle unsere Ziele, die wir treffen wollen. Nun müssen wir nur noch die Fähigkeit
                            * ausführen und den Spielzug beenden. */
                            currentTurn.castSkill(inputSkill, targets)
                            currentTurn.isTurn = false

                        } else {

                            // Falsche Eingabe, wir wollen zurück ins Hauptmenü
                            return false
                        }
                    }

                    // INVENTORY: Wir möchten das Inventar einsehen
                    2 -> {

                        // Zeige zuerst unseren Inventarinhalt an.
                        Prints.printInventoryMenu(inventory.items)

                        /* Wir lesen einen erneuten Input. Dieser soll automatisch 1 abgezogen bekommen, da wir diesen
                        * Wert als index für unsere Listen nehmen werden. In der Anzeige wird allerdings 1,2,3,etc
                        * angezeigt als Zahl die wir eingeben sollen, also wollen wir automatisch aus 1 -> 0, 2 - > 1, etc machen */
                        val inputInventory = readln().toInt() - 1

                        /* Wir überprüfen, ob unsere Eingabe größer-gleich 0 und kleiner als die Anzahl an Gegenständen ist */
                        if (inputInventory >= 0 && inputInventory < inventory.items.size) {

                            /* Wir haben einen gültigen Gegenstand ausgewählt, jetzt wollen wir den Gegenstand auch
                            * benutzen und dann unseren Zug beenden. */
                            inventory.useItem(inputInventory, currentTurn)
                            currentTurn.isTurn = false

                        } else {

                            // Keine gültige Eingabe, wir wollen ins Hauptmenü zurück
                            return false
                        }

                    }

                    // STATUS: Wir möchten den Status eines Kämpfers einsehen
                    3 -> {

                        /* Zuerst wollen wir das Statusmenü ausgeben. Dort werden die Helden und Gegner aufgelistet mit
                        * einer Zahl, die wir eingeben sollen. */
                        Prints.printStatusMenu(partyHeroes, partyEnemies)

                        /* Wir lesen einen erneuten Input. Dieser soll automatisch 1 abgezogen bekommen, da wir diesen
                        * Wert als index für unsere Listen nehmen werden. In der Anzeige wird allerdings 1,2,3,etc
                        * angezeigt als Zahl die wir eingeben sollen, also wollen wir automatisch aus 1 -> 0, 2 - > 1, etc machen */
                        val statusInputSelect = readln().toInt() - 1

                        /* Wir prüfen, ob unser Input GRÖßER-GLEICH 0 UND KLEINER als die wählbaren Elemente, also
                        * die Anzahl von Helden + Gegnern, ist.  */
                        return if (statusInputSelect >= 0 && statusInputSelect < (partyHeroes.size+partyEnemies.size)) {

                            /* Jetzt müssen wir herausfinden, welchen Kämpfer wir ausgewählt haben. Wir gehen davon aus, dass
                            * die Anzeige immer Helden zuerst (z.B. 1-3) und danach Gegner (z.B. 4-5) angibt. D.h. wenn wir
                            * eine Eingabe getroffen haben die kleiner als die Anzahl an Helden ist (z.b. 0-2) dann wollen
                            * wir ein Element aus der Heldenliste mit genau diesem index abspeichern. Andernfalls wollen wir
                            * ein Element aus der Gegnerliste mit dem index der Eingabe (z.B. 4-5) MINUS der Heldenlistenanzahl
                            * (was dann 1-2 (und -1 vom readln() oben) haben. */
                            val selected: Fighter = if (statusInputSelect < partyHeroes.size) {
                                partyHeroes[statusInputSelect]
                            } else {
                                partyEnemies[statusInputSelect-partyHeroes.size]
                            }

                            // Jetzt wollen wir den Status des gewählten Kämpfers ausgeben lassen
                            Prints.printStatusSelected(selected)

                            /* Mit einem readln() ohne dessen Eingabe abzuspeichern, können wir einfach warten, bis der
                            * User irgendetwas eingegeben hat, bevor wir im Code weiter fortfahren. */
                            readln()

                            // Wir wollen ins Hauptmenü zurückgeschickt werden, also gebe false zurück
                            false

                        } else {

                            // Wir wollen ins Hauptmenü geschickt werden bei falscher Eingabe, also gebe false zurück
                            false
                        }

                    }

                    // WAIT: Wir wollen einen Zug warten
                    4 -> {

                        /* Wir geben eine einfache Nachricht aus, dass dieser Kämpfer einen Zug wartet und beenden dann unseren Zug. */
                        Prints.printWaitTurn(currentTurn.name)
                        currentTurn.isTurn = false
                    }

                    // FALSCHE EINGABE: Wir wollen zurück ins Hauptmenü geschickt werden
                    else -> {
                        return false
                    }
                }
            } catch (e: Exception) {

                /* Wir haben eine ungültige Eingabe gemacht, also wollen wir false zurückgeben, damit der Spieler
                * wieder in das Hauptmenü geworfen wird und eine neue Auswahl treffen kann. */
                return false
            }
        }

        currentTurn.tickStatusEffectsPost()

        return true
    }

    /* Mit dieser Funktion generieren wir eine Liste die nach der Geschwindigkeit aller Kampfteilnehmer sortiert ist.
    * Damit entscheiden wir praktisch, wer wann am Zug ist und Aktionen durchführen kann. Wir ergeben das Ergebnis zurück
    * als MutableList zurück, denn während der aktuellen Runde kann z.B. ein Teilnehmer sterben wobei wir ihn dann aus
    * der Liste löschen müssen. */
    /** Generates and returns a new turnset depending on the speed of all combatants **/
    private fun getNextTurnSet(): MutableList<Fighter> {

        // Lege eine neue MutableList an
        val set = mutableListOf<Fighter>()

        /* Jetzt wollen wir sowohl Spieler als auch Computergegner hinzufügen. Wir filtern dabei die jeweiligen Listen nach
        * Kämpfern die NICHT tot sind. */
        set.addAll(partyHeroes.filter { !it.isDead })   // Nur Elemente aus der Liste nehmen deren isDead = false ist!
        set.addAll(partyEnemies.filter { !it.isDead })  // it bezieht sich dabei auf das jeweils iterierte Element

        /* Nun wollen wir unsere Spielzugliste nach dem Geschwindigkeitswert der Teilnehmer sortieren, das können wir
        * mithilfe von sortByDescending tun, der wir den Wert speedReal des jeweils iterierten Elements als Argument
        * übergeben. Wir wollen also den Kämpfer mit dem höchsten Wert an erster Stelle haben. */
        set.sortByDescending { it.speedReal }

        // Gib unsere gefilterte und sortierte Liste zurück
        return set
    }
}
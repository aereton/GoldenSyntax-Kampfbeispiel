import data.*
import utils.*
import utils.Prints
import kotlin.random.*
// Test msc
/*
Changelog 1.0
    - FIXED unable to inspect status of spawned minions
    - FIXED battle loop could get stuck when heroes were stunned due to status effect duration calculations
    - FIXED a crash caused by enemies being aggroed even though that status effect should have been expired already
    - FIXED fighters being able to use beneficial skills on enemies whenn aggroed
    - FIXED nameplates of current turn's party wouldn't be shown in turn order
    - FIXED nameplates of current turn's party now won't get shifted to the right by long names
    - FIXED status effect duration is no longer shown when 0
    - FIXED enemy ai target selection for healing sometimes would not produce any targets
    - FIXED battle win/lose condition to be working again
    - FIXED status effects would get applied to targets if they died within the same attack

    - CHANGED the combat menus to contain more info and to look better
    - CHANGED moved all output prints methods into one file
    - CHANGED using standard nameplates on skill target selection now
    - CHANGED dragon boss now can only use his summon skill when his hp threshold is reached
    - CHANGED enemy combat ai moved from battle logic to enemy class
    - CHANGED enemies now prioritize offensive skills over healing skills if their party doesn't need any healing
    - CHANGED enemies now won't use certain skills of theirs successively to avaid being spammed by certain abilities
    - CHANGED status effect damage like DOTs can no longer be blocked whatsoever
    - CHANGED status effects now have a limit on how many turns they can stack up to on a single target

    - ADDED output message if a status effect fails to get inflicted upon a fighter due to chance
    - ADDED an item to remove hostile status effect from its user to inventory
    - ADDED more a fire and ice dragon to the game data, all with own skills and strategies
    - ADDED a random dragon is now chosen before battle

 */

fun main() {
    Random(System.currentTimeMillis())

    /* Wir wollen zuerst unsere Daten anlegen. Diese sind eine Gruppe an Helden, eine Gruppe an möglichen Bossen,
    * eine tatsächliche Gruppe an Gegnern der ein zufälliges Element der möglichen Bosse hinzugefügt wird,
    * sowie das Inventar mit ein paar Gegenständen.
    * Wir bekommen alle unsere Instanzen der Klassen aus den jeweiligen Funktionen, diese befinden sich in den Dateien
    * data/Enemies, data/Heroes, etc. */
    val partyHeroes: MutableList<Hero> = mutableListOf(
        Heroes.createWarrior(),
        Heroes.createThief(),
        Heroes.createMage()
    )
    val bossOptions: MutableList<Enemy> = mutableListOf(
        Enemies.createDragonUndead(),
        Enemies.createDragonFire(),
        Enemies.createDragonIce()
    )
    val partyEnemies: MutableList<Enemy> = mutableListOf(
        bossOptions.random(),
    )
    val inventory = Inventory(mutableListOf(
        Items.getItemHealPotionL(1),
        Items.getItemHealPotionSm(2),
        Items.getItemRemoveEffect(2),
        Items.getItemBuffC(1),
        Items.getItemBuffD(1)
    ))


    /* Gib unser Spielelogo und einen Changelog aus. */
    Prints.printGameTitle()
    Prints.printChangeLog()

    /* Wir geben außerdem unseren Einleitungstext und eine kleine Anleitung aus */
    Prints.printIntroduction()
    Prints.printEmptyLine()
    Prints.printTutorial()

    /* Ein kleiner Balken um das vorherige vom aktiven Spiel optisch abzutrennen. */
    Prints.printDivider1()

    /* Wir wollen einfach auf irgendeinen Input des Spielers warten, bevor wir das Spiel starten. Deswegen rufen wir einfach
    * readln() auf, ohne dessen Ergebnis abzuspeichern, denn was der Spieler eingegeben hat interessiert uns ja eh nicht. */
    print(Colors.getColorText("\tPress [ENTER] to start: ", Colors.CYAN))
    readln()

    /* Nochmal ein bisschen Narrative */
    Prints.printEmptyLines()
    println(Colors.getColorText("The heroes venture into a dragon lair...", Colors.PURPLE))

    /* Wir geben ein passendes Drachenbild aus, je nachdem welcher Drachentyp sich in unserer Gegnerliste befindet. */
    when (partyEnemies.first().type) {
        EnemyType.DRAGON_UNDEAD -> Prints.printDragonUndeadArt()
        EnemyType.DRAGON_FIRE -> Prints.printDragonFireArt()
        EnemyType.DRAGON_ICE -> Prints.printDragonIceArt()
        else -> {}
    }

    /* Wir geben ein paar erste Informationen zu dem Gegner aus, den wir bekämpfen sollen. */
    println(Colors.getColorText("The heroes encounter ${partyEnemies.first().name}!", Colors.PURPLE))
    Prints.printPartyMembersStatus(partyEnemies, partyEnemies.first())
    println(Colors.getColorText("${partyEnemies.first().name} ${partyEnemies.first().spawnMessage}", Colors.PURPLE))

    /* Jetzt können wir einen Kampf anlegen, ihm unsere Heldengruppe, Gegnergruppe sowie das Inventar übergeben und dann
    * die Funktion doBattle() aufrufen, um den Kampf zu starten. Die Battle Klasse arbeitet dann ihre Logik ab, bis
    * der Kampf vorbei ist und einer der beiden Gruppen gewonnen hat. */
    val battle = Battle(partyHeroes, partyEnemies, inventory)
    battle.doBattle()

    /* Alles ab hier ist das Ende des Programms. Wir trennen nochmal das Kampfgeschehen mit einem Rahmen ab
    * und bedanken uns bei dem Spieler. */
    Prints.printDivider1()
    println(Colors.getColorText("The game is over. Thank you for playing!", Colors.YELLOW))
}

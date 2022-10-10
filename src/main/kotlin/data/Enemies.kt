package data

import Enemy

/* Das Objekt Enemies enthält Funktionen mit festgelegten Daten für unsere Gegner. Somit können wir jederzeit
*  neue Gegner instanziieren ohne jedes mal die selben Daten eingeben zu müssen. Das ist vor allem dann nützlich,
*  wenn wir mit der Spezialattacke des Drachen Diener im laufenden Kampf beschwören möchten oder wenn wir die
*  Spielebalance ändern möchten, falls wir feststellen dass der Kampf zu leicht oder zu schwer ist..
*
*   name                = Name des Gegners
*   maxHp               = maximale Gesundheit
*   speed               = Geschwindigkeit des Gegners, je höher desto schneller am Zug
*   summonHpThreshold   = Prozentwert der Gesundheit, ab wann eine Beschwörungsfähigkeit eingesetzt werden kann
*   spawnMessage        = individuelle Nachricht bei Erscheinen des Gegners
*   type                = Gegnertyp */
object Enemies {

    /* Erstellt eine neue Instanz der Klasse Enemy mit den nötigen Daten für unseren Untotendrachen */
    fun createDragonUndead(): Enemy {

        // Wir wollen unseren Gegner erst einmal in einer Variable abspeichern
        val dragon = Enemy("Maghirth, Bringer of Death", 491, 8, 60, "appears. What a foul stench!", EnemyType.DRAGON_UNDEAD)

        // Nun können wir unserem Gegner mit der Funktion .addSkillSet() eine Liste an Kampffähigkeiten hinzufügen.
        // Wir instanziieren diese aus unserem Skills object
        dragon.addSkillSet(listOf(
            Skills.skEnemyDragonGeneric1(),
            Skills.skEnemyDragonGeneric2(),
            Skills.skEnemyDragonUndead3(),
            Skills.skEnemyDragonUndead4(),
            Skills.skEnemyDragonUndead5(),
            Skills.skEnemyDragonUndead6()
        ))

        return dragon
    }

    /* Erstellt eine neue Instanz der Klasse Enemy mit den nötigen Daten für unseren Feuerdrachen */
    fun createDragonFire(): Enemy {

        val dragon = Enemy("Kersentin, Lord Of The Skies", 367,9, 40, "appears. The air around him gets quite hot.", EnemyType.DRAGON_FIRE)

        dragon.addSkillSet(listOf(
            Skills.skEnemyDragonGeneric1(),
            Skills.skEnemyDragonGeneric2(),
            Skills.skEnemyDragonFire3(),
            Skills.skEnemyDragonFire4(),
            Skills.skEnemyDragonFire5(),
            Skills.skEnemyDragonFire6()
        ))

        return dragon
    }

    /* Erstellt eine neue Instanz der Klasse Enemy mit den nötigen Daten für unseren Eisdrachen */
    fun createDragonIce(): Enemy {

        val dragon = Enemy("Ipharre, The Voiceless", 412, 8, 50, "appears. The air gets cold.", EnemyType.DRAGON_ICE)

        dragon.addSkillSet(listOf(
            Skills.skEnemyDragonGeneric1(),
            Skills.skEnemyDragonGeneric2(),
            Skills.skEnemyDragonIce3(),
            Skills.skEnemyDragonIce4(),
            Skills.skEnemyDragonIce5(),
            Skills.skEnemyDragonIce6()
        ))

        return dragon
    }

    /* Erstellt eine neue Instanz der Klasse Enemy mit den nötigen Daten für unseren Leichen-Drachenhelfer */
    fun createMinionCorpse(): Enemy {

        val minion = Enemy("Rotten Corpse", (20..40).random(), (5..11).random(), 0, "comes out of the ground!", EnemyType.MINION)

        minion.addSkillSet(listOf(
            Skills.skEnemyCorpse1(),
            Skills.skEnemyCorpse2()
        ))

        return minion
    }

    /* Erstellt eine neue Instanz der Klasse Enemy mit den nötigen Daten für unseren Fleischfliegen-Drachenhelfer */
    fun createMinionFly(): Enemy {

        val minion = Enemy("Bloat Fly", (8..15).random(), (2..4).random(), 0, "appears!", EnemyType.MINION)

        minion.addSkillSet(listOf(
            Skills.skEnemyFly1(),
            Skills.skEnemyFly2()
        ))

        return minion
    }

    /* Erstellt eine neue Instanz der Klasse Enemy mit den nötigen Daten für unseren Feuerelementar-Drachenhelfer */
    fun createMinionFireElemental(): Enemy {

        val minion = Enemy("Fire Elemental", (34..41).random(), (3..6).random(), 0, "is being summoned!", EnemyType.MINION)

        minion.addSkillSet(listOf(
            Skills.skEnemyFireElemental1(),
            Skills.skEnemyFireElemental2()
        ))

        return minion
    }

    /* Erstellt eine neue Instanz der Klasse Enemy mit den nötigen Daten für unseren Eisgolem-Drachenhelfer */
    fun createMinionIceGolem(): Enemy {

        val minion = Enemy("Ice Golem", (4..8).random(), (1..6).random(), 0, "comes out of the ice!", EnemyType.MINION)

        minion.addSkillSet(listOf(
            Skills.skEnemyIceGolem1(),
            Skills.skEnemyIceGolem2()
        ))

        return minion
    }
}
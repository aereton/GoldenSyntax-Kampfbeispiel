package data

import Hero

/* Das Objekt Heroes enthält Funktionen mit festgelegten Daten für unsere Helden. Somit können wir jederzeit
*  neue Helden instanziieren ohne jedes mal die selben Daten eingeben zu müssen. Das ist vor allem dann nützlich
*  wenn wir die Spielebalance ändern möchten, falls wir feststellen dass der Kampf zu leicht oder zu schwer ist.
*
*   name                = Name des Gegners
*   maxHp               = maximale Gesundheit
*   speed               = Geschwindigkeit des Gegners, je höher desto schneller am Zug */
object Heroes {

    /* Erstellt eine neue Instanz der Klasse Hero mit den nötigen Daten für unseren Krieger */
    fun createWarrior(): Hero {

        // Wir wollen unseren neuen Helden erst einmal in einer Variable abspeichern.
        val warrior = Hero("Warrior Brightblade", 53, 7)

        // Nun können wir unserem Helden mit der Funktion .addSkillSet() eine Liste an Kampffähigkeiten hinzufügen.
        // Wir instanziieren diese aus unserem Skills object
        warrior.addSkillSet(listOf(
            Skills.skHeroWarrior1(),
            Skills.skHeroWarrior2(),
            Skills.skHeroWarrior3(),
            Skills.skHeroWarrior4()
        ))

        return warrior
    }

    /* Erstellt eine neue Instanz der Klasse Hero mit den nötigen Daten für unseren Dieb */
    fun createThief(): Hero {

        val thief = Hero("Thief Yalathanil", 36, 12)

        thief.addSkillSet(listOf(
            Skills.skHeroThief1(),
            Skills.skHeroThief2(),
            Skills.skHeroThief3(),
            Skills.skHeroThief4()
        ))

        return thief
    }

    /* Erstellt eine neue Instanz der Klasse Hero mit den nötigen Daten für unseren Magier */
    fun createMage(): Hero {

        val mage = Hero("Mage Abrassi", 28, 9)

        mage.addSkillSet(listOf(
            Skills.skHeroMage1(),
            Skills.skHeroMage2(),
            Skills.skHeroMage3(),
            Skills.skHeroMage4()))
        return mage
    }
}
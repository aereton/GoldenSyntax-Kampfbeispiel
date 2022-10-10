package data

import StatusEffect

/* Das Objekt StatusEffects enthält Funktionen mit festgelegten Daten für unsere Statuseffekte. Somit können wir jederzeit
*  neue StatusEffekte instanziieren ohne jedes mal die selben Daten eingeben zu müssen. Das ist vor allem dann nützlich
*  wenn wir die Spielebalance ändern möchten, falls wir feststellen dass der Kampf zu leicht oder zu schwer ist.
*
*   name                = Name des Effekts
*   icon                = Icon für diesen Effekt
*   type                = Typ des Statuseffekts
*   chance              = 0-100% die Chance ob dieser Statuseffekt auf ein Ziel angewendet wirde
*   durationRange       = Die Spanne der möglichen Dauer des Effekts
*   durationMax         = Die maximale Dauer die für diesen Effekt erlaubt ist
*   powerRange          = Effektwert; abhängig vom Typ des Statuseffekts; kann z.B. Schaden, Heilung, Blockprozentwert, etc. sein
*   desc                = Statuseffektbeschreibung */
object StatusEffects {

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Benommen-Effekt */
    fun statEffDazed(chance: Int, duration: IntRange): StatusEffect {
        // Da wir keine Funktion auf der Instanz aufrufen müssen (wie es bspw. bei Helden oder Gegner der Fall ist)
        // können wir das Ganze auf eine Zeile reduzieren und direkt eine neue Instanz zurückgeben (return).
        return StatusEffect("Dazed", "↯", StatusEffectType.STUN, chance, duration, 2, 0..0, "Cannot attack when under this effect.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Verärgert-Effekt */
    fun statEffAnnoy(chance: Int, duration: IntRange): StatusEffect {
        return StatusEffect("Annoyed", "✱", StatusEffectType.AGGRO, chance, duration, 2, 0..0, "Is forced to attack the provocateur.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Verteidigungs-Effekt */
    fun statEffDefend(chance: Int, duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Defensive Stance", "☗", StatusEffectType.BUFF_BLOCK, chance, duration, 3, power, "Blocks " + if (power.min() == power.max()) { power.min() } else {"${power.min()}-${power.max()}"} + "% of all incoming damage.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Blutungs-Effekt */
    fun statEffBleed(chance: Int, duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Bleeding", "⚗", StatusEffectType.DOT, chance, duration, 6, power, "Inflicts " + if (power.min() == power.max()) {power.min()} else {"${power.min()}-${power.max()}"} + "points of damage every turn.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Vergiftungs-Effekt */
    fun statEffPoison(chance: Int, duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Poisoned", "☠", StatusEffectType.DOT, chance, duration, 6, power, "Inflicts " + if (power.min() == power.max()) {power.min()} else {"${power.min()}-${power.max()}"} + "points of damage every turn.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Trägheits-Effekt */
    fun statEffSlow(chance: Int, duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Slowed", "⚖", StatusEffectType.DEBUFF_SPD, chance, duration, 4, power, "Decreases speed by " + if (power.min() == power.max()) {power.min()} else {"${power.min()}-${power.max()}"} + "%.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Unsichtbar-Effekt */
    fun statEffInvis(chance: Int, duration: IntRange): StatusEffect {
        return StatusEffect("Invisible", "⚜", StatusEffectType.BUFF_BLOCK, chance, duration, 2, 100..100, "Evades all incoming damage.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Brennen-Effekt */
    fun statEffBurn(chance: Int, duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Burning", "♨", StatusEffectType.DOT, chance, duration, 6, power, "Inflicts " + if (power.min() == power.max()) {power.min()} else {"${power.min()}-${power.max()}"} + "points of damage every turn.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Frostrüstung-Effekt */
    fun statEffFrostArmor(chance: Int, duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Frost armor", "☃", StatusEffectType.BUFF_BLOCK, chance, duration, 3, power, "Reduces " + if (power.min() == power.max()) {power.min()} else {"${power.min()}-${power.max()}"} + "% of incoming damage.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Regenerations-Effekt */
    fun statEffLingerLight(chance: Int, duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Lingering Light", "☀", StatusEffectType.HOT, chance, duration, 5, power, "Heals " + if (power.min() == power.max()) {power.min()} else {"${power.min()}-${power.max()}"} + "HP every turn.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Angst-Effekt */
    fun statEffFear(chance: Int, duration: IntRange): StatusEffect {
        return StatusEffect("Fear", "☹", StatusEffectType.STUN, chance, duration, 2, 0..0, "Cannot attack when under this effect.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Wurm-Regeneration-Effekt */
    fun statEffWorms(chance: Int, duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Healing Worms", "∾", StatusEffectType.HOT, chance, duration, 5, power, "Heals " + if (power.min() == power.max()) {power.min()} else {"${power.min()}-${power.max()}"} + "HP every turn.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren Frieren-Effekt */
    fun statEffFreeze(chance: Int, duration: IntRange): StatusEffect {
        return StatusEffect("Frozen", "☸", StatusEffectType.STUN, chance, duration, 2, 0..0, "Cannot attack when under this effect.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren VitaminC-Effekt */
    fun statEffBuffC(duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Increased Strength", "♞", StatusEffectType.BUFF_ATK, 100, duration, -1, power, "Increases base damage by " + if (power.min() == power.max()) {power.min()} else {"${power.min()}-${power.max()}"} + "%.")
    }

    /* Erstellt eine neue Instanz der Klasse StatusEffect mit den nötigen Daten für unseren VitaminD-Effekt */
    fun statEffBuffD(duration: IntRange, power: IntRange): StatusEffect {
        return StatusEffect("Increased Resistance", "✪", StatusEffectType.BUFF_RES, 100, duration, 3, power, "" + if (power.min() == power.max()) {power.min()} else {"${power.min()}-${power.max()}"} + "% chance to resist hostile status effects.")
    }
}
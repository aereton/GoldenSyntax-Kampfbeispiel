package data

import Skill
import SkillTarget
import SkillType

/* Das Objekt Skills enthält Funktionen mit festgelegten Daten für unsere Kampffähigkeiten. Somit können wir jederzeit
*  neue Skills instanziieren ohne jedes mal die selben Daten eingeben zu müssen. Das ist vor allem dann nützlich
*  wenn wir die Spielebalance ändern möchten, falls wir feststellen dass der Kampf zu leicht oder zu schwer ist.
*
*   name                = Name des Gegners
*   type                = Fähigkeitstyp
*   power               = Direktschaden
*   usesPerBattle       = Wie oft diese Fähigkeit eingesetzt werden kann; -1 ist unendlich
*   useSuccessiveByAi   = Ob diese Fähigkeit von Gegner KI mehrmals hintereinander eingesetzt werden darf
*   availableTargets    = Welche Ziele sind für diese Fähigkeit erlaubt
*   skillEffect         = Statuseffekt den diese Fähigkeit verursachen soll; kann null sein
*   description         = Fähigkeitsbeschreibung */
object Skills {

    /* ======================
            HERO SKILLS
    ======================= */
    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Kriegerfähigkeit Nr. 1 */
    fun skHeroWarrior1(): Skill {
        // Da wir keine Funktion auf der Instanz aufrufen müssen (wie es bspw. bei Helden oder Gegner der Fall ist)
        // können wir das Ganze auf eine Zeile reduzieren und direkt eine neue Instanz zurückgeben (return).
        return Skill("Sword Slash", SkillType.ATK, 8..11, -1, true, SkillTarget.OPPONENT_SINGLE, null,"8-11 damage to a single target.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Kriegerfähigkeit Nr. 2 */
    fun skHeroWarrior2(): Skill {
        return Skill("Pummel Strike", SkillType.ATK, 5..7, -1, true, SkillTarget.OPPONENT_SINGLE, StatusEffects.statEffDazed(20, 1..2), "5-7 damage to a single target. 20% chance to leave target unable to move for 1-2 turns.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Kriegerfähigkeit Nr. 3 */
    fun skHeroWarrior3(): Skill {
        return Skill("Battle Cry", SkillType.EFFECT_ONLY, 0..0, 3, true, SkillTarget.OPPONENT_SINGLE, StatusEffects.statEffAnnoy(100, 1..1), "Force the target to attack the user next round.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Kriegerfähigkeit Nr. 4 */
    fun skHeroWarrior4(): Skill {
        return Skill("Defensive Stance", SkillType.EFFECT_ONLY, 0..0, 5, true, SkillTarget.SELF, StatusEffects.statEffDefend(100, 1..1, 60..60), "Switch to a defensive stance, blocking 60% of damage until next turn.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Diebesfähigkeit Nr. 1 */
    fun skHeroThief1(): Skill {
        return Skill("Thousand Daggers", SkillType.ATK, 4..10, -1, true, SkillTarget.OPPONENT_SINGLE, StatusEffects.statEffBleed(25, 1..2, 4..6), "4-6 damage to a single target. 25% chance to leave the target Bleeding for 1-2 turns, dealing 4-6 damage each turn.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Diebesfähigkeit Nr. 2 */
    fun skHeroThief2(): Skill {
        return Skill("Poison Vial", SkillType.EFFECT_ONLY, 0..0, -1, true, SkillTarget.OPPONENT_ALL, StatusEffects.statEffPoison(90, 2..5, 3..5), "90% chance for each enemy to become poisoned for 2-5 turns, dealing 3-5 damage each turn.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Diebesfähigkeit Nr. 3 */
    fun skHeroThief3(): Skill {
        return Skill("Kidney Blow", SkillType.ATK, 5..7, 10, true, SkillTarget.OPPONENT_SINGLE, StatusEffects.statEffSlow(25, 2..2, 50..50), "5-7 damage to a single target. 25% chance to reduce the targets' speed by 50% for 2 turns.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Diebesfähigkeit Nr. 4 */
    fun skHeroThief4(): Skill {
        return Skill("Vanish", SkillType.EFFECT_ONLY, 0..0, 2, true, SkillTarget.SELF, StatusEffects.statEffInvis(100, 1..1), "Vanish into the shadows to avoid all incoming damage until next turn.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Magierfähigkeit Nr. 1 */
    fun skHeroMage1(): Skill {
        return Skill("Fireball", SkillType.ATK, 6..9, -1, true, SkillTarget.OPPONENT_ALL, StatusEffects.statEffBurn(8, 1..3, 3..6), "Hits all enemies and deals 6-9 damage. 8% chance to leave targets Burning for 1-3 turns, dealing 3-6 damage each turn.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Magierfähigkeit Nr. 2 */
    fun skHeroMage2(): Skill {
        return Skill("Thunderbolt", SkillType.ATK, 10..14, -1, true, SkillTarget.OPPONENT_SINGLE, null, "Lighting spell that deals 10-14 damage to a single target.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Magierfähigkeit Nr. 3 */
    fun skHeroMage3(): Skill {
        return Skill("Frost Shield", SkillType.EFFECT_ONLY, 1..1, 5, true, SkillTarget.PARTY_ALL, StatusEffects.statEffFrostArmor(100, 2..2, 20..20), "Grants a frost armor for 2 turns, reducing incoming damage by 20%.")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Magierfähigkeit Nr. 4 */
    fun skHeroMage4(): Skill {
        return Skill("Healing Touch",SkillType.HEAL, 9..16, 5, true, SkillTarget.PARTY_SELF, StatusEffects.statEffLingerLight(10, 1..2, 3..5), "Heals a friendly target by 9-16 HP. 10% chance to continue 1-2 turns to heal target for 3-5 HP each turn.")
    }



    /* ======================
            ENEMY SKILLS
    ======================= */
    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Drachenfähigkeit Nr. 1 */
    fun skEnemyDragonGeneric1(): Skill {
        return Skill("Ripping Claws", SkillType.ATK, 5..10, -1, true, SkillTarget.OPPONENT_SINGLE, null, "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Drachenfähigkeit Nr. 2 */
    fun skEnemyDragonGeneric2(): Skill {
        return Skill("Tail Whip", SkillType.ATK, 3..6, -1, true, SkillTarget.OPPONENT_ALL, StatusEffects.statEffDazed(7, 1..1), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Untoten-Drachenfähigkeit Nr. 3 */
    fun skEnemyDragonUndead3(): Skill {
        return Skill("Foul Breath", SkillType.EFFECT_ONLY, 0..0, 10, false, SkillTarget.OPPONENT_ALL, StatusEffects.statEffPoison(80, 1..2, 3..7), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Untoten-Drachenfähigkeit Nr. 4 */
    fun skEnemyDragonUndead4(): Skill {
        return Skill("Hell Scream", SkillType.EFFECT_ONLY, 0..0, 5, false, SkillTarget.OPPONENT_ALL, StatusEffects.statEffFear(70, 1..1), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Untoten-Drachenfähigkeit Nr. 5 */
    fun skEnemyDragonUndead5(): Skill {
        return Skill("Necrotic Regeneration", SkillType.EFFECT_ONLY, 0..0, 5, false, SkillTarget.SELF, StatusEffects.statEffWorms(90, 1..2, 8..15), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Untoten-Drachenfähigkeit Nr. 6 */
    fun skEnemyDragonUndead6(): Skill {
        return Skill("Necromancy", SkillType.SPAWN, 1..3, 1, false, SkillTarget.SELF, null, "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Feuer-Drachenfähigkeit Nr. 3 */
    fun skEnemyDragonFire3(): Skill {
        return Skill("Fire Breath", SkillType.ATK, 4..8, 5, false, SkillTarget.OPPONENT_SINGLE, StatusEffects.statEffBurn(50, 1..3, 2..5), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Feuer-Drachenfähigkeit Nr. 4 */
    fun skEnemyDragonFire4(): Skill {
        return Skill("Wing Flapping", SkillType.EFFECT_ONLY, 0..0, 3, false, SkillTarget.OPPONENT_ALL, StatusEffects.statEffSlow(40, 1..2, 35..35), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Feuer-Drachenfähigkeit Nr. 5 */
    fun skEnemyDragonFire5(): Skill {
        return Skill("Inspiration", SkillType.EFFECT_ONLY, 0..0, 4, false, SkillTarget.PARTY_SELF, StatusEffects.statEffBuffC(1..2, 15..15), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Feuer-Drachenfähigkeit Nr. 6 */
    fun skEnemyDragonFire6(): Skill {
        return Skill("The Calling", SkillType.SPAWN, 1..1, 1, false, SkillTarget.SELF, null, "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Eis-Drachenfähigkeit Nr. 3 */
    fun skEnemyDragonIce3(): Skill {
        return Skill("Chilling Wind", SkillType.ATK, 4..8, 4, false, SkillTarget.OPPONENT_ALL, StatusEffects.statEffSlow(60, 1..1, 40..40), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Eis-Drachenfähigkeit Nr. 4 */
    fun skEnemyDragonIce4(): Skill {
        return Skill("Ice Pike", SkillType.ATK, 8..10, 10, false, SkillTarget.OPPONENT_SINGLE, StatusEffects.statEffFreeze(20, 1..1), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Eis-Drachenfähigkeit Nr. 5 */
    fun skEnemyDragonIce5(): Skill {
        return Skill("Ice Armor", SkillType.EFFECT_ONLY, 0..0, 3, false, SkillTarget.PARTY_SELF, StatusEffects.statEffFrostArmor(90, 1..2, 30..30), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Feuer-Drachenfähigkeit Nr. 6 */
    fun skEnemyDragonIce6(): Skill {
        return Skill("Create Ice Golem", SkillType.SPAWN, 2..5, 1, false, SkillTarget.SELF, null, "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Leichenfähigkeit Nr. 1 */
    fun skEnemyCorpse1(): Skill {
        return Skill("Scratch", SkillType.ATK, 3..5, -1, true, SkillTarget.OPPONENT_SINGLE, null, "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Leichenfähigkeit Nr. 2 */
    fun skEnemyCorpse2(): Skill {
        return Skill("Bite", SkillType.ATK, 4..7, 4, false, SkillTarget.OPPONENT_SINGLE, StatusEffects.statEffBleed(30, 1..2, 2..4), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Fleischfliegenfähigkeit Nr. 1 */
    fun skEnemyFly1(): Skill {
        return Skill("Tackle", SkillType.ATK, 2..4, -1, true, SkillTarget.OPPONENT_SINGLE, null, "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Fleischfliegenfähigkeit Nr. 2 */
    fun skEnemyFly2(): Skill {
        return Skill("Distraction", SkillType.EFFECT_ONLY, 0..0, 2, false, SkillTarget.OPPONENT_SINGLE, StatusEffects.statEffAnnoy(100, 1..1), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Feuerelementarfähigkeit Nr. 1 */
    fun skEnemyFireElemental1(): Skill {
        return Skill("Throw Flames", SkillType.ATK, 3..7, -1, true, SkillTarget.OPPONENT_SINGLE, StatusEffects.statEffBurn(10, 1..1, 2..5), "")
    }

    /* Erstellt eine neue Instanz der Klasse Skill mit den nötigen Daten für unsere Feuerelementarfähigkeit Nr. 2 */
    fun skEnemyFireElemental2(): Skill {
        return Skill("Flame Hull", SkillType.EFFECT_ONLY, 0..0, 3, false, SkillTarget.PARTY_SINGLE, StatusEffects.statEffDefend(80, 1..2, 15..15), "")
    }

    fun skEnemyIceGolem1(): Skill {
        return Skill("Throw Iceblock", SkillType.ATK, 1..3, -1, true, SkillTarget.OPPONENT_SINGLE, null, "")
    }

    fun skEnemyIceGolem2(): Skill {
        return Skill("Cold Water", SkillType.HEAL, 5..8, 2, false, SkillTarget.PARTY_SINGLE, null, "")
    }
}
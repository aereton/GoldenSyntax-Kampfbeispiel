package data

import ItemBuff
import ItemHeal
import ItemRemoveEffect

/* Das Objekt Items enthält Funktionen mit festgelegten Daten für unsere Gegenstände. Somit können wir jederzeit
*  neue Gegenstände instanziieren ohne jedes mal die selben Daten eingeben zu müssen. Das ist vor allem dann nützlich
*  wenn wir die Spielebalance ändern möchten, falls wir feststellen dass der Kampf zu leicht oder zu schwer ist.
*
*   name                = Name des Gegenstands
*   amount              = Anzahl auf einem Stapel
*   healValue           = [ItemHeal] Anzahl an HP die bei Nutzung geheilt werden sollen
*   removeAmount        = [ItemRemoveEffect] Anzahl an zufälligen Statuseffekten die bei Nutzung entfernt werden sollen
*   effect              = Statuseffekt der bei Nutzung auf den Nutzer angewendet werden soll
*   desc                = Gegenstandsbeschreibung */
object Items {

    /* Erstellt eine neue Instanz der Klasse ItemHeal mit den nötigen Daten für unseren kleinen Lebenstrank */
    fun getItemHealPotionSm(amount: Int): ItemHeal {
        // Da wir keine Funktion auf der Instanz aufrufen müssen (wie es bspw. bei Helden oder Gegner der Fall ist)
        // können wir das Ganze auf eine Zeile reduzieren und direkt eine neue Instanz zurückgeben (return).
        return ItemHeal("Small Heal Potion", amount, 15, "Heals the user by 20 HP.")
    }

    /* Erstellt eine neue Instanz der Klasse ItemHeal mit den nötigen Daten für unseren großen Lebenstrank */
    fun getItemHealPotionL(amount: Int): ItemHeal {
        return ItemHeal("Large Heal Potion", amount, 40, "Heals the user by 40 HP.")
    }

    fun getItemRemoveEffect(amount: Int): ItemRemoveEffect {
        return ItemRemoveEffect("Herbs", amount, 1, "Removes 1 random negative status effect from its user.")
    }

    /* Erstellt eine neue Instanz der Klasse ItemBuff mit den nötigen Daten für unser Vitamin C */
    fun getItemBuffC(amount: Int): ItemBuff {
        return ItemBuff("Vitamin C", amount, StatusEffects.statEffBuffC(-1..-1, 10..10), "Increases the user's base damage by 10% permanently.")
    }

    /* Erstellt eine neue Instanz der Klasse ItemBuff mit den nötigen Daten für unser Vitamin D */
    fun getItemBuffD(amount: Int): ItemBuff {
        return ItemBuff("Vitamin D", amount, StatusEffects.statEffBuffD(1..2, 100..100), "Grants the user 20% chance to resist hostile status effects for 1-2 turns"
        )
    }
}
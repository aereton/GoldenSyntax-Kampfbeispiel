/* =============================================
            Klasse für Helden
 ===============================================
 Diese Klasse erbt von der Basisklasse der Kämpfer, aber wir möchten keine weiteren Funktionalitäten erweitern.
 Dies dient ausschließlich der Unterscheidung zwischen Gegnern und Helden! */
/**
 * [Hero] class derived from [Fighter] with a [name], [maxHp] for maximum health and [speed]
 * for turn order decided each round during a [Battle]
 */
class Hero(name: String, maxHp: Int, speed: Int) : Fighter(name, maxHp, speed)
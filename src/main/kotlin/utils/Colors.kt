package utils

object Colors {
    // Farben für Text
    const val RESET = "\u001B[0m"
    const val BLACK = "\u001B[30m"
    const val RED = "\u001B[31m"
    const val GREEN = "\u001B[32m"
    const val YELLOW = "\u001B[33m"
    const val BLUE = "\u001B[34m"
    const val PURPLE = "\u001B[35m"
    const val CYAN = "\u001B[36m"
    const val WHITE = "\u001B[37m"
    // Farben für Hintergrund
    const val BG_BLACK ="\u001B[40m"
    const val BG_RED = "\u001B[41m"
    const val BG_GREEN = "\u001B[42m"
    const val BG_YELLOW = "\u001B[43m"
    const val BG_BLUE = "\u001B[44m"
    const val BG_PURPLE = "\u001B[45m"
    const val BG_CYAN = "\u001B[46m"
    const val BG_WHITE = "\u001B[47m"


    /* Mit dieser Funktion können wir einen Text mit einem Farbcode versehen. Dabei wird am Anfang des Strings
    *  der ANSI-Code für die angegebene Farbe (die wir oben als Konstanten angelegt haben) und am Ende des Strings
    *  der ANSI-Code zum Zurücksetzen der Farben (RESET) hinzugefügt. Beachte, dass RESET wirklich ALLES
    *  zurücksetzt, daher können wir getColorText nicht ineinander verschachteln! */
    /**
     * Encloses [text] with a [color].
     *
     * Note: Adds [RESET] at the end of the string so nesting is NOT supported!
     */
    fun getColorText(text: String, color: String): String {
        return color + text + RESET
    }
}
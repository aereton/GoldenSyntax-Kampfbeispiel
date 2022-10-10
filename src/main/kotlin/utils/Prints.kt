package utils

import Fighter
import Item
import Skill

/* Diese Funktionen sind eigentlich nur Helferfunktionen, um bestimmte Dinge in der Konsole auszudrucken. Das tun wir deshalb
* in einer eigenen Datei, damit wenn wir bspw. einen Rechtschreibfehler korrigieren wollen, oder einen Text allgemein ändern
* wollen, wir nicht jedesmal durch den ganzen Code durchgehen müssen. Wir können einfach in diese Datei reingehen und
* den entsprechenden Text abändern, wie wir lustig sind. */
object Prints {

    fun printChangeLog() {
        val text = """Changelog 1.0
    - FIXED Unable to inspect status of spawned minions
    - FIXED Battle loop can get stuck when heroes are stunned due to status effect duration calculations
    - FIXED A crash caused by enemies being aggroed even though that status effect should have been expired already
    - FIXED Fighters being able to use beneficial skills on enemies when aggroed
    - FIXED Nameplates of current turn's party won't be shown in turn order
    - FIXED Nameplates of current turn's party get shifted to the right because of long names
    - FIXED Status effect duration is shown when duration is 0
    - FIXED Enemy ai target selection for healing sometimes does not produce any targets
    - FIXED Battle win/lose condition has weird behaviour and doesn't recognize the winning team
    - FIXED Status effects won't get applied to targets if they died within the same attack

    - CHANGED The combat menus to contain more info and to look better
    - CHANGED Move all output prints methods into one file
    - CHANGED Using standard nameplates on skill target selection
    - CHANGED Dragon boss can only use his summon skill when his HP threshold is reached
    - CHANGED Move enemy combat ai from battle logic to enemy class
    - CHANGED Enemies prioritize offensive skills over healing skills if their party doesn't need any healing
    - CHANGED Enemies don't use certain skills of theirs successively to avoid being spammed by certain abilities
    - CHANGED Status effect damage like DOTs can not be blocked whatsoever
    - CHANGED Status effects have a limit on how many turns they can stack up to on a single target

    - ADDED Output message if a status effect fails to get inflicted upon a fighter due to chance
    - ADDED An item to remove hostile status effect from its user to inventory
    - ADDED A fire and ice dragon to the game data, all with own skills and strategies
    - ADDED Choose a random dragon boss upon start"""
        println(Colors.getColorText(text, Colors.WHITE))
    }

    // Generated on: http://www.network-science.de/ascii/
    fun printGameTitle() {
        var text = ""
        text += """  _______   ______    __       _______   _______ .__   __. 
 /  _____| /  __  \  |  |     |       \ |   ____||  \ |  | 
|  |  __  |  |  |  | |  |     |  .--.  ||  |__   |   \|  | 
|  | |_ | |  |  |  | |  |     |  |  |  ||   __|  |  . `  | 
|  |__| | |  `--'  | |  `----.|  '--'  ||  |____ |  |\   | 
 \______|  \______/  |_______||_______/ |_______||__| \__| 
     _______.____    ____ .__   __. .___________.    ___      ___   ___ 
    /       |\   \  /   / |  \ |  | |           |   /   \     \  \ /  / 
   |   (----` \   \/   /  |   \|  | `---|  |----`  /  ^  \     \  V  /  
    \   \      \_    _/   |  . `  |     |  |      /  /_\  \     >   <   
.----)   |       |  |     |  |\   |     |  |     /  _____  \   /  .  \  
|_______/        |__|     |__| \__|     |__|    /__/     \__\ /__/ \__\ 
                                        Version 1.0 | Stephan Dittmar"""
        println(Colors.getColorText(text, Colors.YELLOW))
    }

    // ASCII Art: https://www.asciiart.eu
    fun printIntroduction() {
        println(Colors.YELLOW + """     _______________________________________________________
    /                                                       \
(O)===)><><><><><><><><><><><><><><><><><><><><><><><><><><><)==(O)
    \/''''''''''''''''''''''''''''''''''''''''''''''''''''''/
    (                                                      (
     )              Welcome to Golden Syntax,               )
    (                                                      (
     )     a turn-based RPG combat system where you         )
           will fight with a party of heroes against
    (      dangerous dragons of the Undead, Fire and       (
     )     Ice.                                             )
    (                                                      (
     )     Be careful though, as these foul beasts          )
           are enemies you better not underestimate!
    (                                                      (
    /\''''''''''''''''''''''''''''''''''''''''''''''''''''''\    
(O)===)><><><><><><><><><><><><><><><><><><><><><><><><><><><)==(O)
    \/______________________________________________________/""" + Colors.RESET)
    }

    fun printTutorial() {
        println("\t${Colors.YELLOW}HELP:${Colors.RESET} Navigate the battle menus by typing in the number of a corresponding action and hit ENTER." +
                "\n\t\t  Back out of a submenu by just hitting ENTER without any input." +
                "\n\t\t  Use items and status effects to your advantage. Be smart about when to use what action. There are certain" +
                "\n\t\t  limits of what the enemy AI can do in any given circumstance due to implementation, so they may behave" +
                "\n\t\t  somewhat irratically at random.")
    }

    // ASCII Art: https://www.asciiart.eu
    fun printDivider1() {
        println(Colors.getColorText("<<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>><<>>", Colors.YELLOW))
    }

    // ASCII Art: https://www.asciiart.eu
    fun printDivider2() {
        println("--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--<>--")
    }

    // ASCII Art: https://www.asciiart.eu
    fun printDragonUndeadArt() {
        val text = """                                                       ____________
                                 (`-..________....---''  ____..._.-`
                                  \\`._______.._,.---'''     ,'
                                  ; )`.      __..-'`-.      /
                                 / /     _.-' _,.;;._ `-._,'
                                / /   ,-' _.-' .//   ``--._``._
                              ,','_.-' ,-' _.- (( =-    -. `-._`-._____
                            ,;.''__..-'   _..--.\\.--'````--.._``-.`-._`.
             _          |\,' .-''        ```-'`---'`-...__,._  ``-.`-.`-.`.
  _     _.-,'(__)\__)\-'' `     ___  .          `     \      `--._
,',)---' /|)          `     `      ``-.   `     /     /     `     `-.
\_____--.  '`  `               __..-.  \     . (   < _...-----..._   `.
 \_,--..__. \\ .-`.\----'';``,..-.__ \  \      ,`_. `.,-'`--'`---''`.  )
           `.\`.\  `_.-..' ,'   _,-..'  /..,-''(, ,' ; ( _______`___..'__
                   ((,(,__(    ((,(,__,'  ``'-- `'`.(\  `.,..______      
                                                      ``--------..._``--.__"""
        text.trim()
        println(Colors.GREEN + text + Colors.RESET)
    }

    // ASCII Art: https://www.asciiart.eu
    fun printDragonFireArt() {
        val text = """                                                 /===-_---~~~~~~~~~------____
                                                |===-~___                _,-'
                 -==\\                         `//~\\   ~~~~`---.___.-~~
             ______-==|                         | |  \\           _-~`
       __--~~~  ,-/-==\\                        | |   `\        ,'
    _-~       /'    |  \\                      / /      \      /
  .'        /       |   \\                   /' /        \   /'
 /  ____  /         |    \`\.__/-~~ ~ \ _ _/'  /          \/'
/-'~    ~~~~~---__  |     ~-/~         ( )   /'        _--~`
                  \_|      /        _)   ;  ),   __--~~
                    '~~--_/      _-~/-  / \   '-~ \
                   {\__--_/}    / \\_>- )<__\      \
                   /'   (_/  _-~  | |__>--<__|      |
                  |0  0 _/) )-~     | |__>--<__|     |
                  / /~ ,_/       / /__>---<__/      |
                 o o _/        /-~_>---<__-~      /
                 (^(~          /~_>---<__-      _-~
                ,/|           /__>--<__/     _-~
             ,//('(          |__>--<__|     /                  .----_
            ( ( '))          |__>--<__|    |                 /' _---_~\
         `-)) )) (           |__>--<__|    |               /'  /     ~\`\
        ,/,'//( (             \__>--<__\    \            /'   /        ||
      ,( ( ((, ))              ~-__>--<_~-_  ~--____---~' _/'/        /'
    `~/  )` ) ,/|                 ~-_~>--<_/-__       __-~ _/
  ._-~//( )/ )) `                    ~~-'_/_/ /~~~~~~~__--~
   ;'( ')/ ,)(                              ~~~~~~~~~~
  ' ') '( (/
    '   '  `"""
        text.trim()
        println(Colors.RED + text + Colors.RESET)
    }

    fun printDragonIceArt() {
        val text = """                        |\                                       ___.
                        ||\_____          /====================>/`,--`
                        ||    ,(__,      //  ,_____,----------//_:
                        ||   /    |  ,=='  ./                / \  \
                ,      ,|:  / ,___\_/     /     ,____,------(   \_ \
               />\    /<\  \       /       ____'     \.      \   |  \
              // /,,,,\ \\__J_____/      ,     \       \.  ___\  |_  \
             /'  ~""${'"'}${'"'}~  '\      /        \     \        \/     /     >
         __|\___~,|  |,~___/|__ /          \     \       /___--'`    /
         \_  /\  _/       /\    |     \ _____|          /
         /_  \__>|=\/=|<__/  _\      |  \   |      (               /
           |/  |~>====<~|  \|            \  | _____|              /
            \  \/  /              \ |/                 |,/
           / \  >VvvvvV<  /                \/     \            \__,
          /,^,\_\ |  | /_/     \        |          \             /
     _.--'`: :,-//\^^/\\ `,     |      /    ,______=\_____,     /
      `--.__.__-\======/   `====|     |===='    `---,    /     /
                 ~~~~~~         |     |            /    /     /
                              __/     \__       __|    |     |
                             /---^.=,^---\     /---^.=_/     \__
                             '     V     '     '    /---^.=.^---\
                                                    '     V     '"""
        text.trim()
        println(Colors.BLUE + text + Colors.RESET)
    }

    fun printEmptyLine() {
        println("")
    }

    fun printEmptyLines(){
        println("")
        println("")
        println("")
    }



    fun printBattleLose() {
        println(Colors.getColorText("All heroes died. YOU LOSE!", Colors.PURPLE))
    }

    fun printBattleWin() {
        println(Colors.getColorText("All enemies have been slain. YOU WIN!", Colors.PURPLE))
    }

    fun printSpawnMinion(name: String, msg: String) {
        println(Colors.getColorText("$name $msg", Colors.YELLOW))
    }

    fun printNoSkillsIdle(name: String) {
        println(Colors.getColorText("$name stares angrily.", Colors.PURPLE))
    }

    fun printMainMenu() {
        println(Colors.getColorText("AVAILABLE ACTIONS:", Colors.BG_CYAN))
        println(Colors.getColorText("\t[1] Use Skill\n\t[2] Inventory\n\t[3] Status\n\t[4] Wait", Colors.CYAN))
    }

    private fun printMenuBackButton() {
        println(Colors.getColorText("\t[↩] BACK", Colors.CYAN))
    }

    fun printSkillsMenu(skills: List<Skill>) {
        println(Colors.getColorText("AVAILABLE SKILLS:", Colors.BG_CYAN))

        for (i in skills.indices) {
            val uses = skills[i].usesLeft
            val maxUses = skills[i].usesPerBattle
            val color = if (uses != 0) {
                Colors.CYAN
            } else {
                Colors.BLACK
            }
            var text = "$color\t[${i+1}] ${skills[i].name} | "
            text += if (uses >= 0) {
                "$uses/${maxUses} | "
            } else {
                "∞ | "
            }
            text += "${Colors.RESET} ${skills[i].description}"
            println(text)
        }

        printEmptyLine()
        printMenuBackButton()
    }

    fun printTargetsMenu(targets: List<Fighter>) {
        println(Colors.getColorText("AVAILABLE TARGETS:", Colors.BG_CYAN))

        for (i in targets.indices) {
            var text = Colors.CYAN + "\t[${i+1}] " + Colors.RESET
            text += targets[i].fancyDisplayNamePlate()
            println(text)
        }

        printEmptyLine()
        printMenuBackButton()
    }

    fun printInventoryMenu(items: List<Item>) {
        println(Colors.getColorText("AVAILABLE ITEMS:", Colors.BG_CYAN))

        if (items.isEmpty()) {
            println(Colors.getColorText("\t --- EMPTY ---", Colors.CYAN))
        } else {
            for (i in items.indices) {
                val text = "${Colors.CYAN}\t[${i+1}] ${items[i].name} | ${items[i].count}x | ${Colors.RESET}${items[i].desc}"
                println(text)
            }
        }

        printEmptyLine()
        printMenuBackButton()
    }

    fun printStatusMenu(heroes: List<Fighter>, enemies: List<Fighter>) {
        println(Colors.getColorText("SELECT STATUS:", Colors.BG_CYAN))

        println("\t\t--- HEROES ---")
        for (i in heroes.indices) {
            var text = Colors.getColorText("\t[${i+1}] ", Colors.CYAN)
            text += heroes[i].fancyDisplayNamePlate()
            println(text)
        }

        println("\t\t--- ENEMIES ---")
        for (j in enemies.indices) {
            var text = Colors.getColorText("\t[${heroes.size+j+1}] ", Colors.CYAN)
            text += enemies[j].fancyDisplayNamePlate()
            println(text)
        }

        printEmptyLine()
        printMenuBackButton()
    }

    fun printStatusSelected(selected: Fighter) {
        println(Colors.getColorText("FIGHTER STATUS:", Colors.BG_CYAN))

        println("\t${selected.name}")
        println("\t${selected.fancyDisplayHealthFull()}")
        println("\t${("Damage ").padEnd(30, '∙')} ${(selected.dmgReal*100).toInt()}%")
        println("\t${("Resist Status Effect ").padEnd(30, '∙')} ${(selected.resistReal*100).toInt()}%")
        println("\t${("Speed ").padEnd(30, '∙')} ${selected.speedReal}pts")

        for (fx in selected.effects) {
            println(Colors.getColorText("\t${fx.icon} ${fx.name} (${fx.duration}) - ${fx.desc}", Colors.YELLOW))
        }

        printEmptyLine()
        printMenuBackButton()
    }

    fun printWaitTurn(name: String) {
        println(Colors.getColorText("↠ $name waits until next turn.", Colors.WHITE))
    }

    fun printActionFailed(name: String) {
        println(Colors.getColorText("↠ Nothing else happened to $name...", Colors.WHITE))
    }

    fun printPartyMembersStatus(party: List<Fighter>, first: Fighter) {
        val partyOrdered = reorderPartyByFighterAsFirst(party, first)
        var firstLine = ""
        var secondLine = ""
        var thirdLine = ""
        for (i in partyOrdered.indices) {
            val padding = if (i == 0) { 60 } else { 46 }
            if (i == 0) {
                firstLine += "${Colors.CYAN}► ${Colors.RESET}"
            }
            val paddingArrow = if (i == 0) { 2 } else { 0 }
            firstLine += partyOrdered[i].name.take(padding - 1 - 18).padEnd(padding - 18 - paddingArrow, ' ') // 18 magic number because of color codes!!
            secondLine += partyOrdered[i].fancyDisplayHealthFull().padEnd(padding, ' ')
            var fxColumns = ""
            for (fx in partyOrdered[i].effects) {
                fxColumns += "${fx.getIconDuration()} "
            }
            thirdLine += fxColumns.padEnd(padding - 18, ' ') // 18 magic number because of color codes!!
        }
        println(firstLine)
        println(secondLine)
        println(Colors.getColorText(thirdLine, Colors.YELLOW))
    }



    fun printItemUse(itemName: String, targetName: String) {
        println(Colors.getColorText("↪ $targetName used item [$itemName].", Colors.PURPLE))
    }



    fun printCastSkill(caster: String, skill: String, targets: String) {
        println(Colors.getColorText("↠ $caster uses [$skill] on $targets.", Colors.PURPLE))
    }

    fun printTakeDamage(target: String, damage: Int) {
        println(Colors.getColorText("↠ $target has taken $damage damage.", Colors.RED))
    }

    fun printTakeHeal(target: String, heal: Int) {
        println(Colors.getColorText("↠  $target healed $heal HP.", Colors.GREEN))
    }

    fun printDie(target: String) {
        println(Colors.getColorText("↠ $target has died!", Colors.BLUE))
    }

    fun printAddStatusEffect(target: String, effect: String, duration: Int) {
        var result = "↠ $effect casted upon $target"
        if (duration < 0) {
            result += "!"
        } else {
            result += " for $duration turn"
            result += if (duration > 1) {
                "s!"
            } else {
                "!"
            }
        }
        println(Colors.getColorText(result, Colors.WHITE))
    }

    fun printExpireStatusEffect(target: String, effect: String) {
        println(Colors.getColorText("↠ $target's $effect subsides...", Colors.YELLOW))
    }

    fun printRemovedStatusEffect(target: String, effect: String) {
        println(Colors.getColorText("↠ $target's $effect was removed!", Colors.YELLOW))
    }

    fun printNoRemovedStatusEffect(target: String) {
        println(Colors.getColorText("↠ No status effect on $target was removed...", Colors.YELLOW))
    }
    
    fun printAffectStatusEffect(target: String, effect: String) {
        println(Colors.getColorText("↠ $target is affected by $effect...", Colors.YELLOW))
    }

    fun printEffectStun(target: String) {
        println(Colors.getColorText("↠ $target" +
                " cannot move!", Colors.PURPLE))
    }

    fun printEffectAggo(target: String, src: String) {
        println(Colors.getColorText("↠ $target is forced to attack $src", Colors.PURPLE))
    }

    fun printEffectBlock(target: String) {
        println(Colors.getColorText("↠ $target is prepared for incoming attacks.", Colors.PURPLE))
    }

    fun printEffectDebuffSpd(target: String) {
        println(Colors.getColorText("↠ $target is slowed down.", Colors.PURPLE))
    }

    fun printEffectBuffAtk(target: String) {
        println(Colors.getColorText("↠ $target deals increased damage.", Colors.PURPLE))
    }

    fun printEffectBuffRes(target: String) {
        println(Colors.getColorText("↠ $target is more resistant against status effects.", Colors.PURPLE))
    }
}
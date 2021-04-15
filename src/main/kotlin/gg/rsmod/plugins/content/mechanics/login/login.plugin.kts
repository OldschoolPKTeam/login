package gg.rsmod.plugins.content.mechanics.login

import gg.rsmod.plugins.content.inter.welcome.WelcomeScreen.openChristmasWelcome

// TODO this script should be broken up into it's components so it's not a catch all for login stuff

val ENABLE_WELCOME_SCREEN = false

/**
 * Closing main modal for players.
 */
set_modal_close_logic {
    val modal = player.interfaces.getModal()
    if (modal != -1) {
        player.closeInterface(modal)
        player.interfaces.setModal(-1)
        player.world.plugins.executeModalClose(player, modal)
    }
}

/**
 * Check if the player has a menu opened.
 */
set_menu_open_check {
    player.getInterfaceAt(dest = InterfaceDestination.MAIN_SCREEN) != -1
}

/**
 * Execute when a player logs in.
 */
on_login {
    // Skill-related logic.
    if (player.getSkills().getMaxLevel(Skills.HITPOINTS) < 10) {
        player.getSkills().setBaseLevel(Skills.HITPOINTS, 10)
    }
    player.calculateAndSetCombatLevel()
    player.sendWeaponComponentInformation()
    player.sendCombatLevelText()

    // Interface-related logic.
    if (ENABLE_WELCOME_SCREEN) openChristmasWelcome(player) else openViewportInterface(player)

    // Inform the client whether or not we have a display name.
    val displayName = player.username.isNotBlank()
    player.runClientScript(1105, if (displayName) 1 else 0) // Has display name
    player.runClientScript(423, player.username)
    if (player.getVarp(1055) == 0 && displayName) {
        player.syncVarp(1055)
    }
    player.setVarbit(8119, 1) // Has display name

    // Sync attack priority options.
    player.syncVarp(OSRSGameframe.NPC_ATTACK_PRIORITY_VARP)
    player.syncVarp(OSRSGameframe.PLAYER_ATTACK_PRIORITY_VARP)

    // Send player interaction options.
    player.sendOption("Follow", 3)
    player.sendOption("Trade with", 4)
    //player.sendOption("Report", 5)

    // Game-related logic.
    player.sendRunEnergy(player.runEnergy.toInt())
    player.message("Welcome to ${world.gameContext.name}.", ChatMessageType.GAME_MESSAGE)
}

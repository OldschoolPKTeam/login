package gg.rsmod.plugins.content.mechanics.login

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.OSRSGameframe
import gg.rsmod.plugins.api.ext.getVarbit
import gg.rsmod.plugins.api.ext.openInterface
import gg.rsmod.plugins.api.ext.openOverlayInterface

fun openViewportInterface(player: Player, fullscreen: Boolean = false) {
    player.openOverlayInterface(player.interfaces.displayMode)

    InterfaceDestination.getModals().forEach { pane ->
        if (pane == InterfaceDestination.XP_COUNTER && player.getVarbit(OSRSGameframe.XP_DROPS_VISIBLE_VARBIT) == 0) {
            return@forEach
        } else if (pane == InterfaceDestination.MINI_MAP && player.getVarbit(OSRSGameframe.HIDE_DATA_ORBS_VARBIT) == 1) {
            return@forEach
        } else if (pane == InterfaceDestination.QUEST_ROOT) {
            player.openInterface(pane.interfaceId, pane, fullscreen)
            player.openInterface(InterfaceDestination.QUEST_ROOT.interfaceId, 33, 399, 1)
            return@forEach
        }
        player.openInterface(pane.interfaceId, pane, fullscreen)
    }
}
# better-sprint
Minecraft Paper Plugin for versions 1.21-1.21.11, requires ProtcolLib

This plugin cancels certain clientbound packets responsible for changing the sprinting, sneaking or swimming behaviour on the client. This makes normal gameplay a lot more enjoyable for players with high ping and fixes different bugs related to sprinting.

Due to the way sprinting is implemented on the client, it is not possible to completely remove all server-side influence on sprinting. On high ping, certain sprinting glitches can accur, when:
- The player is set on fire or extinguished
- The invisibility/glowing effects are given/removed
- The player exits Elytra flight

This plugin will most likely interfere with some AntiCheats

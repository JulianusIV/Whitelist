[![CodeQL](https://github.com/JulianusIV/Whitelist/actions/workflows/codeql-analysis.yml/badge.svg?branch=master)](https://github.com/JulianusIV/Whitelist/actions/workflows/codeql-analysis.yml)
# Whitelist
A Minecraft plugin for managing a servers whitelist via Discord, and connecting a Discord channel with the gamechat.

## Installation
Download jar file from the [latest release](https://github.com/JulianusIV/Whitelist/releases), or build it yourself with maven by cloning this repository.
Copy the jar file into the plugins folder of your Spigot server.
Run the server once to generate all necessary files, and fill in the config.yml.

## Da Config
This is a default config file:
```yml
# enables discord
enableDiscord: false
# discord bot token - never show that token to anyone - it is a username and password for your bot in one
botToken: null
# discord bot prefix - replace if you want to change from .
botPrefix: '.'
# a list of roles allowed to whitelist through discord. Example:
############################
# allowedRoleIds:          #
#   - '760522213676941332' #
#   - '850019046345801768' #
#   - '701844438153953420' #
#   - '722064316479701083' #
#   - '748533112404705281' #
#   - '819296501451980820' #
#   - '707915540085342248' #
#   - '759456739669704784' #
#   - '749576790929965136' #
#   - '702925135568437270' #
############################
allowedRoleIds:
 - role: null
# a list of roles allowed to whiteban through discord - ingame the whitelist.listban permission is used. Example:
############################
# allowedModRoleIds:       #
#   - '760522213676941332' #
#   - '822957506674163792' #
############################
allowedModRoleIds:
 - null
# should a discord channel be linked with ingame chat
linkChannels: false
# channel id of the linked discord channel
channelLinkId: null
# the message that gets displayed ingame when an online member gets banned (minecrafts color coding should still work here)
banMessage: 'Thou hast been banned™'
# the message that gets displayed when a user is not whitelisted (minecrafts color coding should still work here)
connectionFailMessage: 'You are not whitelisted'
```

In the following sections you will find each setting explained

### enableDiscord
Set this to ``true`` if you want to add a Discord bot to the plugin for whitelisting and/or banning via Discord, aswell as chatsync.
Set it to ``false`` if you just want to use the plugin as a whitelist manager

### botToken
The token of the Discord bot, leave it as null if you have the bot turned off in the previous setting.
How to find the token of a bot (and how to create one) you can read [here](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token).

### botPrefix
The prefix the bot should use (not recommended to use ``/`` due to conflicts with discords slash command system).

### allowedRoleIds
A list of (Discord) roles that is allowed to whitelist via Discord.
ex:
```yml
allowedRoleIds:
  - '760522213676941332'
  - '850019046345801768'
  - '701844438153953420'
  - '722064316479701083'
```

### allowedModRoleIds
A list of (Discord) roles that is allowed to ban via Discord.
ex:
```yml
allowedRoleIds:
  - '760522213676941332'
  - '822957506674163792'
```

### linkChannels
Set to true if you want your Minecraft chat to get sent to a Discord channel.
Set to false if you dont want that.

### channelLinkId
Put the id of the channel to link to Minecraft chat here.
ex:
```yml
channelLinkId: '512370308976607250'
```

### banMessage
The message to be displayed to a user that is banned while on the server.

### connectionFailMessage
The message to be displayed to a user that is not whitelisted.

## How do i get these ID's?
In Discord open your settings and go to the ``Advanced`` tab and enable ``Developer Mode``.
This will enable you to right click roles, channels, users and all that, and select ``Copy ID``

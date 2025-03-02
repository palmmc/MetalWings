# Metal Wings
Server sided, vanilla compatible armored elytra

Like Vanilla Tweaks' Armored Elytra, available for 1.20.6, 1.21(.1), and 1.21.4

## Usage
You can combine elytra with a chestplate in an anvil, make sure you put the chestplate in the left slot. You can 
then separate them by putting them in a grindstone.

The mod works perfectly fine server only, and when on the client it'll render both the elytra and chestplate on 
players and armor stands.

## Vanilla Tweaks
This mod supports Vanilla Tweaks' format for armored elytra, meaning if the datapack is used on a server, this
mod will render both the chestplate and elytra when installed on the client.

It can also store its armored elytra using Vanilla Tweaks' format (this is the default).

Keep in mind that Vanilla Tweaks will act weirdly when using modded chestplate or elytra, and may act weirdly
or break compatibility after an update.

## Storage Format
You can change the format armored elytra are stored in using the command
`/metalwings storageMode [CUSTOM_DATA|BUNDLE_CONTENTS]`

You will either need to be in a singleplayer world or have op to be able to use this command.
When upgrading from 1.0.0, you will probably want to change the storage format to custom data.

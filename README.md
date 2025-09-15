# Horde — Velocity Chat Plugin

A lightweight, zero-fuss chat plugin for Velocity. Horde provides global and local chat with clean, configurable templates, permission-based prefixes, and minimal moving parts.

- Platform: Velocity
- Java: 21+

## Features
- Global and local chat: broadcast across the proxy or keep it server-local.
- Per-player default mode: toggle with `/broadcast` or `/local` without arguments.
- Minimal config: sensible defaults, yet still extremely customizable
- Flexible formatting: MiniMessage or Legacy (§/&) style for localizations, templates and chat.
- Prefixes by permission: inject prefixes via `prefix.yml` in your chat templates.
- Name mappings: show pretty server names (e.g., `survival` -> `Survival`).
- Optional sender UUID forwarding: better compatibility with some client mods.
- Optional message logging: mirror chat into the proxy logs.

## Requirements
- Velocity proxy 3.x
- Java 21+

## Installation
1. Build or download the plugin JAR.
2. Place the JAR into your Velocity `plugins/` folder.
3. Start Velocity once to generate config files at `plugins/horde/`.
4. Edit `config.yml`, `localization.yml`, and `prefix.yml` to taste.
5. Reload with `/horde reload` or restart the proxy.

## Configuration
Files live under `plugins/horde/` unless otherwise noted.

### `config.yml`
- `default-global-chat` (boolean, default `true`):
  - When true, players chat globally by default; when false, locally.
  - Players can override their own default using `/broadcast` or `/local` with no message.
- `log-messages` (boolean, default `false`): also log chat to the proxy console.
- `enable-legacy-chat-formatting` (boolean, default `true`):
  - Applies only to raw messages players type (the message content).
  - When true, players can use `&` codes (e.g., `&aHello`). Parsed as Legacy (§) via Adventure.
  - When false, raw messages are parsed as MiniMessage.
- `enable-legacy-localization-formatting` (boolean, default `false`):
  - Applies to localization strings and templates in `localization.yml` and `prefix.yml`.
  - When true, those files are parsed as Legacy (§); otherwise MiniMessage.
  - Note: default templates assume MiniMessage.
- `forward-sender-uuid` (boolean, default `false`):
  - Sends messages with the author UUID using Adventure Identity when enabled.
  - Can improve compatibility with client mods like Chat Heads.
  - Mojang secure profiles may block unsigned identities; requires `enforce-secure-profiles=false` on backends to fully function (use at your risk).

### `localization.yml`
- General messages and chat templates.
- Placeholders are denoted `{key}` and replaced at runtime.
- Built-in placeholders provided by Horde when formatting chat:
  - `{server}`: pretty server name from `SERVER_NAME_MAPPINGS`.
  - `{player}`: the sender’s username.
  - Any keys you define in `prefix.yml` (see below) can appear here.
- Key templates:
  - `GLOBAL_CHAT_TEMPLATE`: template prefix for global messages.
  - `LOCAL_CHAT_TEMPLATE`: template prefix for local messages.
- Example (default):
  ```
  GLOBAL_CHAT_TEMPLATE: "[{server}]{player}:" # message appended after template
  LOCAL_CHAT_TEMPLATE: "{player}:"           # message appended after template
  ```
- Name mappings:
  ```yaml
  SERVER_NAME_MAPPINGS:
    lobby: "<aqua>Lobby</aqua>"
    survival: "<green>Survival</green>"
  ```

### `prefix.yml`
- Define one or more prefix sets keyed by a placeholder name.
- Each set is an ordered list of permission checks; the first matching permission wins.
- Example:
  ```yaml
  any_permission_prefix_set:
    - permission: "horde.prefix.moderator"
      prefix: "<red>[MOD]</red>"
    - permission: "horde.prefix.member"
      prefix: "<gray>[MEMBER]</gray>"
    - permission: "horde.prefix.default"
      prefix: "<white>[PLAYER]</white>"
  ```
- Use `{any_permission_prefix_set}` in your `localization.yml` templates to insert the resulting prefix (or empty if none matched).

### Data file
- `data/preferences.yml`: per-player defaults (global vs local) stored by UUID. Automatically managed.

## Commands
- `/horde help` — Show help.
- `/horde reload` — Reload configs. Permission: `horde.command.reload`.
- `/broadcast [message…]` (aliases: `/b`, `/bc`)
  - With message: send a global chat message now.
  - Without message: set your default chat mode to Global.
  - Requires `horde.command.broadcast`.
- `/local [message…]` (aliases: `/l`, `/lc`)
  - With message: send a local (same-server) chat message now.
  - Without message: set your default chat mode to Local.
  - Requires `horde.command.local`.

## How chatting works
- Horde listens to player chat events and replaces vanilla chat delivery.
- If your default mode is Global and you have `horde.command.broadcast`, your messages broadcast proxy-wide.
- If your default mode is Local and you have `horde.command.local`, your messages deliver only to your current server.
- If you lack the needed permission for your selected mode, Horde blocks the message and shows a no-permission notice.

## Permissions
- `horde.command.reload`: use `/horde reload`.
- `horde.command.broadcast`: use `/broadcast` or chat globally when set to global.
- `horde.command.local`: use `/local` or chat locally when set to local.
- Prefix examples you might grant via your permissions plugin:
  - `horde.prefix.moderator`, `horde.prefix.member`, `horde.prefix.default`, etc. (you choose names/values)

## Formatting notes
- Templates/localizations: MiniMessage by default; set `enable-legacy-localization-formatting: true` to switch to Legacy (§).
- Raw player message: `enable-legacy-chat-formatting: true` lets players use `&` codes; otherwise raw content is MiniMessage.
- Avoid double-formatting: don’t add color codes both in templates and in the mapped names/prefixes unless intended.

## Build From Source
Requires JDK 21 and Maven.

```powershell
# From the project root
mvn -V -B clean package
```

- The built JAR will be in `target/`.
- Copy it into `plugins/` on your Velocity proxy and start the server.

## Compatibility
- Velocity API: uses `PlayerChatEvent` and command APIs from Velocity 3.x.
- Identity forwarding: marked deprecated by Velocity for secure profiles; toggle `forward-sender-uuid` with care.

## License
MIT License — see `LICENSE`.

---
Need help or have suggestions? Open an issue or PR. Enjoy the chat!
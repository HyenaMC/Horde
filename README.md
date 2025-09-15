# Horde — Velocity 聊天插件 | Velocity Chat Plugin

一款轻量、零折腾的 Velocity 聊天插件。Horde 提供全局与本地聊天、简洁可配的模板、基于权限的前缀，并尽量减少复杂度。
A lightweight, zero-fuss chat plugin for Velocity. Horde provides global and local chat with clean, configurable templates, permission-based prefixes, and minimal moving parts.

- 平台：Velocity
- Platform: Velocity
- Java：21+
- Java: 21+

## 功能 | Features
- 全局与本地聊天：可在整个代理广播，或仅限当前子服本地。
- Global and local chat: broadcast across the proxy or keep it server-local.
- 玩家默认模式：使用不带参数的 `/broadcast` 或 `/local` 在个人默认全局/本地之间切换。
- Per-player default mode: toggle with `/broadcast` or `/local` without arguments.
- 极简配置：开箱即用的合理默认，同时保持高度可定制。
- Minimal config: sensible defaults, yet still extremely customizable.
- 灵活格式：本地化、模板和聊天文本支持 MiniMessage 或传统颜色码（Legacy §/&）。
- Flexible formatting: MiniMessage or Legacy (§/&) style for localizations, templates and chat.
- 基于权限的前缀：通过 `prefix.yml` 在聊天模板中注入前缀。
- Prefixes by permission: inject prefixes via `prefix.yml` in your chat templates.
- 名称映射：为服务器名展示易读的别名（如 `survival` -> `Survival`）。
- Name mappings: show pretty server names (e.g., `survival` -> `Survival`).
- 可选的发送者 UUID 转发：提升与某些客户端模组的兼容性。
- Optional sender UUID forwarding: better compatibility with some client mods.
- 可选的消息日志：将聊天镜像到代理控制台日志。
- Optional message logging: mirror chat into the proxy logs.

## 环境要求 | Requirements
- Velocity 代理 3.x
- Velocity proxy 3.x
- Java 21 及以上
- Java 21+

## 安装 | Installation
1. 构建或下载插件 JAR。
2. 将 JAR 放入 Velocity 的 `plugins/` 目录。
3. 启动一次 Velocity 以在 `plugins/horde/` 生成配置文件。
4. 按需编辑 `config.yml`、`localization.yml` 与 `prefix.yml`。
5. 使用 `/horde reload` 重载或重启代理。
1. Build or download the plugin JAR.
2. Place the JAR into your Velocity `plugins/` folder.
3. Start Velocity once to generate config files at `plugins/horde/`.
4. Edit `config.yml`, `localization.yml`, and `prefix.yml` to taste.
5. Reload with `/horde reload` or restart the proxy.

## 配置 | Configuration
除特别说明外，配置文件位于 `plugins/horde/`。
Files live under `plugins/horde/` unless otherwise noted.

### `config.yml`
- `default-global-chat`（布尔，默认 `true`）：
  - 为 `true` 时玩家默认全局聊天；为 `false` 时默认本地聊天。
  - 玩家可通过不带消息的 `/broadcast` 或 `/local` 覆盖自己的默认模式。
- `default-global-chat` (boolean, default `true`):
  - When true, players chat globally by default; when false, locally.
  - Players can override their own default using `/broadcast` or `/local` with no message.
- `log-messages`（布尔，默认 `false`）：同时将聊天输出到代理控制台日志。
- `log-messages` (boolean, default `false`): also log chat to the proxy console.
- `enable-legacy-chat-formatting`（布尔，默认 `true`）：
  - 仅作用于玩家输入的原始聊天内容。
  - 为 `true` 时允许使用 `&` 颜色码（如 `&aHello`），按 Legacy (§) 解析。
  - 为 `false` 时原始消息按 MiniMessage 解析。
- `enable-legacy-chat-formatting` (boolean, default `true`):
  - Applies only to raw messages players type (the message content).
  - When true, players can use `&` codes (e.g., `&aHello`). Parsed as Legacy (§) via Adventure.
  - When false, raw messages are parsed as MiniMessage.
- `enable-legacy-localization-formatting`（布尔，默认 `false`）：
  - 作用于 `localization.yml` 与 `prefix.yml` 中的本地化字符串与模板。
  - 为 `true` 时按 Legacy (§) 解析；否则为 MiniMessage。
  - 注意：默认模板基于 MiniMessage。
- `enable-legacy-localization-formatting` (boolean, default `false`):
  - Applies to localization strings and templates in `localization.yml` and `prefix.yml`.
  - When true, those files are parsed as Legacy (§); otherwise MiniMessage.
  - Note: default templates assume MiniMessage.
- `forward-sender-uuid`（布尔，默认 `false`）：
  - 启用后通过 Adventure Identity 附带作者 UUID 发送消息。
  - 可提升与 Chat Heads 等客户端模组的兼容性。
  - Mojang 安全档案可能阻止未签名身份；如需完整生效，后端需设置 `enforce-secure-profiles=false`（风险自负）。
- `forward-sender-uuid` (boolean, default `false`):
  - Sends messages with the author UUID using Adventure Identity when enabled.
  - Can improve compatibility with client mods like Chat Heads.
  - Mojang secure profiles may block unsigned identities; requires `enforce-secure-profiles=false` on backends to fully function (use at your risk).

### `localization.yml`
- 存放通用消息与聊天模板。
- 占位符以 `{key}` 表示，并在运行时替换。
- Horde 在格式化聊天时提供的内置占位符：
  - `{server}`：来自 `SERVER_NAME_MAPPINGS` 的美化服务器名称。
  - `{player}`：发送者用户名。
  - 你在 `prefix.yml` 中定义的任意键（见下）也可在此使用。
- General messages and chat templates.
- Placeholders are denoted `{key}` and replaced at runtime.
- Built-in placeholders provided by Horde when formatting chat:
  - `{server}`: pretty server name from `SERVER_NAME_MAPPINGS`.
  - `{player}`: the sender’s username.
  - Any keys you define in `prefix.yml` (see below) can appear here.
- 关键模板键：
  - `GLOBAL_CHAT_TEMPLATE`：全局消息的模板前缀。
  - `LOCAL_CHAT_TEMPLATE`：本地消息的模板前缀。
- Key templates:
  - `GLOBAL_CHAT_TEMPLATE`: template prefix for global messages.
  - `LOCAL_CHAT_TEMPLATE`: template prefix for local messages.
- 示例（默认）：
  ```
  GLOBAL_CHAT_TEMPLATE: "[{server}]{player}:" # 模板后会追加消息内容
  LOCAL_CHAT_TEMPLATE: "{player}:"           # 模板后会追加消息内容
  ```
- Example (default):
  ```
  GLOBAL_CHAT_TEMPLATE: "[{server}]{player}:" # message appended after template
  LOCAL_CHAT_TEMPLATE: "{player}:"           # message appended after template
  ```
- 名称映射：
  ```yaml
  SERVER_NAME_MAPPINGS:
    lobby: "<aqua>Lobby</aqua>"
    survival: "<green>Survival</green>"
  ```
- Name mappings:
  ```yaml
  SERVER_NAME_MAPPINGS:
    lobby: "<aqua>Lobby</aqua>"
    survival: "<green>Survival</green>"
  ```

### `prefix.yml`
- 定义一个或多个以前缀占位符名为键的前缀集合。
- 每个集合是按顺序进行的权限检查，首个匹配的权限决定所用前缀。
- Define one or more prefix sets keyed by a placeholder name.
- Each set is an ordered list of permission checks; the first matching permission wins.
- 示例：
  ```yaml
  any_permission_prefix_set:
    - permission: "horde.prefix.moderator"
      prefix: "<red>[MOD]</red>"
    - permission: "horde.prefix.member"
      prefix: "<gray>[MEMBER]</gray>"
    - permission: "horde.prefix.default"
      prefix: "<white>[PLAYER]</white>"
  ```
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
- 在 `localization.yml` 模板中使用 `{any_permission_prefix_set}` 插入解析出的前缀（无匹配则为空）。
- Use `{any_permission_prefix_set}` in your `localization.yml` templates to insert the resulting prefix (or empty if none matched).

### 数据文件 | Data file
- `data/preferences.yml`：按 UUID 存储玩家默认聊天模式（全局/本地），由插件自动管理。
- `data/preferences.yml`: per-player defaults (global vs local) stored by UUID. Automatically managed.

## 指令 | Commands
- `/horde help` — 显示帮助。
  - _Show help._
- `/horde reload` — 重载配置。权限：`horde.command.reload`。
  - _Reload configs. Permission: `horde.command.reload`._
- `/broadcast [message…]`（别名：`/b`、`/bc`）
  - 带消息：立即发送全局聊天。
  - 不带消息：将你的默认聊天模式设为“全局”。
  - 权限：`horde.command.broadcast`
  - _With message: send a global chat message now. Without message: set your default chat mode to Global. Permission: `horde.command.broadcast`._
- `/local [message…]`（别名：`/l`、`/lc`）
  - 带消息：立即发送本地（同服）聊天。
  - 不带消息：将你的默认聊天模式设为“本地”。
  - 权限：`horde.command.local`
  - _With message: send a local (same-server) chat message now. Without message: set your default chat mode to Local. Permission: `horde.command.local`._

## 工作原理 | How chatting works
- Horde 监听玩家聊天事件并替换原版的聊天投递逻辑。
- 如果你的默认模式是“全局”且拥有 `horde.command.broadcast`，消息将代理范围广播。
- 如果你的默认模式是“本地”且拥有 `horde.command.local`，消息只会发送到当前服务器的玩家。
- 若所选模式缺少对应权限，Horde 会拦截消息并提示没有权限。
- Horde listens to player chat events and replaces vanilla chat delivery.
- If your default mode is Global and you have `horde.command.broadcast`, your messages broadcast proxy-wide.
- If your default mode is Local and you have `horde.command.local`, your messages deliver only to your current server.
- If you lack the needed permission for your selected mode, Horde blocks the message and shows a no-permission notice.

## 权限 | Permissions
- `horde.command.reload`：使用 `/horde reload`。
  - _Use `/horde reload`._
- `horde.command.broadcast`：使用 `/broadcast` 或在默认全局模式下进行全局聊天。
  - _Use `/broadcast` or chat globally when set to global._
- `horde.command.local`：使用 `/local` 或在默认本地模式下进行本地聊天。
  - _Use `/local` or chat locally when set to local._
- 可在权限插件中自定义授予的前缀权限示例：
  - `horde.prefix.moderator`、`horde.prefix.member`、`horde.prefix.default` 等（名称与权限值由你决定）。
  - _Prefix examples you might grant via your permissions plugin: `horde.prefix.moderator`, `horde.prefix.member`, `horde.prefix.default`, etc. (you choose names/values)._ 

## 格式化说明 | Formatting notes
- 模板/本地化：默认使用 MiniMessage；将 `enable-legacy-localization-formatting: true` 可切换为 Legacy (§)。
- Templates/localizations: MiniMessage by default; set `enable-legacy-localization-formatting: true` to switch to Legacy (§).
- 玩家原始消息：`enable-legacy-chat-formatting: true` 允许使用 `&` 颜色码；否则按 MiniMessage 解析。
- Raw player message: `enable-legacy-chat-formatting: true` lets players use `&` codes; otherwise raw content is MiniMessage.
- 避免重复着色：除非确有需要，不要在模板与名称映射/前缀中同时添加颜色代码。
- Avoid double-formatting: don’t add color codes both in templates and in the mapped names/prefixes unless intended.

## 从源码构建 | Build From Source
需要 JDK 21 与 Maven。
Requires JDK 21 and Maven.

```powershell
# 在项目根目录执行 / From the project root
mvn -V -B clean package
```

- 构建产物 JAR 位于 `target/`。
- 将其复制到你的 Velocity 代理的 `plugins/` 目录并启动服务器。
- The built JAR will be in `target/`.
- Copy it into `plugins/` on your Velocity proxy and start the server.

## 兼容性 | Compatibility
- Velocity API：使用 Velocity 3.x 的 `PlayerChatEvent` 与指令 API。
- Velocity API: uses `PlayerChatEvent` and command APIs from Velocity 3.x.
- 身份转发：在安全档案场景下被 Velocity 标记为不推荐；请谨慎使用 `forward-sender-uuid`。
- Identity forwarding: marked deprecated by Velocity for secure profiles; toggle `forward-sender-uuid` with care.

## 许可证 | License
MIT 许可证 —— 见 `LICENSE`。
MIT License — see `LICENSE`.

---
需要帮助或有建议？欢迎提交 Issue 或 PR。祝你聊天愉快！
Need help or have suggestions? Open an issue or PR. Enjoy the chat!
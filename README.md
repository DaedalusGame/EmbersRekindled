<img src="project_logo.png" align="left" width="180px"/>

# Embers - Unofficial Extended Life

[![License](https://img.shields.io/github/license/Ender-Development/Embers-Extended-Life.svg?label=License)](LICENSE)
[![Versions](https://img.shields.io/curseforge/game-versions/936489?logo=curseforge&label=Game%20Version)](https://www.curseforge.com/minecraft/mc-mods/embers-extended-life)
[![Downloads](https://img.shields.io/curseforge/dt/936489?logo=curseforge&label=Downloads)](https://www.curseforge.com/minecraft/mc-mods/embers-extended-life)

*A dwarven magic mod*
• [CurseForge](https://curseforge.com/minecraft/mc-mods/embers-extended-life)
• [Changelog](CHANGELOG.md)
• [Bugtracker](https://github.com/Ender-Development/Embers-Extended-Life/issues)

<br />

Embers is best described as a dwarven magic mod. It features a smattering of magical and technical content, from staples such as ore doubling and item transport to alchemy and magical ray guns. All centered around the core mechanic of Ember, a limited form of power that you must extract from the world's core near bedrock.

## Builds
You can download the latest files here on [curseforge](https://curseforge.com/minecraft/mc-mods/embers-extended-life).

## Maven Dependency
We use GitHub packages. If you need our mod as a dependency:

```groovy
repositories {
    maven {
        name "GitHubPackages.Ender-Development.Embers-Extended-Life"
        url "https://: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@maven.pkg.github.com/Ender-Development/Embers-Extended-Life"
    }
}

dependencies {
    implementation "teamroots.embers:embers_extended_life:1.20.0:dev"
}
```

See https://github.com/Ender-Development/Embers-Extended-Life/packages/

## Changes

Current Changes to Embers Rekindled v1.19:
- added a config option to display the exact amount of Ash needed in an Alcemy recipe
- registered Heat Coil in HEI as a smelting catalyst
- switched to [RetroFuturaGradle](https://github.com/GTNewHorizons/RetroFuturaGradle)
- updated dependencies to maintained forks
- fixed alchemy tablet breaking after 10 days of existing

Planned Changes:
- native [GroovyScript](https://github.com/CleanroomMC/GroovyScript) support
- merging of various Embers addons

## [Ender-Development](https://github.com/Ender-Development)

Our Team currently includes:
- `_MasterEnderman_` - Project-Manager, Lead-Artist, Developer
- `Klebestreifen` - Lead-Developer, Artist

You can contact us on our [Discord](https://discord.gg/JF7x2vG).

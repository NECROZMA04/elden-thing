```
`7MM"""YMM  `7MMF'      `7MM"""Yb. `7MM"""YMM  `7MN.   `7MF'    MMP""MM""YMM `7MMF'  `7MMF'`7MMF'`7MN.   `7MF' .g8"""bgd  
  MM    `7    MM          MM    `Yb. MM    `7    MMN.    M      P'   MM   `7   MM      MM    MM    MMN.    M .dP'     `M  
  MM   d      MM          MM     `Mb MM   d      M YMb   M           MM        MM      MM    MM    M YMb   M dM'       `  
  MMmmMM      MM          MM      MM MMmmMM      M  `MN. M           MM        MMmmmmmmMM    MM    M  `MN. M MM           
  MM   Y  ,   MM      ,   MM     ,MP MM   Y  ,   M   `MM.M           MM        MM      MM    MM    M   `MM.M MM.    `7MMF'
  MM     ,M   MM     ,M   MM    ,dP' MM     ,M   M     YMM           MM        MM      MM    MM    M     YMM `Mb.     MM  
.JMMmmmmMMM .JMMmmmmMMM .JMMmmmdP' .JMMmmmmMMM .JML.    YM         .JMML.    .JMML.  .JMML..JMML..JML.    YM   `"bmmmdPY  
```
# Elden Thing

**Elden Thing** is a turn-based, text-driven adventure game where players and non-player characters (NPCs) roam interconnected maps, interact with challenging terrain, and engage in dynamic combat. Inspired by rogue-like adventures, the game features unique terrain mechanics, interactive actors, and a day/night cycle that directly affects gameplay.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [How to Play](#how-to-play)
- [Installation & Running](#installation-&-running)
- [Contributing](#contributing)
- [License](#license)

## Overview

_Elden Thing_ builds on a custom-built game engine that supports multiple maps and an array of actors. The system processes every actor every turn, even when off-screen, meaning that the whole game world is always active. The central game loop ticks over maps, actors, and items, making each turn a chance for the world to change.

## Features

- **Dynamic Turn-Based Gameplay:** Every actor (including the player) has a turn in which they can perform actions, ranging from movement and combat to interacting with items.
- **Diverse Actor Types:** Play as a protagonist while facing challenging foes and interacting with friendly characters. NPCs such as hostile warriors and non-hostile merchants have their own behaviour logic.
- **Rich Terrain System:** 
  - **Ground Types:** From passable floors to impassable walls and special terrain like *BloodroseGround*‑which damages adjacent actors every turn—to interactive elements like *LockedDoor* that force the player to find alternate paths or break them down.
- **Day/Night and Environmental Effects:** Time managers and controllers adjust the game world (e.g. changing terrain or triggering status effects) as time passes.
- **Inventory and Items:** Pick up, use, and manage items from seeds to talismans, each with their own functionality.
- **Modular Architecture:** Designed for extensibility using behaviours for actors and actions that can be combined or chained to create complex moves.

## Architecture

The game is structured into several core modules:

- **Engine Core:**  
  The engine (found under the `edu.monash.fit2099.engine` package) supports maps, actors, actions, items, and the overall game loop. Key classes include:
  - [`GameMap`](src/edu/monash/fit2099/engine/positions/GameMap.java): Manages the grid, ticks over its cells and items, and processes environmental effects.
  - [`World`](src/edu/monash/fit2099/engine/positions/World.java): The central class that holds all game maps and drives the game loop.
  
- **Gameplay Components:**  
  Under the `game` package, the game-specific logic is implemented:
  - [`Application.java`](src/game/Application.java): The main entry point that sets up maps (such as “Valley of the Inheritree” and “Limveld”), actors, and special items.
  - Actor implementations such as [`Player.java`](src/edu/monash/fit2099/demo/mars/actors/Player.java) and various NPC classes.
  - Ground implementations such as [`LockedDoor.java`](src/edu/monash/fit2099/demo/mars/grounds/LockedDoor.java) and [`BloodroseGround.java`](src/game/grounds/BloodroseGround.java) define environmental behaviour.
  - Special behaviours (e.g. `SpitBehaviour`, `FollowBehaviour`) allow more complex decision-making for NPC actions.

- **Time & Environmental Effects:**  
  Classes like `TimeManager` and `TimeController` hook into the game loop to simulate day/night cycles and other time-dependent effects.

## How to Play

Players interact with the game in a text-based console environment. On each turn:
- The current map is rendered.
- A list of possible actions is generated based on the player's surroundings, available items, and nearby actors.
- The player chooses an action (e.g., move, attack, interact), and then the engine processes turns for all other actors, applying status effects and environmental changes.

Terrain matters: Some ground types may harm you (Bloodrose sweats nearby actors) or restrict your movement (Locked Doors that require a special action like smashing them with a window). The challenge lies in managing your inventory and planning your moves while the whole world is constantly evolving.

## Installation & Running

### Prerequisites
- Java 21 JDK or above

### Running the Game

1. **Clone the Repository:**

   ```shell
   git clone https://github.com/yourusername/elden-thing.git
   cd elden-thing
   ```

2. **Compile the Code:**  
   If you use an IDE like Visual Studio Code or run from the command line:
   
   ```shell
   javac -d bin src/**/*.java
   ```

3. **Run the Application:**  

   ```shell
   java -cp bin game.Application
   ```

Alternatively, if you’ve configured a build system like Maven or Gradle, use the corresponding commands to build and run the game.

Enjoy!!
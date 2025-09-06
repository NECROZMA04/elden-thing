```
`7MM"""YMM  `7MMF'      `7MM"""Yb. `7MM"""YMM  `7MN.   `7MF'    MMP""MM""YMM `7MMF'  `7MMF'`7MMF'`7MN.   `7MF' .g8"""bgd  
  MM    `7    MM          MM    `Yb. MM    `7    MMN.    M      P'   MM   `7   MM      MM    MM    MMN.    M .dP'     `M  
  MM   d      MM          MM     `Mb MM   d      M YMb   M           MM        MM      MM    MM    M YMb   M dM'       `  
  MMmmMM      MM          MM      MM MMmmMM      M  `MN. M           MM        MMmmmmmmMM    MM    M  `MN. M MM           
  MM   Y  ,   MM      ,   MM     ,MP MM   Y  ,   M   `MM.M           MM        MM      MM    MM    M   `MM.M MM.    `7MMF'
  MM     ,M   MM     ,M   MM    ,dP' MM     ,M   M     YMM           MM        MM      MM    MM    M     YMM `Mb.     MM  
.JMMmmmmMMM .JMMmmmmMMM .JMMmmmdP' .JMMmmmmMMM .JML.    YM         .JMML.    .JMML.  .JMML..JMML..JML.    YM   `"bmmmdPY  
```
## LLM-Powered Monolouge NPC

## Feature Story:

In the forgotten of grave, a statues known as the **Ghost of Whispers** stands. Legends speak of its ability to convey riddles and secrets from beyond. Now, through the power of an artificial mind, the Ghost and its variations can speak freely.

When the player approaches and listens, the Ghost creates a response in real time based on the player's inventory or previous actions, for example, if the player has killed creatures, the ghost mentions the whispers of the dead creatures from beyond the grave in the form of riddles.

Beyond the Ghost, two other LLM powered creatures wander the land:
1. **The Wandering Poet** who recites freshly composed verses about the player's actions.
2. **The Seer** who give possible glimpses at the future.

## Design Structure

![UML](docs/design/assignment3/UML/REQ3-(PROPOSAL).png)

```plaintext
Entry Point
└── Application

Higher-level class
└── LLMMonologueManager

Interface / Abstract classes
├── LLMDialogueBehaviour
├── LLMNPCDialogueBehaviour
├── ActionHistoryProvider
└── LLMNPC

NPC Implementations
├── Ghost
├── Poet
└── Seer

Lower-level classes
├── PoetBehaviour
├── SeerBehaviour
├── GhostBehaviour
└── LLMDialogueAction
```

## Class Overview

### Entry Point
- **Application**
  - Registers the LLM-powered NPCs (Ghost of Whispers, Wandering Poet, Seer) and attaches their behaviours and dialogue actions.

### Higher-Level Class
- **LLMMonologueManager**
    - Encapsulates all interactions with the LLM API using the **geminiCall(prompt)** method. 
    - Provides a single entry point for requesting dialogues.

### Interface / Abstract Class
- **LLMDialogueBehaviour**
    - Defines the outline for any NPC that wants to use the LLM for monologues. 
    - Provides methods to constructPrompts() and formatResponses for use by **LLMDialogueAction**.
    - Extends the **Behaviour** class from engine.
- **LLMNPCDialogueBehaviour**
  - Implements **LLMDialogueBehaviour** class to define a common attribute for the **LLMNPC**, **LLMMonologueManager** and **ActionHistoryProvider**, as well as the **getAction()** method.
- **ActionHistoryProvider**
  - Abstracts retrieval of the player's past actions to feed into prompts. 
  - Decouples dialogue behaviours from the concrete Player class.
- **LLMNPC**
  - Base class for all LLM-powered NPCs. 
  - Encapsulates common setup: name, hit points, attaching behaviour and dialogue action.

### NPC Implementations
1. **PoetBehaviour** 
   - Crafts prompts as poetic verses based on the player's recent actions, such as attacking or killing creatures, or planting seeds. Implements both **LLMDialogueConstructor** and **Behaviour**
   - Extends **LLMNPC** and wires up **PoetBehaviour**.
2. **SeerBehaviour** 
   - Crafts prompts that may reference actions being performed in the future while also using the context of all the player's previous actions. Implements both **LLMDialogueConstructor** and **Behaviour**
   - Extends **LLMNPC** and wires up **SeerBehaviour**.
3. **GhostBehaviour** 
   - Crafts prompts as riddles talking about the whispers from the creatures beyond the grave as well as referencing the player's previous actions. Implements both **LLMDialogueConstructor** and **Behaviour**
   - Extends **LLMNPC** and wires up **GhostBehaviour**.
4. **LLMDialogueAction** when the player selects talk, this action calls **LLMDialogueConstructor's** methods to construct prompts and request dialogues from Gemini, receives the response and passes it to another method to produce a Dialogue Action.

### Lower-Level Classes
- **PoetBehaviour**
  - Crafts prompts as poetic verses based on the player's recent actions. 
  - Implements both LLMDialogueBehaviour and Behaviour.
- **SeerBehaviour**
  - Crafts prompts that reference possible future actions while using full action history.
  - Implements both LLMDialogueBehaviour and Behaviour.
- **GhostBehaviour**
  - Crafts prompts as riddles about the whispers from beyond the grave, referencing past deeds.
  - Implements both LLMDialogueBehaviour and Behaviour.
- **LLMDialogueAction**
  - Extends Action so it integrates with the turn‐based system.
  - When selected, it calls LLMDialogueBehaviour’s methods to build prompts, invokes the LLM, then formats and displays the response.

### Integration with Engine
- **LLMNPC** extends **Actor** through extending **NPC**, so all health, location and turn logic remain unchanged.
- **LLMDialogueAction** extends **Action**, making “talk” easy in the existing turn system.
- Each behaviour **(PoetBehaviour, SeerBehaviour, GhostBehaviour)** implements the engine’s Behaviour interface, through the **LLMDialogueBehaviour** implementation.

### Justification (SOLID Principles)

- **Single Responsibility Principle (SRP)**: Each class has one clear responsibility **LLMMonologueManager** handles only the LLM API calls, **LLMDialogueAction** integrates with the turn system, and each behaviour class constructs its own prompts.
- **Open/Closed Principle (OCP)**: New NPC behaviours can be added by implementing the **LLMDialogueBehaviour** interface without modifying existing classes, or they can also extend the **LLMNPCDialogueBehaviour** class, which defines attributes to store manager, and also defines a common method to simply return an action. 
- **Liskov Substitution Principle (LSP)**: Any implementation of **LLMDialogueBehaviour** can replace another easily in the monologue flow.
- **Interface Segregation Principle (ISP)**: Interfaces like **LLMDialogueBehaviour** and **ActionHistoryProvider** remain lean, so implementers aren’t forced to depend on unused methods.
- **Dependency Inversion Principle (DIP)**: High-level modules **(LLMMonologueManager, LLMDialogueAction)** depend on abstractions **(LLMDialogueBehaviour, ActionHistoryProvider)** rather than concrete implementations such as the Player class directly.

This architecture promotes testability, maintainability, and clarity in LLM-driven dialogue logic.


### Drawbacks
- **API Latency & Performance Overhead**, each call of requestDialogue may cause network latency, repeated interactions could cause noticeable delays.
- **Unpredictable LLM Output**, despite careful prompt engineering, the LLM might generate out‐of‐context lore.

### Alternatives Considered
- **Hard‐Coded Dialogue Trees (No LLM)**
    - Pros 
      - Zero external dependencies so no latency.
      - Fully deterministic
    - Cons
        - Rapidly becomes unmanageable: every new context requires adding another condition.
        - Violates OCP: adding new lines forces modification of existing code.

### Future Extensions
- New LLM powered NPCs such as "Historian" which would involve simply implementing **LMMDialogueConstructor, Behaviour**, and extending NPC for **HistorianBehaviour**.
- **LLM-Generated Quests or Tasks**, introduce a new interface **LLMQuestBehaviour** implementing **LLMDialogueConstructor** that crafts simple side-quests.

# Day/Night System

## Feature Story:

“When the Sun Sets…”
In the Lands Between, time does not simply pass—it reshapes the world. With the rising of the sun, the valley awakens with gentle grace. But when the stars crown the skies, creatures mutate, darkness strengthens them, and even the farmer feels fatigue creeping in.

The Lands Between now experience the passage of time. Each game turn represents a tick forward in the world clock. A full day is divided into four time periods: **Morning**, **Afternoon**, **Evening**, and **Night**.

**Day/Night System** with 4 phases:
- **Morning** – sandstorms push the player 1 tile in a random direction.
- **Afternoon** – neutral gameplay, standard behavior.
- **Evening** – certain NPCs begin to become aggressive.
- **Night** – hostile enemies deal more damage.

These phases rotate every turn, simulating a dynamic passage of time. The system affects enemy stats, player attributes.


## Design Structure (Fulfills Provided Diagram)

![UML](docs/design/assignment3/UML/REQ4-(PROPOSAL).png)

```plaintext
Higher-level class
└── TimeManager

Interface / Abstract class
└── TimePhase

Lower-level classes
├── MorningPhase
├── EveningPhase
├── NightPhase
└── AfternoonPhase
```


## Class Overview

### Higher-level Class
- **TimeManager:** The central class responsible for advancing the time of day (Morning, Afternoon, Evening, Night) and notifying registered systems of the change.

### Abstract / Interface
- **TimePhase:** An interface that defines time-based behaviors. It provides methods like applyEffects() and getName().

### Lower-level Class
- **MorningPhase:** Triggers a sandstorm event that moves the player 1 tile in a random direction.
- **AfternoonPhase:** A neutral phase with no gameplay changes.
- **EveningPhase:** Initiates mild changes such as NPCs preparing for hostility.
- **NightPhase:** Applies major gameplay changes—hostile NPCs gain attack bonuses.


## Integration with FIT2099 Engine

- **`TimeManager` uses `GameMap`** to apply time-of-day effects to actors in specific locations.
- **Each TimePhase** can interact with engine components like `ActorLocations`, `Actions`, or `Behaviours` to simulate environmental and physiological changes.

## Justification (SOLID Principles)

- **Single Responsibility Principle (SRP)**: Each `TimePhase` handles exactly one time-of-day’s logic.
- **Open/Closed Principle (OCP)**: New phases can be added without modifying `TimeManager`.
- **Liskov Substitution Principle (LSP)**: Any subclass of `TimePhase` can replace another without failure.
- **Interface Segregation Principle (ISP)**: TimePhase interface is lean (`applyEffects`, `getName`) and doesn’t bloat implementers.
- **Interface Segregation Principle (ISP)**: `TimeManager` depends on `TimePhase` abstraction, not specific phases.

This architecture promotes **testability, maintainability, and clarity** in time-specific logic.


## Drawbacks

- **Memory/complexity overhead**: Adds multiple classes and updates per tick.
- **Requires strict phase coordination**: If more game elements are affected (e.g., merchant behavior), every system must subscribe to the time logic.
- **Visual cues** may be needed to inform players about the current phase.


## Alternatives Considered

- **Enum-based TimePhase:** This would have centralized all time logic inside one monolithic enum, breaking SRP and making future extensions harder due to a lack of modularity.
- **Boolean flags on Actors (e.g., isNightBuff):** This leads to bloated and unmaintainable code, especially when multiple conditions and combinations of time-based effects are introduced.
- **Representing TimePhase using Status or Capability enums:** This misuses the concept of capabilities, which are meant for actor traits—not global environmental states.

The chosen class/interface-based design is significantly more modular, extensible, and easier to test.


## Future Extensions

- **Add a Blood Moon phase:** Simply create a new class like BloodMoonPhase that implements TimePhase, and inject it into the cycle.
- **Weather system integration:** Define a new interface like WeatherCondition and chain its logic through TimeManager alongside the time phases.
- **Time-based merchant inventory:** Within merchant logic, include a condition like if TimeManager.getCurrentPhase() shows its NightPhase to serve special items.
- **Random events during phases:** Trigger world events such as enemy invasions or meteor showers by overriding applyEffects(GameMap) in a specific TimePhase.

This design keeps the system open for new features without needing to change any of the existing logic, maintaining clean separation and adhering to OCP (Open/Closed Principle).
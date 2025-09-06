package game;

import java.util.Arrays;
import java.util.List;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import game.actors.Player;
import game.time.TimeManager;
import game.time.TimeController;
import game.actors.npc.hostile.BedOfChaos;
import game.actors.npc.hostile.Guts;
import game.actors.npc.nonhostile.GoldenBeetle;
import game.actors.npc.nonhostile.merchant.Kale;
import game.behaviours.PrioritySelectionStrategy;
import game.behaviours.RandomSelectionStrategy;
import game.grounds.*;
import game.items.Talisman;
import game.actors.npc.nonhostile.OmenSheep;
import game.actors.npc.nonhostile.merchant.Sellen;
import game.actors.npc.nonhostile.SpiritGoat;
import game.items.seeds.BloodroseSeed;
import game.items.seeds.InheritreeSeed;
import game.ui.FancyMessage;
import game.weapons.Broadsword;
import game.weapons.DragonslayerGreatsword;
import game.weapons.Katana;

/**
 * The main class to setup and run the game.
 *
 * @author Adrian Kristanto
 * Modified by Hassaan Usmani
 */
public class Application {

    public static void main(String[] args) {

        World world = new World(new Display());

        FancyGroundFactory groundFactory = new FancyGroundFactory(new Blight(),
                new Wall(), new Floor(), new Soil());

        List<String> map = Arrays.asList(
                "xxxx...xxxxxxxxxxxxxxxxxxxxxxx........xx",
                "xxx.....xxxxxxx..xxxxxxxxxxxxx.........x",
                "..........xxxx....xxxxxxxxxxxxxx.......x",
                "....xxx...........xxxxxxxxxxxxxxx.....xx",
                "...xxxxx...........xxxxxxxxxxxxxx.....xx",
                "...xxxxxxxxxx.......xxxxxxxx...xx......x",
                "....xxxxxxxxxx........xxxxxx...xxx......",
                "....xxxxxxxxxxx.........xxx....xxxx.....",
                "....xxxxxxxxxxx................xxxx.....",
                "...xxxx...xxxxxx.....#####.....xxx......",
                "...xxx....xxxxxxx....#___#.....xx.......",
                "..xxxx...xxxxxxxxx...#___#....xx........",
                "xxxxx...xxxxxxxxxx...##_##...xxx.......x",
                "xxxxx..xxxxxxxxxxx.........xxxxx......xx",
                "xxxxx..xxxxxxxxxxxx.......xxxxxx......xx");

        List<String> limveldMap = Arrays.asList(
                ".............xxxx",
                "..............xxx",
                "................x",
                ".................",
                "................x",
                "...............xx",
                "..............xxx",
                "..............xxx",
                "..............xxx",
                ".............xxxx",
                ".............xxxx",
                "....xxx.....xxxxx",
                "....xxxx...xxxxxx"
        );

        GameMap limveld = new GameMap("Limveld", groundFactory, limveldMap);
        world.addGameMap(limveld);

        GameMap gameMap = new GameMap("Valley of the Inheritree", groundFactory, map);
        world.addGameMap(gameMap);

        gameMap.addActor(new SpiritGoat(), gameMap.at(5, 0));
        gameMap.addActor(new SpiritGoat(new RandomSelectionStrategy()), gameMap.at(21, 13));

        gameMap.addActor(new OmenSheep(), gameMap.at(10, 4));
        gameMap.addActor(new OmenSheep(), gameMap.at(14, 3));

        gameMap.addActor(new OmenSheep(new RandomSelectionStrategy()), gameMap.at(23, 14));

        gameMap.addActor(new GoldenBeetle(new PrioritySelectionStrategy()), gameMap.at(22, 10));
        gameMap.addActor(new Guts(), gameMap.at(27, 10));

        gameMap.addActor(new Sellen(),  gameMap.at(25, 14));
        gameMap.addActor(new Kale(),  gameMap.at(25, 13));

        gameMap.addActor(new BedOfChaos(),  gameMap.at(22, 14));

        setupTeleporters(gameMap, limveld);

        // BEHOLD, ELDEN THING!
        for (String line : FancyMessage.TITLE.split("\n")) {
            new Display().println(line);
            try {
                Thread.sleep(200);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        Player player = new Player("Farmer", '@', 100);
        world.addPlayer(player, gameMap.at(23, 10));

        player.addItemToInventory(new InheritreeSeed());
        player.addItemToInventory(new BloodroseSeed());

        player.addBalance(10000);

        // game setup
        gameMap.at(24, 11).addItem(new Talisman());

        // hook up day/night system on Valley
        TimeManager timeManager = new TimeManager(gameMap, player);
        gameMap.addActor(new TimeController(timeManager), gameMap.at(0, 0));

        // also hook up day/night system on Limveld
        TimeManager limTimeManager = new TimeManager(limveld, player);
        limveld.addActor(new TimeController(limTimeManager), limveld.at(0, 12));

        world.run();
    }

    /**
     * Configures teleporters between game maps.
     *
     * Sets up bidirectional teleportation between:
     * - Valley of Inheritree (coordinates 24,10)
     * - Limveld (coordinates 0,0)
     *
     * Method ensures:
     * - Teleporters are placed at correct locations
     * - Bidirectional connection is established
     * - Ground types are properly set
     *
     * @param valley  Valley of Inheritree game map
     * @param limveld Limveld game map
     */
    private static void setupTeleporters(GameMap valley, GameMap limveld) {
        // Valley -> Limveld teleporter
        Location valleyTeleporterLoc = valley.at(24, 10);
        TeleportationCircle valleyTeleporter = new TeleportationCircle();
        valleyTeleporterLoc.setGround(valleyTeleporter);
        valleyTeleporter.addDestination(limveld.at(0, 0));

        // Limveld -> Valley teleporter
        Location limveldTeleporterLoc = limveld.at(0, 0);
        TeleportationCircle limveldTeleporter = new TeleportationCircle();
        limveldTeleporterLoc.setGround(limveldTeleporter);
        limveldTeleporter.addDestination(valley.at(24, 10));
    }
}

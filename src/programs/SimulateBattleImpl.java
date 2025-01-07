package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/* Алгоритмическая сложность:

Сортировка юнитов: O(n log n), где n — количество юнитов в армии.

Основной цикл симуляции: O(n^2), так как для каждого юнита выполняется атака, и в худшем случае каждый юнит атакует всех остальных.

Общая сложность: O(n^2 log n). */

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    // No-argument constructor
    public SimulateBattleImpl() {
    }

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        int round = 0;
        List<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits().stream().filter(Unit::isAlive).toList());
        List<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits().stream().filter(Unit::isAlive).toList());

        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            round++;
            System.out.println("Round " + round + " starts!");

            // Sort units by descending attack
            playerUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
            computerUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            // Player units attack
            for (Unit playerUnit : playerUnits) {
                if (playerUnit.isAlive()) {
                    Unit target = playerUnit.getProgram().attack();
                    if (target != null) {
                        printBattleLog.printBattleLog(playerUnit, target);
                    }
                }
            }

            // Computer units attack
            for (Unit computerUnit : computerUnits) {
                if (computerUnit.isAlive()) {
                    Unit target = computerUnit.getProgram().attack();
                    if (target != null) {
                        printBattleLog.printBattleLog(computerUnit, target);
                    }
                }
            }

            // Update lists of alive units
            playerUnits = new ArrayList<>(playerArmy.getUnits().stream().filter(Unit::isAlive).toList());
            computerUnits = new ArrayList<>(computerArmy.getUnits().stream().filter(Unit::isAlive).toList());

            System.out.println("Round " + round + " is over!");
            System.out.println("Player army has " + playerUnits.size() + " units");
            System.out.println("Computer army has " + computerUnits.size() + " units");
            System.out.println();
        }

        System.out.println("Battle is over!");
    }
}
package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private final PrintBattleLog printBattleLog;

    // Конструктор по умолчанию
    public SimulateBattleImpl() {
        this.printBattleLog = new PrintBattleLog() {
            @Override
            public void printBattleLog(Unit attackingUnit, Unit target) {
                if (target != null) {
                    System.out.println(attackingUnit.getName() + " attacks " + target.getName());
                } else {
                    System.out.println(attackingUnit.getName() + " attacks nothing");
                }
            }
        };
    }

    // Конструктор с параметром
    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        int round = 0;
        List<Unit> playerUnits = playerArmy.getUnits().stream().filter(Unit::isAlive).toList();
        List<Unit> computerUnits = computerArmy.getUnits().stream().filter(Unit::isAlive).toList();

        System.out.println("Player army size: " + playerUnits.size());
        System.out.println("Computer army size: " + computerUnits.size());

        if (computerUnits.isEmpty()) {
            System.out.println("Computer army is empty! Player wins by default.");
            return;
        }

        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            round++;
            System.out.println("Round " + round + " starts!");

            // Сортируем юнитов по убыванию атаки
            playerUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());
            computerUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            // Атака юнитов игрока
            for (Unit playerUnit : playerUnits) {
                if (playerUnit.isAlive()) {
                    Unit target = playerUnit.getProgram().attack();
                    if (target != null) {
                        printBattleLog.printBattleLog(playerUnit, target);
                    } else {
                        System.out.println(playerUnit.getName() + " has no target to attack.");
                    }
                }
            }

            // Атака юнитов компьютера
            for (Unit computerUnit : computerUnits) {
                if (computerUnit.isAlive()) {
                    Unit target = computerUnit.getProgram().attack();
                    if (target != null) {
                        printBattleLog.printBattleLog(computerUnit, target);
                    } else {
                        System.out.println(computerUnit.getName() + " has no target to attack.");
                    }
                }
            }

            // Обновляем списки живых юнитов
            playerUnits = playerArmy.getUnits().stream().filter(Unit::isAlive).toList();
            computerUnits = computerArmy.getUnits().stream().filter(Unit::isAlive).toList();

            System.out.println("Round " + round + " is over!");
            System.out.println("Player army has " + playerUnits.size() + " units");
            System.out.println("Computer army has " + computerUnits.size() + " units");
            System.out.println();
        }

        if (playerUnits.isEmpty()) {
            System.out.println("Computer wins!");
        } else if (computerUnits.isEmpty()) {
            System.out.println("Player wins!");
        } else {
            System.out.println("Battle is over!");
        }
    }
}
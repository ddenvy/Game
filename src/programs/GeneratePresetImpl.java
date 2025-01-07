package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

/* Алгоритмическая сложность:

Сортировка юнитов: O(n log n), где n — количество типов юнитов.

Основной цикл: O(n * m), где n — количество типов юнитов, m — максимальное количество юнитов в армии.

Общая сложность: O(n * m). */

public class GeneratePresetImpl implements GeneratePreset {
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army army = new Army();
        List<Unit> selectedUnits = new ArrayList<>();
        Map<String, Integer> unitCounts = new HashMap<>();
        Random random = new Random();
        int usedPoints = 0;

        // Сортируем юниты по эффективности (атака/стоимость и здоровье/стоимость)
        unitList.sort((u1, u2) -> {
            double ratio1 = (double) u1.getBaseAttack() / u1.getCost() + (double) u1.getHealth() / u1.getCost();
            double ratio2 = (double) u2.getBaseAttack() / u2.getCost() + (double) u2.getHealth() / u2.getCost();
            return Double.compare(ratio2, ratio1);
        });

        for (Unit unit : unitList) {
            String unitType = unit.getUnitType();
            int cost = unit.getCost();
            int count = unitCounts.getOrDefault(unitType, 0);

            if (count < 11 && usedPoints + cost <= maxPoints) {
                int[] coordinates = findAvailableCoordinates(selectedUnits, random);
                if (coordinates != null) {
                    Unit newUnit = new Unit(
                            unitType + " " + (count + 1),
                            unit.getUnitType(),
                            unit.getHealth(),
                            unit.getBaseAttack(),
                            unit.getCost(),
                            unit.getAttackType(),
                            unit.getAttackBonuses(),
                            unit.getDefenceBonuses(),
                            coordinates[0],
                            coordinates[1]
                    );
                    selectedUnits.add(newUnit);
                    army.getUnits().add(newUnit);
                    usedPoints += cost;
                    unitCounts.put(unitType, count + 1);
                }
            }
        }

        System.out.println("Used points: " + usedPoints);
        return army;
    }

    private int[] findAvailableCoordinates(List<Unit> units, Random random) {
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(3); // 0..2
            int y = random.nextInt(21); // 0..20
            if (units.stream().noneMatch(u -> u.getxCoordinate() == x && u.getyCoordinate() == y)) {
                return new int[]{x, y};
            }
        }
        return null;
    }
}
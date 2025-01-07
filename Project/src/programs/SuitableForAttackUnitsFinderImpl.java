package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            for (Unit unit : row) {
                if (unit != null && unit.isAlive()) {
                    if (isLeftArmyTarget && isRightmostUnit(unit, row)) {
                        suitableUnits.add(unit);
                    } else if (!isLeftArmyTarget && isLeftmostUnit(unit, row)) {
                        suitableUnits.add(unit);
                    }
                }
            }
        }

        System.out.println("Found " + suitableUnits.size() + " suitable units for attack.");
        return suitableUnits;
    }

    private boolean isRightmostUnit(Unit unit, List<Unit> row) {
        int unitIndex = row.indexOf(unit);
        return row.subList(unitIndex + 1, row.size()).stream().allMatch(Objects::isNull);
    }

    private boolean isLeftmostUnit(Unit unit, List<Unit> row) {
        int unitIndex = row.indexOf(unit);
        return row.subList(0, unitIndex).stream().allMatch(Objects::isNull);
    }
}
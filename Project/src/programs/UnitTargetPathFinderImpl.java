package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        List<Edge> path = new ArrayList<>();
        Set<Edge> occupiedCells = getOccupiedCells(existingUnitList);

        Edge start = new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        Edge goal = new Edge(targetUnit.getxCoordinate(), targetUnit.getyCoordinate());

        Map<Edge, Integer> gScore = new HashMap<>();
        Map<Edge, Integer> fScore = new HashMap<>();
        gScore.put(start, 0);
        fScore.put(start, heuristic(start, goal));

        PriorityQueue<Edge> openSet = new PriorityQueue<>(Comparator.comparingInt(fScore::get));
        openSet.add(start);

        Map<Edge, Edge> cameFrom = new HashMap<>();

        while (!openSet.isEmpty()) {
            Edge current = openSet.poll();

            if (current.equals(goal)) {
                reconstructPath(cameFrom, current, path);
                return path;
            }

            for (Edge neighbor : getNeighbors(current)) {
                if (occupiedCells.contains(neighbor)) continue;

                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;

                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return path;
    }

    private Set<Edge> getOccupiedCells(List<Unit> existingUnitList) {
        Set<Edge> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive()) {
                occupiedCells.add(new Edge(unit.getxCoordinate(), unit.getyCoordinate()));
            }
        }
        return occupiedCells;
    }

    private void reconstructPath(Map<Edge, Edge> cameFrom, Edge current, List<Edge> path) {
        while (cameFrom.containsKey(current)) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
    }

    private int heuristic(Edge a, Edge b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    private List<Edge> getNeighbors(Edge edge) {
        List<Edge> neighbors = new ArrayList<>();
        int x = edge.getX();
        int y = edge.getY();

        if (x > 0) neighbors.add(new Edge(x - 1, y));
        if (x < WIDTH - 1) neighbors.add(new Edge(x + 1, y));
        if (y > 0) neighbors.add(new Edge(x, y - 1));
        if (y < HEIGHT - 1) neighbors.add(new Edge(x, y + 1));

        return neighbors;
    }
}
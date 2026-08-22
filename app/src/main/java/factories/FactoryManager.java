package factories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import constants.Constants;
import world.Pop;
import world.State;

public final class FactoryManager {
    private FactoryManager() { }

    public static void tick(State state) {
        List<Factory> factories = new ArrayList<>(state.getFactories());
        factories.sort(Comparator.comparingDouble(Factory::getWageValue).reversed());
        for (Factory factory : factories) factory.stockInputInventory();
        for (Factory factory : factories) factory.clearWorkers();

        for (Pop pop : state.getJob(Constants.CRAFTSMAN)) {
            double remaining = pop.getPopulation();
            for (Factory factory : factories) {
                if (remaining <= 0) break;
                double before = factory.getWorkerCount();
                factory.assignWorker(pop, remaining);
                remaining -= factory.getWorkerCount() - before;
            }
        }
        for (Factory factory : factories) factory.tickProduction();
    }

    public static void prepareEmployment(State state) {
        List<Factory> factories = new ArrayList<>(state.getFactories());
        factories.sort(Comparator.comparingDouble(Factory::getWageValue).reversed());
        for (Factory factory : factories) factory.clearWorkers();
        for (Pop pop : state.getJob(Constants.CRAFTSMAN)) {
            double remaining = pop.getPopulation();
            for (Factory factory : factories) {
                if (remaining <= 0) break;
                double before = factory.getWorkerCount();
                factory.assignWorker(pop, remaining);
                remaining -= factory.getWorkerCount() - before;
            }
        }
    }

    public static void produce(State state) {
        for (Factory factory : state.getFactories()) factory.tickProduction();
    }
}
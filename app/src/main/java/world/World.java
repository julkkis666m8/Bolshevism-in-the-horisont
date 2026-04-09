package world;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import market.GlobalMarket;

public class World {

    private List<Nation> nations;
    private GlobalMarket globalMarket;

    // date counter for simulation: each tick == one day
    private LocalDate currentDate;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public World() {
        globalMarket = new GlobalMarket();
        nations = new ArrayList<Nation>();
        currentDate = LocalDate.of(1900, 1, 1);
    }

    public void addNation(Nation nation) {
        nations.add(nation);
    }

    public List<State> getAllStates() {
        List<State> allStates = new LinkedList<State>();
        for (int i = 0; i < nations.size(); i++) {
            allStates.addAll(nations.get(i).getStates());
        }
        return allStates;
    }

    public List<Pop> getAllPops() {
        List<Pop> pops = new ArrayList<Pop>();
        for (State s : getAllStates()) {
            pops.addAll(s.getPops());
        }
        return pops;
    }

    public List<Nation> getNations() {
        return nations;
    }

    public GlobalMarket getGlobalMarket() {
        return globalMarket;
    }

    /**
     * Advance the world date by given number of days.
     */
    public void advanceDays(int days) {
        if (days <= 0) return;
        currentDate = currentDate.plusDays(days);
    }

    /** Advance one simulation day. */
    public void advanceOneDay() {
        currentDate = currentDate.plusDays(1);
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public String getCurrentDateString() {
        return currentDate.format(DATE_FMT);
    }

}

package controller;

import world.Army;
import world.Regiment;
import world.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleController {

    private List<Regiment> attackers;
    private List<Regiment> defenders;


    public void startBattle(Army armyAtt, Army armyDef, State state) {

        attackers = armyAtt.getRegiments();
        defenders = armyDef.getRegiments();

        setStartDistance(attackers);
        setStartDistance(defenders);


        //TODO: handel priorizisation, etc





    }

    private void setStartDistance(List<Regiment> regiments) {
        int distSide = 0;
        int distNeg = 1;
        for (Regiment regiment : regiments){
            regiment.setDistFront(regiments.size());
            regiment.setDistSide(distNeg*distSide);
            distNeg = distNeg*(-1);
            distSide++;
        }
    }

    public void tick(){

        //move(attackers);
        attack(defenders, attackers);
        //move(defenders);
        attack(attackers, defenders);
    }

    private void attack(List<Regiment> shooters, List<Regiment> shootees) {
        List<Regiment> targets = new ArrayList<>();
        for(Regiment shooter : shooters){

            int hisRange = shooter.getRange();
            for (Regiment shootee : shootees){
                if (Regiment.distance(shooter, shootee) < hisRange){
                    targets.add(shootee);
                }
            }
            if(targets.size()>0){
                Collections.shuffle(targets); //randomize order and get first to get random target
                targets.get(0).damage(shooter.attack());
                if (targets.get(0).getManpower() <= 0 || targets.get(0).getOrganization() <= 0){
                    System.out.println(targets.get(0).toString()+" was destroyed");
                    shootees.remove(targets.get(0));
                }
                targets.clear();
            }else{
                shooter.move();
            }
        }
    }

    private void move(List<Regiment> movers) {
        for(Regiment mover : movers){
            mover.move();
        }
    }


    public boolean ongoing() {
        if(attackers.size() > 0 && defenders.size() > 0){
            return true;
        }else{
            return false;
        }
    }
}

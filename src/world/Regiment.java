package world;

public class Regiment {
    private final String name;;
    private Nation nation;
    private double organization = 1;
    private double manpower = 1000;
    private double discipline = 1;
    private double tactics = 1;
    private int range = 1;
    private int desiredRange = 1;
    private int speed = 1;
    private int distFront = 0;
    private int distSide = 0;
    private boolean backrow = false;

    //testing
    public int distTEST = 0;
    public int numTarg = 0;

    @Override
    public String toString(){
        return name;
    }

    public Regiment(String name, int range, int speed, double disciplinen, double tactics, boolean backrow) {
        this.name = name;
        this.range = range;
        desiredRange = range;
        this.speed = speed;
        this.discipline = disciplinen;
        this.tactics = tactics;
        this.backrow = backrow;

    }

    public static int distance(Regiment shooter, Regiment shootee) {
        int dist = shooter.getDistFront() + shootee.getDistFront();
        int aux = Math.abs(shooter.getDistSide() - shootee.getDistSide());
        return dist+aux;
    }
    public void move(){
        distFront = distFront-speed;
    }

    public void setDistFront(int distFront) {
        this.distFront = distFront;
    }

    public void setDistSide(int distSide) {
        this.distSide = distSide;
    }

    public int getDistFront() {
        return distFront;
    }

    public int getDistSide() {
        return distSide;
    }

    public double attack(){
        return discipline*organization*manpower/10*Math.random();
    }
    public void damage(double damage){
        damage = damage*(2-tactics);
        manpower = manpower-damage;
        organization = organization-(damage/1000);
    }

    public double getManpower() {
        return manpower;
    }

    public double getOrganization() {
        return organization;
    }

    public int getDesiredRange() {
        return desiredRange;
    }

    public int getRange() {
        return range;
    }

    public int getSpeed() {
        return speed;
    }

    public Nation getNation() {
        return nation;
    }

    public boolean getBackrow() {
        return backrow;
    }
}

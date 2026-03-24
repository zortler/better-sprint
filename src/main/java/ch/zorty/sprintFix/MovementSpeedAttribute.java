package ch.zorty.sprintFix;

import java.util.HashMap;
import java.util.Map;

public class MovementSpeedAttribute {

    private final double baseValue;
    private final Map<String, Double> amount = new HashMap<>();
    private final Map<String, Integer> type = new HashMap<>();

    public MovementSpeedAttribute(double baseValue) {
        this.baseValue = baseValue;
    }

    public void addModifier(String key, double value, int type) {
        this.amount.put(key, value);
        this.type.put(key, type);
    }

    public boolean equals(MovementSpeedAttribute msa) {
        return this.baseValue == msa.getBaseValue() && this.amount.equals(msa.getAmounts()) && this.type.equals(msa.getTypes());
    }

    public double getBaseValue() { return this.baseValue; }

    public Map<String, Double> getAmounts() { return this.amount; }

    public MovementSpeedAttribute copy_without_sprint() {
        MovementSpeedAttribute new_msa = new MovementSpeedAttribute(this.baseValue);
        for(String key : this.amount.keySet()) {
            if(!key.equalsIgnoreCase("minecraft:sprinting")) new_msa.addModifier(key, this.amount.get(key), this.type.get(key));
        }
        return new_msa;
    }

    public Map<String, Integer> getTypes() { return this.type; }
}

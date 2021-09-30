package com.enderio.base.common.capability.capacitors;

import java.util.ArrayList;
import java.util.Optional;

public class MachinePropertie {
    private float base;
    private ArrayList<Property> properties = new ArrayList<MachinePropertie.Property>();
    
    public MachinePropertie(float base, ArrayList<Property> properties ) {
        this.base = base;
        this.properties = properties;
    }
    
    public float getBase() {
        return base;
    }
    
    public ArrayList<Property> getProperties() {
        return properties;
    }
    
    public boolean hasProperty(String type) {
        return this.properties.stream().anyMatch(p -> p.type.equals(type));
    }
    
    public Property getProperty(String type) {
        Optional<Property> optional = this.properties.stream().filter(p -> p.type.equals(type)).findFirst();
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("The type '" + type +"' is not found in properties");
        }
        return optional.get();
        
    }

    class Property {
        private String type;
        private float effect;
        
        public Property(String type, float effect) {
            this.type = type;
            this.effect = effect;
        }
        
        public String getType() {
            return type;
        }
        
        public float getEffect() {
            return effect;
        }
        
    }
}

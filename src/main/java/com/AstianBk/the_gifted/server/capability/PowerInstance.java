package com.AstianBk.the_gifted.server.capability;

import com.AstianBk.the_gifted.server.powers.ElementPower;
import com.AstianBk.the_gifted.server.powers.Power;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Objects;

public class PowerInstance implements Comparable<PowerInstance> {
    public static final String SPELL_ID = "id";
    public static final String SPELL_LEVEL = "level";
    public static final String SPELL_LOCKED = "locked";
    public static final PowerInstance EMPTY = new PowerInstance(Power.NONE, 0, false);
    private MutableComponent displayName;
    protected final Power power;
    protected final int spellLevel;
    protected final boolean locked;

    private PowerInstance() throws Exception {
        throw new Exception("Cannot create empty power slots.");
    }

    public PowerInstance(Power spell, int level, boolean locked) {
        this.power = Objects.requireNonNull(spell);
        this.spellLevel = level;
        this.locked = locked;
    }

    public PowerInstance(Power spell, int level) {
        this(spell, level, false);
    }


    public Power getPower() {
        return power == null ? Power.NONE : power;
    }

    public int getLevel() {
        return spellLevel;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean canRemove() {
        return !locked;
    }

    public ElementPower getRarity() {
        return getPower().elementPower;
    }

    public Component getDisplayName() {
        if (displayName == null) {
            displayName = Component.translatable("the_gifted."+ getPower().name);
        }
        return displayName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof PowerInstance other) {
            return this.power.equals(other.power) && this.spellLevel == other.spellLevel;
        }

        return false;
    }

    public int hashCode() {
        return 31 * this.power.hashCode() + this.spellLevel;
    }
    public int compareTo(PowerInstance other) {
        int i = this.power.name.compareTo(other.power.name);
        if (i == 0) {
            i = Integer.compare(this.spellLevel, other.spellLevel);
        }
        return i;
    }
}

package net.pinger.disguiseplus.statistic;

public abstract class Statistic {

    private boolean active = true;

    protected Statistic() {

    }

    /**
     * Returns whether this statistic
     * is still active.
     * <p>
     * Use {@link #setActive(boolean)}} to change the state
     * of this statistic.
     *
     * @return whether it is active
     */

    public boolean isActive() {
        return this.active;
    }

    /**
     * Changes the active state of this statistic.
     *
     * @param active the active state
     */

    public void setActive(boolean active) {
        this.active = active;
    }

}

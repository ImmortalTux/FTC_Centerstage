package org.firstinspires.ftc.teamcode.custom;

public abstract class State {
    /**
     * @brief       Next state requested by current state.
     */
    protected State nextState;

    /**
     * @brief       Returns the next requested state to state machine.
     * @return      next requested state.
     */
    public State getNextState() {
        return nextState;
    }

    /**
     * @brief       Runs state.
     * @return      Returns wether or not current state should continue running.
     */
    abstract public boolean run();
}
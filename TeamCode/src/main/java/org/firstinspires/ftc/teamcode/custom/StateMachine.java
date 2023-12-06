package org.firstinspires.ftc.teamcode.custom;

public class StateMachine extends State {
    /**
     * @brief       Initializes state machine.
     * @param       beginningState: State that state machine should start with.
     */
    public StateMachine(State beginningState) {
        nextState = beginningState;
    }

    /**
     * @brief       Runs current state and goes to the next one.
     * @return      whether or not state machine is continuing to be ran.
     */
    public boolean run() {
        if (nextState == null) {
            return false;
        }

        if (!nextState.run()) {
            nextState = nextState.getNextState();
        }

        return true;
    }
}

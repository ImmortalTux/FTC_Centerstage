package org.firstinspires.ftc.teamcode.custom.subsystems;

public abstract class Subsystem {
    public static class Singleton {
        Subsystem object = null;
    }

    abstract void cleanup();

    static protected void registerSubsystem(Singleton classSingleton, Subsystem object) {
        if (classSingleton.object != null) {
            throw new SystemInUseException("System currently in use!");
        }

        classSingleton.object = object;
    }

    static protected void unregisterSubsystem(Singleton classSingleton, Subsystem object) {
        if (classSingleton.object == object) {
            classSingleton.object = null;
        }
    }

    /* TODO: Documentation */
}

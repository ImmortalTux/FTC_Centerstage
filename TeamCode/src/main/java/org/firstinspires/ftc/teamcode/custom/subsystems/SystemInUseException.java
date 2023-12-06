package org.firstinspires.ftc.teamcode.custom.subsystems;

public class SystemInUseException extends RuntimeException {
    public SystemInUseException(String message) {
        super(new Throwable(message));
    }
}

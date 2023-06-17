package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class ExceededQRCapacityException extends RuntimeException{
    private int exceedingCapacity;

    public ExceededQRCapacityException() {
        super("Maximum Capacity for QR exceeded, please choose less passwords\n");
        this.setStackTrace(new StackTraceElement[0]);
    }

    public int getExceedingCapacity() {
        return exceedingCapacity;
    }

    public void setExceedingCapacity(int exceedingCapacity) {
        this.exceedingCapacity = exceedingCapacity;
    }
}

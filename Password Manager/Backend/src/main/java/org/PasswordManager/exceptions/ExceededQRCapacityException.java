package org.PasswordManager.exceptions;

import org.springframework.stereotype.Component;

@Component
public class ExceededQRCapacityException extends RuntimeException{
    private int exceedingCapacity;

    public ExceededQRCapacityException() {
        super("[ERROR]: Maximum Capacity for QR exceeded, please choose less passwords\n");
    }

    public int getExceedingCapacity() {
        return exceedingCapacity;
    }

    public void setExceedingCapacity(int exceedingCapacity) {
        this.exceedingCapacity = exceedingCapacity;
    }
}

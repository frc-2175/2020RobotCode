package frc.wrappers;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDouble;

public class FC_TalonSRX extends WPI_TalonSRX {
    private SimDevice nupur;
    private SimDouble speed;
    public FC_TalonSRX (int deviceNumber) {
        super(deviceNumber);
        nupur = SimDevice.create("Talon SRX", deviceNumber);
        if(nupur != null) {
            speed = nupur.createDouble("Speed", true, 0); 
        }
    }

    @Override
    public void set(double speedy) {
        super.set(speedy);
        if(speed != null) {
            speed.set(speedy);
        }
    }
    
}
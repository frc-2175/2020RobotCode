package frc.wrappers;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.hal.SimDouble;

public class FC_VictorSPX extends WPI_VictorSPX {
    private SimDevice nupur;
    private SimDouble speed;
    public FC_VictorSPX (int deviceNumber) {
        super(deviceNumber);
        nupur = SimDevice.create("Victor SPX", deviceNumber);
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
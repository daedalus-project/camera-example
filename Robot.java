package org.team2839.bettercamera;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends SampleRobot {

    public Robot() {
        BetterCameraServer.init("cam0", "cam1");
        BetterCameraServer.start();
    }

    public void operatorControl() {
    	
        while (isOperatorControl() && isEnabled()) {
        	
        	// Every 5 seconds, switch which camera is streaming to the dashboard
            Timer.delay(5.0);
            BetterCameraServer.switchCamera();
            
        }
    }

}
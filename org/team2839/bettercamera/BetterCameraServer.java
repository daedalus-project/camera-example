package org.team2839.bettercamera;

import java.util.concurrent.atomic.AtomicInteger;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class BetterCameraServer {
	
	private static USBCamera cam0, cam1;
	private static CameraServer server;
	private static AtomicInteger selected;
	private static boolean serving;
	
	public static void init(String frontCam, String backCam) {
		BetterCameraServer.server = CameraServer.getInstance();
		BetterCameraServer.server.setQuality(50);
        
		BetterCameraServer.cam0 = new USBCamera(frontCam);
		BetterCameraServer.cam1 = new USBCamera(backCam);
		
		BetterCameraServer.selected = new AtomicInteger(0);
		BetterCameraServer.serving = false;
	}
	
	public synchronized static void start() {
		if (serving) {
			return;
		}
		BetterCameraServer.serving = true;
		
		cam0.openCamera();
		cam1.openCamera();
		
		cam0.startCapture();
		
		Thread captureThread = new Thread(new Runnable() {
			@Override
			public void run() {
			    capture();
			}
	    });
	    captureThread.setName("Better Camera Capture Thread");
	    captureThread.start();
	}
	
	private static void capture() {
		Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		while (true) {
			if (selected.intValue() == 0) {
				cam1.stopCapture();
				cam0.startCapture();
				cam0.getImage(frame);
			} else {
				cam0.stopCapture();
				cam1.startCapture();
				cam1.getImage(frame);
			}
			server.setImage(frame);
		}
	}

	public static int getSelected() {
		return selected.intValue();
	}

	public static void switchCamera() {
		if (selected.intValue() == 0) {
			BetterCameraServer.selected.set(1);
		} else {
			BetterCameraServer.selected.set(0);
		}
	}

}

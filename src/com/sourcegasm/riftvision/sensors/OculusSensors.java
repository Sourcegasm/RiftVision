package com.sourcegasm.riftvision.sensors;

import com.sourcegasm.riftvision.helper.Euler;
import com.sourcegasm.riftvision.helper.Quaternion;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by klemen on 18.8.2015.
 */
public class OculusSensors {

	private double rawRoll;
	private double rawPitch;
	private double rawYaw;

	private double smoothedRool;
	private double smoothedPitch;
	private double smoothedYaw;

	private LowPassFilter rollLowFilter;
	private LowPassFilter pitchLowFilter;
	private LowPassFilter yawLowFilter;

	private HighPassFilter rollHighFilter;
	private HighPassFilter pitchHighFilter;
	private HighPassFilter yawHighFilter;

	private Thread recieverThread = new Thread();

	public void startReceiving() {
		double lowPassSmoothing = 2;
		rollLowFilter = new LowPassFilter(lowPassSmoothing);
		pitchLowFilter = new LowPassFilter(lowPassSmoothing);
		yawLowFilter = new LowPassFilter(lowPassSmoothing);

		double highPassSmoothing = 1 / 1.05;
		rollHighFilter = new HighPassFilter(highPassSmoothing);
		pitchHighFilter = new HighPassFilter(highPassSmoothing);
		yawHighFilter = new HighPassFilter(highPassSmoothing);

		recieverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int port = 1234;
					DatagramSocket dsocket = new DatagramSocket(port);


					while (true) {
						byte[] buffer = new byte[2048];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
						dsocket.receive(packet);
						String line = new String(buffer, 0, packet.getLength());
						packet.setLength(buffer.length);

						Euler euler = new Quaternion(line).toEuler();
						rawRoll = euler.roll;
						rawPitch = euler.pitch;
						rawYaw = euler.yaw;

						/*rollLowFilter.calculate(euler.roll);
						rollHighFilter.calculate(rollLowFilter.smoothedValue);
						smoothedRool = rollHighFilter.smoothedValue;

						pitchLowFilter.calculate(euler.pitch);
						pitchHighFilter.calculate(pitchLowFilter.smoothedValue);
						smoothedPitch = pitchHighFilter.smoothedValue;

						yawLowFilter.calculate(euler.yaw);
						yawHighFilter.calculate(yawLowFilter.smoothedValue);
						smoothedYaw = yawHighFilter.smoothedValue;*/
						
						rollLowFilter.calculate(euler.roll);
						smoothedRool = rollLowFilter.smoothedValue;
						
						pitchLowFilter.calculate(euler.pitch);
						smoothedPitch = pitchLowFilter.smoothedValue;

						yawLowFilter.calculate(euler.yaw);
						smoothedYaw = yawLowFilter.smoothedValue;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		recieverThread.start();
	}

	public double getSmoothedRool() {
		return smoothedRool;
	}

	public double getSmoothedPitch() {
		return smoothedPitch;
	}

	public double getSmoothedYaw() {
		return smoothedYaw;
	}

	public double getRawRool() {
		return rawRoll;
	}

	public double getRawPitch() {
		return rawPitch;
	}

	public double getRawYaw() {
		return rawYaw;
	}

}

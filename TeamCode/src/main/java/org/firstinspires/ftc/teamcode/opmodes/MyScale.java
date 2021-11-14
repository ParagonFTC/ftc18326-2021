package org.firstinspires.ftc.teamcode.opmodes;

import android.util.Log;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.hardware.LoadCellHX711;

@Config
@Autonomous(group = "drive")
public class MyScale extends LinearOpMode {

    @Override
    public void runOpMode() {

        org.firstinspires.ftc.teamcode.hardware.LoadCellHX711 scale  = hardwareMap.get(org.firstinspires.ftc.teamcode.hardware.LoadCellHX711.class, "scale");

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive() && !isStopRequested()) {

            int[] weights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            int index = 0;
            int total_weights = 0;

            int weight = scale.readWeight();
            if (weight > 0) {
                total_weights -= weights[index];
                total_weights += weight;
                weights[index] = weight;
                if (index < 9) {
                    index ++;
                } else {
                    index = 0;
                }
            }
            Log.i("Main", String.format("Weight: %d.", total_weights/10));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

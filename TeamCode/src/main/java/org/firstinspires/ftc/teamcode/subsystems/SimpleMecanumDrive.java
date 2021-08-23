package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.Subsystem;

public class SimpleMecanumDrive implements Subsystem {
    private DcMotorEx[] motors = new DcMotorEx[4];
    private Double[] powers = {0.0, 0.0, 0.0, 0.0};

    public SimpleMecanumDrive (Robot robot) {
        motors[0] = robot.getMotor("DriveLF");
        motors[1] = robot.getMotor("DriveLR");
        motors[2] = robot.getMotor("DriveRR");
        motors[3] = robot.getMotor("DriveRF");

        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[2].setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setDrivePower(Pose2d drivePower) {
        powers[0] = drivePower.getX() - drivePower.getY() - drivePower.getHeading();
        powers[1] = drivePower.getX() + drivePower.getY() - drivePower.getHeading();
        powers[2] = drivePower.getX() - drivePower.getY() + drivePower.getHeading();
        powers[3] = drivePower.getX() + drivePower.getY() + drivePower.getHeading();
    }

    @Override
    public void update(TelemetryPacket packet) {
        for (int i = 0; i < 4; i++){
            motors[i].setPower(powers[i]);
        }
    }
}

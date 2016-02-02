package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Winston on 1/30/16.
 */
public class AutoRedOp extends LinearOpMode {
    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor ballCollect;
    DcMotor extension;
    Servo dump;
    Servo zip;

    double TURN_RADIUS=1.417;
    double WHEEL_RADIUS=0.166;
    double FULL_RPM=50.667;

    public void runOpMode() throws InterruptedException{
		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the zip joint of the manipulator.
		 *    "servo_6" controls the dump joint of the manipulator.
		 */
        motorRight = hardwareMap.dcMotor.get("right");
        motorLeft = hardwareMap.dcMotor.get("left");
        ballCollect = hardwareMap.dcMotor.get("ballCollect");
        extension = hardwareMap.dcMotor.get("extension");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        zip = hardwareMap.servo.get("dump");
        dump = hardwareMap.servo.get("zip");

        //Testing
        motorLeft.setPower(.5);
        motorRight.setPower(.5);

        sleep(2000);

        motorLeft.setPower(0);
        motorRight.setPower(0);

        // Our code
        straight(6.5);
        turn(-90);
        straight(6);
        // Code regarding color sensor + climbers

        straight(-3);
        turn(-45);
        straight(5);
    }

    public void turn(double degrees) throws InterruptedException {
        if (degrees>0) {
            motorLeft.setPower(0.5);
            motorRight.setPower(-0.5);
        }else{
            motorLeft.setPower(-0.5);
            motorRight.setPower(0.5);
        }

        sleep((long)((Math.PI*TURN_RADIUS*Math.abs(degrees)/180)/(Math.PI*2*WHEEL_RADIUS)/(FULL_RPM*0.5*60000)));

        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

    public void straight(double distance) throws InterruptedException {
        if (distance>0) {
            motorLeft.setPower(0.5);
            motorRight.setPower(0.5);
        }else{
            motorLeft.setPower(-0.5);
            motorRight.setPower(-0.5);
        }

        sleep((long)(distance/(Math.PI*2*WHEEL_RADIUS)/(FULL_RPM*0.5*60000)));

        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

}
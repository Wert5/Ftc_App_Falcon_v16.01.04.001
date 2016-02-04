package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Winston on 1/30/16.
 */
public class AutoBlueOp extends LinearOpMode {
    final static double ZIP_MIN_RANGE = 0.00;
    final static double ZIP_MAX_RANGE = 0.50;

    final static double COLLECT_MIN_RANGE = 0.00;
    final static double COLLECT_MAX_RANGE = 0.50;

    final static double DUMP_L_LEFT = 0.00;
    final static double DUMP_L_CLOSE = 0.50;
    final static double DUMP_L_RIGHT = 1.00;

    final static double DUMP_R_LEFT = 0.00;
    final static double DUMP_R_CLOSE = 0.50;
    final static double DUMP_R_RIGHT = 1.00;

    // position of the zipLeft servo.
    boolean zipBool;

    // position of the dump servos
    double dumpLeftPos;
    double dumpRightPos;

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor ballCollect;
    DcMotor extension;
    Servo dumpLeft;
    Servo dumpRight;
    Servo zip;

    double TURN_RADIUS=0.709;
    double WHEEL_RADIUS=0.166;
    double FULL_RPM=50.333;

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
		 *    "servo_1" controls the zipLeft joint of the manipulator.
		 *    "servo_6" controls the dump joint of the manipulator.
		 */
        motorRight = hardwareMap.dcMotor.get("right");
        motorLeft = hardwareMap.dcMotor.get("left");
        /*ballCollect = hardwareMap.dcMotor.get("ballCollect");
        extension = hardwareMap.dcMotor.get("extension");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        dumpLeft = hardwareMap.servo.get("dumpLeft");
        dumpRight = hardwareMap.servo.get("dumpRight");
        zipLeft = hardwareMap.servo.get("zipLeft");

        // assign the starting position of the wrist and dump
        zipBool =true;
        dumpLeftPos = DUMP_L_CLOSE;
        dumpRightPos = DUMP_R_CLOSE;*/

        // Our code
        straight(6.5);
        turn(90);
        straight(6);
        // Code regarding color sensor + climbers

        straight(-3);
        turn(45);
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

        sleep((long)((TURN_RADIUS*Math.abs(degrees)/180)/(2*WHEEL_RADIUS)/(FULL_RPM*0.5/60000)));

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

        sleep((long)((0.6*distance/(Math.PI*2*WHEEL_RADIUS))/(FULL_RPM*0.5/60000)));

        motorLeft.setPower(0);
        motorRight.setPower(0);
    }

}
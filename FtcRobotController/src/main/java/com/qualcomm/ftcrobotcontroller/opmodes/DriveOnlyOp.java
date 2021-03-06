package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Winston on 1/30/16.
 */
public class DriveOnlyOp extends OpMode {

    /*
     * Note: the configuration of the servos is such that
     * as the zip servo approaches 0, the zip position moves up (away from the floor).
     * Also, as the dump servo approaches 0, the dump opens up (drops the game element).
     */
    // TETRIX VALUES.
    final static double ZIP_MIN_RANGE = 0.00;
    final static double ZIP_MAX_RANGE = 0.50;
    final static double COLLECT_MIN_RANGE = 0.00;
    final static double COLLECT_MAX_RANGE = 0.50;
    final static double DUMP_MIN_RANGE = 0.20;
    final static double DUMP_MAX_RANGE = 0.7;

    // position of the zip servo.
    boolean zipBool;
    double zipPosition;

    // amount to change the zip servo position.
//    double zipDelta = 0.1;

    // position of the dump servo
    double dumpPosition;

    // amount to change the dump servo position by
    double dumpDelta = 0.1;

    boolean collectBool=false;
    double collectPower=0.0;

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor ballCollect;
    DcMotor extension;
    Servo dump;
    Servo zip;

    /**
     * Constructor
     */
    public DriveOnlyOp() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
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
        //ballCollect = hardwareMap.dcMotor.get("ballCollect");
        //extension = hardwareMap.dcMotor.get("extension");
        //motorLeft.setDirection(DcMotor.Direction.REVERSE);

        //zip = hardwareMap.servo.get("dump");
        //dump = hardwareMap.servo.get("zip");

        // assign the starting position of the wrist and dump
        zipBool =true;
        dumpPosition = 0.2;
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/dump via the a,b, x, y buttons
		 */

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        motorRight.setPower(right);
        motorLeft.setPower(left);

        // update the position of the zip.
        if (gamepad1.a) {
            // if the A button is pushed on gamepad1, change the position of zip
            if(zipBool){
                zipPosition=ZIP_MIN_RANGE;
                zipBool=!zipBool;
            }else{
                zipPosition=ZIP_MAX_RANGE;
                zipBool=!zipBool;
            }
        }

        if (gamepad1.right_trigger>0.5) {
            // if the right trigger is pushed on gamepad1, change the position of zip
            if(collectBool){
                collectPower=COLLECT_MIN_RANGE;
                collectBool=!collectBool;
            }else{
                collectPower=COLLECT_MAX_RANGE;
                collectBool=!collectBool;
            }
        }

        // update the position of the dump
        if (gamepad2.x) {
            dumpPosition += dumpDelta;
        }

        if (gamepad2.b) {
            dumpPosition -= dumpDelta;
        }

        // clip the position values so that they never exceed their allowed range.
        zipPosition = Range.clip(zipPosition, ZIP_MIN_RANGE, ZIP_MAX_RANGE);
        dumpPosition = Range.clip(dumpPosition, DUMP_MIN_RANGE, DUMP_MAX_RANGE);

        // write position values to the wrist and dump servo
        //zip.setPosition(zipPosL);
        //dump.setPosition(dumpPosition);
        //ballCollect.setPower(collectPower);



		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("zip", "zip:  " + String.format("%.2f", zipPosition));
        telemetry.addData("dump", "dump:  " + String.format("%.2f", dumpPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        telemetry.addData("collector pwr", "collector pwr: " + String.format("%.2f", collectPower));

    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}

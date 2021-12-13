
package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.Arrays;
import java.util.Vector;

/*
 * This is an example of a more complex path to really test the tuning.
 */
@Autonomous(group = "drive")
public class redDuck extends LinearOpMode {
    private DcMotorEx SlidesAngle, LSlides, Intake, Wheel;
    private Servo Bin;
    private ElapsedTime runtime = new ElapsedTime(0);

    public void runOpMode() throws InterruptedException {
        SlidesAngle = hardwareMap.get(DcMotorEx.class, "SlidesAngle");
        LSlides = hardwareMap.get(DcMotorEx.class, "LSlides");
        Bin = hardwareMap.get(Servo.class, "Bin");
        Wheel = hardwareMap.get(DcMotorEx.class, "Wheel");
        Intake = hardwareMap.get(DcMotorEx.class, "Intake");
        Wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

         double LSlidesPower;
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Bin.setPosition(0.5);
        Pose2d duckPose = new Pose2d(-85, -80, Math.toRadians(180));
        Pose2d myPose = new Pose2d(-30, -62, Math.toRadians(270));//intake facing back wall
        drive.setPoseEstimate(myPose);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (isStopRequested()) return;
        //START-------------------------------------------------------------------------------------

        //trajectories
        Trajectory fondue = drive.trajectoryBuilder(myPose)
                //.back(20)
                .lineToSplineHeading(new Pose2d(-1, -14, Math.toRadians(240)))

                .addDisplacementMarker(()->{


                    LSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    LSlides.setTargetPosition(3500);
                    LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    LSlides.setPower(-0.8);
                    SlidesAngle.setPower(0.5);


                    while(LSlides.isBusy()){
                        telemetry.addData("Curent pos: ", LSlides.getCurrentPosition());
                        telemetry.update();

                        //idle

                    }


                    LSlides.setPower(0.0);
                    Bin.setPosition(1.0);
                    sleep(2000);
                    Bin.setPosition(0.5);
                    LSlides.setTargetPosition(0);
                    //LSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    LSlides.setPower(0.7);

                    while(LSlides.isBusy()){
                        telemetry.addData("Curent pos: ", LSlides.getCurrentPosition());
                        telemetry.update();
                        //idle
                        SlidesAngle.setPower(-0.1);
                    }
                    LSlides.setPower(0);
                    SlidesAngle.setPower(0.0);



                })


                .build();
        //avoid barcode
        Trajectory back = drive.trajectoryBuilder(fondue.end())
               .lineTo(new Vector2d(-18, -40))
                .build();


        //duck
        Trajectory duck = drive.trajectoryBuilder(fondue.end())
                .lineToSplineHeading(new Pose2d(-85, -80, Math.toRadians(240)))
                .build();
        /*Trajectory Duck = drive.trajectoryBuilder(fondue.end())
                .lineToSplineHeading(new Pose2d(-85, -80, Math.toRadians(180)))
                .build();*/

        //park in tape
        Trajectory tPark = drive.trajectoryBuilder(duckPose)
                .lineToSplineHeading(new Pose2d(-105,-71, Math.toRadians(180)))

                .build();




        drive.followTrajectory(fondue);


        LSlides.setPower(0);




        //drive.followTrajectory(back);
        drive.followTrajectory(duck);
        drive.turn(Math.toRadians(-60));
        Wheel.setPower(-0.2);
        sleep(3000);
        Wheel.setPower(0);
        drive.followTrajectory(tPark);


    }

}
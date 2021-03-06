# Swerve Drive Visualisation

This project is a simple visualisation of the speeds and angles of the vectors of a swerve drive for a given 3-axis input.

It serves both as a nice visualisation for an omnidirectional drive system, and as a help for those trying to understand the math behind a system like this.

![](img/preview.gif)

## Controls

The simulation features minimalistic controls for the **Logitech Extreme 3D Pro** controller (currently the only supported controller):
* **Left Mouse Click** resizes the chassis of the robot.
* **X axis** of the controller controls the left/right motion.
* **Y axis** of the controller controls the forward/backward motion.
* **Rotation axis** of the controller controls the rotation motion.

## Resources
* [JInput](https://github.com/jinput/jinput) was the library used for the controller input.
* [Processing 3](https://processing.org/) is the graphic library used to visualise the simulation.
* Ether's [derivation of inverse kinematics of swerve drive drive](https://www.chiefdelphi.com/media/papers/download/3027) was an absolutely awesome resource for working through the math of the system.
* Jacob Misirian's [FRC Swerve Drive Programming guide](https://legacy.gitbook.com/book/jacobmisirian/frc-swerve-drive-programming/) was quite helpful as well, as it went over the Java implementation of such system on FRC robots.
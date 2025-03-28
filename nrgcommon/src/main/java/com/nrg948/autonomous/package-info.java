/*
  MIT License

  Copyright (c) 2022 Newport Robotics Group

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
*/

/**
 * The {@code com.nrg948.autonomous} package implements an annotation-based approach for identifying
 * and selecting autonomous commands.
 *
 * <p>The {@link AutonomousCommand} annotation identifies an autonomous command class. All classes
 * with this annotation must be a subclass of {@link Command} and declare the same public
 * constructor taking a single parameter used to access the robot subsystems. This parameter is
 * typically <code>RobotContainer</code> but may be another type managing access to the subsystems.
 *
 * <p>The following example shows how to structure and annotate autonomous command classes.<br>
 *
 * <pre>
 * <code>
 * {@literal @}AutonomousCommand (name = "My Autonomous Command", isDefault = true)
 * public class MyAutonomousCommand extends SequentialCommandGroup {
 *   public MyAutonomousCommand(RobotContainer container) {
 *     addCommands(
 *       new DriveToFirstLocation(container.getDrivetrainSubsystem()),
 *       ...
 *       )
 *   }
 * }
 * </code>
 * </pre>
 *
 * <p>The {@link AutonomousCommandMethod} annotation identifies a public static factory method
 * invoked to create a {@link Command} subclass to be run during autonomous. The method takes a
 * single parameter used to access robot subsystem and returns a sublass of {@link Command}. This
 * parameter is typically <code>RobotContainer</code> but may be another type managing access to the
 * subsystems.
 *
 * <p>The following example shows how to define and annotate an autonomous {@link Command} factory
 * method.<br>
 *
 * <pre>
 * <code>
 * public class Autos {<br>
 *  {@literal @}AutonomousCommandMethod (name = "My Autonomous Command", isDefault = true)
 *  public static Command exampleRoutine(RobotContainer container) {
 *    return Command.sequence(
 *       new DriveToFirstLocation(container.getDrivetrainSubsystem()),
 *       ...
 *    );
 *  }
 * }
 * </code>
 * </pre>
 *
 * <p>Each annotation contains two elements: {@link AutonomousCommand#name()} and {@link
 * AutonomousCommand#isDefault()}. The <code>name</code> element provides the name to display for
 * the command in user interface elements like {@link SendableChooser}. The <code>isDefault</code>
 * element determines which command is the default selection. Only one command class or factory
 * method should be set as the default.
 *
 * <p>The {@link AutonomousCommandGenerator} annotation identifies a public static method invoked to
 * create a {@link Collection} of {@link LabelValue} elements mapping the name to display in user
 * interface elements like {@link SendableChooser} to a {@link Command}.
 *
 * <p>The following example shows how to define and annotate an autonomous {@link Command} generator
 * method.<br>
 *
 * <pre>
 * <code>
 * public class Autos {<br>
 *  {@literal @}AutonomousCommandGenerator
 *  public static Collection&lt;LabelValue&lt;String, Command&gt;&gt; exampleGenerator(RobotContainer container) {
 *    return List.of(
 *      new LabelValue&lt;String, Command&gt;("First Auto", new FirstAuto(container)),
 *      new LabelValue&lt;String, Command&gt;("Second Auto", new SecondAuto(container)),
 *      );
 *  }
 * }
 * </code>
 * </pre>
 *
 * <p>Once all autonomous command classes, factory and/or generator methods have been annotated, the
 * {@link Autonomous#getChooser(Object, String...)} method can be used to create a {@link
 * SendableChooser} object enabling iteractive selection of the autonomous command from Shuffleboard
 * or SmartDashboard.
 *
 * <p>The following example shows how to implement interactive autonomous command selection.<br>
 *
 * <pre>
 * <code>
 * public class RobotContainer {
 *   private DrivetrainSubsystem m_drivetrainSubsystem = new DrivetrainSubsystem();
 *
 *   private SendableChooser{@literal <}Command{@literal >} m_autonomousCommandChooser;
 *
 *   public RobotContainer() {
 *     m_autonomousCommandChooser = Autonomous.getChooser(this, "frc.robot");
 *
 *     // Add an "Autonomous" tab to Shuffleboard, create a new layout and add the
 *     // autonomous command chooser to it.
 *     Shuffleboard.getTab("Autonomous")
 *       .getLayout("Autonomous", BuiltInLayouts.kList)
 *       .withSize(2, 2)
 *       .add("Command", m_autonomousCommandChooser);
 *   }
 *
 *   public DrivetrainSubsystem getDrivetrainSubsystem() {
 *     return m_drivetrainSubsystem;
 *   }
 *
 *   public Command getAutonomousCommand() {
 *     return m_autonomousCommandChooser.getSelected();
 *   }
 * }
 * </code>
 * </pre>
 */
package com.nrg948.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.Collection;
import org.javatuples.LabelValue;

/*
 * Copyright 2020 WinT 3794 (Manuel Diaz Rojo and Alexis Obed Garcia Hernandez)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * The Main Package of the Follower. It contains all math,
 * movements, configurations and more.
 */
package org.wint3794.pathfollower

import java.io.FileReader
import java.io.IOException
import kotlin.jvm.Throws
import java.util.Optional
import org.wint3794.pathfollower.geometry.CurvePoint
import org.wint3794.pathfollower.io.PathReader
import java.lang.IllegalArgumentException
import org.wint3794.pathfollower.geometry.Pose2d
import java.lang.Math
import org.wint3794.pathfollower.util.SpeedOmeter
import org.wint3794.pathfollower.util.MathFunctions
import org.wint3794.pathfollower.exceptions.InvalidMecanumDirectiveException
import org.wint3794.pathfollower.models.MecanumDirectives
import org.wint3794.pathfollower.exceptions.InvalidJoystickCoordinatesException
import org.wint3794.pathfollower.models.JoystickCoordinates
import org.wint3794.pathfollower.drivebase.mecanum.CalculateVelocities
import org.wint3794.pathfollower.util.VelocityChecker
import java.lang.StringBuilder
import org.wint3794.pathfollower.debug.Telemetry
import java.net.Socket
import java.net.ServerSocket
import java.io.DataOutputStream
import org.wint3794.pathfollower.debug.telemetries.TCPServer
import java.lang.Runnable
import java.net.InetAddress
import java.util.concurrent.Semaphore
import org.wint3794.pathfollower.debug.telemetries.UdpServer
import java.lang.InterruptedException
import java.net.DatagramSocket
import java.net.DatagramPacket
import kotlin.jvm.Synchronized
import org.wint3794.pathfollower.debug.RobotLogger
import org.wint3794.pathfollower.debug.ComputerDebugging
import java.text.DecimalFormat
import org.wint3794.pathfollower.models.PIDValues
import java.util.Arrays
import kotlin.jvm.JvmOverloads
import org.wint3794.pathfollower.hardware.EncoderBase
import org.wint3794.pathfollower.hardware.encoder.Encoders
import org.wint3794.pathfollower.hardware.encoder.ZeroPowerBehavior
import org.wint3794.pathfollower.drivebase.ChassisConfiguration
import org.wint3794.pathfollower.drivebase.Kinematic
import org.wint3794.pathfollower.util.MovementVars
import java.util.stream.Collectors
import org.wint3794.pathfollower.models.DcMotorVelocities
import org.wint3794.pathfollower.drivebase.mecanum.MecanumKinematic
import org.wint3794.pathfollower.drivebase.mecanum.CalculatePower
import org.wint3794.pathfollower.drivebase.RobotMovement
import org.wint3794.pathfollower.hardware.DcMotorBase
import org.wint3794.pathfollower.util.ExecutionModes
import org.wint3794.pathfollower.models.EncoderProperties
import org.wint3794.pathfollower.drivebase.ChassisTypes
import org.wint3794.pathfollower.controllers.Follower
import org.wint3794.pathfollower.exceptions.NotCompatibleConfigurationException
import org.wint3794.pathfollower.drivebase.tank.TankKinematic
import org.wint3794.pathfollower.models.PIDFCoefficients

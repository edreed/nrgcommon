/*
  MIT License

  Copyright (c) 2026 Newport Robotics Group

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
package com.nrg948.sensor.rev;

import com.nrg948.sensor.RelativeEncoder;

/** A relative encoder implementation based on the REV Robotics Spark motor controllers. */
public final class SparkEncoderAdapter implements RelativeEncoder {
  private final com.revrobotics.RelativeEncoder encoder;
  private final double distancePerRotation;

  /**
   * Constructs a SparkEncoderAdapter.
   *
   * @param encoder The REV Robotics RelativeEncoder to adapt.
   * @param distancePerRotation The distance per rotation for the encoder.
   */
  public SparkEncoderAdapter(com.revrobotics.RelativeEncoder encoder, double distancePerRotation) {
    this.encoder = encoder;
    this.distancePerRotation = distancePerRotation;
  }

  @Override
  public void setPosition(double position) {
    encoder.setPosition(position / distancePerRotation);
  }

  @Override
  public double getPosition() {
    return encoder.getPosition().get() * distancePerRotation;
  }

  @Override
  public double getVelocity() {
    return (encoder.getVelocity().get() * distancePerRotation) / 60.0; // Convert from RPM to RPS
  }

  @Override
  public void reset() {
    encoder.setPosition(0);
  }
}

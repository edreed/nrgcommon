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
package com.nrg948.dashboard.data;

import org.wpilib.tunable.ComplexTunable;
import org.wpilib.tunable.Tunable;
import org.wpilib.tunable.Tunables;

/** A binding that binds a {@link Tunable} to dashboard data updates. */
final class ComplexTunableBinding extends DataBinding {
  private final String topic;
  private final ComplexTunable tunable;

  /**
   * Constructs a TunableBinding with the given topic and Tunable.
   *
   * @param topic The topic to bind the Tunable to.
   * @param tunable The Tunable to bind.
   */
  ComplexTunableBinding(String topic, ComplexTunable tunable) {
    this.topic = topic;
    this.tunable = tunable;
  }

  @Override
  protected void enableSelf() {
    Tunables.publish(topic, tunable);
  }

  @Override
  protected void disableSelf() {
    Tunables.remove(topic);
  }
}

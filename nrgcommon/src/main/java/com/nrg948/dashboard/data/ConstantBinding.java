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

import java.util.Optional;
import java.util.function.Supplier;
import org.wpilib.tunable.TunableBase;
import org.wpilib.tunable.TunableConfig;
import org.wpilib.tunable.TunableConfig.Polling;
import org.wpilib.tunable.Tunables;

/** A binding that binds a constant value to dashboard data updates. */
final class ConstantBinding extends DataBinding {
  private final TunableConfig config =
      new TunableConfig().withMutable(false).withPolling(Polling.GET_ON_CHANGE);
  private final String topic;
  private Supplier<TunableBase> tunableSupplier;

  @SuppressWarnings("unused")
  private Optional<TunableBase> tunable = Optional.empty();

  /**
   * Constructs a ConstantBinding with the given topic and boolean value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant boolean value to bind.
   */
  public ConstantBinding(String topic, boolean value) {
    this.topic = topic;
    this.tunableSupplier = () -> Tunables.publishBoolean(topic, () -> value, (v) -> {}, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and boolean array value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant boolean array value to bind.
   */
  public ConstantBinding(String topic, boolean[] value) {
    this.topic = topic;
    this.tunableSupplier =
        () -> Tunables.publishValue(topic, () -> value, (v) -> {}, boolean[].class, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and float value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant float value to bind.
   */
  public ConstantBinding(String topic, float value) {
    this.topic = topic;
    this.tunableSupplier = () -> Tunables.publishFloat(topic, () -> value, (v) -> {}, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and float array value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant float array value to bind.
   */
  public ConstantBinding(String topic, float[] value) {
    this.topic = topic;
    this.tunableSupplier =
        () -> Tunables.publishValue(topic, () -> value, (v) -> {}, float[].class, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and double value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant double value to bind.
   */
  public ConstantBinding(String topic, double value) {
    this.topic = topic;
    this.tunableSupplier = () -> Tunables.publishDouble(topic, () -> value, (v) -> {}, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and double array value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant double array value to bind.
   */
  public ConstantBinding(String topic, double[] value) {
    this.topic = topic;
    this.tunableSupplier =
        () -> Tunables.publishValue(topic, () -> value, (v) -> {}, double[].class, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and long value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant long value to bind.
   */
  public ConstantBinding(String topic, int value) {
    this.topic = topic;
    this.tunableSupplier = () -> Tunables.publishInt(topic, () -> value, (v) -> {}, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and long array value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant long array value to bind.
   */
  public ConstantBinding(String topic, int[] value) {
    this.topic = topic;
    this.tunableSupplier =
        () -> Tunables.publishValue(topic, () -> value, (v) -> {}, int[].class, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and string value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant string value to bind.
   */
  public ConstantBinding(String topic, String value) {
    this.topic = topic;
    this.tunableSupplier =
        () -> Tunables.publishValue(topic, () -> value, (v) -> {}, String.class, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and string array value.
   *
   * @param topic The topic to bind the value to.
   * @param value The constant string array value to bind.
   */
  public ConstantBinding(String topic, String[] value) {
    this.topic = topic;
    this.tunableSupplier =
        () -> Tunables.publishValue(topic, () -> value, (v) -> {}, String[].class, config);
  }

  /**
   * Constructs a ConstantBinding with the given topic and raw byte array value.
   *
   * @param topic The topic to bind the value to.
   * @param typeString The type string to use for the publisher.
   * @param value The constant raw byte array value to bind.
   */
  public ConstantBinding(String topic, String typeString, byte[] value) {
    this.topic = topic;
    this.tunableSupplier =
        () ->
            Tunables.publishValue(
                topic, () -> value, (v) -> {}, byte[].class, config.withTypeString(typeString));
  }

  @Override
  public void enableSelf() {
    tunable = Optional.of(tunableSupplier.get());
  }

  @Override
  public void disableSelf() {
    Tunables.remove(topic);
    tunable = Optional.empty();
  }
}

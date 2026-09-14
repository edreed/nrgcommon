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
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import org.wpilib.tunable.TunableConfig;
import org.wpilib.tunable.TunableDouble;
import org.wpilib.tunable.Tunables;

/** A data binding that binds a double publisher and/or subscriber to dashboard data updates. */
final class DoubleBinding extends DataBinding {
  private static final double DEFAULT_VALUE = 0.0;

  private final String topic;
  private final DoubleSupplier supplier;
  private final DoubleConsumer consumer;
  private final TunableConfig config;

  @SuppressWarnings("unused")
  private Optional<TunableDouble> tunable = Optional.empty();

  /**
   * Creates a new DoubleBinding with the given topic and supplier.
   *
   * @param topic The topic to bind to.
   * @param supplier The supplier to use for publishing updates, or null if no publisher is needed
   *     for this binding.
   */
  public DoubleBinding(String topic, DoubleSupplier supplier) {
    this(topic, supplier, null);
  }

  /**
   * Creates a new DoubleBinding with the given topic, supplier, and consumer.
   *
   * @param topic The topic to bind to.
   * @param supplier The supplier to use for publishing updates, or null if no publisher is needed
   *     for this binding.
   * @param consumer The consumer to use for updating the subscriber, or null if no subscriber is
   *     needed for this binding.
   */
  public DoubleBinding(String topic, DoubleSupplier supplier, DoubleConsumer consumer) {
    this(topic, Optional.ofNullable(supplier), Optional.ofNullable(consumer));
  }

  /**
   * Creates a new DoubleBinding with the given topic, supplier, and consumer.
   *
   * @param topic The topic to bind to.
   * @param supplier The supplier to use for publishing updates, or {@link Optional#empty()} if no
   *     publisher is needed for this binding.
   * @param consumer The consumer to use for updating the subscriber, or {@link Optional#empty()} if
   *     no subscriber is needed for this binding.
   */
  public DoubleBinding(
      String topic, Optional<DoubleSupplier> supplier, Optional<DoubleConsumer> consumer) {
    this.topic = topic;
    this.supplier = supplier.orElse(() -> DEFAULT_VALUE);
    this.consumer = consumer.orElse((v) -> {});
    this.config =
        new TunableConfig()
            .withMutable(consumer.isEmpty())
            .withPolling(TunableConfig.Polling.ALWAYS_GET);
  }

  @Override
  protected void enableSelf() {
    tunable = Optional.of(Tunables.publishDouble(topic, supplier, consumer, config));
  }

  @Override
  protected void disableSelf() {
    Tunables.remove(topic);
    tunable = Optional.empty();
  }
}

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
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.wpilib.tunable.Tunable;
import org.wpilib.tunable.TunableConfig;
import org.wpilib.tunable.TunableConfig.Polling;
import org.wpilib.tunable.Tunables;

/**
 * A data binding that binds a boolean array publisher and/or subscriber to dashboard data updates.
 */
abstract class ObjectBinding<T> extends DataBinding {
  private final String topic;
  private final Class<T> clazz;
  private final Supplier<T> supplier;
  private final Consumer<T> consumer;
  private final TunableConfig config;

  @SuppressWarnings("unused")
  private Optional<Tunable<T>> tunable = Optional.empty();

  /**
   * Creates a new ObjectBinding with the given topic, supplier, and consumer.
   *
   * @param topic The topic to bind to.
   * @param supplier The supplier to use for publishing updates, or {@link Optional#empty()} if no
   *     publisher is needed for this binding.
   * @param consumer The consumer to use for updating the subscriber, or {@link Optional#empty()} if
   *     no subscriber is needed for this binding.
   * @param clazz The class of the object being bound.
   * @param defaultValue The default value to use if the supplier is not provided.
   */
  protected ObjectBinding(
      String topic,
      Optional<Supplier<T>> supplier,
      Optional<Consumer<T>> consumer,
      Class<T> clazz,
      T defaultValue) {
    this(topic, supplier, consumer, clazz, defaultValue, new TunableConfig());
  }

  /**
   * Creates a new ObjectBinding with the given topic, supplier, consumer, and configuration.
   *
   * @param topic The topic to bind to.
   * @param supplier The supplier to use for publishing updates, or {@link Optional#empty()} if no
   *     publisher is needed for this binding.
   * @param consumer The consumer to use for updating the subscriber, or {@link Optional#empty()} if
   *     no subscriber is needed for this binding.
   * @param clazz The class of the object being bound.
   * @param defaultValue The default value to use if the supplier is not provided.
   * @param config The configuration for the tunable. The configuration will be modified to set the
   *     mutability and polling mode based on the presence of a consumer.
   */
  protected ObjectBinding(
      String topic,
      Optional<Supplier<T>> supplier,
      Optional<Consumer<T>> consumer,
      Class<T> clazz,
      T defaultValue,
      TunableConfig config) {
    this.topic = topic;
    this.supplier = supplier.orElse(() -> defaultValue);
    this.consumer = consumer.orElse((v) -> {});
    this.clazz = clazz;
    this.config = config.withMutable(consumer.isPresent()).withPolling(Polling.ALWAYS_GET);
  }

  @Override
  protected void enableSelf() {
    tunable = Optional.of(Tunables.publishValue(topic, supplier, consumer, clazz, config));
  }

  @Override
  protected void disableSelf() {
    Tunables.remove(topic);
    tunable = Optional.empty();
  }
}

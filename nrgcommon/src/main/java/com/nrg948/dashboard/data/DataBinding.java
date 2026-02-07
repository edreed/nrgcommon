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

import edu.wpi.first.networktables.PubSub;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.networktables.Publisher;
import edu.wpi.first.networktables.Subscriber;
import edu.wpi.first.networktables.Topic;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A data binding that binds a publisher and/or subscriber to dashboard data updates.
 *
 * @param <P> The type of the publisher.
 * @param <S> The type of the subscriber.
 */
final class DataBinding<T extends Topic, P extends Publisher, S extends Subscriber>
    extends DashboardData {
  private static final PubSubOption[] NO_OPTIONS = new PubSubOption[0];

  private final T topic;
  private final Function<T, P> toPublisher;
  private final Optional<Consumer<P>> publishUpdates;
  private final BiFunction<T, PubSubOption[], S> toSubscriber;
  private final Optional<Consumer<S>> updateSubscriber;

  private Optional<P> publisher = Optional.empty();
  private Optional<S> subscriber = Optional.empty();

  /**
   * Constructs a DataBinding with the given publisher, publishUpdates function, subscriber, and
   * updateSubscriber function.
   *
   * @param topic The topic to bind.
   * @param toPublisher The function to create a publisher from a topic.
   * @param publishUpdates The function to update the publisher.
   * @param toSubscriber The function to create a subscriber from a topic with options.
   * @param updateSubscriber The function to update the subscriber.
   */
  public DataBinding(
      T topic,
      Function<T, P> toPublisher,
      Optional<Consumer<P>> publishUpdates,
      BiFunction<T, PubSubOption[], S> toSubscriber,
      Optional<Consumer<S>> updateSubscriber) {
    this.topic = topic;
    this.toPublisher = toPublisher;
    this.publishUpdates = publishUpdates;
    this.toSubscriber = toSubscriber;
    this.updateSubscriber = updateSubscriber;
  }

  /**
   * Constructs a DataBinding with only a publisher.
   *
   * @param topic The topic to bind.
   * @param toPublisher The function to create a publisher from a topic.
   * @param publishUpdates The function to update the publisher.
   */
  public DataBinding(T topic, Function<T, P> toPublisher, Consumer<P> publishUpdates) {
    this(topic, toPublisher, Optional.of(publishUpdates), (t, u) -> null, Optional.empty());
  }

  /**
   * Constructs a DataBinding with both a publisher and a subscriber.
   *
   * @param toPublisher The function to create a publisher from a topic.
   * @param publishUpdates The function to update the publisher.
   * @param toSubscriber The function to create a subscriber from a topic with options.
   * @param updateSubscriber The function to update the subscriber.
   */
  public DataBinding(
      T topic,
      Function<T, P> toPublisher,
      Consumer<P> publishUpdates,
      BiFunction<T, PubSubOption[], S> toSubscriber,
      Consumer<S> updateSubscriber) {
    this(
        topic,
        toPublisher,
        Optional.of(publishUpdates),
        toSubscriber,
        Optional.of(updateSubscriber));
  }

  @Override
  public void enable() {
    publisher = Optional.ofNullable(toPublisher.apply(topic));

    var options = publisher.map(p -> new PubSubOption[] {PubSubOption.excludePublisher(p)}).orElse(NO_OPTIONS);

    subscriber = Optional.ofNullable(toSubscriber.apply(topic, options));
  }

  @Override
  public void disable() {
    publisher.ifPresent(Publisher::close);
    subscriber.ifPresent(Subscriber::close);
    publisher = Optional.empty();
    subscriber = Optional.empty();
  }

  @Override
  protected void update() {
    subscriber.ifPresent(sub -> updateSubscriber.get().accept(sub));
    publisher.ifPresent(pub -> publishUpdates.get().accept(pub));
  }

  @Override
  public void close() {
    publisher.ifPresent(Publisher::close);
    subscriber.ifPresent(Subscriber::close);
  }
}

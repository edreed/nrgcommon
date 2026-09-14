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

import com.nrg948.preferences.PreferenceValue;
import com.nrg948.util.enums.EnumChooser;
import com.nrg948.util.function.ObjBooleanConsumer;
import com.nrg948.util.function.ObjFloatConsumer;
import com.nrg948.util.function.ToBooleanFunction;
import com.nrg948.util.function.ToFloatFunction;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import org.wpilib.tunable.ComplexTunable;
import org.wpilib.util.function.BooleanConsumer;
import org.wpilib.util.function.FloatConsumer;
import org.wpilib.util.function.FloatSupplier;
import org.wpilib.vision.camera.HttpCamera;

/** Abstract class representing dashboard data bindings. */
public abstract class DashboardData {
  /** An empty array of DashboardData, used when no data bindings are present. */
  public static final DashboardData[] NO_DATA = new DashboardData[] {};

  static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[] {};
  static final int[] EMPTY_INTEGER_ARRAY = new int[] {};
  static final float[] EMPTY_FLOAT_ARRAY = new float[] {};
  static final double[] EMPTY_DOUBLE_ARRAY = new double[] {};
  static final String[] EMPTY_STRING_ARRAY = new String[] {};
  static final byte[] DEFAULT_RAW_VALUE = new byte[] {};

  /** Constructs a new instance of {@code DashboardData}. */
  DashboardData() {}

  /** Enables the dashboard data binding. */
  public abstract void enable();

  /** Disables the dashboard data binding. */
  public abstract void disable();

  /**
   * Bind a boolean value to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the boolean value.
   * @param getter The function to retrieve the boolean value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindBoolean(
      String topic, T container, ToBooleanFunction<T> getter) {
    return new BooleanBinding(topic, () -> getter.applyAsBoolean(container));
  }

  /**
   * Bind a static boolean value to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the boolean value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticBoolean(String topic, BooleanSupplier getter) {
    return new BooleanBinding(topic, getter);
  }

  /**
   * Bind a constant boolean value to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the constant boolean value.
   * @param getter The function to retrieve the constant boolean value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantBoolean(
      String topic, T container, ToBooleanFunction<T> getter) {
    return new ConstantBinding(topic, getter.applyAsBoolean(container));
  }

  /**
   * Bind a constant static boolean value to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant boolean value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantBoolean(String topic, BooleanSupplier getter) {
    return new ConstantBinding(topic, getter.getAsBoolean());
  }

  /**
   * Bind a boolean value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the boolean value.
   * @param getter The function to retrieve the boolean value from the container.
   * @param setter The function to update the boolean value in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindBoolean(
      String topic, T container, ToBooleanFunction<T> getter, ObjBooleanConsumer<T> setter) {
    return new BooleanBinding(
        topic, () -> getter.applyAsBoolean(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static boolean value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the boolean value.
   * @param setter The function to update the boolean value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticBoolean(
      String topic, BooleanSupplier getter, BooleanConsumer setter) {
    return new BooleanBinding(topic, getter, setter);
  }

  /**
   * Bind a boolean array to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the boolean array.
   * @param getter The function to retrieve the boolean array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindBooleanArray(
      String topic, T container, Function<T, boolean[]> getter) {
    return new BooleanArrayBinding(topic, () -> getter.apply(container));
  }

  /**
   * Bind a static boolean array to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the boolean array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticBooleanArray(String topic, Supplier<boolean[]> getter) {
    return new BooleanArrayBinding(topic, getter);
  }

  /**
   * Bind a constant boolean array to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the boolean array.
   * @param getter The function to retrieve the boolean array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantBooleanArray(
      String topic, T container, Function<T, boolean[]> getter) {
    return new ConstantBinding(topic, getter.apply(container));
  }

  /**
   * Bind a constant static boolean array to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant boolean array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantBooleanArray(String topic, Supplier<boolean[]> getter) {
    return new ConstantBinding(topic, getter.get());
  }

  /**
   * Bind a boolean array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the boolean array.
   * @param getter The function to retrieve the boolean array from the container.
   * @param setter The function to update the boolean array in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindBooleanArray(
      String topic, T container, Function<T, boolean[]> getter, BiConsumer<T, boolean[]> setter) {
    return new BooleanArrayBinding(
        topic, () -> getter.apply(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static boolean array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the boolean array.
   * @param setter The function to update the boolean array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticBooleanArray(
      String topic, Supplier<boolean[]> getter, Consumer<boolean[]> setter) {
    return new BooleanArrayBinding(topic, getter, setter);
  }

  /**
   * Bind a float value to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the float value.
   * @param getter The function to retrieve the float value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindFloat(String topic, T container, ToFloatFunction<T> getter) {
    return new FloatBinding(topic, () -> getter.applyAsFloat(container));
  }

  /**
   * Bind a static float value to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the float value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticFloat(String topic, FloatSupplier getter) {
    return new FloatBinding(topic, getter);
  }

  /**
   * Bind a constant float value to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the constant float value.
   * @param getter The function to retrieve the constant float value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantFloat(
      String topic, T container, ToFloatFunction<T> getter) {
    return new ConstantBinding(topic, getter.applyAsFloat(container));
  }

  /**
   * Bind a constant static float value to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant float value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantFloat(String topic, FloatSupplier getter) {
    return new ConstantBinding(topic, getter.getAsFloat());
  }

  /**
   * Bind a float value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the float value.
   * @param getter The function to retrieve the float value from the container.
   * @param setter The function to update the float value in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindFloat(
      String topic, T container, ToFloatFunction<T> getter, ObjFloatConsumer<T> setter) {
    return new FloatBinding(
        topic, () -> getter.applyAsFloat(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static float value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the float value.
   * @param setter The function to update the float value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticFloat(
      String topic, FloatSupplier getter, FloatConsumer setter) {
    return new FloatBinding(topic, getter, setter);
  }

  /**
   * Bind a float array to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the float array.
   * @param getter The function to retrieve the float array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindFloatArray(
      String topic, T container, Function<T, float[]> getter) {
    return new FloatArrayBinding(topic, () -> getter.apply(container));
  }

  /**
   * Bind a static float array to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the float array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticFloatArray(String topic, Supplier<float[]> getter) {
    return new FloatArrayBinding(topic, getter);
  }

  /**
   * Bind a constant float array to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the float array.
   * @param getter The function to retrieve the float array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantFloatArray(
      String topic, T container, Function<T, float[]> getter) {
    return new ConstantBinding(topic, getter.apply(container));
  }

  /**
   * Bind a constant static float array to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant float array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantFloatArray(String topic, Supplier<float[]> getter) {
    return new ConstantBinding(topic, getter.get());
  }

  /**
   * Bind a float array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the float array.
   * @param getter The function to retrieve the float array from the container.
   * @param setter The function to update the float array in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindFloatArray(
      String topic, T container, Function<T, float[]> getter, BiConsumer<T, float[]> setter) {
    return new FloatArrayBinding(
        topic, () -> getter.apply(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static float array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the float array.
   * @param setter The function to update the float array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticFloatArray(
      String topic, Supplier<float[]> getter, Consumer<float[]> setter) {
    return new FloatArrayBinding(topic, getter, setter);
  }

  /**
   * Bind a double value to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the double value.
   * @param getter The function to retrieve the double value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindDouble(
      String topic, T container, ToDoubleFunction<T> getter) {
    return new DoubleBinding(topic, () -> getter.applyAsDouble(container));
  }

  /**
   * Bind a static double value to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the double value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticDouble(String topic, DoubleSupplier getter) {
    return new DoubleBinding(topic, getter);
  }

  /**
   * Bind a constant double value to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the constant double value.
   * @param getter The function to retrieve the constant double value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantDouble(
      String topic, T container, ToDoubleFunction<T> getter) {
    return new ConstantBinding(topic, getter.applyAsDouble(container));
  }

  /**
   * Bind a constant static double value to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant double value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantDouble(String topic, DoubleSupplier getter) {
    return new ConstantBinding(topic, getter.getAsDouble());
  }

  /**
   * Bind a double value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the double value.
   * @param getter The function to retrieve the double value from the container.
   * @param setter The function to update the double value in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindDouble(
      String topic, T container, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter) {
    return new DoubleBinding(
        topic, () -> getter.applyAsDouble(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static double value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the double value.
   * @param setter The function to update the double value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticDouble(
      String topic, DoubleSupplier getter, DoubleConsumer setter) {
    return new DoubleBinding(topic, getter, setter);
  }

  /**
   * Bind a double array to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the double array.
   * @param getter The function to retrieve the double array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindDoubleArray(
      String topic, T container, Function<T, double[]> getter) {
    return new DoubleArrayBinding(topic, () -> getter.apply(container));
  }

  /**
   * Bind a static double array to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the double array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticDoubleArray(String topic, Supplier<double[]> getter) {
    return new DoubleArrayBinding(topic, getter);
  }

  /**
   * Bind a constant double array to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the double array.
   * @param getter The function to retrieve the double array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantDoubleArray(
      String topic, T container, Function<T, double[]> getter) {
    return new ConstantBinding(topic, getter.apply(container));
  }

  /**
   * Bind a constant static double array to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant double array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantDoubleArray(String topic, Supplier<double[]> getter) {
    return new ConstantBinding(topic, getter.get());
  }

  /**
   * Bind a double array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the double array.
   * @param getter The function to retrieve the double array from the container.
   * @param setter The function to update the double array in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindDoubleArray(
      String topic, T container, Function<T, double[]> getter, BiConsumer<T, double[]> setter) {
    return new DoubleArrayBinding(
        topic, () -> getter.apply(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static double array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the double array.
   * @param setter The function to update the double array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticDoubleArray(
      String topic, Supplier<double[]> getter, Consumer<double[]> setter) {
    return new DoubleArrayBinding(topic, getter, setter);
  }

  /**
   * Bind a integer value to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the integer value.
   * @param getter The function to retrieve the integer value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindInteger(String topic, T container, ToIntFunction<T> getter) {
    return new IntegerBinding(topic, () -> getter.applyAsInt(container));
  }

  /**
   * Bind a static integer value to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the integer value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticInteger(String topic, IntSupplier getter) {
    return new IntegerBinding(topic, getter);
  }

  /**
   * Bind a constant integer value to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the constant integer value.
   * @param getter The function to retrieve the constant integer value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantInteger(
      String topic, T container, ToIntFunction<T> getter) {
    return new ConstantBinding(topic, getter.applyAsInt(container));
  }

  /**
   * Bind a constant static integer value to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant integer value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantInteger(String topic, IntSupplier getter) {
    return new ConstantBinding(topic, getter.getAsInt());
  }

  /**
   * Bind a integer value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the integer value.
   * @param getter The function to retrieve the integer value from the container.
   * @param setter The function to update the integer value in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindInteger(
      String topic, T container, ToIntFunction<T> getter, ObjIntConsumer<T> setter) {
    return new IntegerBinding(
        topic, () -> getter.applyAsInt(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static integer value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the integer value.
   * @param setter The function to update the integer value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticInteger(
      String topic, IntSupplier getter, IntConsumer setter) {
    return new IntegerBinding(topic, getter, setter);
  }

  /**
   * Bind a integer array to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the integer array.
   * @param getter The function to retrieve the integer array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindIntegerArray(
      String topic, T container, Function<T, int[]> getter) {
    return new IntegerArrayBinding(topic, () -> getter.apply(container));
  }

  /**
   * Bind a static integer array to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the integer array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticIntegerArray(String topic, Supplier<int[]> getter) {
    return new IntegerArrayBinding(topic, getter);
  }

  /**
   * Bind a constant integer array to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the integer array.
   * @param getter The function to retrieve the integer array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantIntegerArray(
      String topic, T container, Function<T, int[]> getter) {
    return new ConstantBinding(topic, getter.apply(container));
  }

  /**
   * Bind a constant static integer array to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant integer array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantIntegerArray(String topic, Supplier<int[]> getter) {
    return new ConstantBinding(topic, getter.get());
  }

  /**
   * Bind a integer array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the integer array.
   * @param getter The function to retrieve the integer array from the container.
   * @param setter The function to update the integer array in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindIntegerArray(
      String topic, T container, Function<T, int[]> getter, BiConsumer<T, int[]> setter) {
    return new IntegerArrayBinding(
        topic, () -> getter.apply(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static integer array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the integer array.
   * @param setter The function to update the integer array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticIntegerArray(
      String topic, Supplier<int[]> getter, Consumer<int[]> setter) {
    return new IntegerArrayBinding(topic, getter, setter);
  }

  /**
   * Bind a string value to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the string value.
   * @param getter The function to retrieve the string value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindString(
      String topic, T container, Function<T, String> getter) {
    return new StringBinding(topic, () -> getter.apply(container));
  }

  /**
   * Bind a static string value to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the string value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticString(String topic, Supplier<String> getter) {
    return new StringBinding(topic, getter);
  }

  /**
   * Bind a constant string value to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the string value.
   * @param getter The function to retrieve the string value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantString(
      String topic, T container, Function<T, String> getter) {
    return new ConstantBinding(topic, getter.apply(container));
  }

  /**
   * Bind a constant static string value to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant string value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantString(String topic, Supplier<String> getter) {
    return new ConstantBinding(topic, getter.get());
  }

  /**
   * Bind a string value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the string value.
   * @param getter The function to retrieve the string value from the container.
   * @param setter The function to update the string value in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindString(
      String topic, T container, Function<T, String> getter, BiConsumer<T, String> setter) {
    return new StringBinding(
        topic, () -> getter.apply(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static string value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the string value.
   * @param setter The function to update the string value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticString(
      String topic, Supplier<String> getter, Consumer<String> setter) {
    return new StringBinding(topic, getter, setter);
  }

  /**
   * Bind a string array to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the string array.
   * @param getter The function to retrieve the string array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindStringArray(
      String topic, T container, Function<T, String[]> getter) {
    return new StringArrayBinding(topic, () -> getter.apply(container));
  }

  /**
   * Bind a static string array to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the string array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticStringArray(String topic, Supplier<String[]> getter) {
    return new StringArrayBinding(topic, getter);
  }

  /**
   * Bind a constant string array to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the string array.
   * @param getter The function to retrieve the string array from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantStringArray(
      String topic, T container, Function<T, String[]> getter) {
    return new ConstantBinding(topic, getter.apply(container));
  }

  /**
   * Bind a constant static string array to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the constant string array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantStringArray(String topic, Supplier<String[]> getter) {
    return new ConstantBinding(topic, getter.get());
  }

  /**
   * Bind a string array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the string array.
   * @param getter The function to retrieve the string array from the container.
   * @param setter The function to update the string array in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindStringArray(
      String topic, T container, Function<T, String[]> getter, BiConsumer<T, String[]> setter) {
    return new StringArrayBinding(
        topic, () -> getter.apply(container), (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static string array to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the string array.
   * @param setter The function to update the string array.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticStringArray(
      String topic, Supplier<String[]> getter, Consumer<String[]> setter) {
    return new StringArrayBinding(topic, getter, setter);
  }

  /**
   * Bind a raw value to be published to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param typeString The type string for the raw value.
   * @param container The object containing the raw value.
   * @param getter The function to retrieve the raw value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindRaw(
      String topic, String typeString, T container, Function<T, byte[]> getter) {
    return new RawBinding(topic, typeString, () -> getter.apply(container));
  }

  /**
   * Bind a static raw value to be published to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param typeString The type string for the raw value.
   * @param getter The function to retrieve the raw value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticRaw(
      String topic, String typeString, Supplier<byte[]> getter) {
    return new RawBinding(topic, typeString, getter);
  }

  /**
   * Bind a constant raw value to a dashboard topic.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param typeString The type string for the raw value.
   * @param container The object containing the raw value.
   * @param getter The function to retrieve the raw value from the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindConstantRaw(
      String topic, String typeString, T container, Function<T, byte[]> getter) {
    return new ConstantBinding(topic, typeString, getter.apply(container));
  }

  /**
   * Bind a constant static raw value to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param typeString The type string for the raw value.
   * @param getter The function to retrieve the constant raw value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindConstantRaw(
      String topic, String typeString, Supplier<byte[]> getter) {
    return new ConstantBinding(topic, typeString, getter.get());
  }

  /**
   * Bind a raw value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param topic The dashboard topic to bind to.
   * @param typeString The type string for the raw value.
   * @param container The object containing the raw value.
   * @param getter The function to retrieve the raw value from the container.
   * @param setter The function to update the raw value in the container.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindRaw(
      String topic,
      String typeString,
      T container,
      Function<T, byte[]> getter,
      BiConsumer<T, byte[]> setter) {
    return new RawBinding(
        topic,
        typeString,
        () -> getter.apply(container),
        (value) -> setter.accept(container, value));
  }

  /**
   * Bind a static raw value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param topic The dashboard topic to bind to.
   * @param typeString The type string for the raw value.
   * @param getter The function to retrieve the raw value.
   * @param setter The function to update the raw value.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindStaticRaw(
      String topic, String typeString, Supplier<byte[]> getter, Consumer<byte[]> setter) {
    return new RawBinding(topic, typeString, getter, setter);
  }

  /**
   * Bind an enum value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <T> The type of the container object.
   * @param <E> The type of the enum.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the enum value.
   * @param getter The function to retrieve the enum value from the container.
   * @param setter The function to update the enum value in the container.
   * @return The bound DashboardData instance.
   */
  public static <T, E extends Enum<E>> DashboardData bindEnum(
      String topic, T container, Function<T, E> getter, BiConsumer<T, E> setter) {
    E defaultValue = getter.apply(container);
    var enumChooser = EnumChooser.fromDefault(defaultValue);

    enumChooser.onChange(
        v -> {
          if (v != null) {
            setter.accept(container, v);
          } else {
            setter.accept(container, defaultValue);
          }
        });

    return bindTunable(topic, enumChooser);
  }

  /**
   * Bind a static enum value to a dashboard topic that both publishes and subscribes to updates.
   *
   * @param <E> The type of the enum.
   * @param topic The dashboard topic to bind to.
   * @param getter The function to retrieve the enum value.
   * @param setter The function to update the enum value.
   * @return The bound DashboardData instance.
   */
  public static <E extends Enum<E>> DashboardData bindStaticEnum(
      String topic, Supplier<E> getter, Consumer<E> setter) {
    var enumChooser = EnumChooser.fromDefault(getter.get());

    enumChooser.onChange(v -> setter.accept(v));

    return bindTunable(topic, enumChooser);
  }

  /**
   * Bind a {@link PreferenceValue} to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param preferenceValue The PreferenceValue to bind.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindPreferenceValue(String topic, PreferenceValue preferenceValue) {
    return new PreferenceValueBinding(topic, preferenceValue);
  }

  /**
   * Bind a {@link ComplexTunable} to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param tunable The ComplexTunable to bind.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindTunable(String topic, ComplexTunable tunable) {
    return new ComplexTunableBinding(topic, tunable);
  }

  /**
   * Bind a {@link HttpCamera} to a dashboard topic.
   *
   * @param topic The dashboard topic to bind to.
   * @param camera The HttpCamera to bind.
   * @return The bound DashboardData instance.
   */
  public static DashboardData bindHttpCamera(String topic, HttpCamera camera) {
    return new HttpCameraBinding(topic, camera);
  }

  /**
   * Bind the data in a layout container described by a layout definition.
   *
   * @param <T> The type of the layout container.
   * @param topic The dashboard topic to bind to.
   * @param container The object containing the layout container value.
   * @param binder The function to bind the layout container value to the layout data.
   * @return The bound DashboardData instance.
   */
  public static <T> DashboardData bindLayout(
      String topic, T container, BiFunction<String, T, DashboardData[]> binder) {
    return new LayoutBinding(binder.apply(topic, container));
  }

  /**
   * Bind the data in a tab container described by a tab definition.
   *
   * @param <T> The type of the tab container.
   * @param title The tab title.
   * @param container The object containing the tab container value.
   * @param binder The function to bind the tab container value to the tab.
   * @return The bound DashboardData instance.
   */
  public static <T> TabBinding bindTab(
      String title, T container, BiFunction<String, T, DashboardData[]> binder) {
    return new TabBinding(binder.apply(title, container));
  }

  /**
   * Bind the data in an optional tab container described by a tab definition, only if the container
   * is present.
   *
   * @param <T> The type of the tab container.
   * @param title The tab title.
   * @param container The optional object containing the tab container value.
   * @param binder The function to bind the tab container value to the tab.
   * @return An Optional containing the bound DashboardData instance if the container is present, or
   *     an empty Optional if not.
   */
  public static <T> Optional<TabBinding> bindOptionalTab(
      String title, Optional<T> container, BiFunction<String, T, DashboardData[]> binder) {
    return container.map(c -> bindTab(title, c, binder));
  }

  private static final DashboardData INVALID_BINDING =
      new DashboardData() {
        @Override
        public void enable() {
          // No-op
        }

        @Override
        public void disable() {
          // No-op
        }
      };

  /**
   * Returns a no-op binding used when a dashboard binding cannot be created.
   *
   * <p>This is typically called from generated binding code when a binding definition is invalid or
   * otherwise cannot be bound. The returned {@link DashboardData} instance performs no actions when
   * enabled, disabled, updated, or closed.
   *
   * @return a shared, no-op {@link DashboardData} instance
   */
  public static DashboardData bindInvalid() {
    return INVALID_BINDING;
  }
}

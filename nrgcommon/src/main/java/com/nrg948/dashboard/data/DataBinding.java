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

/**
 * An abstract base class for data bindings that bind a publisher and/or subscriber to dashboard
 * data updates.
 */
abstract class DataBinding extends DashboardData {
  private int enabledCount = 0;

  /**
   * Enables the dashboard data binding.
   *
   * <p>This method is called by the {@link DashboardData#enable()} method when the binding is first
   * enabled. It is guaranteed only to be called if the binding was previously disabled.
   */
  protected abstract void enableSelf();

  /**
   * Disables the dashboard data binding.
   *
   * <p>This method is called by the {@link DashboardData#disable()} method when the binding is
   * disabled. It is guaranteed only to be called if the binding was previously enabled.
   */
  protected abstract void disableSelf();

  @Override
  public void enable() {
    if (enabledCount++ > 0) {
      return;
    }

    enableSelf();
  }

  @Override
  public void disable() {
    if (enabledCount <= 0) {
      throw new IllegalStateException(
          getClass().getSimpleName() + " disabled more times than it was enabled");
    }

    if (--enabledCount == 0) {
      disableSelf();
    }
  }
}

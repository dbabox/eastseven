/**
 * 
 */
package org.dongq.pattern.headfirst.observer;

/**
 * @author dongq
 * 
 */
public class CurrentConditionDisplay implements Observer, DisplayElement {

	private float temperature;
	private float humidity;
	@SuppressWarnings("unused")
	private Subject weatherData;

	public CurrentConditionDisplay(Subject weatherData) {
		this.weatherData = weatherData;
		weatherData.registerObserver(this);
	}
	
	@Override
	public void display() {
		System.out.println("Current conditions: " + temperature + "F degress and " + humidity + "% humidity");
	}

	@Override
	public void update(float temp, float humidity, float pressure) {
		this.temperature = temp;
		this.humidity = humidity;
		display();
	}

}

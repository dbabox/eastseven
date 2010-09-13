/**
 * 
 */
package org.dongq.pattern.headfirst.observer;

/**
 * @author dongq
 * 
 */
public interface Observer {

	public void update(float temp, float humidity, float pressure);
}

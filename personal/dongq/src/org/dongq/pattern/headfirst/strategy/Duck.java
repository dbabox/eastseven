/**
 * 
 */
package org.dongq.pattern.headfirst.strategy;

/**
 * @author dongq
 * 
 */
public abstract class Duck {

	private FlyBehavior flyBehavior;
	private QuackBehavior quackBehavior;

	public void swin() {

	}

	public abstract void display();

	public void performFly() {
		this.flyBehavior.fly();
	}

	public void performQuack() {
		this.quackBehavior.quack();
	}

	public void setFlyBehavior(FlyBehavior flyBehavior) {
		this.flyBehavior = flyBehavior;
	}

	public void setQuackBehavior(QuackBehavior quackBehavior) {
		this.quackBehavior = quackBehavior;
	}
}

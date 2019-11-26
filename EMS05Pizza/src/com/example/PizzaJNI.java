package com.example;

public class PizzaJNI {
	static {
		System.loadLibrary("pizzaJNI");
	}
	
	// Text LCD
	public native void on();
	public native void off();
	public native void initialize();
	public native void clear();
	public native void print1Line(String str);
	public native void print2Line(String str);
	
	// LED
	public native void on(char data);
	
	// Segment
	public native void SegOpen();
	public native void SegPrint(int num);
	public native void SegClose();
	
	// Dip SW
	public native void DipOpen();
	public native void DipClose();
	public native short get();
	
	// Piezo
	public native void PiOpen();
	public native void write(char data);
	public native void PiClose();
	
	// Dot
	public native void DotMatrixControl(String str);
	
	// Full
	public native void FLEDControl(int ledNum, int red, int green, int blue);
	
	// Keypad
	public native void KeyOpen();
	public native void KeyClose();
	public native void read(char buf);

}

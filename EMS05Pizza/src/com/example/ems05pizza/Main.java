package com.example.ems05pizza;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Dialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.PizzaJNI;

public class Main extends Activity {
	
	private PizzaJNI pizzaJNI = new PizzaJNI();
	
	// Server
	String serverIP = "192.168.0.18"; // server의 IP 주소
	int serverPort = 114; // server의 port 번호
	private Socket sock;
	BufferedReader sock_in;
	PrintWriter sock_out;
	
	// LED
	private char ledData = (char)0;
	
	// FullColor
	private int[] led_val;
	
	// Dot
	protected static final int DIALOG_SIMPLE_MESSAGE = 0;
	protected static final int DIALOG_ERROR_MESSAGE = 1;
	String result = new String();
	BackThread thread = new BackThread();
	boolean start = false, restart = false;
	boolean alive = true;
	private int speed = 20;
	String value;
	Handler handler = new Handler();

	Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8;
	private ImageButton [] pizzas = new ImageButton[8];
	Button btnR;
	
	// 총 주문가격
	int priceSum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.setPizzaImage();
		
		// FullColor
		led_val = new int[4];
		led_val[0] = 5;
		for(int i = 1 ; i < 4 ; i++)
		{
			led_val[i] = 0;
		}
		
		thread.setDaemon(true);
		thread.start();
		
		btn1 = (Button)findViewById(R.id.btn1);
		btn2 = (Button)findViewById(R.id.btn2);
		btn3 = (Button)findViewById(R.id.btn3);
		btn4 = (Button)findViewById(R.id.btn4);
		btn5 = (Button)findViewById(R.id.btn5);
		btn6 = (Button)findViewById(R.id.btn6);
		btn7 = (Button)findViewById(R.id.btn7);
		btn8 = (Button)findViewById(R.id.btn8);
		btnR = (Button)findViewById(R.id.btnResult);
		
		Thread worker = new Thread() {
			public void run() {
				try {
					sock = new Socket(serverIP, serverPort);	
					sock_out = new PrintWriter(sock.getOutputStream(), true);
					sock_in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}; // new Thread()
		worker.start();
			
		btn1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LCD
				final String str1 = "Bulgogi";
				final String str2 = "Pizza";
				pizzaJNI.clear();
				pizzaJNI.print1Line(str1);
				pizzaJNI.print2Line(str2);				
				
				// LED
				ledData=1;
				pizzaJNI.on(ledData);
				
				// Dot
				value = "Bulgogi Pizza!";
				
				int price=9000;
		
				if (start) {
					restart = true;
					start = false;
					
					// LCD , LED , 7-Seg , Dot Clear
					pizzaJNI.clear();
					ledData=0;
					pizzaJNI.on(ledData);
					int num=0;
					pizzaJNI.SegPrint(num);
					
					// Server Print
					String del = "Bulgogi Pizza Delete!";
					sock_out.println(del);
					
					// 주문하기에 삭제
					priceSum -= price;
					
					btn1.setText("담기");
				} else {
					start = true;
					restart = true;
					
					// Server Print
					sock_out.println(value);
					
					// 7-Segment
					int pizza_num=1;										
					pizzaJNI.SegPrint(pizza_num);
					
					// 주문하기에 추가
					priceSum += price;
					Toast.makeText(Main.this, "가격은 "+price+"원 입니다.",Toast.LENGTH_SHORT).show();
					
					btn1.setText("취소");
				}
				
				// LED , FullColor Clear
				pizzaJNI.on(ledData);
				led_val[1] = 0;
				led_val[2] = 0;
				led_val[3] = 0;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);
			}
			});

		btn2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LCD
				final String str1 = "Pepperoni";
				final String str2 = "Pizza";
				pizzaJNI.clear();
				pizzaJNI.print1Line(str1);
				pizzaJNI.print2Line(str2);				
				
				// LED
				ledData=2;
				pizzaJNI.on(ledData);
				
				// Dot
				value = "Pepperoni Pizza!";
				
				int price=8000;
				
				if (start) {
					restart = true;
					start = false;
					
					// LCD , LED , 7-Seg , Dot Clear
					pizzaJNI.clear();
					ledData=0;
					pizzaJNI.on(ledData);
					int num=0;
					pizzaJNI.SegPrint(num);
					
					// Server Print
					String del = "Pepperoni Pizza Delete!";
					sock_out.println(del);
					
					// 주문하기에 삭제
					priceSum -= price;
					
					btn2.setText("담기");
				} else {
					start = true;
					restart = true;
					
					// Server Print
					sock_out.println(value);
					
					// 7-Segment
					int pizza_num=2;
					pizzaJNI.SegPrint(pizza_num);
					
					// 주문하기에 추가
					priceSum += price;
					Toast.makeText(Main.this, "가격은 "+price+"원 입니다.",Toast.LENGTH_SHORT).show();
					
					btn2.setText("취소");
				}
				
				// LED , FullColor Clear
				pizzaJNI.on(ledData);
				led_val[1] = 0;
				led_val[2] = 0;
				led_val[3] = 0;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);
			}
			});
		
		btn3.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LCD
				final String str1 = "Cheese";
				final String str2 = "Pizza";
				pizzaJNI.clear();
				pizzaJNI.print1Line(str1);
				pizzaJNI.print2Line(str2);				
				
				// LED
				ledData=4;
				pizzaJNI.on(ledData);
				
				// Dot
				value = "Cheese Pizza!";
				
				int price=6000;
				
				if (start) {
					restart = true;
					start = false;
					
					// LCD , LED , 7-Seg , Dot Clear
					pizzaJNI.clear();
					ledData=0;
					pizzaJNI.on(ledData);
					int num=0;
					pizzaJNI.SegPrint(num);
					
					// Server Print
					String del = "Cheese Pizza Delete!";
					sock_out.println(del);
					
					// 주문하기에 삭제
					priceSum -= price;
					
					btn3.setText("담기");
				} else {
					start = true;
					restart = true;
					
					// Server Print
					sock_out.println(value);
					
					// 7-Segment
					int pizza_num=3;
					pizzaJNI.SegPrint(pizza_num);
					
					// 주문하기에 추가
					priceSum += price;
					Toast.makeText(Main.this, "가격은 "+price+"원 입니다.",Toast.LENGTH_SHORT).show();
					
					btn3.setText("취소");
				}
				
				// LED , FullColor Clear
				pizzaJNI.on(ledData);
				led_val[1] = 0;
				led_val[2] = 0;
				led_val[3] = 0;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);
			}
			});
		
		btn4.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LCD
				final String str1 = "Garlic Shrimp";
				final String str2 = "Pizza";
				pizzaJNI.clear();
				pizzaJNI.print1Line(str1);
				pizzaJNI.print2Line(str2);				
				
				// LED
				ledData=8;
				pizzaJNI.on(ledData);
				
				// Dot
				value = "Garlic Shrimp Pizza!";
				
				int price=12000;
				
				if (start) {
					restart = true;
					start = false;
					
					// LCD , LED , 7-Seg , Dot Clear
					pizzaJNI.clear();
					ledData=0;
					pizzaJNI.on(ledData);
					int num=0;
					pizzaJNI.SegPrint(num);
					
					// Server Print
					String del = "Garlic Shrimp Pizza Delete!";
					sock_out.println(del);
					
					// 주문하기에 삭제
					priceSum -= price;
					
					btn4.setText("담기");
				} else {
					start = true;
					restart = true;
					
					// Server Print
					sock_out.println(value);
					
					// 7-Segment
					int pizza_num=4;
					pizzaJNI.SegPrint(pizza_num);
					
					// 주문하기에 추가
					priceSum += price;
					Toast.makeText(Main.this, "가격은 "+price+"원 입니다.",Toast.LENGTH_SHORT).show();
					
					btn4.setText("취소");
				}
				
				// LED , FullColor Clear
				pizzaJNI.on(ledData);
				led_val[1] = 0;
				led_val[2] = 0;
				led_val[3] = 0;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);
			}
			});
		
		btn5.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LCD
				final String str1 = "Cheese King";
				final String str2 = "Pizza";
				pizzaJNI.clear();
				pizzaJNI.print1Line(str1);
				pizzaJNI.print2Line(str2);				
				
				// LED
				ledData=16;
				pizzaJNI.on(ledData);
				
				// Dot
				value = "Cheese King Pizza!";
				
				int price=13000;
				
				if (start) {
					restart = true;
					start = false;
					
					// LCD , LED , 7-Seg , Dot Clear
					pizzaJNI.clear();
					ledData=0;
					pizzaJNI.on(ledData);
					int num=0;
					pizzaJNI.SegPrint(num);
					
					// Server Print
					String del = "Cheese King Pizza Delete!";
					sock_out.println(del);
					
					// 주문하기에 삭제
					priceSum -= price;
					
					btn5.setText("담기");
				} else {
					start = true;
					restart = true;
					
					// Server Print
					sock_out.println(value);
					
					// 7-Segment
					int pizza_num=5;
					pizzaJNI.SegPrint(pizza_num);
					
					// 주문하기에 추가
					priceSum += price;
					Toast.makeText(Main.this, "가격은 "+price+"원 입니다.",Toast.LENGTH_SHORT).show();
					
					btn5.setText("취소");
				}
				
				// LED , FullColor Clear
				pizzaJNI.on(ledData);
				led_val[1] = 0;
				led_val[2] = 0;
				led_val[3] = 0;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);
			}
			});
		
		btn6.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LCD
				final String str1 = "Steak";
				final String str2 = "Pizza";
				pizzaJNI.clear();
				pizzaJNI.print1Line(str1);
				pizzaJNI.print2Line(str2);				
				
				// LED
				ledData=32;
				pizzaJNI.on(ledData);
				
				// Dot
				value = "Steak Pizza!";
				
				int price=10000;
				
				if (start) {
					restart = true;
					start = false;
					
					// LCD , LED , 7-Seg , Dot Clear
					pizzaJNI.clear();
					ledData=0;
					pizzaJNI.on(ledData);
					int num=0;
					pizzaJNI.SegPrint(num);
					
					// Server Print
					String del = "Steak Pizza Delete!";
					sock_out.println(del);
					
					// 주문하기에 삭제
					priceSum -= price;
					
					btn6.setText("담기");
				} else {
					start = true;
					restart = true;
					
					// Server Print
					sock_out.println(value);
					
					// 7-Segment
					int pizza_num=6;
					pizzaJNI.SegPrint(pizza_num);
					
					// 주문하기에 추가
					priceSum += price;
					Toast.makeText(Main.this, "가격은 "+price+"원 입니다.",Toast.LENGTH_SHORT).show();
					
					btn6.setText("취소");
				}
				
				// LED , FullColor Clear
				pizzaJNI.on(ledData);
				led_val[1] = 0;
				led_val[2] = 0;
				led_val[3] = 0;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);
			}
			});
		
		btn7.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LCD
				final String str1 = "Double Barbecue";
				final String str2 = "Pizza";
				pizzaJNI.clear();
				pizzaJNI.print1Line(str1);
				pizzaJNI.print2Line(str2);				
				
				// LED
				ledData=64;
				pizzaJNI.on(ledData);
				
				// Dot
				value = "Double Barbecue Pizza!";
				
				int price=13000;
				
				if (start) {
					restart = true;
					start = false;
					
					// LCD , LED , 7-Seg , Dot Clear
					pizzaJNI.clear();
					ledData=0;
					pizzaJNI.on(ledData);
					int num=0;
					pizzaJNI.SegPrint(num);
					
					// Server Print
					String del = "Double Barbecue Pizza Delete!";
					sock_out.println(del);
					
					// 주문하기에 삭제
					priceSum -= price;
					
					btn7.setText("담기");
				} else {
					start = true;
					restart = true;
					
					// Server Print
					sock_out.println(value);
					
					// 7-Segment
					int pizza_num=7;
					pizzaJNI.SegPrint(pizza_num);
					
					// 주문하기에 추가
					priceSum += price;
					Toast.makeText(Main.this, "가격은 "+price+"원 입니다.",Toast.LENGTH_SHORT).show();
					
					btn7.setText("취소");
				}
				
				// LED , FullColor Clear
				pizzaJNI.on(ledData);
				led_val[1] = 0;
				led_val[2] = 0;
				led_val[3] = 0;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);
			}
			});
		
		btn8.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LCD
				final String str1 = "Cheese Steak";
				final String str2 = "Pizza";
				pizzaJNI.clear();
				pizzaJNI.print1Line(str1);
				pizzaJNI.print2Line(str2);				
				
				// LED
				ledData=128;
				pizzaJNI.on(ledData);
				
				// Dot
				value = "Cheese Steak Pizza!";
				
				int price=14000;
				
				if (start) {
					restart = true;
					start = false;
					
					// LCD , LED , 7-Seg , Dot Clear
					pizzaJNI.clear();
					ledData=0;
					pizzaJNI.on(ledData);
					int num=0;
					pizzaJNI.SegPrint(num);
					
					// Server Print
					String del = "Cheese Steak Pizza Delete!";
					sock_out.println(del);
					
					// 주문하기에 삭제
					priceSum -= price;
					
					btn8.setText("담기");
				} else {
					start = true;
					restart = true;
					
					// Server Print
					sock_out.println(value);
					
					// 7-Segment
					int pizza_num=8;
					pizzaJNI.SegPrint(pizza_num);
					
					// 주문하기에 추가
					priceSum += price;
					Toast.makeText(Main.this, "가격은 "+price+"원 입니다.",Toast.LENGTH_SHORT).show();
					
					btn8.setText("취소");
				}
				
				// LED , FullColor Clear
				pizzaJNI.on(ledData);
				led_val[1] = 0;
				led_val[2] = 0;
				led_val[3] = 0;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);
			}
			});
		
		btnR.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// LCD
				final String str1 = "Order";
				final String str2 = "Pizza";
				pizzaJNI.clear();
				pizzaJNI.print1Line(str1);
				pizzaJNI.print2Line(str2);				
				
				// LED
				ledData=255;
				pizzaJNI.on(ledData);
				
				// Dot
				value = "ORDER !!!";
				
				if (start) {
					restart = true;
					start = false;
			
					// LCD , LED , Dot Clear
					pizzaJNI.clear();
					ledData=0;
					pizzaJNI.on(ledData);
					
					// Server Print
					String del = "ORDER DELETE !!!";
					sock_out.println(del);
					
					btnR.setText("주문 하기");
				} else {
					start = true;
					restart = true;
					
					Toast.makeText(Main.this, "총 가격은 "+priceSum+"원 입니다.",Toast.LENGTH_SHORT).show();
					
					// Server Print
					sock_out.println(value);
					
					btnR.setText("주문 취소");
				}
				
				// FullColor
				int res;
				res = 1 + (int)(Math.random() * 154);
				led_val[1] = res;
				res = 1 + (int)(Math.random() * 154);
				led_val[2] = res;
				res = 1 + (int)(Math.random() * 154);
				led_val[3] = res;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);
			}
			});
		
	}
	
	@Override
	protected void onResume() {
		pizzaJNI.initialize();
		pizzaJNI.SegOpen();
		pizzaJNI.PiOpen();
		super.onResume();
	}

	@Override
	protected void onPause() {
		pizzaJNI.off();
		pizzaJNI.SegClose();
		pizzaJNI.PiClose();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// 각 Image를 선언해주고, Tag를 주어 Piezo를 사용하기 위함
	private void setPizzaImage() { 
		pizzas[0] = (ImageButton)findViewById(R.id.pizza1);
		pizzas[0].setTag(new int[]{1});
		pizzas[1] = (ImageButton)findViewById(R.id.pizza2);
		pizzas[1].setTag(new int[]{2});
		pizzas[2] = (ImageButton)findViewById(R.id.pizza3);
		pizzas[2].setTag(new int[]{3});
		pizzas[3] = (ImageButton)findViewById(R.id.pizza4);
		pizzas[3].setTag(new int[]{4});
		pizzas[4] = (ImageButton)findViewById(R.id.pizza5);
		pizzas[4].setTag(new int[]{5});
		pizzas[5] = (ImageButton)findViewById(R.id.pizza6);
		pizzas[5].setTag(new int[]{6});
		pizzas[6] = (ImageButton)findViewById(R.id.pizza7);
		pizzas[6].setTag(new int[]{7});
		pizzas[7] = (ImageButton)findViewById(R.id.pizza8);
		pizzas[7].setTag(new int[]{17});
		
		// TouchListener 사용하여 각 Image Button 클릭 이벤트
		ImageTouchListener touch = new ImageTouchListener();
		for (int i = 0; i < 8; ++i)
			pizzas[i].setOnTouchListener(touch);
	}
	
	public class ImageTouchListener implements ImageButton.OnTouchListener {

		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
		ImageButton imageButton = (ImageButton) view;
		int [] tags = (int [])imageButton.getTag(); // Tag 가져옴
		
		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN: // 눌렀을 때
				// Piezo
				pizzaJNI.write((char)tags[0]);
				
				// FullColor
				int res;
				res = 1 + (int)(Math.random() * 154);
				led_val[1] = res;
				res = 1 + (int)(Math.random() * 154);
				led_val[2] = res;
				res = 1 + (int)(Math.random() * 154);
				led_val[3] = res;
				pizzaJNI.FLEDControl(led_val[0],led_val[1],led_val[2],led_val[3]);

				break;
		
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_MOVE: // 누르고 떼었을 때
				// Piezo Clear
				pizzaJNI.write( (char)0);
				break;
		}
		return true;
		}
	}
	
	// Dot
	class BackThread extends Thread {
		public void run() {
			while (alive) {
				if (!start) {
					// do nothing
				} else {
					// check string length
					if (value.length() > 50) {
						// show dialog
						handler.post(new Runnable() {
							public void run() {
								showDialog(DIALOG_SIMPLE_MESSAGE);
							}
						});
						start = false;
						continue;
					} else {
						int i, j, ch;
						char buf[] = new char[100];
						buf = value.toCharArray();
						result = "00000000000000000000";
						for (i = 0; i < value.length(); i++) {
							ch = Integer.valueOf(buf[i]);
							if (ch < 32 || ch > 126) {
								handler.post(new Runnable() {
									public void run() {
										showDialog(DIALOG_ERROR_MESSAGE);
									}
								});
								start = false;
								restart = false;
								break;
							} // if(ch < 32
							ch -= 0x20;
							// copy
							for (j = 0; j < 5; j++) {
								String str = new String();
								str = Integer.toHexString((font[ch][j]));
								if (str.length() < 2)
									result += "0";

								result += str;
							}
							result += "00";
						} // for (i = 0;
						result += "00000000000000000000";
						// print
						for (i = 0; i < (result.length() - 18) / 2; i++) {
							// speed control
							for (j = 0; j < speed; j++) {
								// thread control
								if (!start) {
									break; // stop display
								} else {
									pizzaJNI.DotMatrixControl(result
											.substring(2 * i, 2 * i + 20));
								}
							}
						}
					} // if ~ else ~ else ~
					pizzaJNI.DotMatrixControl("00000000000000000000");
				} // if (!start) ~ else ~
			}
		}
	}
	// Dot
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			start = false;
			alive = false;
			thread.interrupt();
		}
		return super.onKeyDown(keyCode, event);
	}
	// Dot
	// Dot
	@Override
	protected Dialog onCreateDialog(int id) {
	// TODO Auto-generated method stub
	Dialog d = new Dialog(Main.this);
	Window window = d.getWindow();
	window.setFlags(WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW,
	WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW);
	switch (id) {
	case DIALOG_SIMPLE_MESSAGE:
	d.setTitle("Max input length is 50.");
	d.show();
	return d;
	case DIALOG_ERROR_MESSAGE:
	d.setTitle("Unsupported character.");
	d.show();
	return d;
	} // switch (id)
	return super.onCreateDialog(id);
	} // protected Dialog onCreateDialog(int id)
	// Dot
	public int font[][] = { /* 5x7 ASCII character font */
			{ 0x00, 0x00, 0x00, 0x00, 0x00 }, /* 0x20 space */
			{ 0x00, 0x00, 0x4f, 0x00, 0x00 }, /* 0x21 ! */
			{ 0x00, 0x07, 0x00, 0x07, 0x00 }, /* 0x22 " */
			{ 0x14, 0x7f, 0x14, 0x7f, 0x14 }, /* 0x23 # */
			{ 0x24, 0x2a, 0x7f, 0x2a, 0x12 }, /* 0x24 $ */
			{ 0x23, 0x13, 0x08, 0x64, 0x62 }, /* 0x25 % */
			{ 0x36, 0x49, 0x55, 0x22, 0x50 }, /* 0x26 & */
			{ 0x00, 0x05, 0x03, 0x00, 0x00 }, /* 0x27 ' */
			{ 0x00, 0x1c, 0x22, 0x41, 0x00 }, /* 0x28 ( */
			{ 0x00, 0x41, 0x22, 0x1c, 0x00 }, /* 0x29 ) */
			{ 0x14, 0x08, 0x3e, 0x08, 0x14 }, /* 0x2a * */
			{ 0x08, 0x08, 0x3e, 0x08, 0x08 }, /* 0x2b + */
			{ 0x00, 0x50, 0x30, 0x00, 0x00 }, /* 0x2c , */
			{ 0x08, 0x08, 0x08, 0x08, 0x08 }, /* 0x2d - */
			{ 0x00, 0x60, 0x60, 0x00, 0x00 }, /* 0x2e . */
			{ 0x20, 0x10, 0x08, 0x04, 0x02 }, /* 0x2f / */
			{ 0x3e, 0x51, 0x49, 0x45, 0x3e }, /* 0x30 0 */
			{ 0x00, 0x42, 0x7f, 0x40, 0x00 }, /* 0x31 1 */
			{ 0x42, 0x61, 0x51, 0x49, 0x46 }, /* 0x32 2 */
			{ 0x21, 0x41, 0x45, 0x4b, 0x31 }, /* 0x33 3 */
			{ 0x18, 0x14, 0x12, 0x7f, 0x10 }, /* 0x34 4 */
			{ 0x27, 0x45, 0x45, 0x45, 0x39 }, /* 0x35 5 */
			{ 0x3c, 0x4a, 0x49, 0x49, 0x30 }, /* 0x36 6 */
			{ 0x01, 0x71, 0x09, 0x05, 0x03 }, /* 0x37 7 */
			{ 0x36, 0x49, 0x49, 0x49, 0x36 }, /* 0x38 8 */
			{ 0x06, 0x49, 0x49, 0x29, 0x1e }, /* 0x39 9 */
			{ 0x00, 0x36, 0x36, 0x00, 0x00 }, /* 0x3a : */
			{ 0x00, 0x56, 0x36, 0x00, 0x00 }, /* 0x3b ; */
			{ 0x08, 0x14, 0x22, 0x41, 0x00 }, /* 0x3c < */
			{ 0x14, 0x14, 0x14, 0x14, 0x14 }, /* 0x3d = */
			{ 0x00, 0x41, 0x22, 0x14, 0x08 }, /* 0x3e > */
			{ 0x02, 0x01, 0x51, 0x09, 0x06 }, /* 0x3f ? */
			{ 0x32, 0x49, 0x79, 0x41, 0x3e }, /* 0x40 @ */
			{ 0x7e, 0x11, 0x11, 0x11, 0x7e }, /* 0x41 A */
			{ 0x7f, 0x49, 0x49, 0x49, 0x36 }, /* 0x42 B */
			{ 0x3e, 0x41, 0x41, 0x41, 0x22 }, /* 0x43 C */
			{ 0x7f, 0x41, 0x41, 0x22, 0x1c }, /* 0x44 D */
			{ 0x7f, 0x49, 0x49, 0x49, 0x41 }, /* 0x45 E */
			{ 0x7f, 0x09, 0x09, 0x09, 0x01 }, /* 0x46 F */
			{ 0x3e, 0x41, 0x49, 0x49, 0x7a }, /* 0x47 G */
			{ 0x7f, 0x08, 0x08, 0x08, 0x7f }, /* 0x48 H */
			{ 0x00, 0x41, 0x7f, 0x41, 0x00 }, /* 0x49 I */
			{ 0x20, 0x40, 0x41, 0x3f, 0x01 }, /* 0x4a J */
			{ 0x7f, 0x08, 0x14, 0x22, 0x41 }, /* 0x4b K */
			{ 0x7f, 0x40, 0x40, 0x40, 0x40 }, /* 0x4c L */
			{ 0x7f, 0x02, 0x0c, 0x02, 0x7f }, /* 0x4d M */
			{ 0x7f, 0x04, 0x08, 0x10, 0x7f }, /* 0x4e N */
			{ 0x3e, 0x41, 0x41, 0x41, 0x3e }, /* 0x4f O */
			{ 0x7f, 0x09, 0x09, 0x09, 0x06 }, /* 0x50 P */
			{ 0x3e, 0x41, 0x51, 0x21, 0x5e }, /* 0x51 Q */
			{ 0x7f, 0x09, 0x19, 0x29, 0x46 }, /* 0x52 R */
			{ 0x26, 0x49, 0x49, 0x49, 0x32 }, /* 0x53 S */
			{ 0x01, 0x01, 0x7f, 0x01, 0x01 }, /* 0x54 T */
			{ 0x3f, 0x40, 0x40, 0x40, 0x3f }, /* 0x55 U */
			{ 0x1f, 0x20, 0x40, 0x20, 0x1f }, /* 0x56 V */
			{ 0x3f, 0x40, 0x38, 0x40, 0x3f }, /* 0x57 W */
			{ 0x63, 0x14, 0x08, 0x14, 0x63 }, /* 0x58 X */
			{ 0x07, 0x08, 0x70, 0x08, 0x07 }, /* 0x59 Y */
			{ 0x61, 0x51, 0x49, 0x45, 0x43 }, /* 0x5a Z */
			{ 0x00, 0x7f, 0x41, 0x41, 0x00 }, /* 0x5b [ */
			{ 0x02, 0x04, 0x08, 0x10, 0x20 }, /* 0x5c \ */
			{ 0x00, 0x41, 0x41, 0x7f, 0x00 }, /* 0x5d ] */
			{ 0x04, 0x02, 0x01, 0x02, 0x04 }, /* 0x5e ^ */
			{ 0x40, 0x40, 0x40, 0x40, 0x40 }, /* 0x5f _ */
			{ 0x00, 0x01, 0x02, 0x04, 0x00 }, /* 0x60 ` */
			{ 0x20, 0x54, 0x54, 0x54, 0x78 }, /* 0x61 a */
			{ 0x7f, 0x48, 0x44, 0x44, 0x38 }, /* 0x62 b */
			{ 0x38, 0x44, 0x44, 0x44, 0x20 }, /* 0x63 c */
			{ 0x38, 0x44, 0x44, 0x48, 0x7f }, /* 0x64 d */
			{ 0x38, 0x54, 0x54, 0x54, 0x18 }, /* 0x65 e */
			{ 0x08, 0x7e, 0x09, 0x01, 0x02 }, /* 0x66 f */
			{ 0x0c, 0x52, 0x52, 0x52, 0x3e }, /* 0x67 g */
			{ 0x7f, 0x08, 0x04, 0x04, 0x78 }, /* 0x68 h */
			{ 0x00, 0x04, 0x7d, 0x00, 0x00 }, /* 0x69 i */
			{ 0x20, 0x40, 0x44, 0x3d, 0x00 }, /* 0x6a j */
			{ 0x7f, 0x10, 0x28, 0x44, 0x00 }, /* 0x6b k */
			{ 0x00, 0x41, 0x7f, 0x40, 0x00 }, /* 0x6c l */
			{ 0x7c, 0x04, 0x18, 0x04, 0x7c }, /* 0x6d m */
			{ 0x7c, 0x08, 0x04, 0x04, 0x78 }, /* 0x6e n */
			{ 0x38, 0x44, 0x44, 0x44, 0x38 }, /* 0x6f o */
			{ 0x7c, 0x14, 0x14, 0x14, 0x08 }, /* 0x70 p */
			{ 0x08, 0x14, 0x14, 0x18, 0x7c }, /* 0x71 q */
			{ 0x7c, 0x08, 0x04, 0x04, 0x08 }, /* 0x72 r */
			{ 0x48, 0x54, 0x54, 0x54, 0x20 }, /* 0x73 s */
			{ 0x04, 0x3f, 0x44, 0x40, 0x20 }, /* 0x74 t */
			{ 0x3c, 0x40, 0x40, 0x20, 0x7c }, /* 0x75 u */
			{ 0x1c, 0x20, 0x40, 0x20, 0x1c }, /* 0x76 v */
			{ 0x3c, 0x40, 0x30, 0x40, 0x3c }, /* 0x77 w */
			{ 0x44, 0x28, 0x10, 0x28, 0x44 }, /* 0x78 x */
			{ 0x0c, 0x50, 0x50, 0x50, 0x3c }, /* 0x79 y */
			{ 0x44, 0x64, 0x54, 0x4c, 0x44 }, /* 0x7a z */
			{ 0x00, 0x08, 0x36, 0x41, 0x00 }, /* 0x7b { */
			{ 0x00, 0x00, 0x77, 0x00, 0x00 }, /* 0x7c | */
			{ 0x00, 0x41, 0x36, 0x08, 0x00 }, /* 0x7d } */
			{ 0x08, 0x04, 0x08, 0x10, 0x08 } }; /* 0x7e ~ */

}

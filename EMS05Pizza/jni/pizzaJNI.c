#include <jni.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <time.h>
#include <math.h>
#include <ctype.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>

#include "textlcd.h"

static int Lcdfd;
static int Segfd;
static int Dipfd;
static int Pifd;
static int Keyfd;

#define FULL_LED1 9
#define FULL_LED2 8
#define FULL_LED3 7
#define FULL_LED4 6
#define ALL_LED 5

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_on__
  (JNIEnv * env, jobject obj){
	if(Lcdfd == 0) Lcdfd = open("/dev/fpga_textlcd", O_WRONLY);
		assert(Lcdfd > 0);
		ioctl(Lcdfd, TEXTLCD_ON);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_off
  (JNIEnv * env, jobject obj){
	if(Lcdfd) {
			ioctl(Lcdfd, TEXTLCD_OFF);
			close(Lcdfd);
		}
	Lcdfd = 0;
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_initialize
  (JNIEnv * env, jobject obj){
	if(Lcdfd ==0) Lcdfd = open("/dev/fpga_textlcd", O_WRONLY);
		assert(Lcdfd > 0);
		ioctl(Lcdfd, TEXTLCD_INIT);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_clear
  (JNIEnv * env, jobject obj){
	ioctl(Lcdfd, TEXTLCD_CLEAR);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_print1Line
  (JNIEnv * env, jobject obj, jstring msg){
	const char *str;
		if(Lcdfd) {
			str = (*env)->GetStringUTFChars(env, msg, 0);
			ioctl(Lcdfd, TEXTLCD_LINE1);
			write(Lcdfd, str, strlen(str));
			(*env)->ReleaseStringUTFChars(env, msg, str);
		}
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_print2Line
  (JNIEnv * env, jobject obj, jstring msg){
	const char *str;
		if(Lcdfd) {
			str = (*env)->GetStringUTFChars(env, msg, 0);
			ioctl(Lcdfd, TEXTLCD_LINE2);
			write(Lcdfd, str, strlen(str));
			(*env)->ReleaseStringUTFChars(env, msg, str);
		}
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_on__C
  (JNIEnv * env, jobject obj, jchar data){
	int Ledfd;

	Ledfd = open("/dev/fpga_led", O_WRONLY);
	assert(Ledfd != 0);

	write(Ledfd, &data, 1);
	close(Ledfd);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_SegOpen
  (JNIEnv * env, jobject obj){
	Segfd = open("/dev/fpga_segment", O_WRONLY);
	assert(Segfd != -1);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_SegPrint
  (JNIEnv * env, jobject obj, jint num){
	char buf[7];
	sprintf(buf, "%06d", num);
	write(Segfd, buf, 6);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_SegClose
  (JNIEnv * env, jobject obj){
	close(Segfd);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_DipOpen
  (JNIEnv * env, jobject obj){
	Dipfd = open("/dev/fpga_dipsw", O_RDONLY);
	assert(Dipfd != 0);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_DipClose
  (JNIEnv * env, jobject obj){
	close(Dipfd);
}

JNIEXPORT jshort JNICALL Java_com_example_PizzaJNI_get
  (JNIEnv * env, jobject obj){
	short int re;
	read(Dipfd, &re, 2);
	return re;
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_PiOpen
  (JNIEnv * env, jobject obj){
	Pifd = open("/dev/fpga_piezo", O_WRONLY);
	assert(fd != -1);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_write
  (JNIEnv * env, jobject obj, jchar data){
	write(Pifd, &data, 1);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_PiClose
  (JNIEnv * env, jobject obj){
	close(Pifd);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_DotMatrixControl
  (JNIEnv* env, jobject thiz, jstring str){
	const char *pStr;
	int Dotfd, len;

	pStr = (*env)->GetStringUTFChars(env, str, 0);
	len = (*env)->GetStringLength(env, str);

	Dotfd = open("/dev/fpga_dotmatrix", O_RDWR | O_SYNC);

	write(Dotfd, pStr, len);
	close(Dotfd);

	(*env)->ReleaseStringUTFChars(env, str, pStr);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_FLEDControl
  (JNIEnv* env, jobject thiz, jint led_num, jint val1, jint val2, jint val3){
	int Fullfd,ret;
	char buf[3];

	Fullfd = open("/dev/fpga_fullcolorled", O_WRONLY);
	if (Fullfd < 0)
	{
		exit(-1);
	}
	ret = (int)led_num;
	switch(ret)
	{
		case FULL_LED1:
		ioctl(Fullfd,FULL_LED1);
		break;
		case FULL_LED2:
		ioctl(Fullfd,FULL_LED2);
		break;
		case FULL_LED3:
		ioctl(Fullfd,FULL_LED3);
		break;
		case FULL_LED4:
		ioctl(Fullfd,FULL_LED4);
		break;
		case ALL_LED:
		ioctl(Fullfd,ALL_LED);
		break;
	}
	buf[0] = val1;
	buf[1] = val2;
	buf[2] = val3;

	write(Fullfd,buf,3);
	close(Fullfd);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_KeyOpen
  (JNIEnv * env, jobject obj){
	Keyfd = open("/dev/ems_dotmatrix", O_WRONLY);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_KeyClose
  (JNIEnv * env, jobject obj){
	close(Keyfd);
}

JNIEXPORT void JNICALL Java_com_example_PizzaJNI_read
  (JNIEnv * env, jobject obj, jchar buf){
	read(Keyfd,buf,20);
}


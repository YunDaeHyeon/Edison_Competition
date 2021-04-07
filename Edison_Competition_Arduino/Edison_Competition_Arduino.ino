#include <SoftwareSerial.h>
#include <Adafruit_NeoPixel.h>

#define BT_RXD 8
#define BT_TXD 7

#define PIN_NEOPIXEL 9 //네오픽셀 핀번호
#define NUMPIXELS 12// led 숫자
#define BRIGHTNESS 255//밝기

Adafruit_NeoPixel neopixel = Adafruit_NeoPixel(NUMPIXELS, PIN_NEOPIXEL, NEO_GRBW + NEO_KHZ800);
SoftwareSerial bDetecttooth(BT_RXD, BT_TXD);

uint16_t i, j;
  
int S0=10,S1=11,S2=12,S3=13;
int out=2;
int frequency = 0;

String str;

unsigned long currentTime;
boolean state;

bool PowerCount = false;
bool DetectCount = false;
bool AutoCount = true;

uint32_t color;
uint32_t DetectColor;

void neopixelAllCtrl(uint32_t color) {
  for (int pixel = 0; pixel < NUMPIXELS; pixel++) {
    neopixel.setPixelColor(pixel, color);
  }
  neopixel.setBrightness(BRIGHTNESS);
  neopixel.show();
}
  
void setup() {
  Serial.begin(115200);
  pinMode(S0, OUTPUT);  //set pin out
  pinMode(S1, OUTPUT);
  pinMode(S2, OUTPUT);
  pinMode(S3, OUTPUT);
  pinMode(out, INPUT);
  digitalWrite(S0,HIGH);
  digitalWrite(S1,LOW);


  neopixel.begin();                                        //  Neopixel 제어를 시작
  neopixel.clear();
  neopixel.show();
  bDetecttooth.begin(115200);
}

void loop() {
  if (bDetecttooth.available()) {
//    unsigned char cmd = bDetecttooth.read();
//    String str;
//    str = cmd;
    str = bDetecttooth.readStringUntil('x');
    int first = str.indexOf(',');
    int second = str.indexOf(',',first+1);
    int nlength = str.length();
    int r = str.substring(0,first).toInt();
    int g = str.substring(first+1,second).toInt();
    int b = str.substring(second+1,nlength).toInt();
    color = neopixel.Color(r, g, b);
    neopixelAllCtrl(color);
    if(str == "PowerOn") { //전원버튼
      neopixel.clear();
      AutoCount = false;
    }
    
    if(str == "DetectOn"){
          digitalWrite(S2,LOW);
          digitalWrite(S3,LOW);
          frequency = pulseIn(out, LOW);
          Serial.print("R= ");
          Serial.print(frequency);
          Serial.print("  ");
          int rDetect = frequency;
          digitalWrite(S2,HIGH);
          digitalWrite(S3,HIGH);
          frequency = pulseIn(out, LOW);
          Serial.print("G= ");
          Serial.print(frequency);
          Serial.print("  ");
          int gDetect = frequency;
          digitalWrite(S2,LOW);
          digitalWrite(S3,HIGH);
          frequency = pulseIn(out, LOW);
          Serial.print("B= ");
          Serial.print(frequency);
          Serial.println("  ");
          int bDetect = frequency;
          if (rDetect >= 0 && rDetect <= 15) rDetect = 0;
          if (rDetect >= 16 && rDetect <= 31)rDetect = 16;
          if (rDetect >= 32 && rDetect <= 47) rDetect = 32;
          if (rDetect >= 48 && rDetect <= 63) rDetect = 48;
          if (rDetect >= 64 && rDetect <= 79) rDetect = 64;
          if (rDetect >= 80 && rDetect <= 95) rDetect = 80;
          if (rDetect >= 96 && rDetect <= 111) rDetect = 96;
          if (rDetect >= 112 && rDetect <= 127) rDetect = 112;
          if (rDetect >= 128 && rDetect <= 143)rDetect = 128;
          if (rDetect >= 144 && rDetect <= 159)rDetect = 144;
          if (rDetect >= 160 && rDetect <= 175)rDetect = 160;
          if (rDetect >= 176 && rDetect <= 191)rDetect = 176;
          if (rDetect >= 192 && rDetect <= 207)rDetect = 192;
          if (rDetect >= 208 && rDetect <= 223)rDetect = 208;
          if (rDetect >= 224 && rDetect <= 239)rDetect = 224;
          if (rDetect >= 240 && rDetect <= 255)rDetect = 240;

          if (gDetect >= 0 && gDetect <= 15) gDetect = 0;
          if (gDetect >= 16 && gDetect <= 31)gDetect = 16;
          if (gDetect >= 32 && gDetect <= 47) gDetect = 32;
          if (gDetect >= 48 && gDetect <= 63) gDetect = 48;
          if (gDetect >= 64 && gDetect <= 79) gDetect = 64;
          if (gDetect >= 80 && gDetect <= 95) gDetect = 80;
          if (gDetect >= 96 && gDetect <= 111) gDetect = 96;
          if (gDetect >= 112 && gDetect <= 127) gDetect = 112;
          if (gDetect >= 128 && gDetect <= 143)gDetect = 128;
          if (gDetect >= 144 && gDetect <= 159)gDetect = 144;
          if (gDetect >= 160 && gDetect <= 175)gDetect = 160;
          if (gDetect >= 176 && gDetect <= 191)gDetect = 176;
          if (gDetect >= 192 && gDetect <= 207)gDetect = 192;
          if (gDetect >= 208 && gDetect <= 223)gDetect = 208;
          if (gDetect >= 224 && gDetect <= 239)gDetect = 224;
          if (gDetect >= 240 && gDetect <= 255)gDetect = 240;
          
          if (bDetect >= 0 && bDetect <= 15) bDetect = 0;
          if (bDetect >= 16 && bDetect <= 31)bDetect = 16;
          if (bDetect >= 32 && bDetect <= 47) bDetect = 32;
          if (bDetect >= 48 && bDetect <= 63) bDetect = 48;
          if (bDetect >= 64 && bDetect <= 79) bDetect = 64;
          if (bDetect >= 80 && bDetect <= 95) bDetect = 80;
          if (bDetect >= 96 && bDetect <= 111) bDetect = 96;
          if (bDetect >= 112 && bDetect <= 127) bDetect = 112;
          if (bDetect >= 128 && bDetect <= 143)bDetect = 128;
          if (bDetect >= 144 && bDetect <= 159)bDetect = 144;
          if (bDetect >= 160 && bDetect <= 175)bDetect = 160;
          if (bDetect >= 176 && bDetect <= 191)bDetect = 176;
          if (bDetect >= 192 && bDetect <= 207)bDetect = 192;
          if (bDetect >= 208 && bDetect <= 223)bDetect = 208;
          if (bDetect >= 224 && bDetect <= 239)bDetect = 224;
          if (bDetect >= 240 && bDetect <= 255)bDetect = 240;

          DetectColor = neopixel.Color(rDetect,gDetect,bDetect);
          neopixelAllCtrl(DetectColor);
          delay(100);
    }
//    Serial.println(r);
//    Serial.println(g);
//    Serial.println(b);
  }
}

void rainbow(String cmd) {
  uint16_t i, j;
  while(str==cmd){
    if(millis() - currentTime > 20){
      currentTime = millis();
    }
    for(j=0; j<256; j++) {
          for(i=0; i<neopixel.numPixels(); i++) {
              neopixel.setPixelColor(i, Wheel((i+j) & 255));
     }
    neopixel.setBrightness(BRIGHTNESS);
    neopixel.show();
   }
  }
}

void rainbowCycle(uint8_t wait) {
  uint16_t i, j;
   for(j=0; j<256*5; j++) { // 5번 사이클
    for(i=0; i< neopixel.numPixels(); i++) {
      neopixel.setPixelColor(i, Wheel(((i * 256 / neopixel.numPixels()) + j) & 255));
    }
    neopixel.setBrightness(BRIGHTNESS);
    neopixel.show();
    }
}

uint32_t Wheel(byte WheelPos) {
  WheelPos = 255 - WheelPos;
  if(WheelPos < 85) {
    return neopixel.Color(255 - WheelPos * 3, 0, WheelPos * 3);
  }
  if(WheelPos < 170) {
    WheelPos -= 85;
    return neopixel.Color(0, WheelPos * 3, 255 - WheelPos * 3);
  }
  WheelPos -= 170;
  return neopixel.Color(WheelPos * 3, 255 - WheelPos * 3, 0);
}

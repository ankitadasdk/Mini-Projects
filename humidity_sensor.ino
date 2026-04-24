#include <Wire.h> 
#include <LiquidCrystal_I2C.h> 
#include <dht.h> 

#define dht_apin A0 // Make sure your sensor's data wire is now in A0!

LiquidCrystal_I2C lcd(0x27, 16, 2); 
dht DHT; 

void setup() {
  lcd.init();       
  lcd.backlight();
  Serial.begin(9600);
}

void loop() {
  int chk = DHT.read11(dht_apin); 

  lcd.clear(); 
  lcd.setCursor(0,0);
  lcd.print("Humid: ");
  lcd.print((int)DHT.humidity);
  lcd.print("%");

  lcd.setCursor(0,1);
  lcd.print("Temp:  ");
  lcd.print((int)DHT.temperature);
  lcd.print("C");

  // This lets us see the data on the laptop too
  Serial.print("H: ");
  Serial.print(DHT.humidity);
  Serial.print("% | T: ");
  Serial.print(DHT.temperature);
  Serial.println("C");

  delay(2000); 
}

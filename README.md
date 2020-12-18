# Root-e
![KakaoTalk_20200902_225920841](https://user-images.githubusercontent.com/48232366/102617573-be562980-417c-11eb-9f57-c13e28605437.png)

## Rpi Zero W
1. ADC
    - spi통신을 통해 MCP3208로부터 ADC값을 읽음.
2. Bluetooth
    - pybluez 모듈을 사용함.
3. DHT
    - Adafruit의 라이브러리를 사용함.
    - DHT22를 사용함.
    - https://github.com/adafruit/Adafruit_Python_DHT
4. Firebase
    - Pyrebase 모듈을 사용함.
    - 참고 : https://m.blog.naver.com/PostView.nhn?blogId=cosmosjs&logNo=220983410440&proxyReferer=https:%2F%2Fwww.google.com%2F
5. GPIO
    - gpiozero 모듈 사용함.
    - https://gpiozero.readthedocs.io/en/stable/
6. LCD
    - 1602 CLCD를 이용함.
    - eleparts의 라이브러리를 사용함.
    - https://github.com/eleparts/RPi_I2C_LCD_driver
7. WI-FI
    - 쉘스크립트를 통해 와이파이 정보를 저장하게끔 함.

# Result
1. Root-e
    - ![KakaoTalk_20200912_162500246](https://user-images.githubusercontent.com/48232366/101913890-f56d8d80-3c06-11eb-9230-f50a59b76150.jpg)

2. PCB
    - ![KakaoTalk_20200912_144844592](https://user-images.githubusercontent.com/48232366/102617401-7931f780-417c-11eb-9e48-f8d353f17a1e.jpg)
    - ![KakaoTalk_20200912_150141462](https://user-images.githubusercontent.com/48232366/102617404-7a632480-417c-11eb-86a2-cc9c0aa1b068.jpg)

3. APP
    - ![KakaoTalk_20200914_172419609](https://user-images.githubusercontent.com/48232366/102617945-4a685100-417d-11eb-9b5d-d014942bc401.jpg)
    - ![KakaoTalk_20200912_155906167_02](https://user-images.githubusercontent.com/48232366/102617949-4b997e00-417d-11eb-8bcf-b6fca208295e.jpg)
    - ![KakaoTalk_20200912_155906167_01](https://user-images.githubusercontent.com/48232366/102617952-4c321480-417d-11eb-9bd2-e01a1d4b84df.jpg)

4. Test lettuce GIF
    - ![식물수정1](https://user-images.githubusercontent.com/48232366/101914079-2e0d6700-3c07-11eb-9b9c-7dbb16eb7abb.gif)

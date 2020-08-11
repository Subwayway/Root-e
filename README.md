# Root-e
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

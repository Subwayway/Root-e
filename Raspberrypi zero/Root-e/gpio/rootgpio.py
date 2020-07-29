from gpiozero import Button, PWMLED, LED
import time

button1 = Button(12)
button2 = Button(16)
button3 = Button(20)
button4 = Button(21)


led = LED(26)
motor = LED(13)

def led_on():
    led.on()

def led_off():
    led.off()

def motor_on():
    motor.on()

def motor_off():
    motor.off()

def gpio_off():
    led.off()
    motor.off()
    led.close()
    motor.close()

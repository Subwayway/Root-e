from gpiozero import Button, PWMLED, LED
import time

button1 = Button(12)
button2 = Button(16)
button3 = Button(20)
button4 = Button(21)

led_pwm = PWMLED(19)
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

# 0~1 (0~100%)
def led_pwmset(i):
    led_pwm.value=i

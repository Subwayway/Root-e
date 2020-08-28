from gpiozero import Button, PWMLED, LED
import time

# back
button1 = Button(21)
# next
button2 = Button(20)
# select
button3 = Button(16)
# cancel
button4 = Button(12)

led = LED(26)
spotlight = LED(7)
motor = LED(13)

def led_on():
    led.on()

def led_off():
    led.off()

def spotlight_on():
    spotlight.on()

def spotlight_off():
    spotlight.off()

def motor_on():
    motor.on()

def motor_off():
    motor.off()

def gpio_off():
    led.off()
    motor.off()
    led.close()
    motor.close()

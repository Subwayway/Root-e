import RPi.GPIO as GPIO
import time

def gpioset():
    GPIO.setmode(GPIO.BCM)
    #led, motor output pin
    GPIO.setup(19, GPIO.OUT)
    GPIO.setup(26, GPIO.OUT)

    #switch pull up set
    GPIO.setup(12, GPIO.IN, pull_up_down = GPIO.PUD_UP)
    GPIO.setup(16, GPIO.IN, pull_up_down = GPIO.PUD_UP)
    GPIO.setup(20, GPIO.IN, pull_up_down = GPIO.PUD_UP)
    GPIO.setup(21, GPIO.IN, pull_up_down = GPIO.PUD_UP)

def motor1(x):
    if x:
        GPIO.output(19, GPIO.HIGH)
    else:
        GPIO.output(19, GPIO.LOW)

def motor2(x):
    if x:
        GPIO.output(26, GPIO.HIGH)
    else:
        GPIO.output(26, GPIO.LOW)

def led(x):
    if x:
        GPIO.output(19, GPIO.HIGH)
    else:
        GPIO.output(19, GPIO.LOW)

# def led_pwm(p):
#     pwm = GPIO.PWM(19, 50)
#     pwm.start(0)
#
#     pwm.ChangeDutyCycle(p)

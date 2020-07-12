from gpiozero import Button, PWMLED
import time

button1 = Button(12)
button2 = Button(16)
button3 = Button(20)
button4 = Button(21)

led = PWMLED(19)

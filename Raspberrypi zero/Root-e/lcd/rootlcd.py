import RPi.GPIO as GPIO
from . import RPi_I2C_driver
import time

lcd = RPi_I2C_driver.lcd(0x27)

#display init screen(team name, now time)
def lcd_init():
    lcd.clear()
    ac_print("Root-e","Team.Lambda")
    time.sleep(0.3)

    #display now time
    lcd.clear()
    now = time.localtime()

    ac_print(time.strftime('%Y-%m-%d', now), time.strftime('%I:%M:%S', now))
    time.sleep(0.3)

#json find screen
def lcd_json():
    lcd.clear()
    lcd.print("Find setting file!")
    for i in range(40) :
        # scroll one position left:
        lcd.scrollDisplayLeft()
        # wait a bit:
        time.sleep(0.15)

#auto center cursor print
def ac_print(x, y=' '):
    lcd.clear()
    len_buff = len(x)
    lcd.setCursor(int((16-len_buff)/2),0)
    lcd.print(x)
    len_buff = len(y)
    lcd.setCursor(int((16-len_buff)/2),1)
    lcd.print(y)


#menu display
def display(i,j,k):
    if (j=='none')&(k=='none'):
        menu('MENU',i)
    elif (k=='none'):
        menu(i,j)
    elif (i=='Select Plant')&(k!='none'):
        yesno(j)
    elif (i=='Custom Plant')&(k!='none'):
        new_value(j,k)
    elif (i=='Setting')&(k!='none'):
        yesno(j)

def menu(i, j):
    second_line = "<"+j+">"
    ac_print(i, second_line)

def new_value(i,j):
    second_line = "set "+j
    ac_print(i,second_line)

def change_value(i,j,k):
    second_line = "now:"+j+"->"+k
    ac_print(i,second_line)

def yesno(i):
    ac_print(i,"set?")

def setok():
    ac_print("Set OK!!!")

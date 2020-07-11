from lcd import rootlcd
from gpio import rootgpio
from json_set import rootjson

import RPi.GPIO as GPIO
import time
import os.path

button_state='none'
menu_state={'main':'none', 'select':'none', 'value':'none'}


#first init main logo display
rootgpio.gpioset()
rootlcd.lcd_init()

#check setting json file
if os.path.isfile("/home/pi/smartfarm/Root-e/rooteset.json"):
    rootlcd.lcd_json()
    menu_state['main']=0
    rootlcd.menu("MENU",rootjson.menu_main_json(menu_state['main']))

else:
    menu_state['main']=0
    rootlcd.menu("MENU",rootjson.menu_main_json(menu_state['main'])) #go to plant setting loop

#button interrupt callback
def IntCall(pin):
    global button_state
    global menu_state

    button={12:"back", 16:"next", 20:"select", 21:"cancel"}
    button_state = button[pin]
    print (" Button pressed:" , button_state)

    if (button_state=="next"):
        bt_next()

    elif (button_state=="back"):
        bt_back()

    elif (button_state=="select"):
        bt_select()

    elif (button_state=="cancel"):
        bt_cancel()

    button_state = "none"

    #menu_state list change to json string
    main_j, select_j, value_j=rootjson.menu_json(menu_state['main'],menu_state['select'],menu_state['value'])
    rootlcd.display(main_j, select_j, value_j)

def bt_next():
    global menu_state
    if (menu_state['value']!='none'):
        menu_state['value']=menu_state['value']+1
    elif (menu_state['select']!='none')&(menu_state['select']!=2):
        menu_state['select']=menu_state['select']+1
    elif(menu_state['select']=='none')&(menu_state['main']!=2):
        menu_state['main']=menu_state['main']+1

def bt_back():
    global menu_state
    if (menu_state['value']!='none'):
        menu_state['value']=menu_state['value']-1
    elif (menu_state['select']!='none')&(menu_state['select']!=0):
        menu_state['select']=menu_state['select']-1
    elif(menu_state['select']=='none')&(menu_state['main']!=0):
        menu_state['main']=menu_state['main']-1

def bt_select():
    global menu_state
    if (menu_state['select']!='none')&(menu_state['value']=='none'):
        menu_state['value']=0
    elif menu_state['value']!='none':
        print('go to json update')
    else:
        menu_state['select']=0

def bt_cancel():
    global menu_state
    menu_state['select']='none'
    menu_state['value']='none'

#button interrupt event
GPIO.add_event_detect(12, GPIO.FALLING, callback=IntCall, bouncetime=500)
GPIO.add_event_detect(16, GPIO.FALLING, callback=IntCall, bouncetime=500)
GPIO.add_event_detect(20, GPIO.FALLING, callback=IntCall, bouncetime=500)
GPIO.add_event_detect(21, GPIO.FALLING, callback=IntCall, bouncetime=500)


try:
       while True:
           time.sleep(1)

except KeyboardInterrupt:
       GPIO.cleanup()

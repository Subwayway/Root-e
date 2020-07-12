from lcd import rootlcd
from gpio import rootgpio
from json_set import rootjson

from gpiozero import Button
import time
import os.path

menu_state={'main':'none', 'select':'none', 'value':'none'}


#first init main logo display
rootlcd.lcd_init()

#check setting json file
if os.path.isfile("/home/pi/smartfarm/Root-e/rooteset.json"):
    rootlcd.lcd_json()
    menu_state['main']=0
    rootlcd.menu("MENU",rootjson.menu_main_json(menu_state['main']))

else:
    menu_state['main']=0
    rootlcd.menu("MENU",rootjson.menu_main_json(menu_state['main'])) #go to plant setting loop


def bt_next():
    global menu_state
    if (menu_state['value']!='none'):
        menu_state['value']=menu_state['value']+1
    elif (menu_state['select']!='none')&(menu_state['select']!=2):
        menu_state['select']=menu_state['select']+1
    elif(menu_state['select']=='none')&(menu_state['main']!=2):
        menu_state['main']=menu_state['main']+1

    #menu_state list change to json string
    main_j, select_j, value_j=rootjson.menu_json(menu_state['main'],menu_state['select'],menu_state['value'])
    rootlcd.display(main_j, select_j, value_j)

def bt_back():
    global menu_state
    if (menu_state['value']!='none'):
        menu_state['value']=menu_state['value']-1
    elif (menu_state['select']!='none')&(menu_state['select']!=0):
        menu_state['select']=menu_state['select']-1
    elif(menu_state['select']=='none')&(menu_state['main']!=0):
        menu_state['main']=menu_state['main']-1

    #menu_state list change to json string
    main_j, select_j, value_j=rootjson.menu_json(menu_state['main'],menu_state['select'],menu_state['value'])
    rootlcd.display(main_j, select_j, value_j)

def bt_select():
    global menu_state
    if (menu_state['select']!='none')&(menu_state['value']=='none'):
        menu_state['value']=0
    elif menu_state['value']!='none':
        # json update
        main_j, select_j, value_j=rootjson.menu_json(menu_state['main'],menu_state['select'],menu_state['value'])
        rootjson.json_update(main_j, select_j, value_j)
        bt_cancel()
    else:
        menu_state['select']=0

    #menu_state list change to json string
    main_j, select_j, value_j=rootjson.menu_json(menu_state['main'],menu_state['select'],menu_state['value'])
    rootlcd.display(main_j, select_j, value_j)

def bt_cancel():
    global menu_state
    menu_state['select']='none'
    menu_state['value']='none'

    #menu_state list change to json string
    main_j, select_j, value_j=rootjson.menu_json(menu_state['main'],menu_state['select'],menu_state['value'])
    rootlcd.display(main_j, select_j, value_j)

# #button interrupt event

rootgpio.button1.when_pressed = bt_back
rootgpio.button2.when_pressed = bt_next
rootgpio.button3.when_pressed = bt_select
rootgpio.button4.when_pressed = bt_cancel

try:
       while True:
           time.sleep(0.1)

except KeyboardInterrupt:
       GPIO.cleanup()

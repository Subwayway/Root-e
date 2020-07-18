from lcd import rootlcd
from gpio import rootgpio
from json_set import rootjson
from bt import bt_slave

from gpiozero import Button
import time
import os.path
import os
import threading

menu_state={'main':'none', 'select':'none', 'value':'none'}
menu_state_str={'main':'none', 'select':'none', 'value':'none'}
connect_state={'Bluetooth':'none','WiFi':'none'}

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

def screen_change():
    #menu_state list change to json string
    menu_state_str['main'],menu_state_str['select'],menu_state_str['value']=rootjson.menu_json(menu_state['main'],menu_state['select'],menu_state['value'])
    rootlcd.display(menu_state_str['main'],menu_state_str['select'],menu_state_str['value'])

def bt_next():
    # main, select, value state change
    if (menu_state['value']!='none'):
        menu_state['value']=menu_state['value']+1
    elif (menu_state['select']!='none')&(menu_state['select']!=2):
        menu_state['select']=menu_state['select']+1
    elif(menu_state['select']=='none')&(menu_state['main']!=2):
        menu_state['main']=menu_state['main']+1

    screen_change()

def bt_back():
    if (menu_state['value']!='none'):
        menu_state['value']=menu_state['value']-1
    elif (menu_state['select']!='none')&(menu_state['select']!=0):
        menu_state['select']=menu_state['select']-1
    elif(menu_state['select']=='none')&(menu_state['main']!=0):
        menu_state['main']=menu_state['main']-1

    screen_change()

def bt_select():
    # select menu->value menu
    if (menu_state['select']!='none')&(menu_state['value']=='none'):
        menu_state['value']=0
    # value menu->change json, BT, wifi set
    elif menu_state['value']!='none':
        # json update
        menu_state_str['main'],menu_state_str['select'],menu_state_str['value']=rootjson.menu_json(menu_state['main'],menu_state['select'],menu_state['value'])
        rootjson.json_update(menu_state_str['main'],menu_state_str['select'],menu_state_str['value'])

        bt_cancel()
    # main menu->select menu
    else:
        menu_state['select']=0

    screen_change()

def bt_cancel():
    menu_state['select']='none'
    menu_state['value']='none'

    screen_change()

# button interrupt event

rootgpio.button1.when_pressed = bt_back
rootgpio.button2.when_pressed = bt_next
rootgpio.button3.when_pressed = bt_select
rootgpio.button4.when_pressed = bt_cancel

# Bluetooth connection, receive thread
def BT_thread():
    while True:
        if (connect_state['Bluetooth']=='none')|(connect_state['Bluetooth']=='disconnected'):
            bt_slave.setBT()
            connect_state['Bluetooth']='connected'
        else :
            connect_state['Bluetooth']=bt_slave.receiveMsg().decode()
            print(connect_state['Bluetooth'])
            if connect_state['Bluetooth']!='disconnected':
                buf_BTmsg=connect_state['Bluetooth'].split(' ')
                print(buf_BTmsg)
                if buf_BTmsg[0]=='WF':
                    sh_join='./wifi/auto_wifi.sh '+buf_BTmsg[1]+' '+buf_BTmsg[2]
                    os.system(sh_join)


t = threading.Thread(target=BT_thread)
t.start()

# main
try:
       while True:
           time.sleep(0.1)

except KeyboardInterrupt:
       GPIO.cleanup()

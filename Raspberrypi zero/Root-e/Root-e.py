#!/usr/bin/env python3

from lcd import rootlcd
from gpio import rootgpio
from json_set import rootjson
from bt import bt_slave
from firebase import rootfire
from adc import rootsensor
from dht import dht_py
from camera import rootcam

from gpiozero import Button, PWMLED
import time
import os.path
import os
import threading

menu_state={'main':'none', 'select':'none', 'value':'none'}
menu_state_str={'main':'none', 'select':'none', 'value':'none'}
connect_state={'Bluetooth':'none','WiFi':'none'}
setting_state={'setting':'none', 'led':'none', 'water_refill':'none', 'DHT_sensor':'none', 'camera':'none'}

#first init main logo display
rootlcd.lcd_init()


def screen_change():
    #menu_state list change to json string
    menu_state_str['main'],menu_state_str['select'],menu_state_str['value']=rootjson.menu_json(menu_state['main'],menu_state['select'],menu_state['value'])
    rootlcd.display(menu_state_str['main'],menu_state_str['select'],menu_state_str['value'])

now = time.localtime()

#check setting json file
if rootjson.set_check_json('setting', 'start'):
    setting_state['setting']=True
    rootlcd.lcd_json()
    menu_state['main']=0
    screen_change()

else:
    menu_state['main']=0
    screen_change() #go to plant setting loop

# button select
def bt_next():
    # main, select, value state change
    if (menu_state['value']!='none'):
        menu_state['value']=menu_state['value']+1
    elif (menu_state['select']!='none'):
        # check next screen exist
        if(rootjson.menu_check_json(menu_state_str['main'],menu_state['select']+1)):
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
        rootjson.json_setupdate(menu_state_str['main'],menu_state_str['select'],menu_state_str['value'],time.strftime('%Y-%m-%d %H:%M:%S', now))
        rootfire.fire_set_update(rootjson.setting_ret_json(), rootjson.setting_read_json("info","id"))
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
    try:
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
                    # elif buf_BTmsg[0]='CP':
    except:
        pass


t = threading.Thread(target=BT_thread)
t.start()


def roote_daycheck():
    split_buf=rootjson.setting_read_json('setting', 'start')
    if time.strftime('%Y-%m-%d')!=split_buf.split(' ')[0]:
        if time.strftime('%Y-%m-%d %H:%M:%S', now)==split_buf.split(' ')[1]:
           buf=rootjson.setting_read_json("setting","day")+1
           rootjson.setting_write_json("setting","day",buf)
           setting_state['water_refill']=False

def roote_gpiosys():
    if setting_state['setting']==True:
       led_pwm.value=rootjson.setting_read_json('setting', 'Bright')
       if (now.tm_hour>=rootjson.setting_read_json('setting', 'Ledon'))&(now.tm_hour<rootjson.setting_read_json('setting', 'Ledoff')):
           rootgpio.led_on()
       elif (now.tm_hour>=rootjson.setting_read_json('setting', 'Ledoff'))|(now.tm_hour<rootjson.setting_read_json('setting', 'Ledon')):
           rootgpio.led_off()
           
       if (rootjson.setting_read_json('setting','day')==rootjson.setting_read_json('setting', 'Water'))&(setting_state['water_refill']!=True):
           while rootsensor.sensor_read(0)!=720:
               rootgpio.motor_on()
           rootgpio.motor_off()
           setting_state['water_refill']=True
           
       if now.tm_min==rootjson.setting_read_json('setting', 'Env'):
           if (setting_state['DHT_sensor']==False)|(setting_state['DHT_sensor']=='none'):
               setting_state['DHT_sensor']=True
               #upload env data to firebase code
               i,j=dht_py.dht22_read()
               rootfire.fire_env_update(i,j,rootjson.setting_read_json("info","id"))
       else:
           setting_state['DHT_sensor']=False
           
       if (now.tm_hour//rootjson.setting_read_json('setting', 'Camera'))==0:
           if (setting_state['camera']==False)|(setting_state['camera']=='none'):
               rootcam.capture()
               rootcam.create_gif()
               rootfire.fire_gif_update('/home/pi/smartfarm/Root-e/img_sample/movie.gif', rootjson.setting_read_json("info","id"))
               setting_state['camera']=True
       else:
           setting_state['camera']==False
    roote_daycheck()

led_pwm=PWMLED(19)


# main
try:
       while True:
           time.sleep(0.1)
           
           roote_gpiosys()

except KeyboardInterrupt:
       print("Main thread shutdown")
       rootgpio.gpio_off()

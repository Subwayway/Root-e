import json

with open('json_set/menu.json') as json_menu_file:
    json_menu = json.load(json_menu_file)
with open('json_set/setting.json') as json_setting_file:
    json_setting = json.load(json_setting_file)

#load main menu form menu.json
def menu_main_json(x):
    json_array = json_menu["menu"]

    return json_array[x]

#load select menu from menu.json
def menu_select_json(i, j):
    json_array = json_menu[menu_main_json(i)]

    return json_array[j]

#check key exist
def menu_check_json(i, j):
    try:
        buf = json_menu[i][j]
    except KeyError:
        return False
    except IndexError:
        return False

    return True

def set_check_json(i, j):
    if json_setting[i][j]=='none':
        return False
    else:
        return True

#main menu, select menu, value change to json string
def menu_json(i,j,k):
    json_array = json_menu["menu"]

    if j=='none':
        array2='none'
    else:
        json_array2 = json_menu[json_array[i]]
        array2=json_array2[j]

    return json_array[i], array2, k

#write setting info to setting.json ex)"info","id",1
def setting_write_json(i,j,k):
    json_setting[i][j]=k

    with open('json_set/setting.json', 'w', encoding='utf-8') as setting_json:
        json.dump(json_setting, setting_json, indent="\t")

def setting_read_json(i,j):
    return json_setting[i][j]

def save_selct_env(i):
    setting_write_json("setting","plant",i)
    setting_write_json("setting","Bright",json_menu[i]["Bright"])
    setting_write_json("setting","Ledon",json_menu[i]["Ledon"])
    setting_write_json("setting","Ledoff",json_menu[i]["Ledoff"])
    setting_write_json("setting","Water",json_menu[i]["Water"])
    setting_write_json("setting","Env",json_menu[i]["Env"])
    setting_write_json("setting","Camera",json_menu[i]["Camera"])

def save_custom_env(i,j,k):
    setting_write_json("setting","plant","custom")
    if j=="Bright":
        setting_write_json("setting","Bright",k)
    elif j=="Ledon":
        setting_write_json("setting","Ledon",k)
    elif j=="Ledoff":
        setting_write_json("setting","Ledoff",k)
    elif j=="Water":
        setting_write_json("setting","Water",k)
    elif j=="Env":
        setting_write_json("setting","Env",k)
    elif j=="Camera":
        setting_write_json("setting","Camera",k)

def json_setupdate(i,j,k,t):
    # save start time
    setting_write_json("setting","start",t)
    setting_write_json("setting","day",1)
    if i=="Select Plant":
        save_selct_env(j)
    elif i=="Custom Plant":
        save_custom_env(i,j,k)
# test code
# print(menu_main_json(1))
# print(menu_select_json(1,1))
# setting_write_json("info","id",1)
#save_env("Lettuce")

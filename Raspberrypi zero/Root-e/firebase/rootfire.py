# 출처: https://m.blog.naver.com/PostView.nhn?blogId=cosmosjs&logNo=220983410440&proxyReferer=https:%2F%2Fwww.google.com%2F

import pyrebase
import time
from datetime import datetime
import json
import os

# x=gif path, y=user id
def fire_gif_update(x,y):
    config={
        "apiKey": "AIzaSyB4MzpCEMtKEOnIJnM9n8yht_Yz2uRzpls", #webkey
        "authDomain": "diy-smartfarm.firebaseapp.com", #프로젝트ID
        "databaseURL": "https://diy-smartfarm.firebaseio.com/", #database url
        "storageBucket": "diy-smartfarm.appspot.com" #storage
    }

    firebase = pyrebase.initialize_app(config)

    id= 'id '+str(y)

    #업로드할 파일명
    uploadfile = x
    #업로드할 파일의 확장자 구하기
    s = os.path.splitext(uploadfile)[1]
    #업로드할 새로운파일이름
    now = datetime.today().strftime("%Y%m%d_%H%M%S")
    filename = now + s

    #Upload files to Firebase
    storage = firebase.storage()

    storage.child("gif/"+id+'/'+filename).put(uploadfile)
    fileUrl = storage.child("gif/"+id+'/'+filename).get_url(1) #0은 저장소 위치 1은 다운로드 url 경로이다.
    print (fileUrl)


    db = firebase.database()
    d = {}
    d[now] = fileUrl
    results = db.child("gif").child(id).update(d)
    print("Gif upload OK")

def fire_set_update(x,y):
    config={
        "apiKey": "AIzaSyB4MzpCEMtKEOnIJnM9n8yht_Yz2uRzpls", #webkey
        "authDomain": "diy-smartfarm.firebaseapp.com", #프로젝트ID
        "databaseURL": "https://diy-smartfarm.firebaseio.com/", #database url
        "storageBucket": "diy-smartfarm.appspot.com" #storage
    }

    firebase = pyrebase.initialize_app(config)

    id= 'id '+str(y)

    db = firebase.database()
    data = x
    results = db.child("setting").child(id).update(data)
    print("setting upload OK")

#x=Temperature y=Humidity z=id
def fire_env_update(x,y,z):
    config={
        "apiKey": "AIzaSyB4MzpCEMtKEOnIJnM9n8yht_Yz2uRzpls", #webkey
        "authDomain": "diy-smartfarm.firebaseapp.com", #프로젝트ID
        "databaseURL": "https://diy-smartfarm.firebaseio.com/", #database url
        "storageBucket": "diy-smartfarm.appspot.com" #storage
    }

    firebase = pyrebase.initialize_app(config)

    id= 'id '+str(z)
    now = datetime.today().strftime("%Y%m%d_%H%M%S")

    db = firebase.database()
    data = {}
    data['T']=x
    data['H']=y
    results = db.child("env").child(id).child(now).update(data)
    print("env upload OK")

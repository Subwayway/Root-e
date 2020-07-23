# 출처: https://m.blog.naver.com/PostView.nhn?blogId=cosmosjs&logNo=220983410440&proxyReferer=https:%2F%2Fwww.google.com%2F

import pyrebase
import time
from datetime import datetime
import json
import os

def fire_upload(x):
	config={
		"apiKey": "AIzaSyB4MzpCEMtKEOnIJnM9n8yht_Yz2uRzpls", #webkey
		"authDomain": "diy-smartfarm.firebaseapp.com", #프로젝트ID
		"databaseURL": "https://diy-smartfarm.firebaseio.com/", #database url
		"storageBucket": "diy-smartfarm.appspot.com" #storage
	}

	firebase = pyrebase.initialize_app(config)

	#업로드할 파일명
	uploadfile = x
	#업로드할 파일의 확장자 구하기
	s = os.path.splitext(uploadfile)[1]
	#업로드할 새로운파일이름
	now = datetime.today().strftime("%Y%m%d_%H%M%S")
	filename = now + s

	#Upload files to Firebase
	storage = firebase.storage()

	storage.child("img/"+filename).put(uploadfile)
	fileUrl = storage.child("img/"+filename).get_url(1) #0은 저장소 위치 1은 다운로드 url 경로이다.
	#동영상 파일 경로를 알았으니 어디에서든지 참조해서 사용할 수 있다.
	print (fileUrl)

	#업로드한 파일과 다운로드 경로를 database에 저장하자. 그래야 나중에 사용할 수 있다. storage에서 검색은 안된다는 것 같다.
	#save files info in database
	db = firebase.database()
	d = {}
	d[filename] = fileUrl
	data = json.dumps(d)
	results = db.child("files").push(data)
	print("OK")

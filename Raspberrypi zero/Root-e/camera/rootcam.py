from picamera import PiCamera
from time import sleep
import os

camera = PiCamera()

# x:file name
def capture():
    x=1
    filename=str(x)+".jpg"
    
    while os.path.isfile('/home/pi/smartfarm/Root-e/img_sample/'+filename):
        x=x+1
        filename=str(x)+".jpg"
        
    camera.capture('/home/pi/smartfarm/Root-e/img_sample/'+filename)
    sleep(1)
    print("capture ok")

def create_gif():
    os.system('convert -delay 10 /home/pi/smartfarm/Root-e/img_sample/*.jpg /home/pi/smartfarm/Root-e/img_sample/movie.gif')

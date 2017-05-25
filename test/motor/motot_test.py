import RPi.GPIO as GPIO
import time

MOTO_1_A = 16
MOTO_1_B = 18
MOTO_2_A = 13
MOTO_2_B = 15


GPIO.setmode(GPIO.BOARD)

GPIO.setup(MOTO_1_A,GPIO.OUT)
GPIO.setup(MOTO_1_B,GPIO.OUT)
GPIO.setup(MOTO_2_A,GPIO.OUT)
GPIO.setup(MOTO_2_B,GPIO.OUT)

try:
	while True:
		GPIO.output(MOTO_1_A,True)
		GPIO.output(MOTO_1_B, False)
		GPIO.output(MOTO_2_A,True)
		GPIO.output(MOTO_2_B, False)

except KeyboardInterrupt:
	GPIO.cleanup()

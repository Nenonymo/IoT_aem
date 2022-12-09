# Imports for MQTT
#!/usr/bin/env python


import time
import datetime
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish
import sys
import requests, json, hashlib, uuid, time



############### Sensor section #################
def get_temp(tempSensorAddresses):
	outVal = ""
	for sensor in tempSensorAddresses:
		try:
			response = requests.get(
				url="https://pa-api.telldus.com/json/sensor/info",
				params={
					"id": sensor,
				},
				headers={
					"Authorization": 'OAuth oauth_consumer_key="{pubkey}", oauth_nonce="{nonce}", oauth_signature="{oauthSignature}", oauth_signature_method="PLAINTEXT", oauth_timestamp="{timestamp}", oauth_token="{token}", oauth_version="1.0"'.format(pubkey=pubkey, nonce=nonce, oauthSignature=oauthSignature, timestamp=timestamp, token=token),
					},
				)
			responseData = response.json()
			temperature = json.dumps(responseData['data'][0]['value'], indent=4, sort_keys=True)
			outVal = f"{outVal}t{sensor}:{temperature:.3f}\n"
		except:
			print(f"Error getting the value of {sensor}")
			outVal = f"{outVal}t{sensor}:ERROR\n"
	return outVal



############### MQTT section ##################

# when connecting to mqtt do this;
def on_connect(client, userdata, flags, rc):
	if rc==0:
		print("Connection established. Code: "+str(rc))
	else:
		print("Connection failed. Code: " + str(rc))
		
def on_publish(client, userdata, mid):
    print("Published: " + str(mid))
	
def on_disconnect(client, userdata, rc):
	if rc != 0:
		print ("Unexpected disonnection. Code: ", str(rc))
	else:
		print("Disconnected. Code: " + str(rc))
	
def on_log(client, userdata, level, buf):		# Message is in buf
    print("MQTT Log: " + str(buf))

#Put everything into the main function so that we can call this file as a package
def main():
	# This is where to insert your generated API keys (http://api.telldus.com/keys)
	pubkey = "FEHUVEW84RAFR5SP22RABURUPHAFRUNU"  # Public Key
	privkey = "ZUXEVEGA9USTAZEWRETHAQUBUR69U6EF" # Private Key
	token = "8aba8385b6f65e0f7bf274e5e673f04b05d541a1e" # Token
	secret = "ecd6a7203c64ec98469df1da577eeff3" # Token Secret 

	localtime = time.localtime(time.time())
	timestamp = str(time.mktime(localtime))
	nonce = uuid.uuid4().hex
	oauthSignature = (privkey + "%26" + secret)

	# Set MQTT broker and topic
	broker = "test.mosquitto.org"	# Broker 

	pub_topic = "IoTLab"

	#In order to differentiate the different sensors we add a letter at the
	#front of the address to identify their type.
	sensors = sys.argv[1:]
	tempSensors = [sensor[1:] for sensor in sensors if sensor[0] == "t"]
	#luxSensors = [sensor[1:] for sensor in sensors if sensor[0] == "l"]
	#atmosSensor = [sensor[1:] for sensor in sensors if sensor[0] == "a"]

	# Connect functions for MQTT
	client = mqtt.Client()
	client.on_connect = on_connect
	client.on_disconnect = on_disconnect
	client.on_publish = on_publish
	client.on_log = on_log

	# Connect to MQTT 
	print("Attempting to connect to broker " + broker)
	client.connect(broker)	# Broker address, port and keepalive (maximum period in seconds allowed between communications with the broker)
	client.loop_start()


	# Loop that publishes message
	while True:
		data_to_send = "{}".format(get_temp(tempSensors))	# Here, call the correct function from the sensor section depending on sensor
		client.publish(pub_topic, str(data_to_send))
		time.sleep(2.0)	# Set delay


if __name__ == "__main__":
	main()
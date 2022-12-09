"""Change the value of some actuators
Args:
    value (bool): Value to send to the actuators
    adresses (list(string)): Actuators to send that value to
"""

pubkey = "FEHUVEW84RAFR5SP22RABURUPHAFRUNU"  # Public Key
privkey = "ZUXEVEGA9USTAZEWRETHAQUBUR69U6EF" # Private Key
token = "8aba8385b6f65e0f7bf274e5e673f04b05d541a1e" # Token
secret = "ecd6a7203c64ec98469df1da577eeff3" #Token Secret

import requests, json, hashlib, uuid, time, sys
localtime = time.localtime(time.time())
timestamp = str(time.mktime(localtime))
nonce = uuid.uuid4().hex
oauthSignature = (privkey + "%26" + secret)

#Failsafe: Need at least 2 arguments (Value plus one actuator)
if (len(sys.argv) <= 1):
    print("Error, not enough arguments.")
    sys.exit(1)

value = "Off" if sys.argv[1] == "0" else "On"
idList = sys.argv[2:]

for actuator in idList:
    response = requests.get(
        url="https://pa-api.telldus.com/json/device/turn{value}",
        params={
            "id": actuator,
        },
        headers={
            "Authorization": 'OAuth oauth_consumer_key="{pubkey}", oauth_nonce="{nonce}", oauth_signature="{oauthSignature}", oauth_signature_method="PLAINTEXT", oauth_timestamp="{timestamp}", oauth_token="{token}", oauth_version="1.0"'.format(pubkey=pubkey, nonce=nonce, oauthSignature=oauthSignature, timestamp=timestamp, token=token),
            },
        )
    # Output/response from GET-request	
    responseData = response.json()
    # Uncomment to print response :) 
    #print(json.dumps(responseData, indent=4, sort_keys=True))
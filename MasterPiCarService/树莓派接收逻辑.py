import paho.mqtt.client as mqtt
import json

# 假设这是你的硬件驱动库
# from MasterPi import Controller 

def on_message(client, userdata, msg):
    actions = json.loads(msg.payload.decode())
    print(f"收到指令流: {actions}")
    
    for cmd in actions:
        type = cmd.get("type")
        action = cmd.get("action")
        val = cmd.get("val", 0)
        
        if type == "move":
            print(f"执行移动: {action}, 速度: {val}")
            # car.move(action, val)
        elif type == "arm":
            print(f"执行机械臂: {action}")
            # arm.execute(action)

client = mqtt.Client()
client.on_message = on_message
client.connect("若依服务器IP", 1883)
client.subscribe("picar/control/1") # 这里的 1 对应 deviceId
client.loop_forever()
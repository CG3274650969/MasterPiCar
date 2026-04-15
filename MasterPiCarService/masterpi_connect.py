#!/usr/bin/env python3
# encoding: utf-8
import sys
import os
import threading

# 确保路径包含根目录，以便导入 HiwonderSDK 文件夹
base_path = '/home/pi/MasterPi/'
if base_path not in sys.path:
    sys.path.append(base_path)

import json
import time
import paho.mqtt.client as mqtt
import subprocess
import signal

# --- 旧版 SDK 导入 ---
import HiwonderSDK.Board as Board
import HiwonderSDK.mecanum as mecanum

from flask import Flask
app = Flask(__name__)

# ─────────────────────────── 初始化 ───────────────────────────
# 初始化底盘
chassis = mecanum.MecanumChassis()

# ─────────────────────────── 机械臂预设位姿 ───────────────────────────
# 舵机脉宽范围: 500-2500, 中位 1500
ARM_PRESETS = {
    "reset": [[1, 1500], [3, 500], [4, 2170], [5, 945], [6, 1500]],
    "up":    [[3, 500], [4, 1500], [5, 1500], [6, 1500]],
    "down":  [[3, 500], [4, 2400], [5, 700], [6, 1500]],
}

# ─────────────────────────── 底盘控制函数 ───────────────────────────
DIRECTION_MAP = {
    "forward":    (90,  0),
    "backward":   (270, 0),
    "left":       (180, 0),
    "right":      (0,   0),
    "turn_left":  (90,  0.3),
    "turn_right": (90, -0.3),
}

def move_chassis(action: str, val: float = 50):
    if action == "stop":
        chassis.set_velocity(0, 90, 0)
        print("[底盘] 停止")
        return

    if action not in DIRECTION_MAP:
        return

    direction, angular_rate = DIRECTION_MAP[action]
    if action in ("turn_left", "turn_right"):
        sign = 1 if action == "turn_left" else -1
        chassis.set_velocity(0, 90, sign * max(0.05, float(val) / 100.0))
    else:
        speed = max(0, min(100, int(val))) * 5
        chassis.set_velocity(speed, direction, angular_rate)
    print(f"[底盘] {action}, val={val}")

# ─────────────────────────── 机械臂控制函数 ───────────────────────────
def control_arm(action: str, val=None):
    if action == "grab":
        Board.setPWMServoPulse(1, 1500, 500)
        print("[机械臂] 抓取")
    elif action == "release":
        Board.setPWMServoPulse(1, 2000, 500)
        print("[机械臂] 释放")
    elif action in ("up", "down", "reset"):
        servos = ARM_PRESETS[action]
        args = [1000, len(servos)]
        for s in servos:
            args.append(s[0])
            args.append(s[1])
        Board.setPWMServosPulse(args)
        print(f"[机械臂] 姿态: {action}")

# ─────────────────────────── 视频服务管理 ───────────────────────────
video_process = None
yolo_process = None

@app.route('/test')
def test_connection():
    return "Network is OK!"

def start_video():
    global video_process
    if video_process is None:
        video_process = subprocess.Popen(
            ["python3", "/home/pi/MasterPi/Functions/video_server.py"],
            preexec_fn=os.setsid
        )
        print("视频流服务已启动 (PID: {})".format(video_process.pid))
        return True
    return False

def stop_video():
    global video_process
    if video_process is not None:
        try:
            os.killpg(os.getpgid(video_process.pid), signal.SIGTERM)
            video_process.wait(timeout=5)
        except:
            video_process.kill()
        video_process = None
        print("视频流服务已关闭")
        return True
    return False

def start_yolo():
    global yolo_process
    if yolo_process is None:
        yolo_process = subprocess.Popen(
            ["python3", "/home/pi/MasterPi/Functions/yolo_server.py"],
            preexec_fn=os.setsid
        )
        print("YOLO 服务已启动 (PID: {})".format(yolo_process.pid))
        return True
    return False

def stop_yolo():
    global yolo_process
    if yolo_process is not None:
        try:
            os.killpg(os.getpgid(yolo_process.pid), signal.SIGTERM)
            yolo_process.wait(timeout=5)
        except:
            yolo_process.kill()
        yolo_process = None
        print("YOLO 服务已关闭")
        return True
    return False

# ─────────────────────────── MQTT 回调逻辑 ───────────────────────────
def on_message(client, userdata, msg):
    try:
        payload = msg.payload.decode()
        actions = json.loads(payload)
        for cmd in actions:
            cmd_type = cmd.get("type")
            action   = cmd.get("action")
            val      = cmd.get("val", 50)
            delay_ms = cmd.get("delay", 0)

            if cmd_type == "move":
                move_chassis(action, val)
            elif cmd_type == "arm":
                control_arm(action, val)
            elif cmd_type == "function":
                if action == "start_video":
                    start_video()
                elif action == "stop_video":
                    stop_video()
                # elif action == "start_yolo":
                #     start_yolo()
                elif action == "start_yolo":
                    print("收到识别请求，确保原始视频流已开启...")
                    # 这里不运行本地 yolo_server.py
                    # 只需要确保视频流服务在跑即可
                    if video_process is None or video_process.poll() is not None:
                        start_video() # 调用你代码里开启 5001 端口视频的方法
                    print("视频流已就绪，请在电脑端查看 YOLO 识别画面")
                elif action == "stop_yolo":
                    stop_yolo()

            if delay_ms > 0:
                time.sleep(delay_ms / 1000.0)
    except Exception as e:
        print(f"处理失败: {e}")

def main():
    client = mqtt.Client()
    client.on_message = on_message
    client.connect("10.107.93.243", 1883, 60)
    client.subscribe("picar/control/#")
    print("服务已启动 (HiwonderSDK版)...")
    print("等待 MQTT 指令...")
    client.loop_forever()

if __name__ == "__main__":
    # 1. 创建一个线程来运行 MQTT
    mqtt_thread = threading.Thread(target=main)
    mqtt_thread.setDaemon(True) # 设置为守护线程，主程序退出时它也退出
    mqtt_thread.start()

    # 2. 在主线程运行 Flask
    # 这样 app.run 就不会被 main() 阻塞了
    app.run(host='0.0.0.0', port=5000, debug=False, threaded=True)

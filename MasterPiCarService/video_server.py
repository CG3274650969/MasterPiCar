#!/usr/bin/env python3
# encoding: utf-8
import cv2
from flask import Flask, Response
import threading
import sys
import json
import time

app = Flask(__name__)
camera = None
camera_lock = threading.Lock()

# 核心修改：使用 MasterPi 默认的流地址
# 这个地址是树莓派后台 mjpg-streamer 开启的服务
STREAM_URL = 'http://127.0.0.1:8080?action=stream'

def get_camera():
    global camera
    with camera_lock:
        if camera is None:
            # 不读 0，读本地循环流
            camera = cv2.VideoCapture(STREAM_URL)
            if not camera.isOpened():
                print("错误: 无法连接到本地流服务，请确保 MasterPi 默认玩法已启动。")
                camera = None
    return camera

def release_camera():
    global camera
    with camera_lock:
        if camera is not None:
            camera.release()
            camera = None

def gen_frames():
    while True:
        cap = get_camera()
        if cap is None:
            time.sleep(1)
            continue
            
        success, frame = cap.read()
        if not success:
            print("流读取失败，正在重连...")
            release_camera()
            time.sleep(1)
            continue
        
        # 编码为 JPEG 发送给前端
        ret, buffer = cv2.imencode('.jpg', frame, [cv2.IMWRITE_JPEG_QUALITY, 80])
        if not ret:
            continue
            
        frame_bytes = buffer.tobytes()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame_bytes + b'\r\n')

@app.route('/video_feed')
def video_feed():
    return Response(gen_frames(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

@app.route('/health')
def health():
    data = {'status': 'ok', 'camera': 'streaming' if camera is not None else 'inactive'}
    return json.dumps(data), 200, {'Content-Type': 'application/json'}

def start_server():
    print("视频转发服务启动。前端请访问: http://10.107.93.135:5001/video_feed")
    app.run(host='0.0.0.0', port=5001, threaded=True)

if __name__ == '__main__':
    start_server()
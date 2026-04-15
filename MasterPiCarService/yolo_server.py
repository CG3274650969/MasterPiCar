import cv2
from flask import Flask, Response
from ultralytics import YOLO

app = Flask(__name__)
# 加载 YOLO 模型 (首次运行会自动下载 yolov8n.pt)
model = YOLO('yolov8n.pt') 

# 树莓派的原始视频流地址
SOURCE_URL = "http://10.107.93.135:5001/video_feed"

def generate_yolo_stream():
    # 增加超时处理，防止树莓派没开摄像头时电脑端卡死
    cap = cv2.VideoCapture(SOURCE_URL)
    if not cap.isOpened():
        print("无法连接到树莓派视频流，请检查树莓派是否开启了 start_video")
        return

    while True:
        success, frame = cap.read()
        if not success:
            break
        
        # YOLO 推理
        results = model(frame, stream=True, conf=0.5)
        annotated_frame = frame # 默认原图
        for r in results:
            annotated_frame = r.plot() 
            
        ret, buffer = cv2.imencode('.jpg', annotated_frame)
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + buffer.tobytes() + b'\r\n')

@app.route('/yolo_feed')
def yolo_feed():
    # 电脑端提供的接口，前端访问这个 URL
    return Response(generate_yolo_stream(), 
                    mimetype='multipart/x-mixed-replace; boundary=frame')

if __name__ == '__main__':
    # 电脑端启动，端口设为 5001（避开 5000 和若依的 8080）
    app.run(host='0.0.0.0', port=5001)
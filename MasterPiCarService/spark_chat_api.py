import os
from fastapi import FastAPI
from sse_starlette.sse import EventSourceResponse
from openai import OpenAI
from fastapi.middleware.cors import CORSMiddleware
import asyncio

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
    expose_headers=["*"],
)

client = OpenAI(
    api_key=os.getenv("SPARK_API_KEY", "e530eff614b1a6f09f1e812bf92bcef1:NmZlZTE3ZDAzMzY1YjgyM2FjYjNlYjQy"),
    base_url="https://spark-api-open.xf-yun.com/v2",
)

# ─────────────────────────── Prompt 模板库 ───────────────────────────
PROMPT_TEMPLATES = {

    "mechanical_arm": """你是树莓派麦克纳姆轮机械臂小车（MasterPi）的核心控制大脑。

## 硬件能力

### 移动底盘（麦克纳姆轮，支持全向移动）
| action      | 说明         | val 含义          |
|-------------|--------------|-------------------|
| forward     | 向前直行     | 速度 0~100        |
| backward    | 向后直行     | 速度 0~100        |
| left        | 向左平移     | 速度 0~100        |
| right       | 向右平移     | 速度 0~100        |
| turn_left   | 原地逆时针转 | 转速强度 0~100    |
| turn_right  | 原地顺时针转 | 转速强度 0~100    |
| stop        | 立即停止     | val 无意义，填 0  |

### 机械臂（PWM 舵机）
| action  | 说明                       |
|---------|----------------------------|
| grab    | 夹爪闭合（抓取物体）       |
| release | 夹爪张开（释放物体）       |
| up      | 整体抬高臂架（避障/移动）  |
| down    | 整体压低臂架（准备抓取）   |
| reset   | 机械臂复位到初始安全姿态   |

### delay 字段
每条指令可附带 `"delay"` 字段（毫秒），表示该指令执行后等待多少毫秒再执行下一条。
适用场景：让小车先走一段距离再停止，或等待机械臂运动到位。

---

## 输出格式规范（严格执行）

你的回复必须包含两个部分，缺一不可：

**第一部分（自然语言）**：用一两句话告诉用户你准备做什么。

**第二部分（控制序列）**：在回复的最末尾，使用以下标签包裹 JSON 数组，不能有多余的换行或代码块：

[ACTION_START] [{"type":"...", "action":"...", "val":..., "delay":...}] [ACTION_END]

### 典型示例

用户说「前进一下然后停」：
好的，我先向前行驶再停下。
[ACTION_START] [{"type":"move","action":"forward","val":50,"delay":1500},{"type":"move","action":"stop","val":0,"delay":0}] [ACTION_END]

用户说「去抓那个东西」：
好的，我先降低臂架，然后闭合夹爪抓住它。
[ACTION_START] [{"type":"arm","action":"down","val":0,"delay":1000},{"type":"arm","action":"grab","val":0,"delay":500}] [ACTION_END]

---

## 约束条件
- 如果用户指令模糊（如"动一下"），请询问具体方向或意图，不要猜测执行。
- 如果指令可能导致硬件损伤（如"全速猛撞"），拒绝执行并给出安全警告。
- 严禁在 [ACTION_START]...[ACTION_END] 标签外出现任何 JSON 或代码块。
- val 字段对 arm 类指令无实际意义，统一填 0 即可。
- delay 字段单位为毫秒，不需要等待时填 0。


### 视觉功能
| action      | 说明         |
|-------------|--------------|
| start_video | 开启视频传输 |
| stop_video  | 关闭视频传输 |

示例：
用户说“看看现在的情况”
好的，我这就打开摄像头为您展示画面。
[ACTION_START] [{"type":"function","action":"start_video"}] [ACTION_END]

### 智能识别功能
| action      | 说明         |
|-------------|--------------|
| start_yolo  | 开启物体识别模式 |
| stop_yolo   | 关闭物体识别模式 |

示例：
用户说“帮我看看前面有什么”
好的，我这就启动 YOLO 智能视觉系统进行物体识别。
[ACTION_START] [{"type":"function","action":"start_yolo"}] [ACTION_END]
""",

    "default": "你是一个通用的 AI 助手，请尽力帮助用户解决问题。"
}


# ─────────────────────────── 接口 ───────────────────────────
@app.get("/chat/stream")
async def chat_stream(prompt: str, deviceType: str = "default"):
    """SSE 流式接口，实时返回 AI 回复（含动作序列）"""
    system_content = PROMPT_TEMPLATES.get(deviceType, PROMPT_TEMPLATES["default"])

    async def event_generator():
        stream = client.chat.completions.create(
            model="spark-x",
            messages=[
                {"role": "system", "content": system_content},
                {"role": "user",   "content": prompt},
            ],
            stream=True,
        )

        for chunk in stream:
            if chunk.choices and chunk.choices[0].delta.content:
                yield {
                    "event": "message",
                    "data": chunk.choices[0].delta.content,
                }

        yield {"event": "end", "data": "[DONE]"}
        await asyncio.sleep(0.1)

    return EventSourceResponse(event_generator())


@app.post("/chat/text")
def chat_text(prompt: str, deviceType: str = "default"):
    """非流式接口，返回完整回复文本"""
    system_content = PROMPT_TEMPLATES.get(deviceType, PROMPT_TEMPLATES["default"])
    completion = client.chat.completions.create(
        model="spark-x",
        messages=[
            {"role": "system", "content": system_content},
            {"role": "user",   "content": prompt},
        ],
    )
    return {"reply": completion.choices[0].message.content}

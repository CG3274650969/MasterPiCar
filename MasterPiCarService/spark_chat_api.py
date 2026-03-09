from fastapi import FastAPI, Query
from sse_starlette.sse import EventSourceResponse
from openai import OpenAI
from fastapi.middleware.cors import CORSMiddleware

import asyncio  # 别忘了在文件顶部加上这一行

app = FastAPI()

# 允许若依前端跨域访问
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
    expose_headers=["*"] # 增加这一行，确保浏览器能看到所有头
)

client = OpenAI(
    api_key="e530eff614b1a6f09f1e812bf92bcef1:NmZlZTE3ZDAzMzY1YjgyM2FjYjNlYjQy", # 生产环境建议用 os.getenv
    base_url="https://spark-api-open.xf-yun.com/v2",
)

# 模拟 Prompt 模板库
# 修改 spark_chat_api.py 中的模板库
PROMPT_TEMPLATES = {
    "mechanical_arm": (
        '提示词名称：MasterPi 智能调度员'

            '角色定位：'
            '你是树莓派机械臂小车（MasterPi）的核心控制大脑。你连接着一套具备移动底盘和 6 自由度机械臂的硬件系统。'

            '硬件能力说明：'

            '移动底盘： 支持 forward, backward, left, right, stop。速度区间 [0, 100]。'

            '机械臂： 支持 grab（闭合爪子）, release（张开爪子）, up（升高臂架）, down（降低臂架）, reset（复位）。'

            '输出规范（极其重要）：'
            '你的回答必须包含两个部分：'

            '第一部分（自然语言）： 告诉用户你准备做什么。例如：“好的，我这就过去帮你把东西抓起来。”'

            '第二部分（控制逻辑）： 在回复的最末尾，使用特定标签包裹 JSON 列表，支持多个动作按顺序执行。格式如下：'
            '[ACTION_START] [{"type":"move","action":"forward","val":50}, {"type":"arm","action":"grab"}] [ACTION_END]'

            '约束条件：'

            '如果用户指令模糊（如“动一下”），请询问具体方向。'

            '如果检测到危险操作（如“全速撞击”），请拒绝执行并给出安全警告。'

            '严禁在 JSON 标签外出现任何代码块。'
    ),
    "default": "你是一个通用的 AI 助手。"
}


@app.get("/chat/stream")
async def chat_stream(prompt: str, deviceType: str = "default"):
    system_content = PROMPT_TEMPLATES.get(deviceType, PROMPT_TEMPLATES["default"])

    # 关键修改：这里要加上 async 变成异步生成器
    async def event_generator():
        # OpenAI SDK 的这种调用方式虽然不是原生异步，但在 async def 中是可以运行的
        stream = client.chat.completions.create(
            model="spark-x",
            messages=[
                {"role": "system", "content": system_content},
                {"role": "user", "content": prompt}
            ],
            stream=True,
        ) 

        for chunk in stream:
            if chunk.choices and chunk.choices[0].delta.content:
                yield {
                    "event": "message",
                    "data": chunk.choices[0].delta.content
                }
        
        # 发送结束标志
        yield {"event": "end", "data": "[DONE]"}
        
        # 现在的 await 就在 async 函数内部了，不会报错
        await asyncio.sleep(0.1) 

    return EventSourceResponse(event_generator())

@app.post("/chat/text")
def chat_text(prompt: str):
    """返回完整文本"""
    completion = client.chat.completions.create(
        model="spark-x",
        messages=[{"role": "user", "content": prompt}],
    )
    return {"reply": completion.choices[0].message.content}
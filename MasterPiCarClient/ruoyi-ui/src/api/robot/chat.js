import request from "@/utils/request";

export function sendChatInput(data) {
  return request({
    url: "/robot/chat/input",
    method: "post",
    data
  });
}

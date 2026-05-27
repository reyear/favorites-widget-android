# 收藏公交卡片（Android）

一个最小可用的 Android App，用于：

- 拉取 `http://i.adigger.cn:8099/favorites` 的收藏公交 JSON
- 在应用内展示收藏线路
- 提供桌面小组件（App Widget）
- 支持点击小组件内“刷新”重新拉取数据

## 当前状态

- 已完成本地源码骨架
- 已包含 GitHub Actions 构建工作流
- 由于当前机器缺少完整 Android SDK / GitHub 可用认证，APK 仍未产出

## 接口约定

默认接口返回格式示例：

```json
{
  "timestamp": "2026-05-27 10:39:45+0800",
  "ok": true,
  "mode": "already_favorites",
  "page": "favorites",
  "favorites": [
    {
      "line": "951路",
      "tag": "上班",
      "wait_station": "阳光100",
      "direction": "开往 体院北公交站",
      "status": "等待首站发车",
      "next_status": "点击查看发车预测"
    }
  ]
}
```

如需改接口地址，可修改：

- `app/src/main/res/values/strings.xml` 中的 `favorites_api_url`

## GitHub Actions 构建

工作流文件：`.github/workflows/android-apk.yml`

触发后会构建 debug APK 并上传 artifact。

<div align="right">
  <a href="./README_en.md" target="_blank">
    <img src="https://img.shields.io/badge/English-README-blue" alt="English README">
  </a>
</div>

# KModuleSDK 使用文档

## 1. 概述

KModuleSDK是硬件SDK的对外核心类，开发者仅需调用此类的API即可完成所有硬件操作。该SDK主要提供以下功能：

| 功能分类 | 描述 |
|---------|------|
| 读卡功能 | 支持读取卡片信息 |
| 继电器控制 | 支持继电器的开关操作 |
| LED灯控制 | 支持LED灯的颜色、闪烁模式和亮度调节 |

## 2. 快速开始

### 2.1 初始化SDK

```java

KModuleManagerListener kModuleManagerListener = new KModuleManagerListener() {
  @Override
  public void onSerialDataRead(String sPort, byte[] buffer, int size) {
    Log.i(TAG, "onSerialDataRead: " + new String(buffer, 0, size));
  }
};
HardwareConfig config = new HardwareConfig.Builder("/dev/ttyS3", 9600)
        .readTimeoutMs(5000)
        .isDebug(true)
        .kModuleManagerListener(kModuleManagerListener)
        .build();
try {
    KModuleSDK.init(config);
} catch (SerialPortOpenFailedException e) {
    Log.e(TAG, "init fai;ed：" + e.getMessage());
}

```

### 2.2 使用示例

```java
// 打开继电器
KModuleSDK.getInstance().relayOn();

// 红灯常亮
KModuleSDK.getInstance().ledRedOn();

// 自定义颜色LED常亮 (FF0000表示红色)
KModuleSDK.getInstance().ledCustomColorAndOn("FF0000");

// 调节LED亮度 (1-99)
KModuleSDK.getInstance().ledBrightness(50);
```

### 2.3 销毁SDK

```java
// 销毁SDK，释放资源
KModuleSDK.getInstance().destroy();
```

## 3. API 详细说明

### 3.1 初始化与配置

#### `KModuleSDK.init(HardwareConfig config)`
- **功能**：初始化SDK
- **参数**：
  - `config`：硬件配置对象，包含串口路径、波特率等信息
- **异常**：
  - `SerialPortOpenFailedException`：串口打开失败时抛出

#### `KModuleSDK.getInstance()`
- **功能**：获取SDK单例实例
- **返回值**：`KModuleSDK`实例
- **异常**：
  - `SDKNotInitializedException`：SDK未初始化时抛出

#### `isDebug()`
- **功能**：检查是否为调试模式
- **返回值**：`boolean` - true表示调试模式，false表示非调试模式

### 3.2 继电器控制

#### `relayOn()`
- **功能**：打开继电器

#### `relayOff()`
- **功能**：关闭继电器

#### `beepOn()`
- **功能**：开启蜂鸣器

#### `beepOff()`
- **功能**：关闭蜂鸣器

#### `setOpenTime(int openTime)`
- **功能**：设置继电器打开时间
- **参数**：
  - `openTime`：继电器打开时间（毫秒）

#### `remoteOpen()`
- **功能**：远程开门控制

### 3.3 读卡功能

#### `sendSuperAdminCard()`
- **功能**：发送超级管理员卡

#### `virtualWeighing()`
- **功能**：韦根输出

#### `cardNumberOutputToggleEec()`
- **功能**：卡号输出切换为10进制

#### `cardNumberOutputToggleHex()`
- **功能**：卡号十六进制输出切换

#### `cardNumberOutputToggleDecReverse()`
- **功能**：卡号输出切换为反向10进制

### 3.3 LED控制

#### 基本颜色控制

##### `ledRedOn()`
- **功能**：红灯常亮

##### `ledGreenOn()`
- **功能**：绿灯常亮

##### `ledBlueOn()`
- **功能**：蓝灯常亮

##### `ledOff()`
- **功能**：关闭所有LED灯

#### 闪烁模式

##### `ledRedBlink()`
- **功能**：红灯闪烁

##### `ledGreenBlink()`
- **功能**：绿灯闪烁

##### `ledBlueBlink()`
- **功能**：蓝灯闪烁

##### `ledMarquee()`
- **功能**：LED跑马灯效果

#### 自定义颜色

##### `ledCustomColorAndOn(String color)`
- **功能**：自定义颜色LED常亮
- **参数**：
  - `color`：16进制颜色字符串（如"FF0000"表示红色）

##### `ledCustomColorBlink(String color)`
- **功能**：自定义颜色LED闪烁
- **参数**：
  - `color`：16进制颜色字符串（如"00FF00"表示绿色）

#### 亮度控制

##### `ledFullBrightness()`
- **功能**：LED满亮度显示

##### `ledBrightness(int brightness)`
- **功能**：调节LED亮度
- **参数**：
  - `brightness`：亮度值（0-100）

### 3.4 资源管理

#### `destroy()`
- **功能**：销毁SDK，释放资源（关闭串口、置空单例）

## 4. 异常处理

| 异常类 | 说明 |
|--------|------|
| `SDKNotInitializedException` | SDK未初始化时调用getInstance()方法抛出 |
| `SerialPortOpenFailedException` | 串口打开失败时抛出 |
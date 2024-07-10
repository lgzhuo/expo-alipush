# expo-alipush

expo module of aliyun push

集成在 expo-notifications 的消息系统下，使用和 expo-notifications 一致的消息对象结构，可以在 expo-notifications 的消息回调中接收来自阿里云推送的通知，如果配置了自定义消息可以在 expo-notifications
的消息处理回调中控制该消息的展示

# Installation in managed Expo projects

For [managed](https://docs.expo.dev/archive/managed-vs-bare/) Expo projects

### 安装

先安装 expo-notifications

```
npx expo install expo-notifications
```

再安装 expo-alipush

```
npm install expo-alipush
```

# Installation in bare React Native projects

For bare React Native projects, you must ensure that you have [installed and configured the `expo` package](https://docs.expo.dev/bare/installing-expo-modules/) before continuing.

### Configure for iOS

Run `npx pod-install` after installing the npm package.

### Configure for Android

因为 gradle 的限制，你需要在工程中添加阿里云推送和第三发华为推送的 maven 仓库地址

#### plugin 配置

```
npx expo install expo-build-properties
```

在`app.json`中配置

```json
{
  "plugins": [
    [
      "expo-build-properties",
      {
        "android": {
          "extraMavenRepos": [
            "https://developer.huawei.com/repo/",
            "https://maven.aliyun.com/nexus/content/repositories/releases/"
          ]
        }
      }
    ]
  ]
}
```

#### 手动

在 android 工程根 build.gradle 中添加如下配置

```gradle
  allprojects {
    repositories {
      maven {url 'https://developer.huawei.com/repo/'}
      maven {
        url 'http://maven.aliyun.com/nexus/content/repositories/releases/'
        name 'aliyun'
        allowInsecureProtocol = true
      }
    }
  }
```

# 配置阿里云推送账号信息

使用 expo module plugin 配置。在你的 app.json 中配置

```json
{
  "plugins": [
    [
      "expo-alipush",
      {
        "android": {
          "appKey": "Your Android AppKey",
          "appSecret": "Your Android AppSecret"
        },
        "ios": {
          "appKey": "Your iOS AppKey",
          "appSecret": "Your iOS AppSecret"
        }
      }
    ]
  ]
}
```

plugin 参数的签名如下

```typescript
interface AlipushConfig {
  android?: {
    appKey: string;
    appSecret: string;
    third?: {
      xiaomi?: {
        appID: string;
        appKey: string;
      };
      huawei?: {
        appID: string;
      };
    };
  };
  ios?: {
    appKey: string;
    appSecret: string;
  };
}
```

# 通知对象结构

Android 平台由于 expo-notifications 消息序列化方法的限制，在 expo-notifications 中接收到的阿里云
消息结构会不太准确，可以使用 expo-alipush 包中导出的同名 api 来获得更准确的消息对象。iOS 平台中没有
该问题

## 消息对象

在 expo-alipush 中添加了一种通知触发器类型代表 Android 端接收的阿里云推送

```typescript
interface BaseAlipushNotification {
  title: string | null;
  summary: string | null;
  extra: Record<string, string>;
  date: number;
  isForeground: boolean;
}

interface AlipushNotification extends BaseAlipushNotification {
  isForeground: false;
}

interface AlipushForegroundNotification extends BaseAlipushNotification {
  isForeground: true;
  openType: number;
  openActivity?: string;
  openUrl?: number;
}
```

### addNotificationReceivedListener 和 setNotificationHandler

Android 端，使用 expo-notifications 中的该 api，如果通知来自阿里云，推送对象中 trigger type
为“unknown”并且没有阿里云通知更多信息。使用 expo-alipush 中的 api 时，阿里云的通知对象 trigger
type 为"push"并附带 alipushNotification 属性表示原始阿里云通知

### addNotificationResponseReceivedListener 和 getLastNotificationResponseAsync

Android 端，使用 expo-notifications 中的该 api，如果通知来自阿里云有两种情况，如果该通知配置了
自定义并是前台接收，通知对象中的 trigger type 为”unknown“，如果通知后台接受或未配置自定义则通知
由阿里云 sdk 创建，api 中接受到对象 trigger type 为“push"，但其他属性如 notification.request.content 都不能正确取值。使用 expo-alipush 中的 api 时，来自阿里云的通知对象都能正确表示

# Contributing

Contributions are very welcome! Please refer to guidelines described in the [contributing guide](https://github.com/expo/expo#contributing).

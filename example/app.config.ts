import type { ConfigContext, ExpoConfig } from "expo/config";
import type {
  AliyunPushConfig,
  AliyunAndroidConfig,
  AliyunIOSConfig,
} from "expo-aliyun-push/plugin";

export default (context: ConfigContext): Partial<ExpoConfig> => {
  let { config } = context;

  const androidPackage = process.env.ANDROID_PACKAGE;
  if (androidPackage) {
    (config.android ??= {}).package = androidPackage;
  }

  const iosBundleIdentifier = process.env.IOS_BUNDLE_IDENTIFIER;
  if (iosBundleIdentifier) {
    (config.ios ??= {}).bundleIdentifier = iosBundleIdentifier;
  }

  config = withAliyunPushAppConfig(config);

  return config;
};

/* aliyun push config */

function withAliyunPushAppConfig(
  config: Partial<ExpoConfig>
): Partial<ExpoConfig> {
  const aliyunPushConfig: AliyunPushConfig = {
    android: getAliyunAndroidConfig(config),
    ios: getAliyunIOSConfig(config),
  };

  config.plugins ??= [];
  config.plugins.push(["expo-aliyun-push", aliyunPushConfig]);
  return config;
}

function getAliyunAndroidConfig(
  config: Partial<ExpoConfig>
): AliyunAndroidConfig | undefined {
  const appKey = process.env.ANDROID_APP_KEY;
  const appSecret = process.env.ANDROID_APP_SECRET;

  if (appKey && appSecret) {
    return {
      appKey,
      appSecret,
      third: {
        xiaomi: getAliyunXiaomiConfig(),
      },
    };
  }
}

function getAliyunXiaomiConfig(): NonNullable<
  AliyunAndroidConfig["third"]
>["xiaomi"] {
  const appID = process.env.XIAOMI_APP_ID;
  const appKey = process.env.XIAOMI_APP_KEY;

  return appID && appKey
    ? {
        appID,
        appKey,
      }
    : undefined;
}

function getAliyunIOSConfig(
  config: Partial<ExpoConfig>
): AliyunIOSConfig | undefined {
  const appKey = process.env.IOS_APP_KEY;
  const appSecret = process.env.IOS_APP_SECRET;

  if (appKey && appSecret) {
    return {
      appKey,
      appSecret,
    };
  }
}

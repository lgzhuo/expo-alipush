import type { ConfigContext, ExpoConfig } from "expo/config";
import type {
  AlipushConfig,
  AlipushAndroidConfig,
  AlipushIOSConfig,
} from "expo-alipush/plugin";

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

  config = withAlipushConfig(config);

  return config;
};

/* alipush config */

function withAlipushConfig(
  config: Partial<ExpoConfig>
): Partial<ExpoConfig> {
  const alipushConfig: AlipushConfig = {
    android: getAlipushAndroidConfig(config),
    ios: getAlipushIOSConfig(config),
  };

  config.plugins ??= [];
  config.plugins.push(["expo-alipush", alipushConfig]);
  return config;
}

function getAlipushAndroidConfig(
  config: Partial<ExpoConfig>
): AlipushAndroidConfig | undefined {
  const appKey = process.env.ANDROID_APP_KEY;
  const appSecret = process.env.ANDROID_APP_SECRET;

  if (appKey && appSecret) {
    return {
      appKey,
      appSecret,
      third: {
        xiaomi: getXiaomiConfig(),
      },
    };
  }
}

function getXiaomiConfig(): NonNullable<
  AlipushAndroidConfig["third"]
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

function getAlipushIOSConfig(
  config: Partial<ExpoConfig>
): AlipushIOSConfig | undefined {
  const appKey = process.env.IOS_APP_KEY;
  const appSecret = process.env.IOS_APP_SECRET;

  if (appKey && appSecret) {
    return {
      appKey,
      appSecret,
    };
  }
}

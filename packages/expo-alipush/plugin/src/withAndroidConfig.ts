import {
  AndroidConfig,
  ConfigPlugin,
  withAndroidManifest,
  withGradleProperties,
} from 'expo/config-plugins';

import type {
  AlipushAndroidConfig,
  AlipushConfig,
  AndroidThirdConfig,
} from './shared';

const {
  Manifest: {
    getMainApplicationOrThrow,
    addMetaDataItemToMainApplication,
    removeMetaDataItemFromMainApplication,
  },
} = AndroidConfig;

const withAlipushAndroidConfig: ConfigPlugin<AlipushConfig> = (
  config,
  {android},
) => {
  config = withAliyunApp(config, android);
  config = withThirdXiaomi(config, android?.third?.xiaomi);
  config = withThirdHuawei(config, android?.third?.huawei);
  return config;
};

const withAliyunApp: ConfigPlugin<AlipushAndroidConfig | undefined> = (
  config,
  andoridConfig,
) => {
  config = withApplicationMeta(config, [
    'com.alibaba.app.appkey',
    andoridConfig?.appKey,
  ]);
  config = withApplicationMeta(config, [
    'com.alibaba.app.appsecret',
    andoridConfig?.appSecret,
  ]);
  return config;
};

const withThirdXiaomi: ConfigPlugin<AndroidThirdConfig['xiaomi']> = (
  config,
  platformConfig,
) => {
  config = withApplicationMeta(config, [
    'com.xiaomi.app.id',
    platformConfig?.appID,
  ]);
  config = withApplicationMeta(config, [
    'com.xiaomi.app.key',
    platformConfig?.appKey,
  ]);

  const enabled = Boolean(platformConfig?.appID && platformConfig.appKey);
  config = withGradleProperties(config, config => {
    config.modResults.push({
      type: 'property',
      key: 'alipush.third.xiaomi.enabled',
      value: String(enabled),
    });
    return config;
  });

  return config;
};

const withThirdHuawei: ConfigPlugin<AndroidThirdConfig['huawei']> = (
  config,
  platfromConfig,
) => {
  config = withApplicationMeta(config, [
    'com.huawei.hms.client.appid',
    platfromConfig?.appID ? `appid=${platfromConfig.appID}` : undefined,
  ]);

  const enabled = Boolean(platfromConfig?.appID);
  config = withGradleProperties(config, config => {
    config.modResults.push({
      type: 'property',
      key: 'alipush.third.huawei.enabled',
      value: String(enabled),
    });
    return config;
  });

  return config;
};

const withApplicationMeta: ConfigPlugin<[name: string, value?: string]> = (
  config,
  [name, value],
) => {
  return withAndroidManifest(config, config => {
    const manifest = config.modResults;
    const mainApplication = getMainApplicationOrThrow(manifest);
    if (value) {
      addMetaDataItemToMainApplication(mainApplication, name, value, 'value');
    } else {
      removeMetaDataItemFromMainApplication(mainApplication, name);
    }
    return config;
  });
};

export default withAlipushAndroidConfig;

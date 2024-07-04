export interface AndroidThirdConfig {
  xiaomi?: {
    appID: string;
    appKey: string;
  };
  huawei?: {
    appID: string;
  };
}

export interface AliyunAndroidConfig {
  appKey: string;
  appSecret: string;

  third?: AndroidThirdConfig;
}

export interface AliyunIOSConfig {
  appKey: string;
  appSecret: string;
}

export interface AliyunPushConfig {
  android?: AliyunAndroidConfig;
  ios?: AliyunIOSConfig;
}

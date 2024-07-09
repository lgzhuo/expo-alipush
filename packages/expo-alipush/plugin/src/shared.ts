export interface AndroidThirdConfig {
  xiaomi?: {
    appID: string;
    appKey: string;
  };
  huawei?: {
    appID: string;
  };
}

export interface AlipushAndroidConfig {
  appKey: string;
  appSecret: string;

  third?: AndroidThirdConfig;
}

export interface AlipushIOSConfig {
  appKey: string;
  appSecret: string;
}

export interface AlipushConfig {
  android?: AlipushAndroidConfig;
  ios?: AlipushIOSConfig;
}
